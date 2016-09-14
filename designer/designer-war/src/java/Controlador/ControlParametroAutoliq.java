package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.ActualUsuario;
import Entidades.AportesEntidades;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.ParametrosAutoliq;
import Entidades.ParametrosEstructuras;
import Entidades.ParametrosInformes;
import Entidades.SolucionesNodos;
import Entidades.Terceros;
import Entidades.TiposEntidades;
import Entidades.TiposTrabajadores;
import Exportar.ExportarPDF;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarParametroAutoliqInterface;
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
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class ControlParametroAutoliq implements Serializable {

    @EJB
    AdministrarParametroAutoliqInterface administrarParametroAutoliq;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    // PARAMETROS AUTOLIQ //
    private List<ParametrosAutoliq> listaParametrosAutoliq;
    private List<ParametrosAutoliq> filtrarListaParametrosAutoliq;
    private ParametrosAutoliq parametroTablaSeleccionado;
    //
    private ParametrosAutoliq nuevoParametro;
    private ParametrosAutoliq editarParametro;
    private ParametrosAutoliq duplicarParametro;
    //
    private List<ParametrosAutoliq> listParametrosAutoliqCrear;
    private List<ParametrosAutoliq> listParametrosAutoliqModificar;
    private List<ParametrosAutoliq> listParametrosAutoliqBorrar;
    //
    private int cualCelda;
    private int bandera, tipoLista;
    private boolean permitirIndex, cambiosParametro;
    private BigInteger backUpSecRegistro;
    //
    //APORTES ENTIDADES//
    private List<AportesEntidades> listaAportesEntidades;
    private List<AportesEntidades> filtrarListaAportesEntidades;
    private AportesEntidades aporteTablaSeleccionado;
    //

    private AportesEntidades nuevoAporteEntidad;
    private AportesEntidades editarAporteEntidad;
    private AportesEntidades duplicarAporteEntidad;
    //

    private List<AportesEntidades> listAportesEntidadesCrear;
    private List<AportesEntidades> listAportesEntidadesModificar;
    private List<AportesEntidades> listAportesEntidadesBorrar;
    //
    private int cualCeldaAporte;
    private int banderaAporte, tipoListaAporte;
    private boolean permitirIndexAporte, cambiosAporte;
    private BigInteger backUpSecRegistroAporte;

    //
    private int tipoActualizacion, k;
    private BigInteger l;
    private boolean aceptar, guardado;
    //LOVS//
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
    private List<AportesEntidades> lovAportesEntidades;
    private List<AportesEntidades> filtrarLovAportesEntidades;
    private AportesEntidades aporteEntidadSeleccionado;
    private String infoRegistroAporteEntidad;
    //
    private String auxTipoTipoTrabajador, auxEmpresa;
    private String auxTipoEntidad, auxEmpleado;
    private long auxNitTercero;
    //
    private String altoTabla;
    private String altoTablaAporte;
    //
    private String paginaAnterior;
    //
    private Column parametroAno, parametroTipoTrabajador, parametroEmpresa;
    //
    private Column aporteCodigoEmpleado, aporteAno, aporteMes, aporteNombreEmpleado, aporteNIT, aporteTercero, aporteTipoEntidad;
    private Column aporteEmpleado, aporteEmpleador, aporteAjustePatronal, aporteSolidaridadl, aporteSubSistencia;
    private Column aporteSubsPensionados, aporteSalarioBasico, aporteIBC, aporteIBCReferencia, aporteDiasCotizados, aporteTipoAportante;
    private Column aporteING, aporteRET, aporteTDA, aporteTAA, aporteVSP, aporteVTE, aporteVST, aporteSLN, aporteIGE, aporteLMA, aporteVAC, aporteAVP, aporteVCT, aporteIRP, aporteSUS, aporteINTE;
    private Column aporteTarifaEPS, aporteTarifaAAFP, aporteTarifaACTT, aporteCodigoCTT, aporteAVPEValor, aporteAVPPValor, aporteRETCONTAValor, aporteCodigoNEPS, aporteCodigoNAFP;
    private Column aporteEGValor, aporteEGAutorizacion, aporteMaternidadValor, aporteMaternidadAuto, aporteUPCValor, aporteTipo, aporteTipoPensionado, aportePensionCompartida, aporteSubTipoCotizante;
    private Column aporteExtranjero, aporteFechaIngreso, aporteTarifaCaja, aporteTarifaSena, aporteTarifaICBF, aporteTarifaESAP, aporteTarifaMEN;
    //
    private boolean disabledBuscar;
    //
    private String infoRegistroParametro, infoRegistroAporte;
    //
    private ParametrosEstructuras parametroEstructura;
    private ParametrosInformes parametroInforme;
    private ActualUsuario usuario;
    //
    private boolean activoBtnsPaginas;
    //
    private int numero, cualTabla;
    //
    private String visibilidadMostrarTodos;
    private DataTable tablaC;

    public ControlParametroAutoliq() {
        visibilidadMostrarTodos = "hidden";
        numero = 7;
        activoBtnsPaginas = false;
        disabledBuscar = true;
        altoTabla = "50";
        altoTablaAporte = "160";
        //
        parametroTablaSeleccionado = new ParametrosAutoliq();
        aporteTablaSeleccionado = new AportesEntidades();
        //
        nuevoParametro = new ParametrosAutoliq();
        nuevoParametro.setEmpresa(new Empresas());
        nuevoParametro.setTipotrabajador(new TiposTrabajadores());
        editarParametro = new ParametrosAutoliq();

        nuevoAporteEntidad = new AportesEntidades();
        nuevoAporteEntidad.setAno(parametroTablaSeleccionado.getAno());
        nuevoAporteEntidad.setMes(parametroTablaSeleccionado.getMes());
        nuevoAporteEntidad.setEmpresa(parametroTablaSeleccionado.getEmpresa());
        nuevoAporteEntidad.setTerceroRegistro(new Terceros());
        nuevoAporteEntidad.setTipoentidad(new TiposEntidades());
        nuevoAporteEntidad.setEmpleado(new Empleados());
        ///
        duplicarAporteEntidad = new AportesEntidades();
        duplicarAporteEntidad.setAno(aporteTablaSeleccionado.getAno());
        duplicarAporteEntidad.setMes(aporteTablaSeleccionado.getMes());
        duplicarAporteEntidad.setEmpresa(aporteTablaSeleccionado.getEmpresa());
        duplicarAporteEntidad.setTerceroRegistro(new Terceros());
        duplicarAporteEntidad.setTipoentidad(new TiposEntidades());
        duplicarAporteEntidad.setEmpleado(new Empleados());
        editarAporteEntidad = new AportesEntidades();

        duplicarParametro = new ParametrosAutoliq();
        duplicarParametro.setEmpresa(new Empresas());
        duplicarParametro.setTipotrabajador(new TiposTrabajadores());
        //
        listParametrosAutoliqCrear = new ArrayList<ParametrosAutoliq>();
        listParametrosAutoliqModificar = new ArrayList<ParametrosAutoliq>();
        listParametrosAutoliqBorrar = new ArrayList<ParametrosAutoliq>();

        listaAportesEntidades = null;
        lovAportesEntidades = null;
        listAportesEntidadesCrear = new ArrayList<AportesEntidades>();
        listAportesEntidadesBorrar = new ArrayList<AportesEntidades>();
        listAportesEntidadesModificar = new ArrayList<AportesEntidades>();
        //
//        parametroTablaSeleccionado = null;
        cualCelda = -1;
        cualCeldaAporte = -1;
        bandera = 0;
        banderaAporte = 0;
        tipoLista = 0;
        tipoListaAporte = 0;
        permitirIndex = true;
        permitirIndexAporte = true;
        cambiosParametro = false;
        cambiosAporte = false;
        //
        aceptar = true;
        guardado = true;
        //
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
        //
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarParametroAutoliq.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());

        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPagina(String pagina) {
        paginaAnterior = pagina;
        listaParametrosAutoliq = null;
        listaAportesEntidades = null;
        getListaParametrosAutoliq();
        getListaAportesEntidades();
        contarRegistrosParametros();
        contarRegistrosAporte();
        if (listaParametrosAutoliq != null) {
            if (!listaParametrosAutoliq.isEmpty()) {
                parametroTablaSeleccionado = listaParametrosAutoliq.get(0);
            }
        }
        if (listaAportesEntidades != null) {
            if (!listaAportesEntidades.isEmpty()) {
                aporteTablaSeleccionado = listaAportesEntidades.get(0);
            }
        }

    }

    public String redirigir() {
        return paginaAnterior;
    }

    public void modificarParametroAutoliq(ParametrosAutoliq parametro) {
        RequestContext context = RequestContext.getCurrentInstance();
        parametroTablaSeleccionado = parametro;
        if (tipoLista == 0) {
            if (!listParametrosAutoliqCrear.contains(parametroTablaSeleccionado)) {

                if (listParametrosAutoliqModificar.isEmpty()) {
                    listParametrosAutoliqModificar.add(parametroTablaSeleccionado);
                } else if (!listParametrosAutoliqModificar.contains(parametroTablaSeleccionado)) {
                    listParametrosAutoliqModificar.add(parametroTablaSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
            cambiosParametro = true;
            activoBtnsPaginas = true;
            RequestContext.getCurrentInstance().update("form:novedadauto");
            RequestContext.getCurrentInstance().update("form:incaPag");
            RequestContext.getCurrentInstance().update("form:eliminarToda");
            RequestContext.getCurrentInstance().update("form:procesoLiq");
            RequestContext.getCurrentInstance().update("form:acumDif");
        } else {
            if (!listParametrosAutoliqCrear.contains(parametroTablaSeleccionado)) {

                if (listParametrosAutoliqModificar.isEmpty()) {
                    listParametrosAutoliqModificar.add(parametroTablaSeleccionado);
                } else if (!listParametrosAutoliqModificar.contains(parametroTablaSeleccionado)) {
                    listParametrosAutoliqModificar.add(parametroTablaSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
            cambiosParametro = true;
            activoBtnsPaginas = true;
            RequestContext.getCurrentInstance().update("form:novedadauto");
            RequestContext.getCurrentInstance().update("form:incaPag");
            RequestContext.getCurrentInstance().update("form:eliminarToda");
            RequestContext.getCurrentInstance().update("form:procesoLiq");
            RequestContext.getCurrentInstance().update("form:acumDif");
        }
        RequestContext.getCurrentInstance().update("form:datosParametroAuto");
    }

    public void modificarAporteEntidad(AportesEntidades aporte) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoListaAporte == 0) {
            if (listAportesEntidadesModificar.isEmpty()) {
                listAportesEntidadesModificar.add(aporteTablaSeleccionado);
            } else if (!listAportesEntidadesModificar.contains(aporteTablaSeleccionado)) {
                listAportesEntidadesModificar.add(aporteTablaSeleccionado);
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambiosAporte = true;
        } else {
            if (listAportesEntidadesModificar.isEmpty()) {
                listAportesEntidadesModificar.add(aporteTablaSeleccionado);
            } else if (!listAportesEntidadesModificar.contains(aporteTablaSeleccionado)) {
                listAportesEntidadesModificar.add(aporteTablaSeleccionado);
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambiosAporte = true;
        }
        RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
    }

    public void modificarParametroAutoliq(ParametrosAutoliq parametro, String confirmarCambio, String valorConfirmar) {
        parametroTablaSeleccionado = parametro;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOTRABAJADOR")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoLista == 0) {
                    parametroTablaSeleccionado.getTipotrabajador().setNombre(auxTipoTipoTrabajador);
                } else {
                    parametroTablaSeleccionado.getTipotrabajador().setNombre(auxTipoTipoTrabajador);
                }
                for (int i = 0; i < lovTiposTrabajadores.size(); i++) {
                    if (lovTiposTrabajadores.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        parametroTablaSeleccionado.setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
                    } else {
                        parametroTablaSeleccionado.setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
                    }
                    lovTiposTrabajadores.clear();
                    getLovTiposTrabajadores();
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("formularioLovTipoTrabajador:TipoTrabajadorDialogo");
                    PrimefacesContextUI.ejecutar("PF('TipoTrabajadorDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                coincidencias = 1;
                if (tipoLista == 0) {
                    parametroTablaSeleccionado.setTipotrabajador(new TiposTrabajadores());
                } else {
                    parametroTablaSeleccionado.setTipotrabajador(new TiposTrabajadores());
                }
                lovTiposTrabajadores.clear();
                getLovTiposTrabajadores();
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listParametrosAutoliqCrear.contains(parametroTablaSeleccionado)) {

                    if (listParametrosAutoliqModificar.isEmpty()) {
                        listParametrosAutoliqModificar.add(parametroTablaSeleccionado);
                    } else if (!listParametrosAutoliqModificar.contains(parametroTablaSeleccionado)) {
                        listParametrosAutoliqModificar.add(parametroTablaSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                cambiosParametro = true;
                activoBtnsPaginas = true;
                RequestContext.getCurrentInstance().update("form:novedadauto");
                RequestContext.getCurrentInstance().update("form:incaPag");
                RequestContext.getCurrentInstance().update("form:eliminarToda");
                RequestContext.getCurrentInstance().update("form:procesoLiq");
                RequestContext.getCurrentInstance().update("form:acumDif");
            } else {
                if (!listParametrosAutoliqCrear.contains(parametroTablaSeleccionado)) {

                    if (listParametrosAutoliqModificar.isEmpty()) {
                        listParametrosAutoliqModificar.add(parametroTablaSeleccionado);
                    } else if (!listParametrosAutoliqModificar.contains(parametroTablaSeleccionado)) {
                        listParametrosAutoliqModificar.add(parametroTablaSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                cambiosParametro = true;
                activoBtnsPaginas = true;
                RequestContext.getCurrentInstance().update("form:novedadauto");
                RequestContext.getCurrentInstance().update("form:incaPag");
                RequestContext.getCurrentInstance().update("form:eliminarToda");
                RequestContext.getCurrentInstance().update("form:procesoLiq");
                RequestContext.getCurrentInstance().update("form:acumDif");
            }
        }
        RequestContext.getCurrentInstance().update("form:datosParametroAuto");
    }

    public void modificarAporteEntidad(AportesEntidades aporte, String confirmarCambio, String valorConfirmar) {
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
                PrimefacesContextUI.ejecutar("PF('TipoEntidadDialogo').show()");
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
                    PrimefacesContextUI.ejecutar("PF('TerceroDialogo').show()");
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
                PrimefacesContextUI.ejecutar("PF('EmpleadoDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoListaAporte == 0) {
                if (listAportesEntidadesModificar.isEmpty()) {
                    listAportesEntidadesModificar.add(aporteTablaSeleccionado);
                } else if (!listAportesEntidadesModificar.contains(aporteTablaSeleccionado)) {
                    listAportesEntidadesModificar.add(aporteTablaSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                cambiosAporte = true;
            } else {
                if (listAportesEntidadesModificar.isEmpty()) {
                    listAportesEntidadesModificar.add(aporteTablaSeleccionado);
                } else if (!listAportesEntidadesModificar.contains(aporteTablaSeleccionado)) {
                    listAportesEntidadesModificar.add(aporteTablaSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                cambiosAporte = true;
            }
        }
        RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("TIPOTRABAJADOR")) {
            if (tipoNuevo == 1) {
                auxTipoTipoTrabajador = nuevoParametro.getTipotrabajador().getNombre();
            } else if (tipoNuevo == 2) {
                auxTipoTipoTrabajador = duplicarParametro.getTipotrabajador().getNombre();
            }
        }
        if (Campo.equals("EMPRESA")) {
            if (tipoNuevo == 1) {
                auxEmpresa = nuevoParametro.getEmpresa().getNombre();
            } else if (tipoNuevo == 2) {
                auxEmpresa = duplicarParametro.getEmpresa().getNombre();
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
                    nuevoParametro.getTipotrabajador().setNombre(auxTipoTipoTrabajador);
                } else if (tipoNuevo == 2) {
                    duplicarParametro.getTipotrabajador().setNombre(auxTipoTipoTrabajador);
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
                    PrimefacesContextUI.ejecutar("PF('TipoTrabajadorDialogo').show()");
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
                nuevoParametro.getEmpresa().setNombre(auxEmpresa);
            } else if (tipoNuevo == 2) {
                duplicarParametro.getEmpresa().setNombre(auxEmpresa);
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
                PrimefacesContextUI.ejecutar("PF('EmpresaDialogo').show()");
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
        cambiarIndice(listaParametrosAutoliq.get(indice), columna);
        System.out.println("parametrotablaseleccionado: " + parametroTablaSeleccionado);

    }

    public void posicionAporteEntidad() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        cambiarIndiceAporteEntidad(listaAportesEntidades.get(indice), columna);
    }

    public void cambiarIndice(ParametrosAutoliq parametro, int celda) {

        if (permitirIndex == true) {
            RequestContext context = RequestContext.getCurrentInstance();
            cualTabla = 1;
            PrimefacesContextUI.ejecutar("PF('operacionEnProceso').hide()");
            parametroTablaSeleccionado = parametro;
            cualCelda = celda;
            parametroTablaSeleccionado.getSecuencia();
            auxTipoTipoTrabajador = parametroTablaSeleccionado.getTipotrabajador().getNombre();
            if (banderaAporte == 1) {
                desactivarFiltradoAporteEntidad();
                banderaAporte = 0;
                filtrarListaAportesEntidades = null;
                tipoListaAporte = 0;
            }
            activoBtnsPaginas = false;
            //RequestContext.getCurrentInstance().update("form:datosAporteEntidad2");
            visibilidadMostrarTodos = "hidden";
            RequestContext.getCurrentInstance().update("form:mostrarTodos");
            RequestContext.getCurrentInstance().update("form:novedadauto");
            RequestContext.getCurrentInstance().update("form:incaPag");
            RequestContext.getCurrentInstance().update("form:eliminarToda");
            RequestContext.getCurrentInstance().update("form:procesoLiq");
            RequestContext.getCurrentInstance().update("form:acumDif");
            RequestContext.getCurrentInstance().update("form:infoRegistroAporte");
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
            getParametroTablaSeleccionado();
            cargarDatosNuevos();
        }
    }

    public void cargarDatosNuevos() {
        try {
            listaAportesEntidades = null;
            getListaAportesEntidades();
//            lovAportesEntidades = null;
//            getLovAportesEntidades();
            if (listaAportesEntidades != null) {
                modificarInfoRegistroAporte(listaAportesEntidades.size());
            }
            Thread.sleep(2000L);
//            RequestContext.getCurrentInstance().update("form:PanelTotal");
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
            PrimefacesContextUI.ejecutar("PF('operacionEnProceso').hide()");

        } catch (Exception e) {
            System.out.println("Error cargarDatosNuevos Controlador : " + e.toString());
        }
    }

    public void cambiarIndiceAporteEntidad(AportesEntidades aporte, int celda) {
        if (permitirIndexAporte == true) {
            aporteTablaSeleccionado = aporte;
            cualCeldaAporte = celda;
            cualTabla = 2;
            if (cualCeldaAporte == 0) {
                aporteTablaSeleccionado.getEmpleado().getCodigoempleado();
            } else if (cualCeldaAporte == 1) {
                aporteTablaSeleccionado.getAno();
            } else if (cualCeldaAporte == 2) {
                aporteTablaSeleccionado.getMes();
            } else if (cualCeldaAporte == 3) {
                aporteTablaSeleccionado.getEmpleado().getPersona().getNombreCompleto();
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
                aporteTablaSeleccionado.getExtranjero();
            } else if (cualCeldaAporte == 52) {
                aporteTablaSeleccionado.getFechaorigen();
            } else if (cualCeldaAporte == 53) {
                aporteTablaSeleccionado.getSubtipocotizanteSTR();
            } else if (cualCeldaAporte == 54) {
                aporteTablaSeleccionado.getTarifacaja();
            } else if (cualCeldaAporte == 55) {
                aporteTablaSeleccionado.getTarifasena();
            } else if (cualCeldaAporte == 56) {
                aporteTablaSeleccionado.getTarifaicbf();
            } else if (cualCeldaAporte == 57) {
                aporteTablaSeleccionado.getTarifaesap();
            } else if (cualCeldaAporte == 58) {
                aporteTablaSeleccionado.getTarifamen();

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
            System.out.println("lista parametro borrar : " + listParametrosAutoliqBorrar.size());
            System.out.println("lista parametro crear : " + listParametrosAutoliqCrear.size());
            System.out.println("lista parametro modificar : " + listParametrosAutoliqModificar.size());
            if (!listParametrosAutoliqBorrar.isEmpty()) {
                administrarParametroAutoliq.borrarParametrosAutoliq(listParametrosAutoliqBorrar);
                listParametrosAutoliqBorrar.clear();
            }
            if (!listParametrosAutoliqCrear.isEmpty()) {
                administrarParametroAutoliq.crearParametrosAutoliq(listParametrosAutoliqCrear);
                listParametrosAutoliqCrear.clear();
            }
            if (!listParametrosAutoliqModificar.isEmpty()) {
                administrarParametroAutoliq.editarParametrosAutoliq(listParametrosAutoliqModificar);
                listParametrosAutoliqModificar.clear();
            }
            listaParametrosAutoliq = null;
            getListaParametrosAutoliq();
            if (listaParametrosAutoliq != null) {
                modificarInfoRegistroParametro(listaParametrosAutoliq.size());
            }
            RequestContext.getCurrentInstance().update("form:infoRegistroParametro");
            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
            k = 0;
            activoBtnsPaginas = true;
            RequestContext.getCurrentInstance().update("form:novedadauto");
            RequestContext.getCurrentInstance().update("form:incaPag");
            RequestContext.getCurrentInstance().update("form:eliminarToda");
            RequestContext.getCurrentInstance().update("form:procesoLiq");
            RequestContext.getCurrentInstance().update("form:acumDif");
            cambiosParametro = false;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos de Parámetros de Liquidación con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
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
        System.out.println("entró a guardar");
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("listAportesEntidadesBorrar.size() : " + listAportesEntidadesBorrar.size());
        System.out.println("listAportesEntidadesCrear.size() : " + listAportesEntidadesCrear.size());
        System.out.println("listAportesEntidadesModificar.size() :" + listAportesEntidadesModificar.size());
        try {
            if (!listAportesEntidadesBorrar.isEmpty()) {
                administrarParametroAutoliq.borrarAportesEntidades(listAportesEntidadesBorrar);
                listAportesEntidadesBorrar.clear();
            }
            if (!listAportesEntidadesCrear.isEmpty()) {
                administrarParametroAutoliq.crearAportesEntidades(listAportesEntidadesCrear);
                listAportesEntidadesCrear.clear();
            }

            if (!listAportesEntidadesModificar.isEmpty()) {
                administrarParametroAutoliq.editarAportesEntidades(listAportesEntidadesModificar);
                listAportesEntidadesModificar.clear();
            }
            listaAportesEntidades = null;
            getListaAportesEntidades();
            System.out.println("tamaño lista aportes : " + listaAportesEntidades.size());
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
            modificarInfoRegistroAporte(listaAportesEntidades.size());
            k = 0;
//            aporteTablaSeleccionado = null;
            cambiosAporte = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos de Aporte Entidad con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception e) {
            System.out.println("Error guardarCambiosAportes  Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Aporte Entidad, Por favor intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            altoTabla = "50";
            parametroAno = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroAno");
            parametroAno.setFilterStyle("display: none; visibility: hidden;");
            parametroTipoTrabajador = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroTipoTrabajador");
            parametroTipoTrabajador.setFilterStyle("display: none; visibility: hidden;");
            parametroEmpresa = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroEmpresa");
            parametroEmpresa.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
            bandera = 0;
            filtrarListaParametrosAutoliq = null;
            tipoLista = 0;
        }
        if (banderaAporte == 1) {
            desactivarFiltradoAporteEntidad();
            banderaAporte = 0;
            filtrarListaAportesEntidades = null;
            tipoListaAporte = 0;
        }
        visibilidadMostrarTodos = "hidden";
        RequestContext.getCurrentInstance().update("form:mostrarTodos");
        //
        listParametrosAutoliqBorrar.clear();
        listParametrosAutoliqCrear.clear();
        listParametrosAutoliqModificar.clear();
        //
        listAportesEntidadesBorrar.clear();
        listAportesEntidadesModificar.clear();
        parametroTablaSeleccionado = null;
        aporteTablaSeleccionado = null;
        k = 0;
        listaParametrosAutoliq = null;
        getListaParametrosAutoliq();
        if (listaParametrosAutoliq != null) {
            modificarInfoRegistroParametro(listaParametrosAutoliq.size());
        }
        listaAportesEntidades = null;
        getListaAportesEntidades();
        if (listaAportesEntidades != null) {
            modificarInfoRegistroAporte(listaAportesEntidades.size());
        } else {
            modificarInfoRegistroAporte(0);
        }
        cambiosParametro = false;
        cambiosAporte = false;
        guardado = true;
        activoBtnsPaginas = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
        RequestContext.getCurrentInstance().update("form:datosParametroAuto");
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualTabla == 1) {
            if (parametroTablaSeleccionado != null) {
                editarParametro = parametroTablaSeleccionado;
                if (cualCelda == 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarAnoD");
                    PrimefacesContextUI.ejecutar("PF('editarAnoD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoTrabajadorD");
                    PrimefacesContextUI.ejecutar("PF('editarTipoTrabajadorD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 3) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresaD");
                    PrimefacesContextUI.ejecutar("PF('editarEmpresaD').show()");
                    cualCelda = -1;
                }
                activoBtnsPaginas = true;
                RequestContext.getCurrentInstance().update("form:novedadauto");
                RequestContext.getCurrentInstance().update("form:incaPag");
                RequestContext.getCurrentInstance().update("form:eliminarToda");
                RequestContext.getCurrentInstance().update("form:procesoLiq");
                RequestContext.getCurrentInstance().update("form:acumDif");
            }
        } else if (cualTabla == 2) {
            if (aporteTablaSeleccionado != null) {
                if (tipoListaAporte == 0) {
                    editarAporteEntidad = aporteTablaSeleccionado;
                } else {
                    editarAporteEntidad = aporteTablaSeleccionado;
                }
                if (cualCeldaAporte == 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCodEmplD");
                    PrimefacesContextUI.ejecutar("PF('editarCodEmplD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarAnoAD");
                    PrimefacesContextUI.ejecutar("PF('editarAnoAD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarMesAD");
                    PrimefacesContextUI.ejecutar("PF('editarMesAD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 3) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreEmplD");
                    PrimefacesContextUI.ejecutar("PF('editarNombreEmplD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 4) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNitTerceroD");
                    PrimefacesContextUI.ejecutar("PF('editarNitTerceroD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 5) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreTerceroD");
                    PrimefacesContextUI.ejecutar("PF('editarNombreTerceroD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 6) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoEntidadD");
                    PrimefacesContextUI.ejecutar("PF('editarTipoEntidadD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 7) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleadoD");
                    PrimefacesContextUI.ejecutar("PF('editarEmpleadoD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 8) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleadorD");
                    PrimefacesContextUI.ejecutar("PF('editarEmpleadorD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 9) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarAjustePatronalD");
                    PrimefacesContextUI.ejecutar("PF('editarAjustePatronalD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 10) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarSolidaridadD");
                    PrimefacesContextUI.ejecutar("PF('editarSolidaridadD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 11) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarSubSistenciaD");
                    PrimefacesContextUI.ejecutar("PF('editarSubSistenciaD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 12) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarSubsPensionadosD");
                    PrimefacesContextUI.ejecutar("PF('editarSubsPensionadosD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 13) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarSalarioBasicoD");
                    PrimefacesContextUI.ejecutar("PF('editarSalarioBasicoD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 14) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarIBCD");
                    PrimefacesContextUI.ejecutar("PF('editarIBCD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 15) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarIBCReferenciaD");
                    PrimefacesContextUI.ejecutar("PF('editarIBCReferenciaD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 16) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarDiasCotizadosD");
                    PrimefacesContextUI.ejecutar("PF('editarDiasCotizadosD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 17) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoAportanteD");
                    PrimefacesContextUI.ejecutar("PF('editarTipoAportanteD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 18) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarINGD");
                    PrimefacesContextUI.ejecutar("PF('editarINGD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 19) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarRETD");
                    PrimefacesContextUI.ejecutar("PF('editarRETD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 20) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTDAD");
                    PrimefacesContextUI.ejecutar("PF('editarTDAD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 21) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTAAD");
                    PrimefacesContextUI.ejecutar("PF('editarTAAD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 22) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarVSPD");
                    PrimefacesContextUI.ejecutar("PF('editarVSPD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 23) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarVTED");
                    PrimefacesContextUI.ejecutar("PF('editarVTED').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 24) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarVSTD");
                    PrimefacesContextUI.ejecutar("PF('editarVSTD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 25) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarSLND");
                    PrimefacesContextUI.ejecutar("PF('editarSLND').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 26) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarIGED");
                    PrimefacesContextUI.ejecutar("PF('editarIGED').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 27) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarLMAD");
                    PrimefacesContextUI.ejecutar("PF('editarLMAD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 28) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarVCAD");
                    PrimefacesContextUI.ejecutar("PF('editarVCAD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 29) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarAVPD");
                    PrimefacesContextUI.ejecutar("PF('editarAVPD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 30) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarVCTD");
                    PrimefacesContextUI.ejecutar("PF('editarVCTD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 31) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarIRPD");
                    PrimefacesContextUI.ejecutar("PF('editarIRPD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 32) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarSUSD");
                    PrimefacesContextUI.ejecutar("PF('editarSUSD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 33) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarINTED");
                    PrimefacesContextUI.ejecutar("PF('editarINTED').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 34) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTarifaEPSD");
                    PrimefacesContextUI.ejecutar("PF('editarTarifaEPSD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 35) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTarifaAAFPD");
                    PrimefacesContextUI.ejecutar("PF('editarTarifaAAFPD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 36) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTarifaACTTD");
                    PrimefacesContextUI.ejecutar("PF('editarTarifaACTTD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 37) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoCTTD");
                    PrimefacesContextUI.ejecutar("PF('editarCodigoCTTD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 38) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarAvpeValorD");
                    PrimefacesContextUI.ejecutar("PF('editarAvpeValorD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 39) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarAvppValorD");
                    PrimefacesContextUI.ejecutar("PF('editarAvppValorD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 40) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarRetcontaValorD");
                    PrimefacesContextUI.ejecutar("PF('editarRetcontaValorD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 41) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoNEPSD");
                    PrimefacesContextUI.ejecutar("PF('editarCodigoNEPSD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 42) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoNAFPD");
                    PrimefacesContextUI.ejecutar("PF('editarCodigoNAFPD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 43) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarEgValorD");
                    PrimefacesContextUI.ejecutar("PF('editarEgValorD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 44) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarEgAutorizacionD");
                    PrimefacesContextUI.ejecutar("PF('editarEgAutorizacionD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 45) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarMaternidadValorD");
                    PrimefacesContextUI.ejecutar("PF('editarMaternidadValorD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 46) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarMaternidadAutoD");
                    PrimefacesContextUI.ejecutar("PF('editarMaternidadAutoD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 47) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarUpcValorD");
                    PrimefacesContextUI.ejecutar("PF('editarUpcValorD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 48) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoD");
                    PrimefacesContextUI.ejecutar("PF('editarTipoD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 49) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTPD");
                    PrimefacesContextUI.ejecutar("PF('editarTPD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 50) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarPCD");
                    PrimefacesContextUI.ejecutar("PF('editarPCD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 51) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarEXTRD");
                    PrimefacesContextUI.ejecutar("PF('editarEXTRD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 52) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaIngreso");
                    PrimefacesContextUI.ejecutar("PF('editarFechaIngreso').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 54) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTarifaCajaD");
                    PrimefacesContextUI.ejecutar("PF('editarTarifaCajaD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 55) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTarifaSenaD");
                    PrimefacesContextUI.ejecutar("PF('editarTarifaSenaD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 56) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTarifaICBFD");
                    PrimefacesContextUI.ejecutar("PF('editarTarifaICBFD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 57) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTarifaESAPD");
                    PrimefacesContextUI.ejecutar("PF('editarTarifaESAPD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 58) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTarifaMEND");
                    PrimefacesContextUI.ejecutar("PF('editarTarifaMEND').show()");
                    cualCeldaAporte = -1;
                }
            }
        } else {
            PrimefacesContextUI.ejecutar("PF('formularioDialogos:seleccionarRegistro').show()");
        }
    }

    public void mostrarDialogoNuevoAporte() {
        nuevoAporteEntidad.setAno(parametroTablaSeleccionado.getAno());
        nuevoAporteEntidad.setMes(parametroTablaSeleccionado.getMes());
        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAporteEntidad");
        PrimefacesContextUI.ejecutar("PF('formularioDialogos:nuevoAporteEntidad').show()");
    }

    public void mostrarDialogoNuevoParametro() {
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroParametro");
        PrimefacesContextUI.ejecutar("PF('formularioDialogos:NuevoRegistroParametro').show()");
    }

    public void mostrarDialogoElegirTabla() {
        RequestContext.getCurrentInstance().update("formularioDialogos:seleccionarTablaNewReg");
        PrimefacesContextUI.ejecutar("PF('formularioDialogos:seleccionarTablaNewReg').show()");
    }

    public void agregarNuevaParametroAutoliq() {
        if (nuevoParametro.getAno() > 0 && nuevoParametro.getMes() > 0
                && nuevoParametro.getEmpresa().getSecuencia() != null) {
            if (bandera == 1) {
                altoTabla = "50";
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                parametroAno = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroAno");
                parametroAno.setFilterStyle("display: none; visibility: hidden;");
                parametroTipoTrabajador = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroTipoTrabajador");
                parametroTipoTrabajador.setFilterStyle("display: none; visibility: hidden;");
                parametroEmpresa = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroEmpresa");
                parametroEmpresa.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosParametroAuto");
                bandera = 0;
                filtrarListaParametrosAutoliq = null;
                tipoLista = 0;
            }
            if (banderaAporte == 1) {
                desactivarFiltradoAporteEntidad();
                banderaAporte = 0;
                filtrarListaAportesEntidades = null;
                tipoListaAporte = 0;
            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
            k++;
            l = BigInteger.valueOf(k);
            nuevoParametro.setSecuencia(l);
            listParametrosAutoliqCrear.add(nuevoParametro);
            if (listaParametrosAutoliq == null) {
                listaParametrosAutoliq = new ArrayList<ParametrosAutoliq>();
            }
            listaParametrosAutoliq.add(nuevoParametro);
            parametroTablaSeleccionado = nuevoParametro;
            cambiarIndice(nuevoParametro, cualCelda);
            nuevoParametro = new ParametrosAutoliq();
            nuevoParametro.setTipotrabajador(new TiposTrabajadores());
            nuevoParametro.setEmpresa(new Empresas());
            RequestContext context = RequestContext.getCurrentInstance();

            modificarInfoRegistroParametro(listaParametrosAutoliq.size());

            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroParametro').hide()");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            activoBtnsPaginas = true;
//                RequestContext.getCurrentInstance().update("form:novedadauto");
//                RequestContext.getCurrentInstance().update("form:incaPag");
//                RequestContext.getCurrentInstance().update("form:eliminarToda");
//                RequestContext.getCurrentInstance().update("form:procesoLiq");
//                RequestContext.getCurrentInstance().update("form:acumDif");
            cambiosParametro = true;
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('errorRegNullParametro').show()");
        }

    }

    public void agregarNuevoAporteEntidad() {

        if (banderaAporte == 1) {
            desactivarFiltradoAporteEntidad();
            banderaAporte = 0;
            filtrarListaAportesEntidades = null;
            tipoListaAporte = 0;
            tipoLista = 0;
        }
        //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
        k++;
        l = BigInteger.valueOf(k);
        nuevoAporteEntidad.setSecuencia(l);
        listAportesEntidadesCrear.add(nuevoAporteEntidad);
        if (listaAportesEntidades == null) {
            listaAportesEntidades = new ArrayList<AportesEntidades>();
        }
        listaAportesEntidades.add(nuevoAporteEntidad);
        aporteTablaSeleccionado = nuevoAporteEntidad;
        nuevoAporteEntidad = new AportesEntidades();
        nuevoAporteEntidad.setAno(parametroTablaSeleccionado.getAno());
        nuevoAporteEntidad.setMes(parametroTablaSeleccionado.getMes());
        nuevoAporteEntidad.setEmpresa(parametroTablaSeleccionado.getEmpresa());
        nuevoAporteEntidad.setTerceroRegistro(new Terceros());
        nuevoAporteEntidad.setTipoentidad(new TiposEntidades());
        nuevoAporteEntidad.setEmpleado(new Empleados());
        RequestContext context = RequestContext.getCurrentInstance();

        modificarInfoRegistroAporte(listaAportesEntidades.size());

        RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
        PrimefacesContextUI.ejecutar("PF('formularioDialogos:nuevoAporteEntidad').hide()");
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void limpiarNuevaParametroAutoliq() {
        nuevoParametro = new ParametrosAutoliq();
        nuevoParametro.setTipotrabajador(new TiposTrabajadores());
        nuevoParametro.setEmpresa(new Empresas());
    }

    public void duplicarAporteEntidad() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (aporteTablaSeleccionado != null) {
            duplicarAporteEntidad = new AportesEntidades();

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
            PrimefacesContextUI.ejecutar("PF('duplicarAporteEntidad').show()");
        } else {
            PrimefacesContextUI.ejecutar("PF('formularioDialogos:seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarAporteEntidad() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (banderaAporte == 1) {
            desactivarFiltradoAporteEntidad();
            banderaAporte = 0;
            filtrarListaAportesEntidades = null;
            tipoListaAporte = 0;
            tipoLista = 0;
        }
        k++;
        l = BigInteger.valueOf(k);
        duplicarAporteEntidad.setSecuencia(l);
        listAportesEntidadesCrear.add(duplicarAporteEntidad);

        if (listaAportesEntidades == null) {
            listaAportesEntidades = new ArrayList<AportesEntidades>();
        }
        listaAportesEntidades.add(duplicarAporteEntidad);
        aporteTablaSeleccionado = duplicarAporteEntidad;
        duplicarAporteEntidad = new AportesEntidades();
        duplicarAporteEntidad.setAno(aporteTablaSeleccionado.getAno());
        duplicarAporteEntidad.setMes(aporteTablaSeleccionado.getMes());
        duplicarAporteEntidad.setEmpresa(aporteTablaSeleccionado.getEmpresa());
        duplicarAporteEntidad.setTerceroRegistro(new Terceros());
        duplicarAporteEntidad.setTipoentidad(new TiposEntidades());
        duplicarAporteEntidad.setEmpleado(new Empleados());
        RequestContext context = RequestContext.getCurrentInstance();
        modificarInfoRegistroAporte(listaAportesEntidades.size());
        RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
        PrimefacesContextUI.ejecutar("PF('formularioDialogos:duplicarAporteEntidad').hide()");
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void duplicarParametroAutoliq() {
        if (cualTabla == 1) {
            if (parametroTablaSeleccionado != null) {
                RequestContext context = RequestContext.getCurrentInstance();
//            if (guardado == false) {
                duplicarParametro = new ParametrosAutoliq();
                if (tipoLista == 0) {
                    duplicarParametro.setAno(parametroTablaSeleccionado.getAno());
                    duplicarParametro.setEmpresa(parametroTablaSeleccionado.getEmpresa());
                    duplicarParametro.setMes(parametroTablaSeleccionado.getMes());
                    duplicarParametro.setTipotrabajador(parametroTablaSeleccionado.getTipotrabajador());
                }
                if (tipoLista == 1) {
                    duplicarParametro.setAno(parametroTablaSeleccionado.getAno());
                    duplicarParametro.setEmpresa(parametroTablaSeleccionado.getEmpresa());
                    duplicarParametro.setMes(parametroTablaSeleccionado.getMes());
                    duplicarParametro.setTipotrabajador(parametroTablaSeleccionado.getTipotrabajador());

                }
                visibilidadMostrarTodos = "hidden";
                RequestContext.getCurrentInstance().update("form:mostrarTodos");
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarParametro");
                PrimefacesContextUI.ejecutar("PF('DuplicarRegistroParametro').show()");
            }
        } else if (cualTabla == 2) {
            if (aporteTablaSeleccionado != null) {
                duplicarAporteEntidad();
            }
        } else {
            PrimefacesContextUI.ejecutar("PF('formularioDialogos:seleccionarRegistro()");
        }
    }

    public void confirmarDuplicarParametroAutoliq() {
        if (duplicarParametro.getAno() > 0 && duplicarParametro.getMes() > 0
                && duplicarParametro.getEmpresa().getSecuencia() != null) {
            if (bandera == 1) {
                altoTabla = "50";
                FacesContext c = FacesContext.getCurrentInstance();
                parametroAno = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroAno");
                parametroAno.setFilterStyle("display: none; visibility: hidden;");
                parametroTipoTrabajador = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroTipoTrabajador");
                parametroTipoTrabajador.setFilterStyle("display: none; visibility: hidden;");
                parametroEmpresa = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroEmpresa");
                parametroEmpresa.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosParametroAuto");
                bandera = 0;
                filtrarListaParametrosAutoliq = null;
                tipoLista = 0;
            }
            if (banderaAporte == 1) {
                desactivarFiltradoAporteEntidad();
                banderaAporte = 0;
                filtrarListaAportesEntidades = null;
                tipoListaAporte = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            duplicarParametro.setSecuencia(l);
            listaParametrosAutoliq.add(duplicarParametro);
            listParametrosAutoliqCrear.add(duplicarParametro);
            parametroTablaSeleccionado = duplicarParametro;
            RequestContext context = RequestContext.getCurrentInstance();

            modificarInfoRegistroParametro(listaParametrosAutoliq.size());

            RequestContext.getCurrentInstance().update("form:infoRegistroParametro");
            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroParametro').hide()");
            activoBtnsPaginas = true;
            RequestContext.getCurrentInstance().update("form:novedadauto");
            RequestContext.getCurrentInstance().update("form:incaPag");
            RequestContext.getCurrentInstance().update("form:eliminarToda");
            RequestContext.getCurrentInstance().update("form:procesoLiq");
            RequestContext.getCurrentInstance().update("form:acumDif");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            duplicarParametro = new ParametrosAutoliq();
            cambiosParametro = true;
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('errorRegNullParametro').show()");
        }
    }

    public void limpiarDuplicarParametroAutoliq() {
        duplicarParametro = new ParametrosAutoliq();
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
        nuevoAporteEntidad = new AportesEntidades();
        nuevoAporteEntidad.setAno(parametroTablaSeleccionado.getAno());
        nuevoAporteEntidad.setMes(parametroTablaSeleccionado.getMes());
        nuevoAporteEntidad.setEmpresa(parametroTablaSeleccionado.getEmpresa());
        nuevoAporteEntidad.setTerceroRegistro(new Terceros());
        nuevoAporteEntidad.setTipoentidad(new TiposEntidades());
        nuevoAporteEntidad.setEmpleado(new Empleados());
    }

    public void limpiarDuplicarAporteEntidad() {
        duplicarAporteEntidad = new AportesEntidades();
        duplicarAporteEntidad.setAno(parametroTablaSeleccionado.getAno());
        duplicarAporteEntidad.setMes(parametroTablaSeleccionado.getMes());
        duplicarAporteEntidad.setEmpresa(parametroTablaSeleccionado.getEmpresa());
        duplicarAporteEntidad.setTerceroRegistro(new Terceros());
        duplicarAporteEntidad.setTipoentidad(new TiposEntidades());
        duplicarAporteEntidad.setEmpleado(new Empleados());
    }

    public void borrarParametroAutoliq() {
        if (listaParametrosAutoliq != null) {
            if (listaAportesEntidades.size() == 0) {
                if (!listParametrosAutoliqModificar.isEmpty() && listParametrosAutoliqModificar.contains(parametroTablaSeleccionado)) {
                    int modIndex = listParametrosAutoliqModificar.indexOf(parametroTablaSeleccionado);
                    listParametrosAutoliqModificar.remove(modIndex);
                    listParametrosAutoliqBorrar.add(parametroTablaSeleccionado);
                } else if (!listParametrosAutoliqCrear.isEmpty() && listParametrosAutoliqCrear.contains(parametroTablaSeleccionado)) {
                    int crearIndex = listParametrosAutoliqCrear.indexOf(parametroTablaSeleccionado);
                    listParametrosAutoliqCrear.remove(crearIndex);
                } else {
                    listParametrosAutoliqBorrar.add(parametroTablaSeleccionado);
                }
                listaParametrosAutoliq.remove(parametroTablaSeleccionado);
                if (tipoLista == 1) {
                    filtrarListaParametrosAutoliq.remove(parametroTablaSeleccionado);
                }
                RequestContext context = RequestContext.getCurrentInstance();
                modificarInfoRegistroParametro(listaParametrosAutoliq.size());
                RequestContext.getCurrentInstance().update("form:datosParametroAuto");
                parametroTablaSeleccionado = null;
                activoBtnsPaginas = true;
                cambiosParametro = true;
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            } else {
                PrimefacesContextUI.ejecutar("PF('errorBorrarParametro').show()");
            }
        } else {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        }
    }

    public void borrarAporteEntidad() {
        if (aporteTablaSeleccionado != null) {
            System.out.println("entró a borrar aportes entidades");
            System.out.println("lista de aportes entidades antes de borrar : " + listaAportesEntidades.size());
            if (!listAportesEntidadesModificar.isEmpty() && listAportesEntidadesModificar.contains(aporteTablaSeleccionado)) {
                int modIndex = listAportesEntidadesModificar.indexOf(aporteTablaSeleccionado);
                listAportesEntidadesModificar.remove(modIndex);
                listAportesEntidadesBorrar.add(aporteTablaSeleccionado);
            } else if (!listAportesEntidadesCrear.isEmpty() && listAportesEntidadesCrear.contains(aporteTablaSeleccionado)) {
                listAportesEntidadesBorrar.remove(listAportesEntidadesCrear.indexOf(aporteTablaSeleccionado));
            } else {
                listAportesEntidadesBorrar.add(aporteTablaSeleccionado);
            }
            listaAportesEntidades.remove(aporteTablaSeleccionado);
            if (tipoLista == 1) {
                filtrarListaAportesEntidades.remove(aporteTablaSeleccionado);
            }
            System.out.println("lista de aportes entidades después de borrar : " + listaAportesEntidades.size());
            modificarInfoRegistroAporte(listaAportesEntidades.size());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
            aporteTablaSeleccionado = null;
            cambiosAporte = true;
            System.out.println("se borró un registro de aportes entidad");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        }
    }

    public void borrarAporteEntidadProcesoAutomatico() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (listaAportesEntidades != null) {
                if (!listaAportesEntidades.isEmpty()) {
//                    if (guardado == false) {
//                        guardadoGeneral();
//                    }
                    administrarParametroAutoliq.borrarAportesEntidadesProcesoAutomatico(parametroTablaSeleccionado.getEmpresa().getSecuencia(), parametroTablaSeleccionado.getMes(), parametroTablaSeleccionado.getAno());
//                    listaParametrosAutoliq = null;
//                    getListaParametrosAutoliq();
//                    modificarInfoRegistroParametro(listaParametrosAutoliq.size());
                    listaAportesEntidades = null;
                    modificarInfoRegistroAporte(0);
                    disabledBuscar = true;
                    visibilidadMostrarTodos = "hidden";
                    RequestContext.getCurrentInstance().update("form:mostrarTodos");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    RequestContext.getCurrentInstance().update("form:buscar");
                    RequestContext.getCurrentInstance().update("form:infoRegistroAporte");
                    RequestContext.getCurrentInstance().update("form:infoRegistroParametro");
                    RequestContext.getCurrentInstance().update("form:datosParametroAuto");
                    RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
                    activoBtnsPaginas = true;
                    RequestContext.getCurrentInstance().update("form:novedadauto");
                    RequestContext.getCurrentInstance().update("form:incaPag");
                    RequestContext.getCurrentInstance().update("form:eliminarToda");
                    RequestContext.getCurrentInstance().update("form:procesoLiq");
                    RequestContext.getCurrentInstance().update("form:acumDif");
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
                        ano = parametroTablaSeleccionado.getAno();
                        mes = parametroTablaSeleccionado.getMes();
                    } else {
                        ano = parametroTablaSeleccionado.getAno();
                        mes = parametroTablaSeleccionado.getMes();
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
                        PrimefacesContextUI.ejecutar("PF('procesoLiquidacionOK').show()");
                    } else {
                        PrimefacesContextUI.ejecutar("PF('errorProcesoLiquidacionFechas').show()");
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

                System.out.println("fechadesde : " + parametroEstructura.getFechadesdecausado());
                System.out.println("fecha hasta:  " + parametroEstructura.getFechahastacausado());
                System.out.println("tipo trabajador: " + parametroTablaSeleccionado.getTipotrabajador().getSecuencia());
                System.out.println("empresa : " + parametroTablaSeleccionado.getEmpresa().getNombre());

                if (tipoLista == 0) {
                    System.out.println("entró a if 2");
                    procesoInsertar = administrarParametroAutoliq.ejecutarPKGInsertar(parametroEstructura.getFechadesdecausado(), parametroEstructura.getFechahastacausado(), parametroTablaSeleccionado.getTipotrabajador().getSecuencia(), parametroTablaSeleccionado.getEmpresa().getSecuencia());
                    System.out.println("procesoinsertar del if 2 : " + procesoInsertar);
                    procesoActualizar = administrarParametroAutoliq.ejecutarPKGActualizarNovedades(parametroTablaSeleccionado.getAno(), parametroTablaSeleccionado.getMes(), parametroTablaSeleccionado.getEmpresa().getSecuencia());
                    System.out.println("procesoActualizar del if 2 : " + procesoActualizar);
                } else {
                    System.out.println("entró a else 1");
                    procesoInsertar = administrarParametroAutoliq.ejecutarPKGInsertar(parametroEstructura.getFechadesdecausado(), parametroEstructura.getFechahastacausado(), parametroTablaSeleccionado.getTipotrabajador().getSecuencia(), parametroTablaSeleccionado.getEmpresa().getSecuencia());
                    procesoActualizar = administrarParametroAutoliq.ejecutarPKGActualizarNovedades(parametroTablaSeleccionado.getAno(), parametroTablaSeleccionado.getMes(), parametroTablaSeleccionado.getEmpresa().getSecuencia());
                }
                if ((procesoInsertar.equals("PROCESO_EXITOSO")) && (procesoActualizar.equals("PROCESO_EXITOSO"))) {
                    System.out.println("entró a if 3");
                    System.out.println("procesoinsertar del if 3 : " + procesoInsertar);
                    System.out.println("procesoActualizar del if 3 : " + procesoActualizar);
//                listaParametrosAutoliq = null;
//                getListaParametrosAutoliq();
//                modificarInfoRegistroParametro(listaParametrosAutoliq.size());
                    listaAportesEntidades = null;
                    getListaAportesEntidades();
                    modificarInfoRegistroAporte(listaAportesEntidades.size());
                    disabledBuscar = true;
                    activoBtnsPaginas = true;
                    visibilidadMostrarTodos = "hidden";
                    RequestContext.getCurrentInstance().update("form:mostrarTodos");
                    RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
                    System.out.println("El proceso de Liquidación fue realizado con éxito");
                    System.out.println("lista aportes entidades : " + listaAportesEntidades.size());
                    FacesMessage msg = new FacesMessage("Información", "El proceso de Liquidación fue realizado con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                } else if ((procesoInsertar.equals("ERROR_PERSISTENCIA")) || (procesoActualizar.equals("ERROR_PERSISTENCIA"))) {
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
                ano = parametroTablaSeleccionado.getAno();
                mes = parametroTablaSeleccionado.getMes();
            } else {
                ano = parametroTablaSeleccionado.getAno();
                mes = parametroTablaSeleccionado.getMes();
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

            administrarParametroAutoliq.modificarParametroEstructura(parametroEstructura);
            administrarParametroAutoliq.modificarParametroInforme(parametroInforme);

            parametroInforme = null;   ///
            parametroEstructura = null;///////
            activoBtnsPaginas = true;
            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
            FacesMessage msg = new FacesMessage("Información", "Se realizo con éxito el cambio de fechas de ParametrosEstructuras y ParametrosReportes");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        } catch (Exception e) {
            System.out.println("Error cambiarFechasParametros Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en la modificacio de las fechas de ParametrosEstructuras y ParametrosReportes");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void procesoAcumularDiferenciaAporteEntidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
//            usuario = null;
            getUsuario();
            parametroTablaSeleccionado = getParametroTablaSeleccionado();
//            System.out.println("usuario :" +  usuario.getAlias());
            if (usuario.getAlias() != null) {
                getParametroEstructura();
                getParametroInforme();
                System.out.println("parametro estructuras : " + parametroEstructura);
                System.out.println("parametro informe : " + parametroInforme);
                System.out.println("parametro seleccionado : " + parametroTablaSeleccionado);
                System.out.println("parada 1");
                if (parametroEstructura != null) {
                    if (parametroInforme != null) {
//
                        boolean fechasIgualesEstructura = true;
                        boolean fechasIgualesInforme = true;
//                    short ano = 0;
//                    short mes = 0;
//                    ano = getParametroTablaSeleccionado().getAno();
//                    mes = getParametroTablaSeleccionado().getMes();
////
//                    System.out.println("Año al entrar al if : " + ano);
//                    System.out.println("Mes al entrar al if : " + mes);
//                    if ((parametroEstructura.getFechahastacausado().getMonth() + 1) != mes) {
//                        fechasIgualesEstructura = false;
//                    }
//                    if ((parametroEstructura.getFechahastacausado().getYear() + 1900) != ano) {
//                        fechasIgualesEstructura = false;
//                    }
//                    if ((parametroInforme.getFechahasta().getMonth() + 1) != mes) {
//                        fechasIgualesInforme = false;
//                    }
//                    if ((parametroInforme.getFechahasta().getYear() + 1900) != ano) {
//                        fechasIgualesInforme = false;
//                    }
//                    System.out.println("fechas iguales estructura : " + fechasIgualesEstructura);
//                    System.out.println("fechas iguales informe : " + fechasIgualesInforme);

                        System.out.println("parada 2");
                        if (fechasIgualesEstructura == true && fechasIgualesInforme == true) {
                            System.out.println("entra a acumular dif ok");
                            PrimefacesContextUI.ejecutar("PF('acumularDiferenciaOK').show()");
                        } else {
                            PrimefacesContextUI.ejecutar("PF('errorAcumularDiferencia').show()");
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("se estalló");
            System.out.println("Error procesoLiquidacionAporteEntidad Controlador : " + e.toString());
        }
    }

    public void acumularDiferenciaOK() {
        RequestContext context = RequestContext.getCurrentInstance();
        String resultado;
        try {
            parametroTablaSeleccionado = getParametroTablaSeleccionado();
            System.out.println("Año " + parametroTablaSeleccionado.getAno());
            System.out.println("Mes " + parametroTablaSeleccionado.getMes());
            System.out.println("Empresa " + parametroTablaSeleccionado.getEmpresa().getNombre());

            resultado = administrarParametroAutoliq.ejecutarPKGActualizarNovedades(parametroTablaSeleccionado.getAno(), parametroTablaSeleccionado.getMes(), parametroTablaSeleccionado.getEmpresa().getSecuencia());
            System.out.println("resultado consulta : " + resultado);
//            listaParametrosAutoliq = null;
//            getListaParametrosAutoliq();
//            modificarInfoRegistroParametro(listaParametrosAutoliq.size());

            disabledBuscar = true;
            activoBtnsPaginas = true;
            visibilidadMostrarTodos = "hidden";
            System.out.println("entró a actualizar");
            RequestContext.getCurrentInstance().update("form:mostrarTodos");
            System.out.println("El proceso de Acumular Diferencias de Aportes Entidades fue realizado con éxito");
            FacesMessage msg = new FacesMessage("Información", "El proceso de Acumular Diferencias de Aportes Entidades fue realizado con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
//            listaAportesEntidades = null;
//            getListaAportesEntidades();
//            modificarInfoRegistroAporte(listaAportesEntidades.size());
            guardadoGeneral();
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");

        } catch (Exception e) {
            System.out.println("Error acumularDiferenciaOK Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el proceso de Acumular Diferencias.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void activarCtrlF11() {

        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "30";
            parametroAno = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroAno");
            parametroAno.setFilterStyle("width: 85% !important");
            parametroTipoTrabajador = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroTipoTrabajador");
            parametroTipoTrabajador.setFilterStyle("width: 85% !important");
            parametroEmpresa = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroEmpresa");
            parametroEmpresa.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
            bandera = 1;
            activarFiltradoAporteEntidad();
        } else if (bandera == 1) {
            altoTabla = "50";
            parametroAno = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroAno");
            parametroAno.setFilterStyle("display: none; visibility: hidden;");
            parametroTipoTrabajador = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroTipoTrabajador");
            parametroTipoTrabajador.setFilterStyle("display: none; visibility: hidden;");
            parametroEmpresa = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroEmpresa");
            parametroEmpresa.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
            bandera = 0;
            filtrarListaParametrosAutoliq = null;
            tipoLista = 0;
            desactivarFiltradoAporteEntidad();
        }
    }

    public void desactivarFiltradoAporteEntidad() {
        FacesContext c = FacesContext.getCurrentInstance();
        altoTablaAporte = "160";

        aporteCodigoEmpleado = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteCodigoEmpleado");
        aporteCodigoEmpleado.setFilterStyle("display: none; visibility: hidden;");

        aporteAno = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteAno");
        aporteAno.setFilterStyle("display: none; visibility: hidden;");

        aporteMes = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteMes");
        aporteMes.setFilterStyle("display: none; visibility: hidden;");

        aporteNombreEmpleado = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteNombreEmpleado");
        aporteNombreEmpleado.setFilterStyle("display: none; visibility: hidden;");

        aporteNIT = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteNIT");
        aporteNIT.setFilterStyle("display: none; visibility: hidden;");

        aporteTercero = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTercero");
        aporteTercero.setFilterStyle("display: none; visibility: hidden;");

        aporteTipoEntidad = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTipoEntidad");
        aporteTipoEntidad.setFilterStyle("display: none; visibility: hidden;");

        aporteEmpleado = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteEmpleado");
        aporteEmpleado.setFilterStyle("display: none; visibility: hidden;");

        aporteEmpleador = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteEmpleador");
        aporteEmpleador.setFilterStyle("display: none; visibility: hidden;");

        aporteAjustePatronal = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteAjustePatronal");
        aporteAjustePatronal.setFilterStyle("display: none; visibility: hidden;");

        aporteSolidaridadl = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSolidaridadl");
        aporteSolidaridadl.setFilterStyle("display: none; visibility: hidden;");

        aporteSubSistencia = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSubSistencia");
        aporteSubSistencia.setFilterStyle("display: none; visibility: hidden;");

        aporteSubsPensionados = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSubsPensionados");
        aporteSubsPensionados.setFilterStyle("display: none; visibility: hidden;");

        aporteSalarioBasico = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSalarioBasico");
        aporteSalarioBasico.setFilterStyle("display: none; visibility: hidden;");

        aporteIBC = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteIBC");
        aporteIBC.setFilterStyle("display: none; visibility: hidden;");

        aporteIBCReferencia = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteIBCReferencia");
        aporteIBCReferencia.setFilterStyle("display: none; visibility: hidden;");

        aporteDiasCotizados = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteDiasCotizados");
        aporteDiasCotizados.setFilterStyle("display: none; visibility: hidden;");

        aporteTipoAportante = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTipoAportante");
        aporteTipoAportante.setFilterStyle("display: none; visibility: hidden;");

        aporteING = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteING");
        aporteING.setFilterStyle("display: none; visibility: hidden;");

        aporteRET = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteRET");
        aporteRET.setFilterStyle("display: none; visibility: hidden;");

        aporteTDA = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTDA");
        aporteTDA.setFilterStyle("display: none; visibility: hidden;");

        aporteTAA = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTAA");
        aporteTAA.setFilterStyle("display: none; visibility: hidden;");

        aporteVSP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteVSP");
        aporteVSP.setFilterStyle("display: none; visibility: hidden;");

        aporteVTE = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteVTE");
        aporteVTE.setFilterStyle("display: none; visibility: hidden;");

        aporteVST = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteVST");
        aporteVST.setFilterStyle("display: none; visibility: hidden;");

        aporteSLN = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSLN");
        aporteSLN.setFilterStyle("display: none; visibility: hidden;");

        aporteIGE = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteIGE");
        aporteIGE.setFilterStyle("display: none; visibility: hidden;");

        aporteLMA = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteLMA");
        aporteLMA.setFilterStyle("display: none; visibility: hidden;");

        aporteVAC = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteVAC");
        aporteVAC.setFilterStyle("display: none; visibility: hidden;");

        aporteAVP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteAVP");
        aporteAVP.setFilterStyle("display: none; visibility: hidden;");

        aporteVCT = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteVCT");
        aporteVCT.setFilterStyle("display: none; visibility: hidden;");

        aporteIRP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteIRP");
        aporteIRP.setFilterStyle("display: none; visibility: hidden;");

        aporteSUS = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSUS");
        aporteSUS.setFilterStyle("display: none; visibility: hidden;");

        aporteINTE = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteINTE");
        aporteINTE.setFilterStyle("display: none; visibility: hidden;");

        aporteTarifaEPS = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaEPS");
        aporteTarifaEPS.setFilterStyle("display: none; visibility: hidden;");

        aporteTarifaAAFP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaAAFP");
        aporteTarifaAAFP.setFilterStyle("display: none; visibility: hidden;");

        aporteTarifaACTT = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaACTT");
        aporteTarifaACTT.setFilterStyle("display: none; visibility: hidden;");

        aporteCodigoCTT = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteCodigoCTT");
        aporteCodigoCTT.setFilterStyle("display: none; visibility: hidden;");

        aporteAVPEValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteAVPEValor");
        aporteAVPEValor.setFilterStyle("display: none; visibility: hidden;");

        aporteAVPPValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteAVPPValor");
        aporteAVPPValor.setFilterStyle("display: none; visibility: hidden;");

        aporteRETCONTAValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteRETCONTAValor");
        aporteRETCONTAValor.setFilterStyle("display: none; visibility: hidden;");

        aporteCodigoNEPS = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteCodigoNEPS");
        aporteCodigoNEPS.setFilterStyle("display: none; visibility: hidden;");

        aporteCodigoNAFP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteCodigoNAFP");
        aporteCodigoNAFP.setFilterStyle("display: none; visibility: hidden;");

        aporteEGValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteEGValor");
        aporteEGValor.setFilterStyle("display: none; visibility: hidden;");

        aporteEGAutorizacion = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteEGAutorizacion");
        aporteEGAutorizacion.setFilterStyle("display: none; visibility: hidden;");

        aporteMaternidadValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteMaternidadValor");
        aporteMaternidadValor.setFilterStyle("display: none; visibility: hidden;");

        aporteMaternidadAuto = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteMaternidadAuto");
        aporteMaternidadAuto.setFilterStyle("display: none; visibility: hidden;");

        aporteUPCValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteUPCValor");
        aporteUPCValor.setFilterStyle("display: none; visibility: hidden;");

        aporteTipo = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTipo");
        aporteTipo.setFilterStyle("display: none; visibility: hidden;");

        aporteTipoPensionado = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTipoPensionado");
        aporteTipoPensionado.setFilterStyle("display: none; visibility: hidden;");

        aportePensionCompartida = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aportePensionCompartida");
        aportePensionCompartida.setFilterStyle("display: none; visibility: hidden;");

        aporteExtranjero = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteExtranjero");
        aporteExtranjero.setFilterStyle("display: none; visibility: hidden;");

        aporteFechaIngreso = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteFechaIngreso");
        aporteFechaIngreso.setFilterStyle("display: none; visibility: hidden;");

        aporteSubTipoCotizante = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSubTipoCotizante");
        aporteSubTipoCotizante.setFilterStyle("display: none; visibility: hidden;");

        aporteTarifaCaja = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaCaja");
        aporteTarifaCaja.setFilterStyle("display: none; visibility: hidden;");

        aporteTarifaSena = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaSena");
        aporteTarifaSena.setFilterStyle("display: none; visibility: hidden;");

        aporteTarifaICBF = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaICBF");
        aporteTarifaICBF.setFilterStyle("display: none; visibility: hidden;");

        aporteTarifaESAP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaESAP");
        aporteTarifaESAP.setFilterStyle("display: none; visibility: hidden;");

        aporteTarifaMEN = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaMEN");
        aporteTarifaMEN.setFilterStyle("display: none; visibility: hidden;");

        RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
    }

    public void activarFiltradoAporteEntidad() {
        FacesContext c = FacesContext.getCurrentInstance();
        altoTablaAporte = "138";

        aporteCodigoEmpleado = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteCodigoEmpleado");
        aporteCodigoEmpleado.setFilterStyle("width: 85% !important");

        aporteAno = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteAno");
        aporteAno.setFilterStyle("width: 85% !important");

        aporteMes = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteMes");
        aporteMes.setFilterStyle("width: 85% !important");

        aporteNombreEmpleado = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteNombreEmpleado");
        aporteNombreEmpleado.setFilterStyle("width: 85% !important");

        aporteNIT = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteNIT");
        aporteNIT.setFilterStyle("width: 85% !important");

        aporteTercero = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTercero");
        aporteTercero.setFilterStyle("width: 85% !important");

        aporteTipoEntidad = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTipoEntidad");
        aporteTipoEntidad.setFilterStyle("width: 85% !important");

        aporteEmpleado = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteEmpleado");
        aporteEmpleado.setFilterStyle("width: 85% !important");

        aporteEmpleador = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteEmpleador");
        aporteEmpleador.setFilterStyle("width: 85% !important");

        aporteAjustePatronal = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteAjustePatronal");
        aporteAjustePatronal.setFilterStyle("width: 85% !important");

        aporteSolidaridadl = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSolidaridadl");
        aporteSolidaridadl.setFilterStyle("width: 85% !important");

        aporteSubSistencia = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSubSistencia");
        aporteSubSistencia.setFilterStyle("width: 85% !important");

        aporteSubsPensionados = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSubsPensionados");
        aporteSubsPensionados.setFilterStyle("width: 85% !important");

        aporteSalarioBasico = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSalarioBasico");
        aporteSalarioBasico.setFilterStyle("width: 85% !important");

        aporteIBC = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteIBC");
        aporteIBC.setFilterStyle("width: 85% !important");

        aporteIBCReferencia = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteIBCReferencia");
        aporteIBCReferencia.setFilterStyle("width: 85% !important");

        aporteDiasCotizados = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteDiasCotizados");
        aporteDiasCotizados.setFilterStyle("width: 85% !important");

        aporteTipoAportante = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTipoAportante");
        aporteTipoAportante.setFilterStyle("width: 85% !important");

        aporteING = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteING");
        aporteING.setFilterStyle("width: 85% !important");

        aporteRET = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteRET");
        aporteRET.setFilterStyle("width: 85% !important");

        aporteTDA = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTDA");
        aporteTDA.setFilterStyle("width: 85% !important");

        aporteTAA = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTAA");
        aporteTAA.setFilterStyle("width: 85% !important");

        aporteVSP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteVSP");
        aporteVSP.setFilterStyle("width: 85% !important");

        aporteVTE = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteVTE");
        aporteVTE.setFilterStyle("width: 85% !important");

        aporteVST = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteVST");
        aporteVST.setFilterStyle("width: 85% !important");

        aporteSLN = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSLN");
        aporteSLN.setFilterStyle("width: 85% !important");

        aporteIGE = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteIGE");
        aporteIGE.setFilterStyle("width: 85% !important");

        aporteLMA = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteLMA");
        aporteLMA.setFilterStyle("width: 85% !important");

        aporteVAC = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteVAC");
        aporteVAC.setFilterStyle("width: 85% !important");

        aporteAVP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteAVP");
        aporteAVP.setFilterStyle("width: 85% !important");

        aporteVCT = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteVCT");
        aporteVCT.setFilterStyle("width: 85% !important");

        aporteIRP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteIRP");
        aporteIRP.setFilterStyle("width: 85% !important");

        aporteSUS = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSUS");
        aporteSUS.setFilterStyle("width: 85% !important");

        aporteINTE = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteINTE");
        aporteINTE.setFilterStyle("width: 85% !important");

        aporteTarifaEPS = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaEPS");
        aporteTarifaEPS.setFilterStyle("width: 85% !important");

        aporteTarifaAAFP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaAAFP");
        aporteTarifaAAFP.setFilterStyle("width: 85% !important");

        aporteTarifaACTT = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaACTT");
        aporteTarifaACTT.setFilterStyle("width: 85% !important");

        aporteCodigoCTT = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteCodigoCTT");
        aporteCodigoCTT.setFilterStyle("width: 85% !important");

        aporteAVPEValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteAVPEValor");
        aporteAVPEValor.setFilterStyle("width: 85% !important");

        aporteAVPPValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteAVPPValor");
        aporteAVPPValor.setFilterStyle("width: 85% !important");

        aporteRETCONTAValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteRETCONTAValor");
        aporteRETCONTAValor.setFilterStyle("width: 85% !important");

        aporteCodigoNEPS = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteCodigoNEPS");
        aporteCodigoNEPS.setFilterStyle("width: 85% !important");

        aporteCodigoNAFP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteCodigoNAFP");
        aporteCodigoNAFP.setFilterStyle("width: 85% !important");

        aporteEGValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteEGValor");
        aporteEGValor.setFilterStyle("width: 85% !important");

        aporteEGAutorizacion = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteEGAutorizacion");
        aporteEGAutorizacion.setFilterStyle("width: 85% !important");

        aporteMaternidadValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteMaternidadValor");
        aporteMaternidadValor.setFilterStyle("width: 85% !important");

        aporteMaternidadAuto = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteMaternidadAuto");
        aporteMaternidadAuto.setFilterStyle("width: 85% !important");

        aporteUPCValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteUPCValor");
        aporteUPCValor.setFilterStyle("width: 85% !important");

        aporteTipo = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTipo");
        aporteTipo.setFilterStyle("width: 85% !important");

        aporteTipoPensionado = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTipoPensionado");
        aporteTipoPensionado.setFilterStyle("width: 85% !important");

        aportePensionCompartida = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aportePensionCompartida");
        aportePensionCompartida.setFilterStyle("width: 85% !important");

        aporteExtranjero = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteExtranjero");
        aporteExtranjero.setFilterStyle("width: 85% !important");

        aporteFechaIngreso = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteFechaIngreso");
        aporteFechaIngreso.setFilterStyle("width: 85% !important");

        aporteSubTipoCotizante = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSubTipoCotizante");
        aporteSubTipoCotizante.setFilterStyle("width: 85% !important");

        aporteTarifaCaja = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaCaja");
        aporteTarifaCaja.setFilterStyle("width: 85% !important");

        aporteTarifaSena = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaSena");
        aporteTarifaSena.setFilterStyle("width: 85% !important");

        aporteTarifaICBF = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaICBF");
        aporteTarifaICBF.setFilterStyle("width: 85% !important");

        aporteTarifaESAP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaESAP");
        aporteTarifaESAP.setFilterStyle("width: 85% !important");

        aporteTarifaMEN = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaMEN");
        aporteTarifaMEN.setFilterStyle("width: 85% !important");

        RequestContext.getCurrentInstance().update("form:tablaAportesEntidades:tablaAportesEntidades");

        RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");

    }

    public void salir() {
        if (bandera == 1) {
            altoTabla = "50";
            FacesContext c = FacesContext.getCurrentInstance();
            parametroAno = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroAno");
            parametroAno.setFilterStyle("display: none; visibility: hidden;");
            parametroTipoTrabajador = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroTipoTrabajador");
            parametroTipoTrabajador.setFilterStyle("display: none; visibility: hidden;");
            parametroEmpresa = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroEmpresa");
            parametroEmpresa.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
            bandera = 0;
            filtrarListaParametrosAutoliq = null;
            tipoLista = 0;
        }
        if (banderaAporte == 1) {
            desactivarFiltradoAporteEntidad();
            banderaAporte = 0;
            filtrarListaAportesEntidades = null;
            tipoListaAporte = 0;
        }

        listParametrosAutoliqBorrar.clear();
        listParametrosAutoliqCrear.clear();
        listParametrosAutoliqModificar.clear();
        //
        listAportesEntidadesBorrar.clear();
        listAportesEntidadesModificar.clear();
        //
        parametroTablaSeleccionado = null;
        activoBtnsPaginas = true;
        aporteTablaSeleccionado = null;
        k = 0;
        listaParametrosAutoliq = null;
        listaAportesEntidades = null;
        guardado = true;
        cambiosParametro = false;
        cambiosAporte = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void asignarIndex(ParametrosAutoliq parametro, int LND, int dialogo) {
        parametroTablaSeleccionado = parametro;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        System.out.println("tipo actualizacion aignar index : " + tipoActualizacion);
        if (dialogo == 1) {
            RequestContext.getCurrentInstance().update("formularioLovTipoTrabajador:TipoTrabajadorDialogo");
            PrimefacesContextUI.ejecutar("PF('TipoTrabajadorDialogo').show()");
            modificarInfoRegistroTiposTrabajadores(lovTiposTrabajadores.size());
        }
        if (dialogo == 2) {
            RequestContext.getCurrentInstance().update("formularioLovEmpresa:EmpresaDialogo");
            PrimefacesContextUI.ejecutar("PF('EmpresaDialogo').show()");
            modificarInfoRegistroEmpresa(lovEmpresas.size());
        }
    }

    public void asignarIndexAporte(AportesEntidades aporte, int LND, int dialogo) {
        aporteTablaSeleccionado = aporte;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        System.out.println("tipo actualizacion aignar index aporte : " + tipoActualizacion);
        if (dialogo == 1) {
            modificarInfoRegistroEmpleados(lovEmpleados.size());
            RequestContext.getCurrentInstance().update("formularioLovEmpleado:EmpleadoDialogo");
            PrimefacesContextUI.ejecutar("PF('EmpleadoDialogo').show()");
        }
        if (dialogo == 2) {
            modificarInfoRegistroTercero(lovTerceros.size());
            RequestContext.getCurrentInstance().update("formularioLovTercero:TerceroDialogo");
            PrimefacesContextUI.ejecutar("PF('TerceroDialogo').show()");
        }
        if (dialogo == 3) {
            modificarInfoRegistroTiposEntidades(lovTiposEntidades.size());
            RequestContext.getCurrentInstance().update("formularioLovTipoEntidad:TipoEntidadDialogo");
            PrimefacesContextUI.ejecutar("PF('TipoEntidadDialogo').show()");
        }
    }

    public void actualizarTipoTrabajador() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                parametroTablaSeleccionado.setTipotrabajador(tipoTrabajadorSeleccionado);
                if (!listParametrosAutoliqCrear.contains(parametroTablaSeleccionado)) {
                    if (listParametrosAutoliqModificar.isEmpty()) {
                        listParametrosAutoliqModificar.add(parametroTablaSeleccionado);
                    } else if (!listParametrosAutoliqModificar.contains(parametroTablaSeleccionado)) {
                        listParametrosAutoliqModificar.add(parametroTablaSeleccionado);
                    }
                }
            } else {
                parametroTablaSeleccionado.setTipotrabajador(tipoTrabajadorSeleccionado);
                if (!listParametrosAutoliqCrear.contains(parametroTablaSeleccionado)) {
                    if (listParametrosAutoliqModificar.isEmpty()) {
                        listParametrosAutoliqModificar.add(parametroTablaSeleccionado);
                    } else if (!listParametrosAutoliqModificar.contains(parametroTablaSeleccionado)) {
                        listParametrosAutoliqModificar.add(parametroTablaSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            cambiosParametro = true;
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
        activoBtnsPaginas = true;
        RequestContext.getCurrentInstance().update("form:novedadauto");
        RequestContext.getCurrentInstance().update("form:incaPag");
        RequestContext.getCurrentInstance().update("form:eliminarToda");
        RequestContext.getCurrentInstance().update("form:procesoLiq");
        RequestContext.getCurrentInstance().update("form:acumDif");
        tipoActualizacion = -1;/*
         RequestContext.getCurrentInstance().update("formularioLovTipoTrabajador:TipoTrabajadorDialogo");
         RequestContext.getCurrentInstance().update("formularioLovTipoTrabajador:lovTipoTrabajador");
         RequestContext.getCurrentInstance().update("formularioLovTipoTrabajador:aceptarTT");*/

        context.reset("formularioLovTipoTrabajador:lovTipoTrabajador:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTipoTrabajador').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('TipoTrabajadorDialogo').hide()");
    }

    public void cancelarCambioTipoTrabajador() {
        filtrarLovTiposTrabajadores = null;
        tipoTrabajadorSeleccionado = new TiposTrabajadores();
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        activoBtnsPaginas = true;
        RequestContext.getCurrentInstance().update("form:novedadauto");
        RequestContext.getCurrentInstance().update("form:incaPag");
        RequestContext.getCurrentInstance().update("form:eliminarToda");
        RequestContext.getCurrentInstance().update("form:procesoLiq");
        RequestContext.getCurrentInstance().update("form:acumDif");
        tipoActualizacion = -1;
        permitirIndex = true;
        context.reset("formularioLovTipoTrabajador:lovTipoTrabajador:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTipoTrabajador').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('TipoTrabajadorDialogo').hide()");
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
        activoBtnsPaginas = true;
        RequestContext.getCurrentInstance().update("form:novedadauto");
        RequestContext.getCurrentInstance().update("form:incaPag");
        RequestContext.getCurrentInstance().update("form:eliminarToda");
        RequestContext.getCurrentInstance().update("form:procesoLiq");
        RequestContext.getCurrentInstance().update("form:acumDif");
        tipoActualizacion = -1;/*
         RequestContext.getCurrentInstance().update("formularioLovEmpresa:EmpresaDialogo");
         RequestContext.getCurrentInstance().update("formularioLovEmpresa:lovEmpresa");
         RequestContext.getCurrentInstance().update("formularioLovEmpresa:aceptarE");*/

        context.reset("formularioLovEmpresa:lovEmpresa:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpresa').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpresaDialogo').hide()");
    }

    public void cancelarCambioEmpresa() {
        filtrarLovEmpresas = null;
        empresaSeleccionada = new Empresas();
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        activoBtnsPaginas = true;
        RequestContext.getCurrentInstance().update("form:novedadauto");
        RequestContext.getCurrentInstance().update("form:incaPag");
        RequestContext.getCurrentInstance().update("form:eliminarToda");
        RequestContext.getCurrentInstance().update("form:procesoLiq");
        RequestContext.getCurrentInstance().update("form:acumDif");
        tipoActualizacion = -1;
        permitirIndex = true;
        context.reset("formularioLovEmpresa:lovEmpresa:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpresa').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpresaDialogo').hide()");
    }

    public void actualizarEmpleado() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaAporte == 0) {
                aporteTablaSeleccionado.setEmpleado(empleadoSeleccionado);
                if (listAportesEntidadesModificar.isEmpty()) {
                    listAportesEntidadesModificar.add(aporteTablaSeleccionado);
                } else if (!listAportesEntidadesModificar.contains(aporteTablaSeleccionado)) {
                    listAportesEntidadesModificar.add(aporteTablaSeleccionado);
                }
            } else {
                aporteTablaSeleccionado.setEmpleado(empleadoSeleccionado);
                if (listAportesEntidadesModificar.isEmpty()) {
                    listAportesEntidadesModificar.add(aporteTablaSeleccionado);
                } else if (!listAportesEntidadesModificar.contains(aporteTablaSeleccionado)) {
                    listAportesEntidadesModificar.add(aporteTablaSeleccionado);
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndexAporte = true;
            cambiosAporte = true;
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
        } else if (tipoActualizacion == 1) {
            nuevoAporteEntidad.setEmpleado(empleadoSeleccionado);
            System.out.println("empleado seleccionado :" + empleadoSeleccionado.getSecuencia());
            System.out.println("nuevoaporteempleado empleado seleccionado : " + nuevoAporteEntidad.getEmpleado().getSecuencia());
//            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoApEntidad");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodempl");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNomEmpl");
        } else if (tipoActualizacion == 2) {
            duplicarAporteEntidad.setEmpleado(empleadoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodempl");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNomEmpl");
        }

        filtrarLovEmpleados = null;
//        empleadoSeleccionado = new Empleados();
        aceptar = true;
        tipoActualizacion = -1;/*
         RequestContext.getCurrentInstance().update("formularioLovEmpleado:EmpleadoDialogo");
         RequestContext.getCurrentInstance().update("formularioLovEmpleado:lovEmpleado");
         RequestContext.getCurrentInstance().update("formularioLovEmpleado:aceptarEMPL");*/

        context.reset("formularioLovEmpleado:lovEmpleado:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpleado').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpleadoDialogo').hide()");
    }

    public void cancelarCambioEmpleado() {
        filtrarLovEmpleados = null;
        empleadoSeleccionado = new Empleados();
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndexAporte = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioLovEmpleado:lovEmpleado:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpleado').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpleadoDialogo').hide()");
    }

    public void actualizarTercero() {
        System.out.println("tipo actualización actualizar tercero : " + tipoActualizacion);
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaAporte == 0) {
                aporteTablaSeleccionado.setTerceroRegistro(terceroSeleccionado);
                if (!listAportesEntidadesCrear.contains(terceroSeleccionado)) {
                    if (listAportesEntidadesModificar.isEmpty()) {
                        listAportesEntidadesModificar.add(aporteTablaSeleccionado);
                    } else if (!listAportesEntidadesModificar.contains(aporteTablaSeleccionado)) {
                        listAportesEntidadesModificar.add(aporteTablaSeleccionado);
                    }
                }
            } else {
                aporteTablaSeleccionado.setTerceroRegistro(terceroSeleccionado);
                if (!listAportesEntidadesCrear.contains(terceroSeleccionado)) {
                    if (listAportesEntidadesModificar.isEmpty()) {
                        listAportesEntidadesModificar.add(aporteTablaSeleccionado);
                    } else if (!listAportesEntidadesModificar.contains(aporteTablaSeleccionado)) {
                        listAportesEntidadesModificar.add(aporteTablaSeleccionado);
                    }

                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndexAporte = true;
            cambiosAporte = true;
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
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
        tipoActualizacion = -1;/*
         */

        context.reset("formularioLovTercero:lovTercero:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTercero').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('TerceroDialogo').hide()");
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
        PrimefacesContextUI.ejecutar("PF('lovTercero').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('TerceroDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioLovTercero:TerceroDialogo");
        RequestContext.getCurrentInstance().update("formularioLovTercero:lovTercero");
        RequestContext.getCurrentInstance().update("formularioLovTercero:aceptarT");
    }

    public void actualizarTipoEntidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaAporte == 0) {
                aporteTablaSeleccionado.setTipoentidad(tipoEntidadSeleccionado);
                if (listAportesEntidadesModificar.isEmpty()) {
                    listAportesEntidadesModificar.add(aporteTablaSeleccionado);
                } else if (!listAportesEntidadesModificar.contains(aporteTablaSeleccionado)) {
                    listAportesEntidadesModificar.add(aporteTablaSeleccionado);
                }
            } else {
                aporteTablaSeleccionado.setTipoentidad(tipoEntidadSeleccionado);
                if (listAportesEntidadesModificar.isEmpty()) {
                    listAportesEntidadesModificar.add(aporteTablaSeleccionado);
                } else if (!listAportesEntidadesModificar.contains(aporteTablaSeleccionado)) {
                    listAportesEntidadesModificar.add(aporteTablaSeleccionado);
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndexAporte = true;
            cambiosAporte = true;
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
        } else if (tipoActualizacion == 1) {
            nuevoAporteEntidad.setTipoentidad(tipoEntidadSeleccionado);
            System.out.println("tipo entidad seleccionado : " + tipoEntidadSeleccionado);
            System.out.println("nuevoaporteempleado tipoentidad : " + nuevoAporteEntidad.getTipoentidad().getSecuencia());
//            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoApEntidad");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevotipoentidad");
        } else if (tipoActualizacion == 2) {
            duplicarAporteEntidad.setTipoentidad(tipoEntidadSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicartipoentidad");
        }
        filtrarLovTiposEntidades = null;
        tipoEntidadSeleccionado = new TiposEntidades();
        aceptar = true;
        tipoActualizacion = -1;/*
         RequestContext.getCurrentInstance().update("formularioLovTipoEntidad:TipoEntidadDialogo");
         RequestContext.getCurrentInstance().update("formularioLovTipoEntidad:lovTipoEntidad");
         RequestContext.getCurrentInstance().update("formularioLovTipoEntidad:aceptarTE");*/

        context.reset("formularioLovTipoEntidad:lovTipoEntidad:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTipoEntidad').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('TipoEntidadDialogo').hide()");
    }

    public void cancelarCambioTipoEntidad() {
        filtrarLovTiposEntidades = null;
        tipoEntidadSeleccionado = new TiposEntidades();
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndexAporte = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioLovTipoEntidad:lovTipoEntidad:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTipoEntidad').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('TipoEntidadDialogo').hide()");
    }

    public void dispararDialogoBuscar() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (guardado == true) {
            RequestContext.getCurrentInstance().update("formularioLovAporteEntidad:BuscarAporteDialogo");
            PrimefacesContextUI.ejecutar("PF('BuscarAporteDialogo').show()");
            modificarInfoRegistroAportesEntidades(lovAportesEntidades.size());
        } else {
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }
    }

    public void actualizarAporteEntidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (banderaAporte == 1) {
            desactivarFiltradoAporteEntidad();
            banderaAporte = 0;
            filtrarListaAportesEntidades = null;
            tipoListaAporte = 0;
        }
        listaAportesEntidades.clear();
        listaAportesEntidades.add(aporteEntidadSeleccionado);
        aporteTablaSeleccionado = listaAportesEntidades.get(0);
        filtrarLovAportesEntidades = null;
        aporteEntidadSeleccionado = new AportesEntidades();
        aceptar = true;
        activoBtnsPaginas = true;
        RequestContext.getCurrentInstance().update("form:novedadauto");
        RequestContext.getCurrentInstance().update("form:incaPag");
        RequestContext.getCurrentInstance().update("form:eliminarToda");
        RequestContext.getCurrentInstance().update("form:procesoLiq");
        RequestContext.getCurrentInstance().update("form:acumDif");
        tipoActualizacion = -1;
        visibilidadMostrarTodos = "visible";
        RequestContext.getCurrentInstance().update("form:mostrarTodos");
        modificarInfoRegistroParametro(listaParametrosAutoliq.size());
        modificarInfoRegistroAporte(listaAportesEntidades.size());
        RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
        /*
         RequestContext.getCurrentInstance().update("formularioLovAporteEntidad:BuscarAporteDialogo");
         RequestContext.getCurrentInstance().update("formularioLovAporteEntidad:lovBuscarAporte");
         RequestContext.getCurrentInstance().update("formularioLovAporteEntidad:aceptarBA");*/
        context.reset("formularioLovAporteEntidad:lovBuscarAporte:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovBuscarAporte').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('BuscarAporteDialogo').hide()");
    }

    public void cancelarCambioAporteEntidad() {
        filtrarLovEmpresas = null;
        empresaSeleccionada = new Empresas();
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        activoBtnsPaginas = true;
        RequestContext.getCurrentInstance().update("form:novedadauto");
        RequestContext.getCurrentInstance().update("form:incaPag");
        RequestContext.getCurrentInstance().update("form:eliminarToda");
        RequestContext.getCurrentInstance().update("form:procesoLiq");
        RequestContext.getCurrentInstance().update("form:acumDif");
        tipoActualizacion = -1;
        permitirIndex = true;
        context.reset("formularioLovAporteEntidad:lovBuscarAporte:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovBuscarAporte').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('BuscarAporteDialogo').hide()");
    }

    public void mostrarTodosAporteEntidad() {
        RequestContext.getCurrentInstance().update("form:mostrarTodos");
        //index = indexAUX;
        aporteTablaSeleccionado = null;
        if (banderaAporte == 1) {
            desactivarFiltradoAporteEntidad();
            banderaAporte = 0;
            filtrarListaAportesEntidades = null;
            tipoListaAporte = 0;
        }
        activoBtnsPaginas = false;
        cargarDatosNuevos();
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualTabla == 1) {
            if (parametroTablaSeleccionado != null) {
                if (cualCelda == 2) {
                    RequestContext.getCurrentInstance().update("formularioLovTipoTrabajador:TipoTrabajadorDialogo");
                    PrimefacesContextUI.ejecutar("PF('TipoTrabajadorDialogo').show()");
                    tipoActualizacion = 0;
                }
            }
        } else if (cualTabla == 2) {

            if (aporteTablaSeleccionado != null) {
                if (cualCeldaAporte == 3) {
                    RequestContext.getCurrentInstance().update("formularioLovEmpleado:EmpleadoDialogo");
                    PrimefacesContextUI.ejecutar("PF('EmpleadoDialogo').show()");
                    tipoActualizacion = 0;
                }
                if (cualCeldaAporte == 4) {
                    RequestContext.getCurrentInstance().update("formularioLovTercero:TerceroDialogo");
                    PrimefacesContextUI.ejecutar("PF('TerceroDialogo').show()");
                    tipoActualizacion = 0;
                }
                if (cualCeldaAporte == 6) {
                    RequestContext.getCurrentInstance().update("formularioLovTipoEntidad:TipoEntidadDialogo");
                    PrimefacesContextUI.ejecutar("PF('TipoEntidadDialogo').show()");
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
            if (parametroTablaSeleccionado != null) {
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
            if (parametroTablaSeleccionado != null) {
                nombre = "ParametrosAutoliquidacion_XML";
            }
        } else if (cualTabla == 2) {
            if (aporteTablaSeleccionado != null) {
                nombre = "AportesEntidades_XML";
            }
        }

        return nombre;
    }

    public void validarExportPDF() throws IOException {
        if (cualTabla == 1) {
            if (parametroTablaSeleccionado != null) {
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
        exporter.export(context, tabla, "ParametrosAutoliquidacion_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        RequestContext context2 = RequestContext.getCurrentInstance();
        activoBtnsPaginas = true;
        context2.update("form:novedadauto");
        context2.update("form:incaPag");
        context2.update("form:eliminarToda");
        context2.update("form:procesoLiq");
        context2.update("form:acumDif");
    }

    public void exportPDF_AE() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosAporteEntidadExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "AportesEntidades_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void validarExportXLS() throws IOException {
        if (cualTabla == 1) {
            if (parametroTablaSeleccionado != null) {
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
        exporter.export(context, tabla, "ParametrosAutoliquidacion_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        RequestContext context2 = RequestContext.getCurrentInstance();
        activoBtnsPaginas = true;
        context2.update("form:novedadauto");
        context2.update("form:incaPag");
        context2.update("form:eliminarToda");
        context2.update("form:procesoLiq");
        context2.update("form:acumDif");
    }

    public void exportXLS_AE() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosAporteEntidadExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "AportesEntidades_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void eventoFiltrar() {
        if (parametroTablaSeleccionado != null) {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            modificarInfoRegistroParametro(filtrarListaParametrosAutoliq.size());
        } else {
            modificarInfoRegistroParametro(0);
        }
    }

    public void eventoFiltrarAportes() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (aporteTablaSeleccionado != null) {
            if (tipoListaAporte == 0) {
                tipoListaAporte = 1;
            }
            modificarInfoRegistroAporte(filtrarListaAportesEntidades.size());
//            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
        }
    }

    public void verificarRastroTablas() {
        if (parametroTablaSeleccionado != null) {
            verificarRastro();
        }
        if (aporteTablaSeleccionado != null) {
            verificarRastroAporteEntidad();
        }
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (parametroTablaSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(parametroTablaSeleccionado.getSecuencia(), "PARAMETROSAUTOLIQ");
            backUpSecRegistro = parametroTablaSeleccionado.getSecuencia();
            if (resultado == 1) {
                PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
            } else if (resultado == 2) {
                PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
            } else if (resultado == 3) {
                PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
            } else if (resultado == 4) {
                PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
            } else if (resultado == 5) {
                PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
            }
        } else if (administrarRastros.verificarHistoricosTabla("PARAMETROSAUTOLIQ")) {
            PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
        } else {
            PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
        }
        activoBtnsPaginas = true;
        RequestContext.getCurrentInstance().update("form:novedadauto");
        RequestContext.getCurrentInstance().update("form:incaPag");
        RequestContext.getCurrentInstance().update("form:eliminarToda");
        RequestContext.getCurrentInstance().update("form:procesoLiq");
        RequestContext.getCurrentInstance().update("form:acumDif");
    }

    public void verificarRastroAporteEntidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaAportesEntidades != null) {
            if (aporteTablaSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(aporteTablaSeleccionado.getSecuencia(), "APORTESENTIDADES");
                backUpSecRegistroAporte = aporteTablaSeleccionado.getSecuencia();
                if (resultado == 1) {
                    PrimefacesContextUI.ejecutar("PF('errorObjetosDBAporte').show()");
                } else if (resultado == 2) {
                    PrimefacesContextUI.ejecutar("PF('confirmarRastroAporte').show()");
                } else if (resultado == 3) {
                    PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
                } else if (resultado == 5) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
                }
            } else {
                PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
            }
        } else if (administrarRastros.verificarHistoricosTabla("APORTESENTIDADES")) {
            PrimefacesContextUI.ejecutar("PF('confirmarRastroHistoricoAporte').show()");
        } else {
            PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
        }
    }

    public void recordarSeleccion() {
        if (parametroTablaSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosParametroAuto");
            tablaC.setSelection(parametroTablaSeleccionado);
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

    public void modificarInfoRegistroAportesEntidades(int valor) {
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
        modificarInfoRegistroAportesEntidades(filtrarLovAportesEntidades.size());
    }

    public void contarRegistrosAporte() {
        if (listaAportesEntidades != null) {
            if (!listaAportesEntidades.isEmpty()) {
                modificarInfoRegistroAporte(listaAportesEntidades.size());
            }
        } else {
            modificarInfoRegistroAporte(0);
        }
    }

    public void contarRegistrosParametros() {
        if (listaParametrosAutoliq != null) {
            if (!listaParametrosAutoliq.isEmpty()) {
                modificarInfoRegistroParametro(listaParametrosAutoliq.size());
            }
        } else {
            modificarInfoRegistroParametro(0);
        }
    }

    //GET - SET//
    public List<ParametrosAutoliq> getListaParametrosAutoliq() {
        try {
            if (listaParametrosAutoliq == null) {
                listaParametrosAutoliq = administrarParametroAutoliq.consultarParametrosAutoliq();
                if (listaParametrosAutoliq != null) {

                    for (int i = 0; i < listaParametrosAutoliq.size(); i++) {
                        if (listaParametrosAutoliq.get(i).getTipotrabajador() == null) {
                            listaParametrosAutoliq.get(i).setTipotrabajador(new TiposTrabajadores());
                        }
                    }
                }
            }
            return listaParametrosAutoliq;
        } catch (Exception e) {
            System.out.println("Error !!!!!!!!! getListaParametrosAutoliq : " + e.toString());
            return null;
        }
    }

    public void setListaParametrosAutoliq(List<ParametrosAutoliq> listaParametrosAutoliq) {
        this.listaParametrosAutoliq = listaParametrosAutoliq;
    }

    public List<ParametrosAutoliq> getFiltrarListaParametrosAutoliq() {
        return filtrarListaParametrosAutoliq;
    }

    public void setFiltrarListaParametrosAutoliq(List<ParametrosAutoliq> filtrarListaParametrosAutoliq) {
        this.filtrarListaParametrosAutoliq = filtrarListaParametrosAutoliq;
    }

    public ParametrosAutoliq getParametroTablaSeleccionado() {
        return parametroTablaSeleccionado;
    }

    public void setParametroTablaSeleccionado(ParametrosAutoliq parametroTablaSeleccionado) {
        this.parametroTablaSeleccionado = parametroTablaSeleccionado;
    }

    public ParametrosAutoliq getNuevoParametro() {
        return nuevoParametro;
    }

    public void setNuevoParametro(ParametrosAutoliq nuevoParametro) {
        this.nuevoParametro = nuevoParametro;
    }

    public ParametrosAutoliq getEditarParametro() {
        return editarParametro;
    }

    public void setEditarParametro(ParametrosAutoliq editarParametro) {
        this.editarParametro = editarParametro;
    }

    public ParametrosAutoliq getDuplicarParametro() {
        return duplicarParametro;
    }

    public void setDuplicarParametro(ParametrosAutoliq duplicarParametro) {
        this.duplicarParametro = duplicarParametro;
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

    public List<TiposTrabajadores> getLovTiposTrabajadores() {
        if (lovTiposTrabajadores == null) {
            lovTiposTrabajadores = administrarParametroAutoliq.lovTiposTrabajadores();
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

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public List<Empresas> getLovEmpresas() {
        lovEmpresas = administrarParametroAutoliq.lovEmpresas();
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

    public BigInteger getBackUpSecRegistro() {
        return backUpSecRegistro;
    }

    public void setBackUpSecRegistro(BigInteger backUpSecRegistro) {
        this.backUpSecRegistro = backUpSecRegistro;
    }

    public List<AportesEntidades> getListaAportesEntidades() {
        try {
            if (listaAportesEntidades == null) {
                if (parametroTablaSeleccionado != null) {
                    listaAportesEntidades = administrarParametroAutoliq.consultarAportesEntidadesPorParametroAutoliq(parametroTablaSeleccionado.getEmpresa().getSecuencia(), parametroTablaSeleccionado.getMes(), parametroTablaSeleccionado.getAno());
                    if (listaAportesEntidades != null) {
                            for (int i = 0; i < listaAportesEntidades.size(); i++) {
                                if (listaAportesEntidades.get(i).getTerceroRegistro() == null) {
                                    listaAportesEntidades.get(i).setTerceroRegistro(null);
                                }
                            }
                        
                    }
                }
            }
            modificarInfoRegistroAporte(listaAportesEntidades.size());
            return listaAportesEntidades;
        } catch (Exception e) {
            System.out.println("Error !!!!!!!!!!! getListaAportesEntidades : " + e.toString());
            return null;
        }
    }

    public void setListaAportesEntidades(List<AportesEntidades> listaAportesEntidades) {
        this.listaAportesEntidades = listaAportesEntidades;
    }

    public List<AportesEntidades> getFiltrarListaAportesEntidades() {
        return filtrarListaAportesEntidades;
    }

    public void setFiltrarListaAportesEntidades(List<AportesEntidades> filtrarListaAportesEntidades) {
        this.filtrarListaAportesEntidades = filtrarListaAportesEntidades;
    }

    public AportesEntidades getAporteTablaSeleccionado() {
        return aporteTablaSeleccionado;
    }

    public void setAporteTablaSeleccionado(AportesEntidades aporteTablaSeleccionado) {
        this.aporteTablaSeleccionado = aporteTablaSeleccionado;
    }

    public AportesEntidades getEditarAporteEntidad() {
        return editarAporteEntidad;
    }

    public void setEditarAporteEntidad(AportesEntidades editarAporteEntidad) {
        this.editarAporteEntidad = editarAporteEntidad;
    }

    public BigInteger getBackUpSecRegistroAporte() {
        return backUpSecRegistroAporte;
    }

    public void setBackUpSecRegistroAporte(BigInteger backUpSecRegistroAporte) {
        this.backUpSecRegistroAporte = backUpSecRegistroAporte;
    }

    public List<Empleados> getLovEmpleados() {
        if (lovEmpleados == null) {
            lovEmpleados = administrarParametroAutoliq.lovEmpleados();
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
            lovTerceros = administrarParametroAutoliq.lovTerceros();
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
            lovTiposEntidades = administrarParametroAutoliq.lovTiposEntidades();
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

    public String getAltoTablaAporte() {
        return altoTablaAporte;
    }

    public void setAltoTablaAporte(String altoTablaAporte) {
        this.altoTablaAporte = altoTablaAporte;
    }

    public List<AportesEntidades> getLovAportesEntidades() {
        if (lovAportesEntidades == null) {
            if (parametroTablaSeleccionado != null) {
                lovAportesEntidades = administrarParametroAutoliq.consultarAportesEntidadesPorParametroAutoliq(parametroTablaSeleccionado.getEmpresa().getSecuencia(), parametroTablaSeleccionado.getMes(), parametroTablaSeleccionado.getAno());
                if (lovAportesEntidades != null) {
                    for (int i = 0; i < lovAportesEntidades.size(); i++) {
                        if (lovAportesEntidades.get(i).getTercero() == null) {
                            lovAportesEntidades.get(i).setTerceroRegistro(new Terceros());
                        }
                    }
                }
            }
        }
        return lovAportesEntidades;
    }

    public void setLovAportesEntidades(List<AportesEntidades> lovAportesEntidades) {
        this.lovAportesEntidades = lovAportesEntidades;
    }

    public List<AportesEntidades> getFiltrarLovAportesEntidades() {
        return filtrarLovAportesEntidades;
    }

    public void setFiltrarLovAportesEntidades(List<AportesEntidades> filtrarLovAportesEntidades) {
        this.filtrarLovAportesEntidades = filtrarLovAportesEntidades;
    }

    public AportesEntidades getAporteEntidadSeleccionado() {
        return aporteEntidadSeleccionado;
    }

    public void setAporteEntidadSeleccionado(AportesEntidades aporteEntidadSeleccionado) {
        this.aporteEntidadSeleccionado = aporteEntidadSeleccionado;
    }

    public String getInfoRegistroAporteEntidad() {
        return infoRegistroAporteEntidad;
    }

    public void setInfoRegistroAporteEntidad(String infoRegistroAporteEntidad) {
        this.infoRegistroAporteEntidad = infoRegistroAporteEntidad;
    }

    public boolean isDisabledBuscar() {
        return disabledBuscar;
    }

    public void setDisabledBuscar(boolean disabledBuscar) {
        this.disabledBuscar = disabledBuscar;
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

    public ParametrosEstructuras getParametroEstructura() {
        getUsuario();
        if (usuario.getAlias() != null) {
            parametroEstructura = administrarParametroAutoliq.buscarParametroEstructura(usuario.getAlias());
        }
        return parametroEstructura;
    }

    public void setParametroEstructura(ParametrosEstructuras parametroEstructura) {
        this.parametroEstructura = parametroEstructura;
    }

    public ParametrosInformes getParametroInforme() {
        getUsuario();
        parametroInforme = administrarParametroAutoliq.buscarParametroInforme(usuario.getAlias());
        return parametroInforme;
    }

    public void setParametroInforme(ParametrosInformes parametroInforme) {
        this.parametroInforme = parametroInforme;
    }

    public ActualUsuario getUsuario() {
        if (usuario == null) {
            usuario = administrarParametroAutoliq.obtenerActualUsuario();
        }
        return usuario;
    }

    public void setUsuario(ActualUsuario usuario) {
        this.usuario = usuario;
    }

    public boolean isActivoBtnsPaginas() {
        return activoBtnsPaginas;
    }

    public void setActivoBtnsPaginas(boolean activoBtnsPaginas) {
        this.activoBtnsPaginas = activoBtnsPaginas;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getVisibilidadMostrarTodos() {
        return visibilidadMostrarTodos;
    }

    public void setVisibilidadMostrarTodos(String visibilidadMostrarTodos) {
        this.visibilidadMostrarTodos = visibilidadMostrarTodos;
    }

    public AportesEntidades getNuevoAporteEntidad() {
        return nuevoAporteEntidad;
    }

    public void setNuevoAporteEntidad(AportesEntidades nuevoAporteEntidad) {
        this.nuevoAporteEntidad = nuevoAporteEntidad;
    }

    public AportesEntidades getDuplicarAporteEntidad() {
        return duplicarAporteEntidad;
    }

    public void setDuplicarAporteEntidad(AportesEntidades duplicarAporteEntidad) {
        this.duplicarAporteEntidad = duplicarAporteEntidad;
    }

}
