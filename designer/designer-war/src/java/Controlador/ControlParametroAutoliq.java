package Controlador;

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
    private AportesEntidades editarAporteEntidad;
    //
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
    private String altoDivTablaInferiorIzquierda, altoDivTablaInferiorDerecha;
    private String topDivTablaInferiorIzquierda, topDivTablaInferiorDerecha;
    //
    private int numeroScrollAporte;
    private int rowsAporteEntidad;
    //
    private int numero;
    //
    private String visibilidadMostrarTodos;
    private DataTable tablaC;

    public ControlParametroAutoliq() {
        visibilidadMostrarTodos = "hidden";
//        infoRegistroAporte = "Cantidad de registros : 0";
//        infoRegistroParametro = "Cantidad de registros : 0";
        numero = 7;
        activoBtnsPaginas = false;
        disabledBuscar = true;
        altoTabla = "50";
        altoTablaAporte = "180";
        //
        parametroTablaSeleccionado = new ParametrosAutoliq();
        aporteTablaSeleccionado = new AportesEntidades();
        //
        nuevoParametro = new ParametrosAutoliq();
        nuevoParametro.setEmpresa(new Empresas());
        nuevoParametro.setTipotrabajador(new TiposTrabajadores());

        editarParametro = new ParametrosAutoliq();
        editarAporteEntidad = new AportesEntidades();

        duplicarParametro = new ParametrosAutoliq();
        duplicarParametro.setEmpresa(new Empresas());
        duplicarParametro.setTipotrabajador(new TiposTrabajadores());
        //
        listParametrosAutoliqCrear = new ArrayList<ParametrosAutoliq>();
        listParametrosAutoliqModificar = new ArrayList<ParametrosAutoliq>();
        listParametrosAutoliqBorrar = new ArrayList<ParametrosAutoliq>();

        listaAportesEntidades = null;
        listAportesEntidadesBorrar = new ArrayList<AportesEntidades>();
        listAportesEntidadesModificar = new ArrayList<AportesEntidades>();
        //
        parametroTablaSeleccionado = null;
        aporteTablaSeleccionado = null;
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
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarParametroAutoliq.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            numeroScrollAporte = 600;
            rowsAporteEntidad = 20;
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPagina(String pagina) {
        paginaAnterior = pagina;
        listaParametrosAutoliq = null;
        getListaParametrosAutoliq();
        getListaAportesEntidades();
        if (listaParametrosAutoliq != null) {
            modificarInfoRegistroParametro(listaParametrosAutoliq.size());
        }
        if(listaAportesEntidades != null){
            modificarInfoRegistroAporte(listaAportesEntidades.size());
        }
        
    }

    public String redirigir() {
        return paginaAnterior;
    }

    public int obtenerNumeroScrollAporte() {
        return numeroScrollAporte;
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
                    context.update("form:ACEPTAR");
                }
            }
            cambiosParametro = true;
            activoBtnsPaginas = true;
            context.update("form:novedadauto");
            context.update("form:incaPag");
            context.update("form:eliminarToda");
            context.update("form:procesoLiq");
            context.update("form:acumDif");
        } else {
            if (!listParametrosAutoliqCrear.contains(parametroTablaSeleccionado)) {

                if (listParametrosAutoliqModificar.isEmpty()) {
                    listParametrosAutoliqModificar.add(parametroTablaSeleccionado);
                } else if (!listParametrosAutoliqModificar.contains(parametroTablaSeleccionado)) {
                    listParametrosAutoliqModificar.add(parametroTablaSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    context.update("form:ACEPTAR");
                }
            }
            cambiosParametro = true;
            activoBtnsPaginas = true;
            context.update("form:novedadauto");
            context.update("form:incaPag");
            context.update("form:eliminarToda");
            context.update("form:procesoLiq");
            context.update("form:acumDif");
        }
        context.update("form:datosParametroAuto");
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
                context.update("form:ACEPTAR");
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
                context.update("form:ACEPTAR");
            }
            cambiosAporte = true;
        }
        context.update("form:tablaAportesEntidades");
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
                    context.update("formularioLovTipoTrabajador:TipoTrabajadorDialogo");
                    context.execute("TipoTrabajadorDialogo.show()");
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
                context.update("form:novedadauto");
                context.update("form:incaPag");
                context.update("form:eliminarToda");
                context.update("form:procesoLiq");
                context.update("form:acumDif");
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
                context.update("form:novedadauto");
                context.update("form:incaPag");
                context.update("form:eliminarToda");
                context.update("form:procesoLiq");
                context.update("form:acumDif");
            }
        }
        context.update("form:datosParametroAuto");
    }


    public void modificarAporteEntidad(AportesEntidades aporte, String confirmarCambio, String valorConfirmar) {
        aporteTablaSeleccionado = aporte;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOENTIDAD")) {
            if (tipoListaAporte == 0) {
                aporteTablaSeleccionado.getTipoentidad().setNombre(auxTipoEntidad);
            } else {
                aporteTablaSeleccionado.getTipoentidad().setNombre(auxTipoEntidad);
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
                context.update("formularioLovTipoEntidad:TipoEntidadDialogo");
                context.execute("TipoEntidadDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoListaAporte == 0) {
                    aporteTablaSeleccionado.getTerceroRegistro().setNit(auxNitTercero);
                } else {
                    aporteTablaSeleccionado.getTerceroRegistro().setNit(auxNitTercero);
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
                    context.update("formularioLovTercero:TerceroDialogo");
                    context.execute("TerceroDialogo.show()");
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
            if (tipoListaAporte == 0) {
                aporteTablaSeleccionado.getEmpleado().getPersona().setNombreCompleto(auxEmpleado);
            } else {
                aporteTablaSeleccionado.getEmpleado().getPersona().setNombreCompleto(auxEmpleado);
            }
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
                context.update("formularioLovEmpleado:EmpleadoDialogo");
                context.execute("EmpleadoDialogo.show()");
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
                    context.update("form:ACEPTAR");
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
                    context.update("form:ACEPTAR");
                }
                cambiosAporte = true;
            }
        }
        context.update("form:tablaAportesEntidades");
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
                        context.update("formularioDialogos:nuevaTipoTrabajadorParametro");
                    } else if (tipoNuevo == 2) {
                        duplicarParametro.setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
                        context.update("formularioDialogos:duplicarTipoTrabajadorParametro");
                    }
                    lovTiposTrabajadores.clear();
                    getLovTiposTrabajadores();
                } else {
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        context.update("formularioDialogos:nuevaTipoTrabajadorParametro");
                    } else if (tipoNuevo == 2) {
                        context.update("formularioDialogos:duplicarTipoTrabajadorParametro");
                    }
                    context.update("formularioLovTipoTrabajador:TipoTrabajadorDialogo");
                    context.execute("TipoTrabajadorDialogo.show()");
                }
            } else {
                if (tipoNuevo == 1) {
                    nuevoParametro.setTipotrabajador(new TiposTrabajadores());
                    context.update("formularioDialogos:nuevaTipoTrabajadorParametro");
                } else if (tipoNuevo == 2) {
                    duplicarParametro.setTipotrabajador(new TiposTrabajadores());
                    context.update("formularioDialogos:duplicarTipoTrabajadorParametro");
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
                    context.update("formularioDialogos:nuevaEmpresaParametro");
                } else if (tipoNuevo == 2) {
                    duplicarParametro.setEmpresa(lovEmpresas.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarEmpresaParametro");
                }
                lovEmpresas.clear();
                getLovEmpresas();
            } else {
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaEmpresaParametro");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarEmpresaParametro");
                }
                context.update("formularioLovEmpresa:EmpresaDialogo");
                context.execute("EmpresaDialogo.show()");
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

            RequestContext.getCurrentInstance().execute("operacionEnProceso.hide()");
            parametroTablaSeleccionado = parametro;
            cualCelda = celda;
            if (tipoLista == 0) {
                parametroTablaSeleccionado.getSecuencia();
                auxTipoTipoTrabajador = parametroTablaSeleccionado.getTipotrabajador().getNombre();
            } else {
                parametroTablaSeleccionado.getSecuencia();
                auxTipoTipoTrabajador = parametroTablaSeleccionado.getTipotrabajador().getNombre();
            }
            if (banderaAporte == 1) {
                desactivarFiltradoAporteEntidad();
                banderaAporte = 0;
                filtrarListaAportesEntidades = null;
                tipoListaAporte = 0;
            }
            activoBtnsPaginas = false;
            numeroScrollAporte = 505;
            rowsAporteEntidad = 20;
            //context.update("form:datosAporteEntidad2");
            visibilidadMostrarTodos = "hidden";
            context.update("form:mostrarTodos");
            context.update("form:novedadauto");
            context.update("form:incaPag");
            context.update("form:eliminarToda");
            context.update("form:procesoLiq");
            context.update("form:acumDif");
            context.update("form:infoRegistroAporte");
            context.update("form:tablaAportesEntidades");
            getParametroTablaSeleccionado();
            cargarDatosNuevos();
        }
    }

    public void cargarDatosNuevos() {
        try {
            listaAportesEntidades = null;
            getListaAportesEntidades();
            lovAportesEntidades = null;
            getLovAportesEntidades();
            if (listaAportesEntidades != null) {
                modificarInfoRegistroAporte(listaAportesEntidades.size());
            }
            Thread.sleep(2000L);
            RequestContext.getCurrentInstance().update("form:PanelTotal");
            RequestContext.getCurrentInstance().execute("operacionEnProceso.hide()");

        } catch (Exception e) {
            System.out.println("Error cargarDatosNuevos Controlador : " + e.toString());
        }
    }

    public void cambiarIndiceAporteEntidad(AportesEntidades aporte, int celda) {
        if (permitirIndexAporte == true) {
            aporteTablaSeleccionado = aporte;
            cualCeldaAporte = celda;
            activoBtnsPaginas = true;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:novedadauto");
            context.update("form:incaPag");
            context.update("form:eliminarToda");
            context.update("form:procesoLiq");
            context.update("form:acumDif");
            if (tipoListaAporte == 0) {
                aporteTablaSeleccionado.getSecuencia();
                auxEmpleado = aporteTablaSeleccionado.getEmpleado().getPersona().getNombreCompleto();
                auxNitTercero = aporteTablaSeleccionado.getTerceroRegistro().getNit();
                auxTipoEntidad = aporteTablaSeleccionado.getTipoentidad().getNombre();
                if (cualCeldaAporte >= 0 && cualCeldaAporte <= 3) {
                    //aporteTablaSeleccionado = aporteTablaSeleccionado;
                    context.update("form:tablaAportesEntidades");
                } else if (cualCeldaAporte >= 4) {
                    //aporteTablaSeleccionado = aporteTablaSeleccionado;
                    context.update("form:tablaAportesEntidades");
                }
            } else {
                aporteTablaSeleccionado.getSecuencia();
                auxEmpleado = aporteTablaSeleccionado.getEmpleado().getPersona().getNombreCompleto();
                auxNitTercero = aporteTablaSeleccionado.getTerceroRegistro().getNit();
                auxTipoEntidad = aporteTablaSeleccionado.getTipoentidad().getNombre();
                if (cualCeldaAporte >= 0 && cualCeldaAporte <= 3) {
                    // aporteTablaSeleccionado = aporteTablaSeleccionado;
                    context.update("form:tablaAportesEntidades");
                } else if (cualCeldaAporte >= 4) {
                    // aporteTablaSeleccionado = aporteTablaSeleccionado;
                    context.update("form:tablaAportesEntidades");
                }
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
            if (cambiosParametro == true) {
                guardarCambiosParametro();
            }
            if (cambiosAporte == true) {
                guardarCambiosAportes();
            }
            visibilidadMostrarTodos = "hidden";
            RequestContext.getCurrentInstance().update("form:mostrarTodos");
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
            if (listaParametrosAutoliq != null) {
                modificarInfoRegistroParametro(listaParametrosAutoliq.size());
            }
            context.update("form:infoRegistroParametro");
            context.update("form:datosParametroAuto");
            k = 0;
            activoBtnsPaginas = true;
            context.update("form:novedadauto");
            context.update("form:incaPag");
            context.update("form:eliminarToda");
            context.update("form:procesoLiq");
            context.update("form:acumDif");
            parametroTablaSeleccionado = null;
            cambiosParametro = false;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos de Parámetros de Liquidación con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception e) {
            System.out.println("Error guardarCambiosParametro  Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Parámetros de Liquidación, Por favor intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
        }
    }

    public void guardarCambiosAportes() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (!listAportesEntidadesBorrar.isEmpty()) {
                administrarParametroAutoliq.borrarAportesEntidades(listAportesEntidadesBorrar);
                listAportesEntidadesBorrar.clear();
            }
            if (!listAportesEntidadesModificar.isEmpty()) {
                administrarParametroAutoliq.editarAportesEntidades(listAportesEntidadesModificar);
                listAportesEntidadesModificar.clear();
            }
//            listaAportesEntidades = null;
//            getListaAportesEntidades();
            modificarInfoRegistroAporte(listaAportesEntidades.size());
            context.update("form:PanelTotal");
            k = 0;
            aporteTablaSeleccionado = null;
            parametroTablaSeleccionado = null;
            cambiosAporte = false;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos de Aporte Entidad con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception e) {
            System.out.println("Error guardarCambiosAportes  Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Aporte Entidad, Por favor intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
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
        numeroScrollAporte = 505;
        rowsAporteEntidad = 20;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:PanelTotal");
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (parametroTablaSeleccionado != null) {
            editarParametro = parametroTablaSeleccionado;
            if (cualCelda == 0) {
                context.update("formularioDialogos:editarAnoD");
                context.execute("editarAnoD.show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                context.update("formularioDialogos:editarTipoTrabajadorD");
                context.execute("editarTipoTrabajadorD.show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                context.update("formularioDialogos:editarEmpresaD");
                context.execute("editarEmpresaD.show()");
                cualCelda = -1;
            }
            activoBtnsPaginas = true;
            context.update("form:novedadauto");
            context.update("form:incaPag");
            context.update("form:eliminarToda");
            context.update("form:procesoLiq");
            context.update("form:acumDif");
        } else if (aporteTablaSeleccionado != null) {
            if (tipoListaAporte == 0) {
                editarAporteEntidad = aporteTablaSeleccionado;
            } else {
                editarAporteEntidad = aporteTablaSeleccionado;
            }
            if (cualCeldaAporte == 0) {
                context.update("formularioDialogos:editarCodEmplD");
                context.execute("editarCodEmplD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 1) {
                context.update("formularioDialogos:editarAnoAD");
                context.execute("editarAnoAD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 2) {
                context.update("formularioDialogos:editarMesAD");
                context.execute("editarMesAD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 3) {
                context.update("formularioDialogos:editarNombreEmplD");
                context.execute("editarNombreEmplD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 4) {
                context.update("formularioDialogos:editarNitTerceroD");
                context.execute("editarNitTerceroD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 5) {
                context.update("formularioDialogos:editarNombreTerceroD");
                context.execute("editarNombreTerceroD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 6) {
                context.update("formularioDialogos:editarTipoEntidadD");
                context.execute("editarTipoEntidadD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 7) {
                context.update("formularioDialogos:editarEmpleadoD");
                context.execute("editarEmpleadoD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 8) {
                context.update("formularioDialogos:editarEmpleadorD");
                context.execute("editarEmpleadorD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 9) {
                context.update("formularioDialogos:editarAjustePatronalD");
                context.execute("editarAjustePatronalD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 10) {
                context.update("formularioDialogos:editarSolidaridadD");
                context.execute("editarSolidaridadD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 11) {
                context.update("formularioDialogos:editarSubSistenciaD");
                context.execute("editarSubSistenciaD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 12) {
                context.update("formularioDialogos:editarSubsPensionadosD");
                context.execute("editarSubsPensionadosD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 13) {
                context.update("formularioDialogos:editarSalarioBasicoD");
                context.execute("editarSalarioBasicoD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 14) {
                context.update("formularioDialogos:editarIBCD");
                context.execute("editarIBCD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 15) {
                context.update("formularioDialogos:editarIBCReferenciaD");
                context.execute("editarIBCReferenciaD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 16) {
                context.update("formularioDialogos:editarDiasCotizadosD");
                context.execute("editarDiasCotizadosD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 17) {
                context.update("formularioDialogos:editarTipoAportanteD");
                context.execute("editarTipoAportanteD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 18) {
                context.update("formularioDialogos:editarINGD");
                context.execute("editarINGD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 19) {
                context.update("formularioDialogos:editarRETD");
                context.execute("editarRETD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 20) {
                context.update("formularioDialogos:editarTDAD");
                context.execute("editarTDAD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 21) {
                context.update("formularioDialogos:editarTAAD");
                context.execute("editarTAAD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 22) {
                context.update("formularioDialogos:editarVSPD");
                context.execute("editarVSPD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 23) {
                context.update("formularioDialogos:editarVTED");
                context.execute("editarVTED.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 24) {
                context.update("formularioDialogos:editarVSTD");
                context.execute("editarVSTD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 25) {
                context.update("formularioDialogos:editarSLND");
                context.execute("editarSLND.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 26) {
                context.update("formularioDialogos:editarIGED");
                context.execute("editarIGED.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 27) {
                context.update("formularioDialogos:editarLMAD");
                context.execute("editarLMAD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 28) {
                context.update("formularioDialogos:editarVCAD");
                context.execute("editarVCAD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 29) {
                context.update("formularioDialogos:editarAVPD");
                context.execute("editarAVPD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 30) {
                context.update("formularioDialogos:editarVCTD");
                context.execute("editarVCTD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 31) {
                context.update("formularioDialogos:editarIRPD");
                context.execute("editarIRPD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 32) {
                context.update("formularioDialogos:editarSUSD");
                context.execute("editarSUSD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 33) {
                context.update("formularioDialogos:editarINTED");
                context.execute("editarINTED.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 34) {
                context.update("formularioDialogos:editarTarifaEPSD");
                context.execute("editarTarifaEPSD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 35) {
                context.update("formularioDialogos:editarTarifaAAFPD");
                context.execute("editarTarifaAAFPD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 36) {
                context.update("formularioDialogos:editarTarifaACTTD");
                context.execute("editarTarifaACTTD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 37) {
                context.update("formularioDialogos:editarCodigoCTTD");
                context.execute("editarCodigoCTTD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 38) {
                context.update("formularioDialogos:editarAvpeValorD");
                context.execute("editarAvpeValorD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 39) {
                context.update("formularioDialogos:editarAvppValorD");
                context.execute("editarAvppValorD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 40) {
                context.update("formularioDialogos:editarRetcontaValorD");
                context.execute("editarRetcontaValorD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 41) {
                context.update("formularioDialogos:editarCodigoNEPSD");
                context.execute("editarCodigoNEPSD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 42) {
                context.update("formularioDialogos:editarCodigoNAFPD");
                context.execute("editarCodigoNAFPD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 43) {
                context.update("formularioDialogos:editarEgValorD");
                context.execute("editarEgValorD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 44) {
                context.update("formularioDialogos:editarEgAutorizacionD");
                context.execute("editarEgAutorizacionD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 45) {
                context.update("formularioDialogos:editarMaternidadValorD");
                context.execute("editarMaternidadValorD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 46) {
                context.update("formularioDialogos:editarMaternidadAutoD");
                context.execute("editarMaternidadAutoD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 47) {
                context.update("formularioDialogos:editarUpcValorD");
                context.execute("editarUpcValorD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 48) {
                context.update("formularioDialogos:editarTipoD");
                context.execute("editarTipoD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 49) {
                context.update("formularioDialogos:editarTPD");
                context.execute("editarTPD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 50) {
                context.update("formularioDialogos:editarPCD");
                context.execute("editarPCD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 51) {
                context.update("formularioDialogos:editarEXTRD");
                context.execute("editarEXTRD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 52) {
                context.update("formularioDialogos:editarFechaIngreso");
                context.execute("editarFechaIngreso.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 54) {
                context.update("formularioDialogos:editarTarifaCajaD");
                context.execute("editarTarifaCajaD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 55) {
                context.update("formularioDialogos:editarTarifaSenaD");
                context.execute("editarTarifaSenaD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 56) {
                context.update("formularioDialogos:editarTarifaICBFD");
                context.execute("editarTarifaICBFD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 57) {
                context.update("formularioDialogos:editarTarifaESAPD");
                context.execute("editarTarifaESAPD.show()");
                cualCeldaAporte = -1;
            } else if (cualCeldaAporte == 58) {
                context.update("formularioDialogos:editarTarifaMEND");
                context.execute("editarTarifaMEND.show()");
                cualCeldaAporte = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("formularioDialogos:seleccionarRegistro.show()");
        }
    }

    public void dispararDialogoNuevoRegistro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (guardado == true) {
            visibilidadMostrarTodos = "hidden";
            RequestContext.getCurrentInstance().update("form:mostrarTodos");
            context.update("formularioDialogos:nuevaParametro");
            context.execute("NuevoRegistroParametro.show()");
        } else {
            context.execute("confirmarGuardar.show()");
        }
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

            context.update("form:datosParametroAuto");
            context.execute("NuevoRegistroParametro.hide()");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            // parametroTablaSeleccionado = null;
            activoBtnsPaginas = true;
            context.update("form:novedadauto");
            context.update("form:incaPag");
            context.update("form:eliminarToda");
            context.update("form:procesoLiq");
            context.update("form:acumDif");
            cambiosParametro = true;
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("errorRegNullParametro.show()");
        }
    }

    public void limpiarNuevaParametroAutoliq() {
        nuevoParametro = new ParametrosAutoliq();
        nuevoParametro.setTipotrabajador(new TiposTrabajadores());
        nuevoParametro.setEmpresa(new Empresas());
        //  parametroTablaSeleccionado = null;
        activoBtnsPaginas = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:novedadauto");
        context.update("form:incaPag");
        context.update("form:eliminarToda");
        context.update("form:procesoLiq");
        context.update("form:acumDif");
    }

    public void duplicarParametroAutoliq() {
        if (parametroTablaSeleccionado != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (guardado == false) {
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
                context.update("formularioDialogos:duplicarParametro");
                context.execute("DuplicarRegistroParametro.show()");
                // parametroTablaSeleccionado = null;
                activoBtnsPaginas = true;
                context.update("form:novedadauto");
                context.update("form:incaPag");
                context.update("form:eliminarToda");
                context.update("form:procesoLiq");
                context.update("form:acumDif");
            } else {
                context.execute("confirmarGuardar.show()");
            }
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

            context.update("form:infoRegistroParametro");
            context.update("form:datosParametroAuto");
            context.execute("DuplicarRegistroParametro.hide()");
            activoBtnsPaginas = true;
            context.update("form:novedadauto");
            context.update("form:incaPag");
            context.update("form:eliminarToda");
            context.update("form:procesoLiq");
            context.update("form:acumDif");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            duplicarParametro = new ParametrosAutoliq();
            cambiosParametro = true;
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("errorRegNullParametro.show()");
        }
    }

    public void limpiarDuplicarParametroAutoliq() {
        duplicarParametro = new ParametrosAutoliq();
        duplicarParametro.setEmpresa(new Empresas());
        duplicarParametro.setTipotrabajador(new TiposTrabajadores());
    }

    public void validarBorradoPagina() {
        if (parametroTablaSeleccionado != null) {
            int tam = 0;
            if (listaAportesEntidades != null) {
                tam = listaAportesEntidades.size();
            }
            if (tam == 0) {
                borrarParametroAutoliq();
            } else {
                RequestContext.getCurrentInstance().execute("errorBorrarParametro.show()");
            }
        }
        if (aporteTablaSeleccionado != null) {
            borrarAporteEntidad();
        }
    }

    public void borrarParametroAutoliq() {
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

        context.update("form:infoRegistroParametro");
        context.update("form:datosParametroAuto");
        parametroTablaSeleccionado = null;
        activoBtnsPaginas = true;
        context.update("form:novedadauto");
        context.update("form:incaPag");
        context.update("form:eliminarToda");
        context.update("form:procesoLiq");
        context.update("form:acumDif");
        cambiosParametro = true;
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrarAporteEntidad() {
        if (!listAportesEntidadesModificar.isEmpty() && listAportesEntidadesModificar.contains(aporteTablaSeleccionado)) {
            int modIndex = listAportesEntidadesModificar.indexOf(aporteTablaSeleccionado);
            listAportesEntidadesModificar.remove(modIndex);
            listAportesEntidadesBorrar.add(aporteTablaSeleccionado);
        } else {
            listAportesEntidadesBorrar.add(aporteTablaSeleccionado);
        }
        listaParametrosAutoliq.remove(aporteTablaSeleccionado);
        if (tipoLista == 1) {
            filtrarListaAportesEntidades.remove(aporteTablaSeleccionado);
        }

        RequestContext context = RequestContext.getCurrentInstance();

        modificarInfoRegistroAporte(listaAportesEntidades.size());

        context.update("form:infoRegistroAporte");
        context.update("form:tablaAportesEntidades");
        aporteTablaSeleccionado = null;
        parametroTablaSeleccionado = null;
        cambiosAporte = true;
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void borrarAporteEntidadProcesoAutomatico() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (listaAportesEntidades != null) {
                if (!listaAportesEntidades.isEmpty()) {
                    if (guardado == false) {
                        guardadoGeneral();
                    }
                    administrarParametroAutoliq.borrarAportesEntidadesProcesoAutomatico(parametroTablaSeleccionado.getEmpresa().getSecuencia(), parametroTablaSeleccionado.getMes(), parametroTablaSeleccionado.getAno());
                    listaParametrosAutoliq = null;
                    getListaParametrosAutoliq();
                    modificarInfoRegistroParametro(listaParametrosAutoliq.size());
                    listaAportesEntidades = null;
                    modificarInfoRegistroAporte(0);
                    disabledBuscar = true;
                    visibilidadMostrarTodos = "hidden";
                    RequestContext.getCurrentInstance().update("form:mostrarTodos");
                    context.update("form:ACEPTAR");
                    context.update("form:buscar");
                    context.update("form:infoRegistroAporte");
                    context.update("form:infoRegistroParametro");
                    context.update("form:datosParametroAuto");
                    context.update("form:tablaAportesEntidades");
                    activoBtnsPaginas = true;
                    context.update("form:novedadauto");
                    context.update("form:incaPag");
                    context.update("form:eliminarToda");
                    context.update("form:procesoLiq");
                    context.update("form:acumDif");
                    System.out.println("El borrado fue realizado con éxito");
                    FacesMessage msg = new FacesMessage("Información", "El borrado fue realizado con éxito. Recuerde que los cambios manuales deben ser borrados manualmente");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    context.update("form:growl");
                }
            } else {
                System.out.println("No hay información para borrar");
                FacesMessage msg = new FacesMessage("Información", "No hay información para borrar");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                context.update("form:growl");
            }

        } catch (Exception e) {
            System.out.println("Error borrarAporteEntidadProcesoAutomatico Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el proceso de borrado de Aportes Entidades.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
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
                        context.execute("procesoLiquidacionOK.show()");
                    } else {
                        context.execute("errorProcesoLiquidacionFechas.show()");
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
                String procesoInsertar = "";
                String procesoActualizar = "";

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
                    //parametroTablaSeleccionado = null;
                    activoBtnsPaginas = true;
                    visibilidadMostrarTodos = "hidden";
                    RequestContext.getCurrentInstance().update("form:mostrarTodos");
                    context.update("form:PanelTotal");
                    System.out.println("El proceso de Liquidación fue realizado con éxito");
                    System.out.println("lista aportes entidades : " + listaAportesEntidades.size());
                    FacesMessage msg = new FacesMessage("Información", "El proceso de Liquidación fue realizado con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    context.update("form:growl");
                } else if ((procesoInsertar.equals("ERROR_PERSISTENCIA")) || (procesoActualizar.equals("ERROR_PERSISTENCIA"))) {
                    System.out.println("entró a else if");
                    FacesMessage msg = new FacesMessage("Información", "Ocurrió un error en la ejecución del proceso de liquidación. Por favor, revisar los archivos de error de la carpeta SalidasUTL");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    context.update("form:growl");
                }

            }

        } catch (Exception e) {
            System.out.println("Error procesoLiquidacionOK Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el proceso de Liquidación.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
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
            // parametroTablaSeleccionado = null;
            activoBtnsPaginas = true;
            context.update("form:PanelTotal");
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
                            context.execute("acumularDiferenciaOK.show()");
                        } else {
                            context.execute("errorAcumularDiferencia.show()");
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
            context.update("form:growl");
//            listaAportesEntidades = null;
//            getListaAportesEntidades();
//            modificarInfoRegistroAporte(listaAportesEntidades.size());
            guardadoGeneral();
            context.update("form:tablaAportesEntidades");

        } catch (Exception e) {
            System.out.println("Error acumularDiferenciaOK Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el proceso de Acumular Diferencias.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
        }
    }

    public void activarCtrlF11() {

//        if (parametroTablaSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 0) {
                altoTabla = "26";
                parametroAno = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroAno");
                parametroAno.setFilterStyle("width: 85%");
                parametroTipoTrabajador = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroTipoTrabajador");
                parametroTipoTrabajador.setFilterStyle("width: 85%");
                parametroEmpresa = (Column) c.getViewRoot().findComponent("form:datosParametroAuto:parametroEmpresa");
                parametroEmpresa.setFilterStyle("width: 85%");
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
//        }
//        if (aporteTablaSeleccionado != null) {
//            if (banderaAporte == 0) {
//                activarFiltradoAporteEntidad();
//                banderaAporte = 1;
//            } else if (banderaAporte == 1) {
//                desactivarFiltradoAporteEntidad();
//                banderaAporte = 0;
//                filtrarListaAportesEntidades = null;
//                tipoListaAporte = 0;
//            }
//        }
    }

    public void desactivarFiltradoAporteEntidad() {
        FacesContext c = FacesContext.getCurrentInstance();
        altoTablaAporte = "180";

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

        altoDivTablaInferiorIzquierda = "195px";
        topDivTablaInferiorIzquierda = "37px";

        altoDivTablaInferiorDerecha = "195px";
        topDivTablaInferiorDerecha = "37px";

        RequestContext.getCurrentInstance().update("form:PanelTotal");

    }

    public void activarFiltradoAporteEntidad() {
        FacesContext c = FacesContext.getCurrentInstance();
        altoTablaAporte = "158";

        aporteCodigoEmpleado = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteCodigoEmpleado");
        aporteCodigoEmpleado.setFilterStyle("width: 85%");

        aporteAno = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteAno");
        aporteAno.setFilterStyle("width: 85%");

        aporteMes = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteMes");
        aporteMes.setFilterStyle("width: 85%");

        aporteNombreEmpleado = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteNombreEmpleado");
        aporteNombreEmpleado.setFilterStyle("width: 85%");

        aporteNIT = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteNIT");
        aporteNIT.setFilterStyle("width: 85%");

        aporteTercero = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTercero");
        aporteTercero.setFilterStyle("width: 85%");

        aporteTipoEntidad = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTipoEntidad");
        aporteTipoEntidad.setFilterStyle("width: 85%");

        aporteEmpleado = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteEmpleado");
        aporteEmpleado.setFilterStyle("width: 85%");

        aporteEmpleador = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteEmpleador");
        aporteEmpleador.setFilterStyle("width: 85%");

        aporteAjustePatronal = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteAjustePatronal");
        aporteAjustePatronal.setFilterStyle("width: 85%");

        aporteSolidaridadl = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSolidaridadl");
        aporteSolidaridadl.setFilterStyle("width: 85%");

        aporteSubSistencia = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSubSistencia");
        aporteSubSistencia.setFilterStyle("width: 85%");

        aporteSubsPensionados = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSubsPensionados");
        aporteSubsPensionados.setFilterStyle("width: 85%");

        aporteSalarioBasico = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSalarioBasico");
        aporteSalarioBasico.setFilterStyle("width: 85%");

        aporteIBC = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteIBC");
        aporteIBC.setFilterStyle("width: 85%");

        aporteIBCReferencia = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteIBCReferencia");
        aporteIBCReferencia.setFilterStyle("width: 85%");

        aporteDiasCotizados = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteDiasCotizados");
        aporteDiasCotizados.setFilterStyle("width: 85%");

        aporteTipoAportante = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTipoAportante");
        aporteTipoAportante.setFilterStyle("width: 85%");

        aporteING = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteING");
        aporteING.setFilterStyle("width: 85%");

        aporteRET = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteRET");
        aporteRET.setFilterStyle("width: 85%");

        aporteTDA = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTDA");
        aporteTDA.setFilterStyle("width: 85%");

        aporteTAA = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTAA");
        aporteTAA.setFilterStyle("width: 85%");

        aporteVSP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteVSP");
        aporteVSP.setFilterStyle("width: 85%");

        aporteVTE = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteVTE");
        aporteVTE.setFilterStyle("width: 85%");

        aporteVST = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteVST");
        aporteVST.setFilterStyle("width: 85%");

        aporteSLN = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSLN");
        aporteSLN.setFilterStyle("width: 85%");

        aporteIGE = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteIGE");
        aporteIGE.setFilterStyle("width: 85%");

        aporteLMA = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteLMA");
        aporteLMA.setFilterStyle("width: 85%");

        aporteVAC = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteVAC");
        aporteVAC.setFilterStyle("width: 85%");

        aporteAVP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteAVP");
        aporteAVP.setFilterStyle("width: 85%");

        aporteVCT = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteVCT");
        aporteVCT.setFilterStyle("width: 85%");

        aporteIRP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteIRP");
        aporteIRP.setFilterStyle("width: 85%");

        aporteSUS = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSUS");
        aporteSUS.setFilterStyle("width: 85%");

        aporteINTE = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteINTE");
        aporteINTE.setFilterStyle("width: 85%");

        aporteTarifaEPS = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaEPS");
        aporteTarifaEPS.setFilterStyle("width: 85%");

        aporteTarifaAAFP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaAAFP");
        aporteTarifaAAFP.setFilterStyle("width: 85%");

        aporteTarifaACTT = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaACTT");
        aporteTarifaACTT.setFilterStyle("width: 85%");

        aporteCodigoCTT = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteCodigoCTT");
        aporteCodigoCTT.setFilterStyle("width: 85%");

        aporteAVPEValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteAVPEValor");
        aporteAVPEValor.setFilterStyle("width: 85%");

        aporteAVPPValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteAVPPValor");
        aporteAVPPValor.setFilterStyle("width: 85%");

        aporteRETCONTAValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteRETCONTAValor");
        aporteRETCONTAValor.setFilterStyle("width: 85%");

        aporteCodigoNEPS = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteCodigoNEPS");
        aporteCodigoNEPS.setFilterStyle("width: 85%");

        aporteCodigoNAFP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteCodigoNAFP");
        aporteCodigoNAFP.setFilterStyle("width: 85%");

        aporteEGValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteEGValor");
        aporteEGValor.setFilterStyle("width: 85%");

        aporteEGAutorizacion = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteEGAutorizacion");
        aporteEGAutorizacion.setFilterStyle("width: 85%");

        aporteMaternidadValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteMaternidadValor");
        aporteMaternidadValor.setFilterStyle("width: 85%");

        aporteMaternidadAuto = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteMaternidadAuto");
        aporteMaternidadAuto.setFilterStyle("width: 85%");

        aporteUPCValor = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteUPCValor");
        aporteUPCValor.setFilterStyle("width: 85%");

        aporteTipo = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTipo");
        aporteTipo.setFilterStyle("width: 85%");

        aporteTipoPensionado = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTipoPensionado");
        aporteTipoPensionado.setFilterStyle("width: 85%");

        aportePensionCompartida = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aportePensionCompartida");
        aportePensionCompartida.setFilterStyle("width: 85%");

        aporteExtranjero = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteExtranjero");
        aporteExtranjero.setFilterStyle("width: 85%");

        aporteFechaIngreso = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteFechaIngreso");
        aporteFechaIngreso.setFilterStyle("width: 85%");

        aporteSubTipoCotizante = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteSubTipoCotizante");
        aporteSubTipoCotizante.setFilterStyle("width: 85%");

        aporteTarifaCaja = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaCaja");
        aporteTarifaCaja.setFilterStyle("width: 85%");

        aporteTarifaSena = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaSena");
        aporteTarifaSena.setFilterStyle("width: 85%");

        aporteTarifaICBF = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaICBF");
        aporteTarifaICBF.setFilterStyle("width: 85%");

        aporteTarifaESAP = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaESAP");
        aporteTarifaESAP.setFilterStyle("width: 85%");

        aporteTarifaMEN = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteTarifaMEN");
        aporteTarifaMEN.setFilterStyle("width: 85%");

        RequestContext.getCurrentInstance().update("form:tablaAportesEntidades:tablaAportesEntidades");

        altoDivTablaInferiorIzquierda = "173px";
        altoDivTablaInferiorDerecha = "173px";

        topDivTablaInferiorIzquierda = "59px";
        topDivTablaInferiorDerecha = "59px";

        RequestContext.getCurrentInstance().update("form:PanelTotal");

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
        numeroScrollAporte = 505;
        rowsAporteEntidad = 20;

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
        if (LND == 0) {
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
        } else if (LND == 2) {
            tipoActualizacion = 2;
        }
        if (dialogo == 1) {
            context.update("formularioLovTipoTrabajador:TipoTrabajadorDialogo");
            context.execute("TipoTrabajadorDialogo.show()");
        }
        if (dialogo == 2) {
            context.update("formularioLovEmpresa:EmpresaDialogo");
            context.execute("EmpresaDialogo.show()");
            modificarInfoRegistroEmpresa(lovEmpresas.size());
        }
    }

    public void asignarIndexAporte(AportesEntidades aporte, int LND, int dialogo) {
        aporteTablaSeleccionado = aporte;
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            tipoActualizacion = 0;
        }
        if (dialogo == 1) {
            context.update("formularioLovEmpleado:EmpleadoDialogo");
            context.execute("EmpleadoDialogo.show()");
        }
        if (dialogo == 2) {
            context.update("formularioLovTercero:TerceroDialogo");
            context.execute("TerceroDialogo.show()");
        }
        if (dialogo == 3) {
            context.update("formularioLovTipoEntidad:TipoEntidadDialogo");
            context.execute("TipoEntidadDialogo.show()");
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
            context.update("form:datosParametroAuto");
        } else if (tipoActualizacion == 1) {
            nuevoParametro.setTipotrabajador(tipoTrabajadorSeleccionado);
            context.update("formularioDialogos:nuevaTipoTrabajadorParametro");
        } else if (tipoActualizacion == 2) {
            duplicarParametro.setTipotrabajador(tipoTrabajadorSeleccionado);
            context.update("formularioDialogos:duplicarTipoTrabajadorParametro");
        }
        filtrarLovTiposTrabajadores = null;
        tipoTrabajadorSeleccionado = new TiposTrabajadores();
        aceptar = true;
        parametroTablaSeleccionado = null;

        activoBtnsPaginas = true;
        context.update("form:novedadauto");
        context.update("form:incaPag");
        context.update("form:eliminarToda");
        context.update("form:procesoLiq");
        context.update("form:acumDif");
        tipoActualizacion = -1;/*
         context.update("formularioLovTipoTrabajador:TipoTrabajadorDialogo");
         context.update("formularioLovTipoTrabajador:lovTipoTrabajador");
         context.update("formularioLovTipoTrabajador:aceptarTT");*/

        context.reset("formularioLovTipoTrabajador:lovTipoTrabajador:globalFilter");
        context.execute("lovTipoTrabajador.clearFilters()");
        context.execute("TipoTrabajadorDialogo.hide()");
    }

    public void cancelarCambioTipoTrabajador() {
        filtrarLovTiposTrabajadores = null;
        tipoTrabajadorSeleccionado = new TiposTrabajadores();
        aceptar = true;
        parametroTablaSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        activoBtnsPaginas = true;
        context.update("form:novedadauto");
        context.update("form:incaPag");
        context.update("form:eliminarToda");
        context.update("form:procesoLiq");
        context.update("form:acumDif");
        tipoActualizacion = -1;
        permitirIndex = true;
        context.reset("formularioLovTipoTrabajador:lovTipoTrabajador:globalFilter");
        context.execute("lovTipoTrabajador.clearFilters()");
        context.execute("TipoTrabajadorDialogo.hide()");
    }

    public void actualizarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 1) {
            nuevoParametro.setEmpresa(empresaSeleccionada);
            context.update("formularioDialogos:nuevaEmpresaParametro");
        } else if (tipoActualizacion == 2) {
            duplicarParametro.setEmpresa(empresaSeleccionada);
            context.update("formularioDialogos:duplicarEmpresaParametro");
        }
        filtrarLovEmpresas = null;
        empresaSeleccionada = new Empresas();
        aceptar = true;
        parametroTablaSeleccionado = null;
        activoBtnsPaginas = true;
        context.update("form:novedadauto");
        context.update("form:incaPag");
        context.update("form:eliminarToda");
        context.update("form:procesoLiq");
        context.update("form:acumDif");
        tipoActualizacion = -1;/*
         context.update("formularioLovEmpresa:EmpresaDialogo");
         context.update("formularioLovEmpresa:lovEmpresa");
         context.update("formularioLovEmpresa:aceptarE");*/

        context.reset("formularioLovEmpresa:lovEmpresa:globalFilter");
        context.execute("lovEmpresa.clearFilters()");
        context.execute("EmpresaDialogo.hide()");
    }

    public void cancelarCambioEmpresa() {
        filtrarLovEmpresas = null;
        empresaSeleccionada = new Empresas();
        aceptar = true;
        parametroTablaSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        activoBtnsPaginas = true;
        context.update("form:novedadauto");
        context.update("form:incaPag");
        context.update("form:eliminarToda");
        context.update("form:procesoLiq");
        context.update("form:acumDif");
        tipoActualizacion = -1;
        permitirIndex = true;
        context.reset("formularioLovEmpresa:lovEmpresa:globalFilter");
        context.execute("lovEmpresa.clearFilters()");
        context.execute("EmpresaDialogo.hide()");
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
            context.update("form:tablaAportesEntidades");

        }
        filtrarLovEmpleados = null;
        empleadoSeleccionado = new Empleados();
        aceptar = true;
        aporteTablaSeleccionado = null;
        parametroTablaSeleccionado = null;
        tipoActualizacion = -1;/*
         context.update("formularioLovEmpleado:EmpleadoDialogo");
         context.update("formularioLovEmpleado:lovEmpleado");
         context.update("formularioLovEmpleado:aceptarEMPL");*/

        context.reset("formularioLovEmpleado:lovEmpleado:globalFilter");
        context.execute("lovEmpleado.clearFilters()");
        context.execute("EmpleadoDialogo.hide()");
    }

    public void cancelarCambioEmpleado() {
        filtrarLovEmpleados = null;
        empleadoSeleccionado = new Empleados();
        aceptar = true;
        aporteTablaSeleccionado = null;

        parametroTablaSeleccionado = null;
        tipoActualizacion = -1;
        permitirIndexAporte = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioLovEmpleado:lovEmpleado:globalFilter");
        context.execute("lovEmpleado.clearFilters()");
        context.execute("EmpleadoDialogo.hide()");
    }

    public void actualizarTercero() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaAporte == 0) {
                aporteTablaSeleccionado.setTerceroRegistro(terceroSeleccionado);
                if (listAportesEntidadesModificar.isEmpty()) {
                    listAportesEntidadesModificar.add(aporteTablaSeleccionado);
                } else if (!listAportesEntidadesModificar.contains(aporteTablaSeleccionado)) {
                    listAportesEntidadesModificar.add(aporteTablaSeleccionado);
                }
            } else {
                aporteTablaSeleccionado.setTerceroRegistro(terceroSeleccionado);
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
            context.update("form:tablaAportesEntidades");
        }
        filtrarLovTerceros = null;
        terceroSeleccionado = new Terceros();
        aceptar = true;
        aporteTablaSeleccionado = null;
        parametroTablaSeleccionado = null;
        tipoActualizacion = -1;/*
         context.update("formularioLovTercero:TerceroDialogo");
         context.update("formularioLovTercero:lovTercero");
         context.update("formularioLovTercero:aceptarT");*/

        context.reset("formularioLovTercero:lovTercero:globalFilter");
        context.execute("lovTercero.clearFilters()");
        context.execute("TerceroDialogo.hide()");
    }

    public void cancelarCambioTercero() {
        filtrarLovTerceros = null;
        terceroSeleccionado = new Terceros();
        aceptar = true;
        aporteTablaSeleccionado = null;
        parametroTablaSeleccionado = null;
        tipoActualizacion = -1;
        permitirIndexAporte = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioLovTercero:lovTercero:globalFilter");
        context.execute("lovTercero.clearFilters()");
        context.execute("TerceroDialogo.hide()");
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
            context.update("form:tablaAportesEntidades");
        }
        filtrarLovTiposEntidades = null;
        tipoEntidadSeleccionado = new TiposEntidades();
        aceptar = true;
        aporteTablaSeleccionado = null;
        parametroTablaSeleccionado = null;
        tipoActualizacion = -1;/*
         context.update("formularioLovTipoEntidad:TipoEntidadDialogo");
         context.update("formularioLovTipoEntidad:lovTipoEntidad");
         context.update("formularioLovTipoEntidad:aceptarTE");*/

        context.reset("formularioLovTipoEntidad:lovTipoEntidad:globalFilter");
        context.execute("lovTipoEntidad.clearFilters()");
        context.execute("TipoEntidadDialogo.hide()");
    }

    public void cancelarCambioTipoEntidad() {
        filtrarLovTiposEntidades = null;
        tipoEntidadSeleccionado = new TiposEntidades();
        aceptar = true;
        parametroTablaSeleccionado = null;
        tipoActualizacion = -1;
        permitirIndexAporte = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioLovTipoEntidad:lovTipoEntidad:globalFilter");
        context.execute("lovTipoEntidad.clearFilters()");
        context.execute("TipoEntidadDialogo.hide()");
    }

    public void dispararDialogoBuscar() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (guardado == true) {
            context.update("formularioLovAporteEntidad:BuscarAporteDialogo");
            context.execute("BuscarAporteDialogo.show()");
        } else {
            context.execute("confirmarGuardar.show()");
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
        context.update("form:novedadauto");
        context.update("form:incaPag");
        context.update("form:eliminarToda");
        context.update("form:procesoLiq");
        context.update("form:acumDif");
        tipoActualizacion = -1;
        visibilidadMostrarTodos = "visible";
        RequestContext.getCurrentInstance().update("form:mostrarTodos");
        modificarInfoRegistroParametro(listaParametrosAutoliq.size());
        modificarInfoRegistroAporte(listaAportesEntidades.size());
        context.update("form:PanelTotal");
        /*
         context.update("formularioLovAporteEntidad:BuscarAporteDialogo");
         context.update("formularioLovAporteEntidad:lovBuscarAporte");
         context.update("formularioLovAporteEntidad:aceptarBA");*/
        context.reset("formularioLovAporteEntidad:lovBuscarAporte:globalFilter");
        context.execute("lovBuscarAporte.clearFilters()");
        context.execute("BuscarAporteDialogo.hide()");
    }

    public void cancelarCambioAporteEntidad() {
        filtrarLovEmpresas = null;
        empresaSeleccionada = new Empresas();
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        activoBtnsPaginas = true;
        context.update("form:novedadauto");
        context.update("form:incaPag");
        context.update("form:eliminarToda");
        context.update("form:procesoLiq");
        context.update("form:acumDif");
        tipoActualizacion = -1;
        permitirIndex = true;
        context.reset("formularioLovAporteEntidad:lovBuscarAporte:globalFilter");
        context.execute("lovBuscarAporte.clearFilters()");
        context.execute("BuscarAporteDialogo.hide()");
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
        numeroScrollAporte = 505;
        rowsAporteEntidad = 20;
        cargarDatosNuevos();
//        parametroTablaSeleccionado = null;
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (parametroTablaSeleccionado != null) {
            if (cualCelda == 2) {
                context.update("formularioLovTipoTrabajador:TipoTrabajadorDialogo");
                context.execute("TipoTrabajadorDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (aporteTablaSeleccionado != null) {
            if (cualCeldaAporte == 3) {
                context.update("formularioLovEmpleado:EmpleadoDialogo");
                context.execute("EmpleadoDialogo.show()");
                tipoActualizacion = 0;
            }
            if (cualCeldaAporte == 4) {
                context.update("formularioLovTercero:TerceroDialogo");
                context.execute("TerceroDialogo.show()");
                tipoActualizacion = 0;
            }
            if (cualCeldaAporte == 6) {
                context.update("formularioLovTipoEntidad:TipoEntidadDialogo");
                context.execute("TipoEntidadDialogo.show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public String exportXMLTabla() {
        String tabla = "";
        if (parametroTablaSeleccionado != null) {
            tabla = ":formExportar:datosParametroAutoExportar";
        }
        if (aporteTablaSeleccionado != null) {
            tabla = ":formExportar:datosAporteEntidadExportar";
        }
        return tabla;
    }

    public String exportXMLNombreArchivo() {
        String nombre = "";
        if (parametroTablaSeleccionado != null) {
            nombre = "ParametrosAutoliquidacion_XML";
        }
        if (aporteTablaSeleccionado != null) {
            nombre = "AportesEntidades_XML";
        }
        return nombre;
    }

    public void validarExportPDF() throws IOException {
        if (parametroTablaSeleccionado != null) {
            exportPDF();
        }
        if (aporteTablaSeleccionado != null) {
            exportPDF_AE();
        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosParametroAutoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "ParametrosAutoliquidacion_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        parametroTablaSeleccionado = null;
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
        if (parametroTablaSeleccionado != null) {
            exportXLS();
        }
        if (aporteTablaSeleccionado != null) {
            exportXLS_AE();
        }
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosParametroAutoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "ParametrosAutoliquidacion_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        parametroTablaSeleccionado = null;
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
            context.update("form:tablaAportesEntidades");
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
            if (administrarRastros.verificarHistoricosTabla("PARAMETROSAUTOLIQ")) {
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

        }
        parametroTablaSeleccionado = null;
        activoBtnsPaginas = true;
        context.update("form:novedadauto");
        context.update("form:incaPag");
        context.update("form:eliminarToda");
        context.update("form:procesoLiq");
        context.update("form:acumDif");
    }

    public void verificarRastroAporteEntidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaAportesEntidades != null) {
            if (aporteTablaSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(aporteTablaSeleccionado.getSecuencia(), "APORTESENTIDADES");
                backUpSecRegistroAporte = aporteTablaSeleccionado.getSecuencia();
                parametroTablaSeleccionado = null;
                if (resultado == 1) {
                    context.execute("errorObjetosDBAporte.show()");
                } else if (resultado == 2) {
                    context.execute("confirmarRastroAporte.show()");
                } else if (resultado == 3) {
                    context.execute("errorRegistroRastro.show()");
                } else if (resultado == 4) {
                    context.execute("errorTablaConRastro.show()");
                } else if (resultado == 5) {
                    context.execute("errorTablaSinRastro.show()");
                }
            } else {
                context.execute("seleccionarRegistro.show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("APORTESENTIDADES")) {
                context.execute("confirmarRastroHistoricoAporte.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

        }
        aporteTablaSeleccionado = null;
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
//        RequestContext.getCurrentInstance().update("formularioLovTercero:infoRegistroTercero");
    }

    public void eventoFiltrarLovTipoEntidades() {
        modificarInfoRegistroTiposEntidades(filtrarLovTiposEntidades.size());
//        RequestContext.getCurrentInstance().update("formularioLovTipoEntidad:infoRegistroTipoEntidad");
    }

    public void eventoFiltrarLovTipoTrabajador() {
        modificarInfoRegistroTiposTrabajadores(filtrarLovTiposTrabajadores.size());
//        RequestContext.getCurrentInstance().update("formularioLovTipoTrabajador:infoRegistroTipoTrabajador");
    }

    public void eventoFiltrarEmpleados() {
        modificarInfoRegistroEmpleados(filtrarLovEmpleados.size());
    }

    public void eventoFiltrarAporteEntidad() {
        modificarInfoRegistroAportesEntidades(filtrarLovAportesEntidades.size());
//        RequestContext.getCurrentInstance().update("formularioLovTipoEntidad:infoRegistroTipoEntidad");
    }

    //GET - SET//
    public List<ParametrosAutoliq> getListaParametrosAutoliq() {
        try {
            if (listaParametrosAutoliq == null) {
                listaParametrosAutoliq = administrarParametroAutoliq.consultarParametrosAutoliq();
                if (listaParametrosAutoliq != null) {
                    int tam = listaParametrosAutoliq.size();
                    if (tam > 0) {
                        parametroTablaSeleccionado = listaParametrosAutoliq.get(0);
                    }

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
        lovTiposTrabajadores = administrarParametroAutoliq.lovTiposTrabajadores();
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
                    ParametrosAutoliq aux = null;
                    if (tipoLista == 0) {
                        aux = parametroTablaSeleccionado;
                    } else {
                        aux = parametroTablaSeleccionado;
                    }
                    listaAportesEntidades = administrarParametroAutoliq.consultarAportesEntidadesPorParametroAutoliq(aux.getEmpresa().getSecuencia(), aux.getMes(), aux.getAno());
                    if (listaAportesEntidades != null) {
                        int tam = listaAportesEntidades.size();
                        if (tam > 0) {
                            aporteTablaSeleccionado = listaAportesEntidades.get(0);
                        }
                        for (int i = 0; i < listaAportesEntidades.size(); i++) {
                            if (listaAportesEntidades.get(i).getTerceroRegistro() == null) {
                                listaAportesEntidades.get(i).setTerceroRegistro(null);
                            }
                        }
                    }
                }
            }
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
        lovEmpleados = administrarParametroAutoliq.lovEmpleados();
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
        lovTerceros = administrarParametroAutoliq.lovTerceros();
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
        lovTiposEntidades = administrarParametroAutoliq.lovTiposEntidades();
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
                ParametrosAutoliq aux = null;
                if (tipoLista == 0) {
                    aux = parametroTablaSeleccionado;
                } else {
                    aux = parametroTablaSeleccionado;
                }
                lovAportesEntidades = administrarParametroAutoliq.consultarAportesEntidadesPorParametroAutoliq(aux.getEmpresa().getSecuencia(), aux.getMes(), aux.getAno());
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

    public String getAltoDivTablaInferiorIzquierda() {
        return altoDivTablaInferiorIzquierda;
    }

    public void setAltoDivTablaInferiorIzquierda(String altoDivTablaInferiorIzquierda) {
        this.altoDivTablaInferiorIzquierda = altoDivTablaInferiorIzquierda;
    }

    public String getAltoDivTablaInferiorDerecha() {
        return altoDivTablaInferiorDerecha;
    }

    public void setAltoDivTablaInferiorDerecha(String altoDivTablaInferiorDerecha) {
        this.altoDivTablaInferiorDerecha = altoDivTablaInferiorDerecha;
    }

    public String getTopDivTablaInferiorIzquierda() {
        return topDivTablaInferiorIzquierda;
    }

    public void setTopDivTablaInferiorIzquierda(String topDivTablaInferiorIzquierda) {
        this.topDivTablaInferiorIzquierda = topDivTablaInferiorIzquierda;
    }

    public String getTopDivTablaInferiorDerecha() {
        return topDivTablaInferiorDerecha;

    }

    public void setTopDivTablaInferiorDerecha(String topDivTablaInferiorDerecha) {
        this.topDivTablaInferiorDerecha = topDivTablaInferiorDerecha;
    }

    public int getNumeroScrollAporte() {
        return numeroScrollAporte;
    }

    public void setNumeroScrollAporte(int numeroPrueba) {
        this.numeroScrollAporte = numeroPrueba;
    }

    public int getRowsAporteEntidad() {
        return rowsAporteEntidad;
    }

    public void setRowsAporteEntidad(int rowsAporteEntidad) {
        this.rowsAporteEntidad = rowsAporteEntidad;
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
}
