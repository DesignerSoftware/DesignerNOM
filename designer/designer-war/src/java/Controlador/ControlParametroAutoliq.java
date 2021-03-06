package Controlador;

import Entidades.ActualUsuario;
import Entidades.AportesEntidades;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.ParametrosAutoliq;
import Entidades.ParametrosEstructuras;
import Entidades.ParametrosInformes;
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
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import ControlNavegacion.ListasRecurrentes;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
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

    private static Logger log = Logger.getLogger(ControlParametroAutoliq.class);

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
    private String altoTablaAporte, altoTablaRows;
    //
    private Column parametroAno, parametroTipoTrabajador, parametroEmpresa, mesParametro;
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
    private boolean activarLov;
    private DataTable tablaC;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private String msgError, msgErrorAux;
    private ListasRecurrentes listasRecurrentes;

    public ControlParametroAutoliq() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        listasRecurrentes = controlListaNavegacion.getListasRecurrentes();
        visibilidadMostrarTodos = "hidden";
        numero = 7;
        activoBtnsPaginas = false;
        disabledBuscar = true;
        altoTabla = "56";
        altoTablaAporte = "165";
        altoTablaRows = "6";
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
        nuevoAporteEntidad.setTipoentidad(new TiposEntidades());
        ///
        duplicarAporteEntidad = new AportesEntidades();
        duplicarAporteEntidad.setAno(aporteTablaSeleccionado.getAno());
        duplicarAporteEntidad.setMes(aporteTablaSeleccionado.getMes());
        duplicarAporteEntidad.setEmpresa(aporteTablaSeleccionado.getEmpresa());
        duplicarAporteEntidad.setTipoentidad(new TiposEntidades());
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
        mapParametros.put("paginaAnterior", paginaAnterior);
        activarLov = true;
        msgError = "";
        msgErrorAux = "";
    }

    public void limpiarListasValor() {
        lovAportesEntidades = null;
        lovEmpleados = null;
        lovEmpresas = null;
        lovTerceros = null;
        lovTiposEntidades = null;
        lovTiposTrabajadores = null;
    }

    @PreDestroy
    public void destruyendoce() {
        log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarParametroAutoliq.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());

        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listaParametrosAutoliq = null;
        getListaParametrosAutoliq();
        if (listaParametrosAutoliq != null) {
            if (!listaParametrosAutoliq.isEmpty()) {
                parametroTablaSeleccionado = listaParametrosAutoliq.get(0);
            }
        }
//      listaAportesEntidades = null;
//      getListaAportesEntidades();
//      if (listaAportesEntidades != null) {
//         if (!listaAportesEntidades.isEmpty()) {
//            aporteTablaSeleccionado = listaAportesEntidades.get(0);
//         }
//      }
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        listaParametrosAutoliq = null;
        listaAportesEntidades = null;
        getListaParametrosAutoliq();
        getListaAportesEntidades();
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

    //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "parametroautoliq";
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
        } else {
            controlListaNavegacion.guardarNavegacion(pagActual, pag);
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
            //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
            //mapParaEnviar.put("paginaAnterior", pagActual);
            //mas Parametros
            //         if (pag.equals("rastrotabla")) {
            //           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
            //      } else if (pag.equals("rastrotablaH")) {
            //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //     controlRastro.historicosTabla("Conceptos", pagActual);
            //   pag = "rastrotabla";
            //}
        }
        limpiarListasValor();
    }

    public void modificarParametroAutoliq(ParametrosAutoliq parametro) {
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
            RequestContext.getCurrentInstance().update("form:eliminarToda");
            RequestContext.getCurrentInstance().update("form:procesoLiq");
//         RequestContext.getCurrentInstance().update("form:acumDif");
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
            RequestContext.getCurrentInstance().update("form:eliminarToda");
            RequestContext.getCurrentInstance().update("form:procesoLiq");
//         RequestContext.getCurrentInstance().update("form:acumDif");
        }
        RequestContext.getCurrentInstance().update("form:datosParametroAuto");
    }

    public void modificarAporteEntidad(AportesEntidades aporte) {
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
                    RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
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
                RequestContext.getCurrentInstance().update("form:eliminarToda");
                RequestContext.getCurrentInstance().update("form:procesoLiq");
//            RequestContext.getCurrentInstance().update("form:acumDif");
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
                RequestContext.getCurrentInstance().update("form:eliminarToda");
                RequestContext.getCurrentInstance().update("form:procesoLiq");
//            RequestContext.getCurrentInstance().update("form:acumDif");
            }
        }
        RequestContext.getCurrentInstance().update("form:datosParametroAuto");
    }

    public void modificarAporteEntidad(AportesEntidades aporte, String confirmarCambio, String valorConfirmar) {
        aporteTablaSeleccionado = aporte;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        log.info("valor confirmar : " + valorConfirmar);
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
            log.info("entro a  modificarTercero");
            if (!valorConfirmar.isEmpty()) {
                aporteTablaSeleccionado.setNittercero(aporteTablaSeleccionado.getNittercero());
                aporteTablaSeleccionado.setNombretercero(aporteTablaSeleccionado.getNombretercero());
                for (int i = 0; i < lovTerceros.size(); i++) {
                    if (lovTerceros.get(i).getStrNit().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    aporteTablaSeleccionado.setTercero(lovTerceros.get(indiceUnicoElemento).getSecuencia());
                    aporteTablaSeleccionado.setNittercero(lovTerceros.get(indiceUnicoElemento).getNit());
                    aporteTablaSeleccionado.setNombretercero(lovTerceros.get(indiceUnicoElemento).getNombre());
                    lovTerceros.clear();
                    getLovTerceros();
                } else {
                    permitirIndexAporte = false;
                    RequestContext.getCurrentInstance().update("formularioLovTercero:TerceroDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
                    tipoActualizacion = 0;
                }

                log.info("aporte tabla seleccionado modificado, secuencia = " + aporteTablaSeleccionado.getTercero());
                log.info("aporte tabla seleccionado modificado, nombre = " + aporteTablaSeleccionado.getNombretercero());
                log.info("aporte tabla seleccionado modificado, nit tercero = " + aporteTablaSeleccionado.getNittercero());
            } else {
                coincidencias = 1;
                aporteTablaSeleccionado.setTercero(BigInteger.ZERO);
                aporteTablaSeleccionado.setNittercero(Long.valueOf(0));
                aporteTablaSeleccionado.setNombretercero(" ");
                lovTerceros.clear();
                getLovTerceros();
            }
        }
        if (confirmarCambio.equalsIgnoreCase("EMPLEADO")) {
            aporteTablaSeleccionado.setEmpleado(aporteTablaSeleccionado.getEmpleado());
            aporteTablaSeleccionado.setNombrecompleto(aporteTablaSeleccionado.getNombrecompleto());
            aporteTablaSeleccionado.setCodigoempleado(aporteTablaSeleccionado.getCodigoempleado());
            for (int i = 0; i < lovEmpleados.size(); i++) {
                if (lovEmpleados.get(i).getNombreCompleto().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                aporteTablaSeleccionado.setEmpleado(lovEmpleados.get(indiceUnicoElemento).getSecuencia());
                aporteTablaSeleccionado.setNombrecompleto(lovEmpleados.get(indiceUnicoElemento).getNombreCompleto());
                aporteTablaSeleccionado.setCodigoempleado(lovEmpleados.get(indiceUnicoElemento).getCodigoempleado());
//                lovEmpleados.clear();
//                getLovEmpleados();
            } else {
                permitirIndexAporte = false;
                RequestContext.getCurrentInstance().update("formularioLovEmpleado:EmpleadoDialogo");
                RequestContext.getCurrentInstance().execute("PF('EmpleadoDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (listAportesEntidadesModificar.isEmpty()) {
                listAportesEntidadesModificar.add(aporteTablaSeleccionado);
            } else if (!listAportesEntidadesModificar.contains(aporteTablaSeleccionado)) {
                listAportesEntidadesModificar.add(aporteTablaSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            cambiosAporte = true;
        } else {
            if (listAportesEntidadesModificar.isEmpty()) {
                listAportesEntidadesModificar.add(aporteTablaSeleccionado);
            } else if (!listAportesEntidadesModificar.contains(aporteTablaSeleccionado)) {
                listAportesEntidadesModificar.add(aporteTablaSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            cambiosAporte = true;
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
        parametroTablaSeleccionado = listaParametrosAutoliq.get(indice);
        cambiarIndice(parametroTablaSeleccionado, columna);
//        log.info("parametrotablaseleccionado: " + parametroTablaSeleccionado);

    }

    public void posicionAporteEntidad() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        aporteEntidadSeleccionado = listaAportesEntidades.get(indice);
        cambiarIndiceAporteEntidad(aporteEntidadSeleccionado, columna);
    }

    public void cambiarIndice(ParametrosAutoliq parametro, int celda) {

        if (permitirIndex == true) {
            RequestContext context = RequestContext.getCurrentInstance();
            cualTabla = 1;
            parametroTablaSeleccionado = parametro;
            cualCelda = celda;
            activoBtnsPaginas = false;
            visibilidadMostrarTodos = "hidden";
            auxTipoTipoTrabajador = parametroTablaSeleccionado.getTipotrabajador().getNombre();

            if (cualCelda == 0) {
                deshabilitarBotonLov();
                parametroTablaSeleccionado.getAno();
            } else if (cualCelda == 1) {
                deshabilitarBotonLov();
                parametroTablaSeleccionado.getMes();
            } else if (cualCelda == 2) {
                habilitarBotonLov();
                parametroTablaSeleccionado.getTipotrabajador().getNombre();
            } else if (cualCelda == 3) {
                deshabilitarBotonLov();
                parametroTablaSeleccionado.getEmpresa().getNombre();
            }
            cargarDatosNuevos();
        }
    }

    public void cargarDatosNuevos() {
        try {
            listaAportesEntidades = null;
            getListaAportesEntidades();
//            Thread.sleep(2000L);
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
            RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
        } catch (Exception e) {
            log.warn("Error cargarDatosNuevos Controlador : " + e.getMessage());
        }
        contarRegistrosAporte();
    }

    public void cambiarIndiceAporteEntidad(AportesEntidades aporte, int celda) {
        if (permitirIndexAporte == true) {
            aporteTablaSeleccionado = aporte;
            cualCeldaAporte = celda;
            cualTabla = 2;
            if (cualCeldaAporte == 0) {
                deshabilitarBotonLov();
//            aporteTablaSeleccionado.getEmpleado().getCodigoempleado();
            } else if (cualCeldaAporte == 1) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getAno();
            } else if (cualCeldaAporte == 2) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getMes();
            } else if (cualCeldaAporte == 3) {
                habilitarBotonLov();
//            aporteTablaSeleccionado.getEmpleado().getNombreCompleto();
            } else if (cualCeldaAporte == 4) {
                habilitarBotonLov();
                aporteTablaSeleccionado.getNittercero();
            } else if (cualCeldaAporte == 5) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getNombretercero();
            } else if (cualCeldaAporte == 6) {
                habilitarBotonLov();
                aporteTablaSeleccionado.getTipoentidad().getNombre();
            } else if (cualCeldaAporte == 7) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getCotizacion1();
            } else if (cualCeldaAporte == 8) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getCotizacion2();
            } else if (cualCeldaAporte == 9) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getCotizacion5();
            } else if (cualCeldaAporte == 10) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getNovedad3();
            } else if (cualCeldaAporte == 11) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getNovedad4();
            } else if (cualCeldaAporte == 12) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getNovedad5();
            } else if (cualCeldaAporte == 13) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getSalariobasico();
            } else if (cualCeldaAporte == 14) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getIbc();
            } else if (cualCeldaAporte == 15) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getIbcreferencia();
            } else if (cualCeldaAporte == 16) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getDiascotizados();
            } else if (cualCeldaAporte == 17) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getTipoaportante();
            } else if (cualCeldaAporte == 18) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getIng();
            } else if (cualCeldaAporte == 19) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getRet();
            } else if (cualCeldaAporte == 20) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getTda();
            } else if (cualCeldaAporte == 21) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getTaa();
            } else if (cualCeldaAporte == 22) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getVsp();
            } else if (cualCeldaAporte == 23) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getVte();
            } else if (cualCeldaAporte == 24) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getVst();
            } else if (cualCeldaAporte == 25) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getSln();
            } else if (cualCeldaAporte == 26) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getIge();
            } else if (cualCeldaAporte == 27) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getLma();
            } else if (cualCeldaAporte == 28) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getVac();
            } else if (cualCeldaAporte == 29) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getAvp();
            } else if (cualCeldaAporte == 30) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getVct();
            } else if (cualCeldaAporte == 31) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getIrp();
            } else if (cualCeldaAporte == 32) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getSus();
            } else if (cualCeldaAporte == 33) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getIntegral();
            } else if (cualCeldaAporte == 34) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getTarifaeps();
            } else if (cualCeldaAporte == 35) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getTarifaafp();
            } else if (cualCeldaAporte == 36) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getTarifactt();
            } else if (cualCeldaAporte == 37) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getCodigoctt();
            } else if (cualCeldaAporte == 38) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getAvpevalor();
            } else if (cualCeldaAporte == 39) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getAvppvalor();
            } else if (cualCeldaAporte == 40) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getRetcontavpvalor();
            } else if (cualCeldaAporte == 41) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getCodigoneps();
            } else if (cualCeldaAporte == 42) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getCodigonafp();
            } else if (cualCeldaAporte == 43) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getEgvalor();
            } else if (cualCeldaAporte == 44) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getEgautorizacion();
            } else if (cualCeldaAporte == 45) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getMaternidadvalor();
            } else if (cualCeldaAporte == 46) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getMaternidadautorizacion();
            } else if (cualCeldaAporte == 47) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getUpcvalor();
            } else if (cualCeldaAporte == 48) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getTipo();
            } else if (cualCeldaAporte == 49) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getTipopensionado();
            } else if (cualCeldaAporte == 50) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getPensioncompartida();
            } else if (cualCeldaAporte == 51) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getExtranjero();
            } else if (cualCeldaAporte == 52) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getFechaorigen();
            } else if (cualCeldaAporte == 53) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getSubtipocotizanteSTR();
            } else if (cualCeldaAporte == 54) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getTarifacaja();
            } else if (cualCeldaAporte == 55) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getTarifasena();
            } else if (cualCeldaAporte == 56) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getTarifaicbf();
            } else if (cualCeldaAporte == 57) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getTarifaesap();
            } else if (cualCeldaAporte == 58) {
                deshabilitarBotonLov();
                aporteTablaSeleccionado.getTarifamen();
            }
        }
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void guardarYSalir() {
        guardadoGeneral();
        salir();
    }

    public void cancelarYSalir() {
//        cancelarModificacion();
        salir();
    }

    public void guardadoGeneral() {
        if (guardado == false) {
            RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').show()");
            guardarCambiosParametro();
            guardarCambiosAportes();
            visibilidadMostrarTodos = "hidden";
            RequestContext.getCurrentInstance().update("form:mostrarTodos");
        } else {
            log.info("no hay datos para guardar");
            FacesMessage msg = new FacesMessage("Información", "No se han realizado cambios a guardar");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void guardarCambiosParametro() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
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
//            if (listaParametrosAutoliq != null) {
            contarRegistrosParametros();
//            }
//            RequestContext.getCurrentInstance().update("form:infoRegistroParametro");
            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
            k = 0;
            activoBtnsPaginas = true;
            RequestContext.getCurrentInstance().update("form:novedadauto");
            RequestContext.getCurrentInstance().update("form:eliminarToda");
            RequestContext.getCurrentInstance().update("form:procesoLiq");
//         RequestContext.getCurrentInstance().update("form:acumDif");
            cambiosParametro = false;
            log.info("Se guardaron los datos de Parámetros de Liquidación con éxito");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception e) {
            log.warn("Error guardarCambiosParametro  Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Parámetros de Liquidación, Por favor intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void guardarCambiosAportes() {
        log.info("entró a guardar");
        RequestContext context = RequestContext.getCurrentInstance();
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
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
            contarRegistrosAporte();
            k = 0;
            cambiosAporte = true;
            RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");

            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception e) {
            log.warn("Error guardarCambiosAportes  Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Aporte Entidad, Por favor intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            altoTabla = "56";
            parametroAno = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroAno");
            parametroAno.setFilterStyle("display: none; visibility: hidden;");
            mesParametro = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroMes");
            mesParametro.setFilterStyle("display: none; visibility: hidden;");
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
//        if (listaParametrosAutoliq != null) {
        contarRegistrosParametros();
//        }
        listaAportesEntidades = null;
        getListaAportesEntidades();
        contarRegistrosAporte();
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
                    RequestContext.getCurrentInstance().execute("PF('editarAnoD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoTrabajadorD");
                    RequestContext.getCurrentInstance().execute("PF('editarTipoTrabajadorD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 3) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresaD");
                    RequestContext.getCurrentInstance().execute("PF('editarEmpresaD').show()");
                    cualCelda = -1;
                }
                activoBtnsPaginas = true;
                RequestContext.getCurrentInstance().update("form:novedadauto");
                RequestContext.getCurrentInstance().update("form:eliminarToda");
                RequestContext.getCurrentInstance().update("form:procesoLiq");
//            RequestContext.getCurrentInstance().update("form:acumDif");
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
                    RequestContext.getCurrentInstance().execute("PF('editarCodEmplD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarAnoAD");
                    RequestContext.getCurrentInstance().execute("PF('editarAnoAD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarMesAD");
                    RequestContext.getCurrentInstance().execute("PF('editarMesAD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 3) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreEmplD");
                    RequestContext.getCurrentInstance().execute("PF('editarNombreEmplD').show()");
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
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarEXTRD");
                    RequestContext.getCurrentInstance().execute("PF('editarEXTRD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 52) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaIngreso");
                    RequestContext.getCurrentInstance().execute("PF('editarFechaIngreso').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 54) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTarifaCajaD");
                    RequestContext.getCurrentInstance().execute("PF('editarTarifaCajaD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 55) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTarifaSenaD");
                    RequestContext.getCurrentInstance().execute("PF('editarTarifaSenaD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 56) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTarifaICBFD");
                    RequestContext.getCurrentInstance().execute("PF('editarTarifaICBFD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 57) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTarifaESAPD");
                    RequestContext.getCurrentInstance().execute("PF('editarTarifaESAPD').show()");
                    cualCeldaAporte = -1;
                } else if (cualCeldaAporte == 58) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTarifaMEND");
                    RequestContext.getCurrentInstance().execute("PF('editarTarifaMEND').show()");
                    cualCeldaAporte = -1;
                }
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void mostrarDialogoNuevoAporte() {
        nuevoAporteEntidad.setAno(parametroTablaSeleccionado.getAno());
        nuevoAporteEntidad.setMes(parametroTablaSeleccionado.getMes());
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
        if (nuevoParametro.getAno() > 0 && nuevoParametro.getMes() > 0
                && nuevoParametro.getEmpresa().getSecuencia() != null) {
            if (bandera == 1) {
                altoTabla = "56";
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                parametroAno = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroAno");
                parametroAno.setFilterStyle("display: none; visibility: hidden;");
                mesParametro = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroMes");
                mesParametro.setFilterStyle("display: none; visibility: hidden;");
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

            contarRegistrosParametros();

            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroParametro').hide()");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            activoBtnsPaginas = true;
//                RequestContext.getCurrentInstance().update("form:novedadauto");
//                RequestContext.getCurrentInstance().update("form:eliminarToda");
//                RequestContext.getCurrentInstance().update("form:procesoLiq");
//                RequestContext.getCurrentInstance().update("form:acumDif");
            cambiosParametro = true;
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorRegNullParametro').show()");
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
        contarRegistrosAporte();
        RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
        RequestContext.getCurrentInstance().execute("PF('nuevoAporteEntidad').hide()");
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

            duplicarAporteEntidad.setAno(aporteTablaSeleccionado.getAno());
            duplicarAporteEntidad.setMes(aporteTablaSeleccionado.getMes());
            duplicarAporteEntidad.setEmpresa(aporteTablaSeleccionado.getEmpresa());
            duplicarAporteEntidad.setTerceroRegistro(new Terceros());
            duplicarAporteEntidad.setTipoentidad(new TiposEntidades());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAporteEntidad");
            RequestContext.getCurrentInstance().execute("PF('duplicarAporteEntidad').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
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
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistrosAporte();
        RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
        RequestContext.getCurrentInstance().execute("PF('duplicarAporteEntidad').hide()");
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
                RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroParametro').show()");
            }
        } else if (cualTabla == 2) {
            if (aporteTablaSeleccionado != null) {
                duplicarAporteEntidad();
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro()");
        }
    }

    public void confirmarDuplicarParametroAutoliq() {
        if (duplicarParametro.getAno() > 0 && duplicarParametro.getMes() > 0
                && duplicarParametro.getEmpresa().getSecuencia() != null) {
            if (bandera == 1) {
                altoTabla = "56";
                FacesContext c = FacesContext.getCurrentInstance();
                parametroAno = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroAno");
                parametroAno.setFilterStyle("display: none; visibility: hidden;");
                mesParametro = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroMes");
                mesParametro.setFilterStyle("display: none; visibility: hidden;");
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
            contarRegistrosParametros();
            RequestContext.getCurrentInstance().update("form:infoRegistroParametro");
            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroParametro').hide()");
            activoBtnsPaginas = true;
            RequestContext.getCurrentInstance().update("form:novedadauto");
            RequestContext.getCurrentInstance().update("form:eliminarToda");
            RequestContext.getCurrentInstance().update("form:procesoLiq");
//         RequestContext.getCurrentInstance().update("form:acumDif");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            duplicarParametro = new ParametrosAutoliq();
            cambiosParametro = true;
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorRegNullParametro').show()");
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
    }

    public void limpiarDuplicarAporteEntidad() {
        duplicarAporteEntidad = new AportesEntidades();
        duplicarAporteEntidad.setAno(parametroTablaSeleccionado.getAno());
        duplicarAporteEntidad.setMes(parametroTablaSeleccionado.getMes());
        duplicarAporteEntidad.setEmpresa(parametroTablaSeleccionado.getEmpresa());
        duplicarAporteEntidad.setTerceroRegistro(new Terceros());
        duplicarAporteEntidad.setTipoentidad(new TiposEntidades());
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
                contarRegistrosParametros();
                RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
                RequestContext.getCurrentInstance().update("form:datosParametroAuto");
                parametroTablaSeleccionado = null;
                activoBtnsPaginas = true;
                cambiosParametro = true;
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorBorrarParametro').show()");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void borrarAporteEntidad() {
        if (aporteTablaSeleccionado != null) {
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
            contarRegistrosAporte();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
            aporteTablaSeleccionado = null;
            cambiosAporte = true;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void borrarAporteEntidadProcesoAutomatico() {
        RequestContext context = RequestContext.getCurrentInstance();
        msgError = "";
        msgErrorAux = "";
        if (listaAportesEntidades != null) {
            if (!listaAportesEntidades.isEmpty()) {
                msgError = administrarParametroAutoliq.borrarAportesEntidadesProcesoAutomatico(parametroTablaSeleccionado.getEmpresa().getSecuencia(), parametroTablaSeleccionado.getMes(), parametroTablaSeleccionado.getAno());
                msgErrorAux = administrarParametroAutoliq.ejecutarPKGEliminarAportesXDia(parametroTablaSeleccionado.getAno(), parametroTablaSeleccionado.getMes(), parametroTablaSeleccionado.getEmpresa().getSecuencia());
                if (msgError.equals("PROCESO_EXITOSO") && msgErrorAux.equals("PROCESO_EXITOSO")) {
                    listaAportesEntidades = null;
                    contarRegistrosAporte();
                    disabledBuscar = true;
                    visibilidadMostrarTodos = "hidden";
                    RequestContext.getCurrentInstance().update("form:mostrarTodos");
                    RequestContext.getCurrentInstance().update("form:buscar");
                    RequestContext.getCurrentInstance().update("form:infoRegistroAporte");
                    RequestContext.getCurrentInstance().update("form:infoRegistroParametro");
                    RequestContext.getCurrentInstance().update("form:datosParametroAuto");
                    RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
                    activoBtnsPaginas = true;
                    RequestContext.getCurrentInstance().update("formularioDialogos:procesoPilaOK");
                    RequestContext.getCurrentInstance().execute("PF('procesoPilaOK').show()");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    RequestContext.getCurrentInstance().update("formularioDialogos:errorProcesoPila");
                    RequestContext.getCurrentInstance().execute("PF('errorProcesoPila').show()");
                }
            }
        } else {
            FacesMessage msg = new FacesMessage("Información", "No hay información para borrar");
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
                    ano = parametroTablaSeleccionado.getAno();
                    mes = parametroTablaSeleccionado.getMes();

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
            log.warn("Error procesoLiquidacionAporteEntidad Controlador : " + e.toString());
        }
    }

    public void procesoLiquidacionOK() {
        RequestContext context = RequestContext.getCurrentInstance();
        msgError = "";
        msgErrorAux = "";
        String msgprocesar = "", msgeliminar = "";
        if (guardado == false) {
            guardadoGeneral();
        } else {

            getParametroEstructura();
            getParametroInforme();
            msgError = administrarParametroAutoliq.ejecutarPKGEliminarAportesXDia(parametroTablaSeleccionado.getAno(), parametroTablaSeleccionado.getMes(), parametroTablaSeleccionado.getEmpresa().getSecuencia());
            if ((msgError.equals("PROCESO_EXITOSO"))) {
                msgError = "";
                msgError = administrarParametroAutoliq.ejecutarPKGInsertar(parametroEstructura.getFechadesdecausado(), parametroEstructura.getFechahastacausado(), parametroTablaSeleccionado.getTipotrabajador().getSecuencia(), parametroTablaSeleccionado.getEmpresa().getSecuencia());
                msgErrorAux = administrarParametroAutoliq.ejecutarPKGActualizarNovedades(parametroTablaSeleccionado.getAno(), parametroTablaSeleccionado.getMes(), parametroTablaSeleccionado.getEmpresa().getSecuencia());
                if ((msgError.equals("PROCESO_EXITOSO")) && (msgErrorAux.equals("PROCESO_EXITOSO"))) {
                    msgErrorAux = "";
                    msgErrorAux = administrarParametroAutoliq.ejecutarPKGProcesarAportesXDia(parametroTablaSeleccionado.getAno(), parametroTablaSeleccionado.getMes(), parametroTablaSeleccionado.getEmpresa().getSecuencia());
                    if ((msgErrorAux.equals("PROCESO_EXITOSO"))) {

                        listaAportesEntidades = null;
                        getListaAportesEntidades();
                        contarRegistrosAporte();
                        disabledBuscar = true;
                        activoBtnsPaginas = true;
                        visibilidadMostrarTodos = "hidden";
                        RequestContext.getCurrentInstance().update("form:mostrarTodos");
                        RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
                        FacesMessage msg = new FacesMessage("Información", "El proceso de Liquidación fue realizado con éxito");
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                        RequestContext.getCurrentInstance().update("form:growl");
                    } else {
                        RequestContext.getCurrentInstance().update("formularioDialogos:errorProceso");
                        RequestContext.getCurrentInstance().execute("PF('errorProceso').show()");
                    }
                } else {
                    RequestContext.getCurrentInstance().update("formularioDialogos:errorProceso");
                    RequestContext.getCurrentInstance().execute("PF('errorProceso').show()");
                }
            }

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
            log.info("ControlParametroAutoliq.cambiarFechasParametros()");
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
            log.warn("Error cambiarFechasParametros Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en la modificacio de las fechas de ParametrosEstructuras y ParametrosReportes");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void procesoAcumularDiferenciaAporteEntidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            getUsuario();
            parametroTablaSeleccionado = getParametroTablaSeleccionado();
            if (usuario.getAlias() != null) {
                getParametroEstructura();
                getParametroInforme();
                if (parametroEstructura != null) {
                    if (parametroInforme != null) {
//
                        boolean fechasIgualesEstructura = true;
                        boolean fechasIgualesInforme = true;
                        if (fechasIgualesEstructura == true && fechasIgualesInforme == true) {
                            RequestContext.getCurrentInstance().execute("PF('acumularDiferenciaOK').show()");
                        } else {
                            RequestContext.getCurrentInstance().execute("PF('errorAcumularDiferencia').show()");
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.info("se estalló");
            log.warn("Error procesoLiquidacionAporteEntidad Controlador : " + e.toString());
        }
    }

    public void acumularDiferenciaOK() {
        RequestContext context = RequestContext.getCurrentInstance();
        msgError = "";
        parametroTablaSeleccionado = getParametroTablaSeleccionado();
        msgError = administrarParametroAutoliq.ejecutarPKGActualizarNovedades(parametroTablaSeleccionado.getAno(), parametroTablaSeleccionado.getMes(), parametroTablaSeleccionado.getEmpresa().getSecuencia());
        if (msgError.equals("PROCESO_EXITOSO")) {
            disabledBuscar = true;
            activoBtnsPaginas = true;
            visibilidadMostrarTodos = "hidden";
            log.info("entró a actualizar");
            RequestContext.getCurrentInstance().update("form:mostrarTodos");
            FacesMessage msg = new FacesMessage("Información", "El proceso de Acumular Diferencias de Aportes Entidades fue realizado con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            guardadoGeneral();
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:errorProcesoPila");
            RequestContext.getCurrentInstance().execute("PF('errorProcesoPila').show()");
        }
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "36";
            parametroAno = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroAno");
            parametroAno.setFilterStyle("width: 75% !important");
            mesParametro = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroMes");
            mesParametro.setFilterStyle("width: 85% !important");
            parametroTipoTrabajador = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroTipoTrabajador");
            parametroTipoTrabajador.setFilterStyle("width: 85% !important");
            parametroEmpresa = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroEmpresa");
            parametroEmpresa.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosParametroAuto");
            bandera = 1;
            activarFiltradoAporteEntidad();
        } else if (bandera == 1) {
            altoTabla = "56";
            parametroAno = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroAno");
            parametroAno.setFilterStyle("display: none; visibility: hidden;");
            mesParametro = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroMes");
            mesParametro.setFilterStyle("display: none; visibility: hidden;");
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
        altoTablaAporte = "165";
        altoTablaRows = "6";
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
        altoTablaAporte = "145";
        altoTablaRows = "5";
        aporteCodigoEmpleado = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteCodigoEmpleado");
        aporteCodigoEmpleado.setFilterStyle("width: 80% !important");

        aporteAno = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteAno");
        aporteAno.setFilterStyle("width: 75% !important");

        aporteMes = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteMes");
        aporteMes.setFilterStyle("width: 75% !important");

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
        aporteDiasCotizados.setFilterStyle("width: 70% !important");

        aporteTipoAportante = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTipoAportante");
        aporteTipoAportante.setFilterStyle("width: 70% !important");

        aporteING = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteING");
        aporteING.setFilterStyle("width: 70% !important");

        aporteRET = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteRET");
        aporteRET.setFilterStyle("width: 70% !important");

        aporteTDA = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTDA");
        aporteTDA.setFilterStyle("width: 70% !important");

        aporteTAA = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTAA");
        aporteTAA.setFilterStyle("width: 70% !important");

        aporteVSP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteVSP");
        aporteVSP.setFilterStyle("width: 70% !important");

        aporteVTE = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteVTE");
        aporteVTE.setFilterStyle("width: 70% !important");

        aporteVST = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteVST");
        aporteVST.setFilterStyle("width: 70% !important");

        aporteSLN = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSLN");
        aporteSLN.setFilterStyle("width: 70% !important");

        aporteIGE = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteIGE");
        aporteIGE.setFilterStyle("width: 70% !important");

        aporteLMA = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteLMA");
        aporteLMA.setFilterStyle("width: 70% !important");

        aporteVAC = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteVAC");
        aporteVAC.setFilterStyle("width: 70% !important");

        aporteAVP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteAVP");
        aporteAVP.setFilterStyle("width: 70% !important");

        aporteVCT = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteVCT");
        aporteVCT.setFilterStyle("width: 70% !important");

        aporteIRP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteIRP");
        aporteIRP.setFilterStyle("width: 70% !important");

        aporteSUS = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSUS");
        aporteSUS.setFilterStyle("width: 70% !important");

        aporteINTE = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteINTE");
        aporteINTE.setFilterStyle("width: 70% !important");

        aporteTarifaEPS = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaEPS");
        aporteTarifaEPS.setFilterStyle("width: 85% !important");

        aporteTarifaAAFP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaAAFP");
        aporteTarifaAAFP.setFilterStyle("width: 85% !important");

        aporteTarifaACTT = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaACTT");
        aporteTarifaACTT.setFilterStyle("width: 85% !important");

        aporteCodigoCTT = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteCodigoCTT");
        aporteCodigoCTT.setFilterStyle("width: 85% !important");

        aporteAVPEValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteAVPEValor");
        aporteAVPEValor.setFilterStyle("width: 70% !important");

        aporteAVPPValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteAVPPValor");
        aporteAVPPValor.setFilterStyle("width: 70% !important");

        aporteRETCONTAValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteRETCONTAValor");
        aporteRETCONTAValor.setFilterStyle("width: 70% !important");

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
        aporteTipoPensionado.setFilterStyle("width: 70% !important");

        aportePensionCompartida = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aportePensionCompartida");
        aportePensionCompartida.setFilterStyle("width: 70% !important");

        aporteExtranjero = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteExtranjero");
        aporteExtranjero.setFilterStyle("width: 70% !important");

        aporteFechaIngreso = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteFechaIngreso");
        aporteFechaIngreso.setFilterStyle("width: 85% !important");

        aporteSubTipoCotizante = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSubTipoCotizante");
        aporteSubTipoCotizante.setFilterStyle("width: 85% !important");

        aporteTarifaCaja = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaCaja");
        aporteTarifaCaja.setFilterStyle("width: 75% !important");

        aporteTarifaSena = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaSena");
        aporteTarifaSena.setFilterStyle("width: 75% !important");

        aporteTarifaICBF = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaICBF");
        aporteTarifaICBF.setFilterStyle("width: 75% !important");

        aporteTarifaESAP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaESAP");
        aporteTarifaESAP.setFilterStyle("width: 75% !important");

        aporteTarifaMEN = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaMEN");
        aporteTarifaMEN.setFilterStyle("width: 75% !important");

        RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");

        RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");

    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            altoTabla = "56";
            FacesContext c = FacesContext.getCurrentInstance();
            parametroAno = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroAno");
            parametroAno.setFilterStyle("display: none; visibility: hidden;");
            mesParametro = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroMes");
            mesParametro.setFilterStyle("display: none; visibility: hidden;");
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
        navegar("atras");
    }

    public void asignarIndex(ParametrosAutoliq parametro, int LND, int dialogo) {
        parametroTablaSeleccionado = parametro;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        log.info("tipo actualizacion aignar index : " + tipoActualizacion);
        if (dialogo == 1) {
            lovTiposTrabajadores = null;
            cargarLovTipoTrabjador();
            RequestContext.getCurrentInstance().update("formularioLovTipoTrabajador:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
            contarRegistroTiposTrabajadores();
        }
        if (dialogo == 2) {
            lovEmpresas = null;
            cargarLovEmpresa();
            RequestContext.getCurrentInstance().update("formularioLovEmpresa:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
            contarRegistroEmpresa();
        }
    }

    public void asignarIndexAporte(AportesEntidades aporte, int LND, int dialogo) {
        aporteTablaSeleccionado = aporte;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        if (dialogo == 1) {
            lovEmpleados = null;
            cargarLovEmpleado();
            contarRegistroEmpleados();
            RequestContext.getCurrentInstance().update("formularioLovEmpleado:EmpleadoDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDialogo').show()");
        }
        if (dialogo == 2) {
            lovTerceros = null;
            cargarLovTercero();
            contarRegistroTercero();
            RequestContext.getCurrentInstance().update("formularioLovTercero:TerceroDialogo");
            RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
        }
        if (dialogo == 3) {
            lovTiposEntidades = null;
            cargarLovTipoEntidad();
            contarRegistroTiposEntidades();
            RequestContext.getCurrentInstance().update("formularioLovTipoEntidad:TipoEntidadDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').show()");
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
        RequestContext.getCurrentInstance().update("form:eliminarToda");
        RequestContext.getCurrentInstance().update("form:procesoLiq");
//      RequestContext.getCurrentInstance().update("form:acumDif");
        tipoActualizacion = -1;/*
         RequestContext.getCurrentInstance().update("formularioLovTipoTrabajador:TipoTrabajadorDialogo");
         RequestContext.getCurrentInstance().update("formularioLovTipoTrabajador:lovTipoTrabajador");
         RequestContext.getCurrentInstance().update("formularioLovTipoTrabajador:aceptarTT");*/

        context.reset("formularioLovTipoTrabajador:lovTipoTrabajador:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').hide()");
    }

    public void cancelarCambioTipoTrabajador() {
        filtrarLovTiposTrabajadores = null;
        tipoTrabajadorSeleccionado = new TiposTrabajadores();
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        activoBtnsPaginas = true;
        RequestContext.getCurrentInstance().update("form:novedadauto");
        RequestContext.getCurrentInstance().update("form:eliminarToda");
        RequestContext.getCurrentInstance().update("form:procesoLiq");
//      RequestContext.getCurrentInstance().update("form:acumDif");
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
        activoBtnsPaginas = true;
        RequestContext.getCurrentInstance().update("form:novedadauto");
        RequestContext.getCurrentInstance().update("form:eliminarToda");
        RequestContext.getCurrentInstance().update("form:procesoLiq");
//      RequestContext.getCurrentInstance().update("form:acumDif");
        tipoActualizacion = -1;/*
         RequestContext.getCurrentInstance().update("formularioLovEmpresa:EmpresaDialogo");
         RequestContext.getCurrentInstance().update("formularioLovEmpresa:lovEmpresa");
         RequestContext.getCurrentInstance().update("formularioLovEmpresa:aceptarE");*/

        context.reset("formularioLovEmpresa:lovEmpresa:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
    }

    public void cancelarCambioEmpresa() {
        filtrarLovEmpresas = null;
        empresaSeleccionada = new Empresas();
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        activoBtnsPaginas = true;
        RequestContext.getCurrentInstance().update("form:novedadauto");
        RequestContext.getCurrentInstance().update("form:eliminarToda");
        RequestContext.getCurrentInstance().update("form:procesoLiq");
//      RequestContext.getCurrentInstance().update("form:acumDif");
        tipoActualizacion = -1;
        permitirIndex = true;
        context.reset("formularioLovEmpresa:lovEmpresa:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
    }

    public void actualizarEmpleado() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            aporteTablaSeleccionado.setEmpleado(empleadoSeleccionado.getSecuencia());
            aporteTablaSeleccionado.setNombrecompleto(empleadoSeleccionado.getNombreCompleto());
            aporteTablaSeleccionado.setCodigoempleado(empleadoSeleccionado.getCodigoempleado());
            if (listAportesEntidadesModificar.isEmpty()) {
                listAportesEntidadesModificar.add(aporteTablaSeleccionado);
            } else if (!listAportesEntidadesModificar.contains(aporteTablaSeleccionado)) {
                listAportesEntidadesModificar.add(aporteTablaSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            permitirIndexAporte = true;
            cambiosAporte = true;
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
        } else if (tipoActualizacion == 1) {
            nuevoAporteEntidad.setEmpleado(empleadoSeleccionado.getSecuencia());
            nuevoAporteEntidad.setNombrecompleto(empleadoSeleccionado.getNombreCompleto());
            nuevoAporteEntidad.setCodigoempleado(empleadoSeleccionado.getCodigoempleado());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodempl");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNomEmpl");
        } else if (tipoActualizacion == 2) {
            duplicarAporteEntidad.setEmpleado(empleadoSeleccionado.getSecuencia());
            duplicarAporteEntidad.setNombrecompleto(empleadoSeleccionado.getNombreCompleto());
            duplicarAporteEntidad.setCodigoempleado(empleadoSeleccionado.getCodigoempleado());
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
        RequestContext.getCurrentInstance().execute("PF('lovEmpleado').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioLovEmpleado:EmpleadoDialogo");
        RequestContext.getCurrentInstance().update("formularioLovEmpleado:lovEmpleado");
        RequestContext.getCurrentInstance().update("formularioLovEmpleado:aceptarEMPL");
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
        RequestContext.getCurrentInstance().update("formularioLovEmpleado:EmpleadoDialogo");
        RequestContext.getCurrentInstance().update("formularioLovEmpleado:lovEmpleado");
        RequestContext.getCurrentInstance().update("formularioLovEmpleado:aceptarEMPL");

    }

    public void actualizarTercero() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            aporteTablaSeleccionado.setTercero(terceroSeleccionado.getSecuencia());
            aporteTablaSeleccionado.setNittercero(terceroSeleccionado.getNit());
            aporteTablaSeleccionado.setNombretercero(terceroSeleccionado.getNombre());
            if (!listAportesEntidadesCrear.contains(terceroSeleccionado)) {
                if (listAportesEntidadesModificar.isEmpty()) {
                    listAportesEntidadesModificar.add(aporteTablaSeleccionado);
                } else if (!listAportesEntidadesModificar.contains(aporteTablaSeleccionado)) {
                    listAportesEntidadesModificar.add(aporteTablaSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                permitirIndexAporte = true;
                cambiosAporte = true;
            }
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
        } else if (tipoActualizacion == 1) {
            nuevoAporteEntidad.setTercero(terceroSeleccionado.getSecuencia());
            nuevoAporteEntidad.setNittercero(terceroSeleccionado.getNit());
            nuevoAporteEntidad.setNombretercero(terceroSeleccionado.getNombre());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNitTercero");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevonombretercero");
        } else if (tipoActualizacion == 2) {
            duplicarAporteEntidad.setTercero(terceroSeleccionado.getSecuencia());
            duplicarAporteEntidad.setNittercero(terceroSeleccionado.getNit());
            duplicarAporteEntidad.setNombretercero(terceroSeleccionado.getNombre());
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
//            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoApEntidad");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevotipoentidad");
        } else if (tipoActualizacion == 2) {
            duplicarAporteEntidad.setTipoentidad(tipoEntidadSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicartipoentidad");
        }
        filtrarLovTiposEntidades = null;
        tipoEntidadSeleccionado = new TiposEntidades();
        aceptar = true;
        tipoActualizacion = -1;

        RequestContext.getCurrentInstance().update("formularioLovTipoEntidad:TipoEntidadDialogo");
        RequestContext.getCurrentInstance().update("formularioLovTipoEntidad:lovTipoEntidad");
        RequestContext.getCurrentInstance().update("formularioLovTipoEntidad:aceptarTE");
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
        RequestContext.getCurrentInstance().update("formularioLovTipoEntidad:TipoEntidadDialogo");
        RequestContext.getCurrentInstance().update("formularioLovTipoEntidad:lovTipoEntidad");
        RequestContext.getCurrentInstance().update("formularioLovTipoEntidad:aceptarTE");
        context.reset("formularioLovTipoEntidad:lovTipoEntidad:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoEntidad').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').hide()");
    }

    public void dispararDialogoBuscar() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (guardado == true) {
            lovAportesEntidades = null;
            cargarLovAporteEntidad();
            RequestContext.getCurrentInstance().update("formularioLovAporteEntidad:BuscarAporteDialogo");
            RequestContext.getCurrentInstance().execute("PF('BuscarAporteDialogo').show()");
            RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
            contarRegistroAportesEntidades();
        } else {
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void actualizarAporteEntidad() {
        System.out.println("ControlParametroAutoliq.actualizarAporteEntidad()");
        RequestContext context = RequestContext.getCurrentInstance();
        if (banderaAporte == 1) {
            desactivarFiltradoAporteEntidad();
            banderaAporte = 0;
            filtrarListaAportesEntidades = null;
            tipoListaAporte = 0;
        }
        listaAportesEntidades.clear();
        listaAportesEntidades = administrarParametroAutoliq.consultarAportesEntidadesPorEmpleado(aporteEntidadSeleccionado.getEmpleado(), aporteEntidadSeleccionado.getMes(), aporteEntidadSeleccionado.getAno());
        if (listaAportesEntidades != null) {
            if (!listaAportesEntidades.isEmpty()) {
                aporteTablaSeleccionado = listaAportesEntidades.get(0);
            }
        }
        filtrarLovAportesEntidades = null;
        aporteEntidadSeleccionado = null;
        aceptar = true;
        activoBtnsPaginas = true;
        RequestContext.getCurrentInstance().update("form:novedadauto");
        RequestContext.getCurrentInstance().update("form:eliminarToda");
        RequestContext.getCurrentInstance().update("form:procesoLiq");
        tipoActualizacion = -1;
        visibilidadMostrarTodos = "visible";
        RequestContext.getCurrentInstance().update("form:mostrarTodos");
        contarRegistrosParametros();
        contarRegistrosAporte();
        RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");

        RequestContext.getCurrentInstance().update("formularioLovAporteEntidad:BuscarAporteDialogo");
        RequestContext.getCurrentInstance().update("formularioLovAporteEntidad:lovBuscarAporte");
        RequestContext.getCurrentInstance().update("formularioLovAporteEntidad:aceptarBA");
        context.reset("formularioLovAporteEntidad:lovBuscarAporte:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovBuscarAporte').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('BuscarAporteDialogo').hide()");
    }

    public void cancelarCambioAporteEntidad() {
        filtrarLovEmpresas = null;
        aporteEntidadSeleccionado = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        activoBtnsPaginas = true;
        RequestContext.getCurrentInstance().update("form:novedadauto");
        RequestContext.getCurrentInstance().update("form:eliminarToda");
        RequestContext.getCurrentInstance().update("form:procesoLiq");
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext.getCurrentInstance().update("formularioLovAporteEntidad:BuscarAporteDialogo");
        RequestContext.getCurrentInstance().update("formularioLovAporteEntidad:lovBuscarAporte");
        RequestContext.getCurrentInstance().update("formularioLovAporteEntidad:aceptarBA");
        context.reset("formularioLovAporteEntidad:lovBuscarAporte:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovBuscarAporte').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('BuscarAporteDialogo').hide()");
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
                    lovTiposTrabajadores = null;
                    cargarLovTipoTrabjador();
                    RequestContext.getCurrentInstance().update("formularioLovTipoTrabajador:TipoTrabajadorDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
                    tipoActualizacion = 0;
                }
            }
        } else if (cualTabla == 2) {
            if (aporteTablaSeleccionado != null) {
                if (cualCeldaAporte == 3) {
                    lovEmpleados = null;
                    cargarLovEmpleado();
                    RequestContext.getCurrentInstance().update("formularioLovEmpleado:EmpleadoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('EmpleadoDialogo').show()");
                    tipoActualizacion = 0;
                }
                if (cualCeldaAporte == 4) {
                    lovTerceros = null;
                    cargarLovTercero();
                    RequestContext.getCurrentInstance().update("formularioLovTercero:TerceroDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
                    tipoActualizacion = 0;
                }
                if (cualCeldaAporte == 6) {
                    lovAportesEntidades = null;
                    cargarLovTipoEntidad();
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
//      context2.update("form:acumDif");
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
//      context2.update("form:acumDif");
    }

    public void exportXLS_AE() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosAporteEntidadExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "AportesEntidades_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistrosParametros();
    }

    public void eventoFiltrarAportes() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (aporteTablaSeleccionado != null) {
            if (tipoListaAporte == 0) {
                tipoListaAporte = 1;
            }
            contarRegistrosAporte();
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
        } else if (administrarRastros.verificarHistoricosTabla("PARAMETROSAUTOLIQ")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        activoBtnsPaginas = true;
        RequestContext.getCurrentInstance().update("form:novedadauto");
        RequestContext.getCurrentInstance().update("form:eliminarToda");
        RequestContext.getCurrentInstance().update("form:procesoLiq");
//      RequestContext.getCurrentInstance().update("form:acumDif");
    }

    public void verificarRastroAporteEntidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaAportesEntidades != null) {
            if (aporteTablaSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(aporteTablaSeleccionado.getSecuencia(), "APORTESENTIDADES");
                backUpSecRegistroAporte = aporteTablaSeleccionado.getSecuencia();
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

    public void contarRegistroEmpresa() {
        RequestContext.getCurrentInstance().update("formularioLovEmpresa:infoRegistroEmpresa");
    }

    public void contarRegistroEmpleados() {
        RequestContext.getCurrentInstance().update("formularioLovEmpleado:infoRegistroEmpleado");
    }

    public void contarRegistroTercero() {
        RequestContext.getCurrentInstance().update("formularioLovTercero:infoRegistroTercero");
    }

    public void contarRegistroTiposTrabajadores() {
        RequestContext.getCurrentInstance().update("formularioLovTipoTrabajador:infoRegistroTipoTrabajador");
    }

    public void contarRegistroTiposEntidades() {
        RequestContext.getCurrentInstance().update("formularioLovTipoEntidad:infoRegistroTipoEntidad");
    }

    public void contarRegistroAportesEntidades() {
        RequestContext.getCurrentInstance().update("formularioLovAporteEntidad:infoRegistroAporteEntidad");
    }

    public void contarRegistrosAporte() {
        RequestContext.getCurrentInstance().update("form:infoRegistroAporte");
    }

    public void contarRegistrosParametros() {
        RequestContext.getCurrentInstance().update("form:infoRegistroParametro");
    }

    public void cargarLovTipoEntidad() {
        if (lovTiposEntidades == null) {
            log.info("ControlParametroAutoliq.cargarLovTipoEntidad()");
            lovTiposEntidades = administrarParametroAutoliq.lovTiposEntidades();
        }
    }

    public void cargarLovTipoTrabjador() {
        if (lovTiposTrabajadores == null) {
            log.info("ControlParametroAutoliq.cargarLovTipoTrabjador()");
            if (listasRecurrentes.getLovTiposTrabajadores().isEmpty()) {
                lovTiposTrabajadores = administrarParametroAutoliq.lovTiposTrabajadores();
                if (lovTiposTrabajadores != null) {
                    listasRecurrentes.setLovTiposTrabajadores(lovTiposTrabajadores);
                }
            } else {
                lovTiposTrabajadores = new ArrayList<TiposTrabajadores>(listasRecurrentes.getLovTiposTrabajadores());
            }
        }
    }

    public void cargarLovEmpresa() {
        if (lovEmpresas == null) {
            log.info("ControlParametroAutoliq.cargarLovEmpresa()");
            lovEmpresas = administrarParametroAutoliq.lovEmpresas();
        }
    }

    public void cargarLovEmpleado() {
        if (lovEmpleados == null) {
            log.info("ControlParametroAutoliq.cargarLovEmpleado()");
            lovEmpleados = administrarParametroAutoliq.lovEmpleados();
        }
    }

    public void cargarLovTercero() {
        if (lovTerceros == null) {
            log.info("ControlParametroAutoliq.cargarLovTercero()");
            lovTerceros = administrarParametroAutoliq.lovTerceros();
        }
    }

    public void cargarLovAporteEntidad() {
        if (lovAportesEntidades == null) {
            if (parametroTablaSeleccionado != null) {
                log.info("ControlParametroAutoliq.cargarLovAporteEntidad()");
                lovAportesEntidades = administrarParametroAutoliq.consultarAportesEntidadesPorParametroAutoliq(parametroTablaSeleccionado.getEmpresa().getSecuencia(), parametroTablaSeleccionado.getMes(), parametroTablaSeleccionado.getAno());
            }
        }
    }

    //GET - SET//
    public List<ParametrosAutoliq> getListaParametrosAutoliq() {
        if (listaParametrosAutoliq == null) {
            listaParametrosAutoliq = administrarParametroAutoliq.consultarParametrosAutoliq();
        }
        return listaParametrosAutoliq;
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
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioLovTipoTrabajador:lovTipoTrabajador");
        infoRegistroTipoTrabajador = String.valueOf(tabla.getRowCount());
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
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioLovEmpresa:lovEmpresa");
        infoRegistroEmpresa = String.valueOf(tabla.getRowCount());
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
        if (listaAportesEntidades == null) {
            if (parametroTablaSeleccionado != null) {
                listaAportesEntidades = administrarParametroAutoliq.consultarAportesEntidadesPorParametroAutoliq(parametroTablaSeleccionado.getEmpresa().getSecuencia(), parametroTablaSeleccionado.getMes(), parametroTablaSeleccionado.getAno());
            }
        }
        return listaAportesEntidades;
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
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioLovEmpleado:lovEmpleado");
        infoRegistroEmpleado = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpleado;
    }

    public void setInfoRegistroEmpleado(String infoRegistroEmpleado) {
        this.infoRegistroEmpleado = infoRegistroEmpleado;
    }

    public List<Terceros> getLovTerceros() {
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
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioLovTercero:lovTercero");
        infoRegistroTercero = String.valueOf(tabla.getRowCount());
        return infoRegistroTercero;
    }

    public void setInfoRegistroTercero(String infoRegistroTercero) {
        this.infoRegistroTercero = infoRegistroTercero;
    }

    public List<TiposEntidades> getLovTiposEntidades() {
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
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioLovTipoEntidad:lovTipoEntidad");
        infoRegistroTipoEntidad = String.valueOf(tabla.getRowCount());
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
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioLovAporteEntidad:lovBuscarAporte");
        infoRegistroAporteEntidad = String.valueOf(tabla.getRowCount());
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
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosParametroAuto");
        infoRegistroParametro = String.valueOf(tabla.getRowCount());
        return infoRegistroParametro;
    }

    public void setInfoRegistroParametro(String infoRegistroParametro) {
        this.infoRegistroParametro = infoRegistroParametro;
    }

    public String getInfoRegistroAporte() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:tablaAportesEntidades");
        infoRegistroAporte = String.valueOf(tabla.getRowCount());
        return infoRegistroAporte;
    }

    public void setInfoRegistroAporte(String infoRegistroAporte) {
        this.infoRegistroAporte = infoRegistroAporte;
    }

    public ParametrosEstructuras getParametroEstructura() {
        getUsuario();
        if (usuario.getAlias() != null) {
            log.info("ControlParametroAutoliq.getParametroEstructura()");
            parametroEstructura = administrarParametroAutoliq.buscarParametroEstructura(usuario.getAlias());
        }
        return parametroEstructura;
    }

    public void setParametroEstructura(ParametrosEstructuras parametroEstructura) {
        this.parametroEstructura = parametroEstructura;
    }

    public ParametrosInformes getParametroInforme() {
        log.info("ControlParametroAutoliq.getParametroInforme() para Us");
        getUsuario();
        log.info("ControlParametroAutoliq.getParametroInforme() para buscarPara");
        parametroInforme = administrarParametroAutoliq.buscarParametroInforme(usuario.getAlias());
        return parametroInforme;
    }

    public void setParametroInforme(ParametrosInformes parametroInforme) {
        this.parametroInforme = parametroInforme;
    }

    public ActualUsuario getUsuario() {
        if (usuario == null) {
            log.info("ControlParametroAutoliq.getUsuario()");
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

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public String getAltoTablaRows() {
        return altoTablaRows;
    }

    public void setAltoTablaRows(String altoTablaRows) {
        this.altoTablaRows = altoTablaRows;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public String getMsgErrorAux() {
        return msgErrorAux;
    }

    public void setMsgErrorAux(String msgErrorAux) {
        this.msgErrorAux = msgErrorAux;
    }

}
