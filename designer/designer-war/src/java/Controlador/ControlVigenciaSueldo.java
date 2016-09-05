package Controlador;

import Entidades.*;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciasSueldosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
 * @author AndresPineda
 */
@ManagedBean
@SessionScoped
public class ControlVigenciaSueldo implements Serializable {

    @EJB
    AdministrarVigenciasSueldosInterface administrarVigenciasSueldos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //Vigencias Sueldos 
    private List<VigenciasSueldos> listVigenciasSueldos;
    private List<VigenciasSueldos> filtrarVigenciasSueldos;
    private VigenciasSueldos vigenciaSueldoSeleccionada;
    //Vigencias Afiliaciones
    private List<VigenciasAfiliaciones> listVigenciasAfiliaciones;
    private List<VigenciasAfiliaciones> filtrarVigenciasAfiliaciones;
    private VigenciasAfiliaciones vigenciaAfiliacioneSeleccionada;
    //Tipo Sueldo
    private List<TiposSueldos> listTiposSueldos;
    private TiposSueldos tipoSueldoSeleccionado;
    private List<TiposSueldos> filtrarTiposSueldos;
    //Motivo Cambio Sueldo
    private List<MotivosCambiosSueldos> listMotivosCambiosSueldos;
    private MotivosCambiosSueldos motivoCambioSueldoSeleccionado;
    private List<MotivosCambiosSueldos> filtrarMotivosCambiosSueldos;
    //Tipos Entidades
    private List<TiposEntidades> listTiposEntidades;
    private TiposEntidades tipoEntidadSeleccionado;
    private List<TiposEntidades> filtrarTiposEntidades;
    //Terceros
    private List<Terceros> listTerceros;
    private Terceros terceroSeleccionado;
    private List<Terceros> filtrarTerceros;
    //Empleado
    private Empleados empleado;
    //Tipo Actualizacion
    private int tipoActualizacion;
    //Activo/Desactivo Crtl + F11
    private int bandera;
    //Activo/Desactivo VP Crtl + F11
    private int banderaVA;
    //Columnas Tabla VL
    private Column vSFechaVigencia, vSMotivoCambioSueldo, vSTipoSueldo, vSVigenciaRetroactivo, vSValor, vSObservaciones, vSRetroactivo;
    //Columnas Tabla VP
    private Column vAFechaInicial, vAFechaFinal, vATercero, vATipoEntidad, vAValor;
    //Otros
    private boolean aceptar;
    //private int index;
    //modificar
    private List<VigenciasSueldos> listVSModificar;
    private List<VigenciasAfiliaciones> listVAModificar;
    private boolean guardado;
    //crear VL
    public VigenciasSueldos nuevaVigenciaS;
    //crear VP
    public VigenciasAfiliaciones nuevaVigenciaA;
    private List<VigenciasSueldos> listVSCrear;
    private BigInteger nuevaVSueldoSecuencia;
    private int paraNuevaVSueldo;
    //borrar VL
    private List<VigenciasSueldos> listVSBorrar;
    private List<VigenciasAfiliaciones> listVABorrar;
    //editar celda
    private VigenciasSueldos editarVS;
    private VigenciasAfiliaciones editarVA;
    private int cualCelda, tipoLista;
    //duplicar
    private VigenciasSueldos duplicarVS;
    //Autocompletar
    private boolean permitirIndex, permitirIndexVA;
    //Variables Autompletar
    private String motivoCambioSueldo, tiposEntidades, tiposSueldos, terceros;
    //Indices VigenciaProrrateo / VigenciaProrrateoProyecto
    //private int indexVA;
    //Indice de celdas VigenciaProrrateo / VigenciaProrrateoProyecto
    private int cualCeldaVA, tipoListaVA;
    //Index Auxiliar Para Nuevos Registros
    //private int indexAuxVS;
    //Duplicar Vigencia Prorrateo
    private VigenciasAfiliaciones duplicarVA;
    private List<VigenciasAfiliaciones> listVACrear;
    private String nombreXML;
    private String nombreTabla;
    //Banderas Boolean de operaciones sobre vigencias prorrateos y vigencias prorrateos proyectos
    private boolean cambioVigenciaA;
    //Retroactivo o no para prestaciones
    private boolean retroactivo;
    //Mostrar todos o actual
    private boolean mostrarActual;
    //valor total consultar actual
    private int valorTotal;
    //bandera cambios vigencias sueldos
    private boolean cambiosVS;
    private String msnConfirmarRastro, msnConfirmarRastroHistorico;
    private BigInteger backUp;
    private String nombreTablaRastro;
    private Date fechaParametro, fechaVig, fechaRetro, fechaIni, fechaFin, fechaSis;
    private String altoTabla1, altoTabla2;
    //
    private String infoRegistroS, infoRegistroTipoSueldo, infoRegistroMotivoCambioSueldo;
    private String infoRegistroD, infoRegistroTercero, infoRegistroTipoEntidad;
    // variable para crear VS
    private Unidades uniPago;
    // validaciones
    String mensajeValidacion;
    private DataTable tabla;
    //
    private boolean activarLOV;

    public ControlVigenciaSueldo() {

        nombreTablaRastro = "";
        backUp = null;
        listVigenciasAfiliaciones = null;
        msnConfirmarRastro = "";
        msnConfirmarRastroHistorico = "";
        listTiposSueldos = null;
        listVigenciasSueldos = null;
        listMotivosCambiosSueldos = null;
        listTiposEntidades = null;
        empleado = new Empleados();
        listTerceros = null;
        //Otros
        aceptar = true;
        //borrar aficiones
        listVSBorrar = new ArrayList<VigenciasSueldos>();
        listVABorrar = new ArrayList<VigenciasAfiliaciones>();
        //crear aficiones
        listVSCrear = new ArrayList<VigenciasSueldos>();
        paraNuevaVSueldo = 0;
        //modificar aficiones
        listVSModificar = new ArrayList<VigenciasSueldos>();
        listVAModificar = new ArrayList<VigenciasAfiliaciones>();
        //editar
        editarVS = new VigenciasSueldos();
        editarVA = new VigenciasAfiliaciones();
        cualCelda = -1;
        tipoLista = 0;
        tipoListaVA = 0;
        //guardar
        guardado = true;
        //Crear VC
        nuevaVigenciaS = new VigenciasSueldos();
        nuevaVigenciaS.setMotivocambiosueldo(new MotivosCambiosSueldos());
        nuevaVigenciaS.setTiposueldo(new TiposSueldos());
        vigenciaSueldoSeleccionada = null;
        bandera = 0;
        banderaVA = 0;
        permitirIndex = true;
        permitirIndexVA = true;
        vigenciaAfiliacioneSeleccionada = null;
        cualCeldaVA = -1;

        nuevaVigenciaA = new VigenciasAfiliaciones();
        nuevaVigenciaA.setTipoentidad(new TiposEntidades());
        nuevaVigenciaA.setTercerosucursal(new TercerosSucursales());
        listVACrear = new ArrayList<VigenciasAfiliaciones>();

        nombreTabla = ":formExportarVS:datosVSEmpleadoExportar";
        nombreXML = "VigenciasSueldosXML";

        cambioVigenciaA = false;

        retroactivo = true;
        duplicarVS = new VigenciasSueldos();
        mostrarActual = false;
        cambiosVS = false;
        altoTabla1 = "105";
        altoTabla2 = "105";

        uniPago = new Unidades();
        fechaSis = new Date();
        mensajeValidacion = "";

        activarLOV = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarVigenciasSueldos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    //EMPLEADO DE LA VIGENCIA
    /**
     * Metodo que recibe la secuencia empleado desde la pagina anterior y
     * obtiene el empleado referenciado
     *
     * @param sec Secuencia del Empleado
     */
    public void recibirEmpleado(Empleados empl) {
        empleado = empl;

        getListVigenciasSueldos();
        //INICIALIZAR BOTONES NAVEGACION
        if (listVigenciasSueldos != null) {
            modificarInfoRegistroS(listVigenciasSueldos.size());
            if (!listVigenciasSueldos.isEmpty()) {
                vigenciaSueldoSeleccionada = listVigenciasSueldos.get(0);
            }
        } else {
            modificarInfoRegistroS(0);
        }

        getListVigenciasAfiliaciones();
        //INICIALIZAR BOTONES NAVEGACION
        if (listVigenciasAfiliaciones != null) {
            modificarInfoRegistroD(listVigenciasAfiliaciones.size());
        } else {
            modificarInfoRegistroD(0);
        }
    }

    /**
     * Modifica los elementos de la tabla VigenciaLocalizacion que no usan
     * autocomplete
     *
     * @param indice Fila donde se efectu el cambio
     */
    public void modificarVS(VigenciasSueldos vSueldo) {
        vigenciaSueldoSeleccionada = vSueldo;
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listVSCrear.contains(vigenciaSueldoSeleccionada)) {
            if (listVSModificar.isEmpty()) {
                listVSModificar.add(vigenciaSueldoSeleccionada);
            } else if (!listVSModificar.contains(vigenciaSueldoSeleccionada)) {
                listVSModificar.add(vigenciaSueldoSeleccionada);
            }
            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
        }

        cambiosVS = true;
        getValorTotal();
        context.update("form:totalConsultarActual");
        context.update("form:valorConsultarActual");

    }

    public boolean validarFechasRegistroVigenciaSueldo(int i) {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (i == 0) {
            VigenciasSueldos auxiliar = null;
            auxiliar = vigenciaSueldoSeleccionada;

            if (auxiliar.getFechavigencia().after(fechaParametro) && auxiliar.getFechavigenciaretroactivo().after(fechaParametro)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        if (i == 1) {
            if (nuevaVigenciaS.getFechavigencia().after(fechaParametro) && nuevaVigenciaS.getFechavigenciaretroactivo().after(fechaParametro)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarVS.getFechavigencia().after(fechaParametro) && duplicarVS.getFechavigenciaretroactivo().after(fechaParametro)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public void modificarFechasVigenciaSueldo(VigenciasSueldos vSueldo, int c) {
        vigenciaSueldoSeleccionada = vSueldo;

        if (vigenciaSueldoSeleccionada.getFechavigencia() != null && vigenciaSueldoSeleccionada.getFechavigenciaretroactivo() != null) {
            boolean retorno = false;
            retorno = validarFechasRegistroVigenciaSueldo(0);
            if (retorno) {
                cambiarIndice(vigenciaSueldoSeleccionada, c);
                modificarVS(vigenciaSueldoSeleccionada);
            } else {
                vigenciaSueldoSeleccionada.setFechavigencia(fechaVig);
                vigenciaSueldoSeleccionada.setFechavigenciaretroactivo(fechaRetro);

                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("errorFechasVS.show()");
            }
        } else {
            vigenciaSueldoSeleccionada.setFechavigencia(fechaVig);
            vigenciaSueldoSeleccionada.setFechavigenciaretroactivo(fechaRetro);

            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("negacionNuevaS.show()");
        }
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    /**
     * Metodo que modifica los cambios efectuados en la tabla
     * VigenciasLocalizaciones de la pagina
     *
     * @param indice Fila en la cual se realizo el cambio
     * @param confirmarCambio
     * @param valor
     */
    public void modificarVS(VigenciasSueldos vSueldo, String confirmarCambio, String valor) {
        vigenciaSueldoSeleccionada = vSueldo;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("MOTIVOCAMBIOSUELDO")) {
            vigenciaSueldoSeleccionada.getMotivocambiosueldo().setNombre(motivoCambioSueldo);

            for (int i = 0; i < listMotivosCambiosSueldos.size(); i++) {
                if (listMotivosCambiosSueldos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                vigenciaSueldoSeleccionada.setMotivocambiosueldo(listMotivosCambiosSueldos.get(indiceUnicoElemento));

                listMotivosCambiosSueldos.clear();
                getListMotivosCambiosSueldos();
            } else {
                permitirIndex = false;
                getInfoRegistroMotivoCambioSueldo();
                context.update("form:MotivoCambioSueldoDialogo");
                context.execute("MotivoCambioSueldoDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (confirmarCambio.equalsIgnoreCase("TIPOSUELDO")) {
            vigenciaSueldoSeleccionada.getTiposueldo().setDescripcion(tiposSueldos);

            for (int i = 0; i < listTiposSueldos.size(); i++) {
                if (listTiposSueldos.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                vigenciaSueldoSeleccionada.setTiposueldo(listTiposSueldos.get(indiceUnicoElemento));

                listTiposSueldos.clear();
                getListTiposSueldos();
            } else {
                permitirIndex = false;
                getInfoRegistroTipoSueldo();
                context.update("form:TipoSueldoDialogo");
                context.execute("TipoSueldoDialogo.show()");
                tipoActualizacion = 0;
            }

        }
        if (coincidencias == 1) {
            if (!listVSCrear.contains(vigenciaSueldoSeleccionada)) {

                if (listVSModificar.isEmpty()) {
                    listVSModificar.add(vigenciaSueldoSeleccionada);
                } else if (!listVSModificar.contains(vigenciaSueldoSeleccionada)) {
                    listVSModificar.add(vigenciaSueldoSeleccionada);
                }
                if (guardado) {
                    guardado = false;
                    context.update("form:ACEPTAR");
                }
            }
            cambiosVS = true;
        }
        activarLOV = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
        context.update("form:datosVSEmpleado");
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * Modifica los elementos de la tabla VigenciaProrrateo que no usan
     * autocomplete
     *
     * @param indice Fila donde se efectu el cambio
     */
    public void modificarVA(VigenciasAfiliaciones vAfiliacion) {
        vigenciaAfiliacioneSeleccionada = vAfiliacion;
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listVACrear.contains(vigenciaAfiliacioneSeleccionada)) {
            if (listVAModificar.isEmpty()) {
                listVAModificar.add(vigenciaAfiliacioneSeleccionada);
            } else if (!listVAModificar.contains(vigenciaAfiliacioneSeleccionada)) {
                listVAModificar.add(vigenciaAfiliacioneSeleccionada);
            }
            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
        }
        cambioVigenciaA = true;
    }

    public boolean validarFechasRegistroVigenciaAfiliaciones(int cualAct) {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);

        boolean retorno = true;
        if (cualAct == 0) {
            VigenciasAfiliaciones auxiliar = null;
            auxiliar = vigenciaAfiliacioneSeleccionada;

            if (auxiliar.getFechafinal() != null) {
                if (auxiliar.getFechainicial().after(fechaParametro) && auxiliar.getFechainicial().before(auxiliar.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
            if (auxiliar.getFechafinal() == null) {
                if (auxiliar.getFechainicial().after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        if (cualAct == 1) {
            if (nuevaVigenciaA.getFechafinal() != null) {
                if (nuevaVigenciaA.getFechainicial().after(fechaParametro) && nuevaVigenciaA.getFechainicial().before(nuevaVigenciaA.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
            if (nuevaVigenciaA.getFechafinal() == null) {
                if (nuevaVigenciaA.getFechainicial().after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        if (cualAct == 2) {
            if (duplicarVA.getFechafinal() != null) {
                if (duplicarVA.getFechainicial().after(fechaParametro) && duplicarVA.getFechainicial().before(duplicarVA.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
            if (duplicarVA.getFechafinal() == null) {
                if (duplicarVA.getFechainicial().after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        return retorno;
    }

    public void modificarFechasVigenciaAfiliaciones(VigenciasAfiliaciones vAfiliacion, int c) {
        vigenciaAfiliacioneSeleccionada = vAfiliacion;
        if (vigenciaAfiliacioneSeleccionada.getFechainicial() != null) {
            boolean retorno = false;
            retorno = validarFechasRegistroVigenciaAfiliaciones(c);
            if (retorno) {
                cambiarIndiceVA(vigenciaAfiliacioneSeleccionada, c);
                modificarVA(vigenciaAfiliacioneSeleccionada);
            } else {
                vigenciaAfiliacioneSeleccionada.setFechafinal(fechaFin);
                vigenciaAfiliacioneSeleccionada.setFechainicial(fechaIni);

                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:datosVAVigencia");
                context.execute("errorFechasVA.show()");
            }
        } else {
            vigenciaAfiliacioneSeleccionada.setFechainicial(fechaIni);

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosVAVigencia");
            context.execute("negacionNuevaVA.show()");
        }
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    /**
     * Metodo que modifica los cambios efectuados en la tabla VigenciaProrrateo
     * de la pagina
     *
     * @param indice Fila en la cual se realizo el cambio
     */
    public void modificarVA(VigenciasAfiliaciones vAfiliacion, String confirmarCambio, String valorConfirmar) {
        vigenciaAfiliacioneSeleccionada = vAfiliacion;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOENTIDAD")) {
            vigenciaAfiliacioneSeleccionada.getTipoentidad().setNombre(tiposEntidades);

            for (int i = 0; i < listTiposEntidades.size(); i++) {
                if (listTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                cambioVigenciaA = true;
                vigenciaAfiliacioneSeleccionada.setTipoentidad(listTiposEntidades.get(indiceUnicoElemento));

                listTiposEntidades.clear();
                getListTiposEntidades();
            } else {
                permitirIndexVA = false;
                getInfoRegistroTipoEntidad();
                context.update("form:TipoEntidadDialogo");
                context.execute("TipoEntidadDialogo.show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("TERCEROS")) {
            vigenciaAfiliacioneSeleccionada.getTercerosucursal().getTercero().setNombre(terceros);

            for (int i = 0; i < listTerceros.size(); i++) {
                if (listTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                vigenciaAfiliacioneSeleccionada.setTercerosucursal(new TercerosSucursales());
                vigenciaAfiliacioneSeleccionada.getTercerosucursal().setTercero(new Terceros());
                vigenciaAfiliacioneSeleccionada.getTercerosucursal().setTercero(listTerceros.get(indiceUnicoElemento));
                cambioVigenciaA = true;

                listTerceros.clear();
                getListTerceros();
            } else {
                permitirIndexVA = false;
                getInfoRegistroTercero();
                context.update("form:TerceroDialogo");
                context.execute("TerceroDialogo.show()");
                tipoActualizacion = 0;
            }
        }

        if (coincidencias == 1) {
            if (!listVACrear.contains(vigenciaAfiliacioneSeleccionada)) {

                if (listVAModificar.isEmpty()) {
                    listVAModificar.add(vigenciaAfiliacioneSeleccionada);
                } else if (!listVAModificar.contains(vigenciaAfiliacioneSeleccionada)) {
                    listVAModificar.add(vigenciaAfiliacioneSeleccionada);
                }
                if (guardado) {
                    guardado = false;
                    context.update("form:ACEPTAR");
                }
            }
            cambioVigenciaA = true;
        }
        activarLOV = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
        context.update("form:datosVAVigencia");
    }

    /**
     * Metodo que obtiene los valores de los dialogos para realizar los
     * autocomplete de los campos (VigenciaLocalizacion)
     *
     * @param tipoNuevo Tipo de operacion: Nuevo Registro - Duplicar Registro
     * @param Campo Campo que toma el cambio de autocomplete
     */
    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("MOTIVOCAMBIOSUELDO")) {
            if (tipoNuevo == 1) {
                motivoCambioSueldo = nuevaVigenciaS.getMotivocambiosueldo().getNombre();
            } else if (tipoNuevo == 2) {
                motivoCambioSueldo = duplicarVS.getMotivocambiosueldo().getNombre();
            }
        } else if (Campo.equals("TIPOSUELDO")) {
            if (tipoNuevo == 1) {
                tiposSueldos = nuevaVigenciaS.getTiposueldo().getDescripcion();
            } else if (tipoNuevo == 2) {
                tiposSueldos = duplicarVS.getTiposueldo().getDescripcion();
            }
        }
    }

    /**
     * Metodo que genera el auto completar de un proceso nuevoRegistro o
     * duplicarRegistro de VigenciasLocalizaciones
     *
     * @param confirmarCambio Tipo de elemento a modificar: CENTROCOSTO -
     * MOTIVOLOCALIZACION - PROYECTO
     * @param valorConfirmar Valor ingresado para confirmar
     * @param tipoNuevo Tipo de proceso: Nuevo - Duplicar
     */
    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("MOTIVOCAMBIOSUELDO")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaS.getMotivocambiosueldo().setNombre(motivoCambioSueldo);
            } else if (tipoNuevo == 2) {
                duplicarVS.getMotivocambiosueldo().setNombre(motivoCambioSueldo);
            }
            for (int i = 0; i < listMotivosCambiosSueldos.size(); i++) {
                if (listMotivosCambiosSueldos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaS.setMotivocambiosueldo(listMotivosCambiosSueldos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaMotivoCambioSueldo");
                } else if (tipoNuevo == 2) {
                    duplicarVS.setMotivocambiosueldo(listMotivosCambiosSueldos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarMotivoCambioSueldo");
                }
            } else {
                context.update("form:MotivoCambioSueldoDialogo");
                context.execute("MotivoCambioSueldoDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaMotivoCambioSueldo");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarMotivoCambioSueldo");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("TIPOSUELDO")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaS.getTiposueldo().setDescripcion(tiposSueldos);
            } else if (tipoNuevo == 2) {
                duplicarVS.getTiposueldo().setDescripcion(tiposSueldos);
            }
            for (int i = 0; i < listTiposSueldos.size(); i++) {
                if (listTiposSueldos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaS.setTiposueldo(listTiposSueldos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaTipoSueldo");
                } else if (tipoNuevo == 2) {
                    duplicarVS.setTiposueldo(listTiposSueldos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarTipoSueldo");
                }
            } else {
                context.update("form:TipoSueldoDialogo");
                context.execute("TipoSueldoDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaTipoSueldo");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarTipoSueldo");
                }
            }
        }
    }

    /**
     * Metodo que obtiene los valores de los dialogos para realizar los
     * autocomplete de los campos (VigenciaProrrateo)
     *
     * @param tipoNuevo Tipo de operacion: Nuevo Registro - Duplicar Registro
     * @param Campo Campo que toma el cambio de autocomplete
     */
    public void valoresBackupAutocompletarVA(int tipoNuevo, String Campo) {

        if (Campo.equals("TIPOENTIDAD")) {
            if (tipoNuevo == 1) {
                tiposEntidades = nuevaVigenciaA.getTipoentidad().getNombre();
            } else if (tipoNuevo == 2) {
                tiposEntidades = duplicarVA.getTipoentidad().getNombre();
            }
        } else if (Campo.equals("TERCERO")) {
            if (tipoNuevo == 1) {
                terceros = nuevaVigenciaA.getTercerosucursal().getTercero().getNombre();
            } else if (tipoNuevo == 2) {
                terceros = duplicarVA.getTercerosucursal().getTercero().getNombre();
            }
        }
    }

    /**
     *
     * Metodo que genera el auto completar de un proceso nuevoRegistro o
     * duplicarRegistro de VigenciasProrrateos
     *
     * @param confirmarCambio Tipo de elemento a modificar: CENTROCOSTO -
     * PROYECTO
     * @param valorConfirmar Valor ingresado para confirmar
     * @param tipoNuevo Tipo de proceso: Nuevo - Duplicar
     */
    public void autocompletarNuevoyDuplicadoVA(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOENTIDAD")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaA.getTipoentidad().setNombre(tiposEntidades);
            } else if (tipoNuevo == 2) {
                duplicarVA.getTipoentidad().setNombre(tiposEntidades);
            }
            for (int i = 0; i < listTiposEntidades.size(); i++) {
                if (listTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaA.setTipoentidad(listTiposEntidades.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaTipoEntidadVA");
                } else if (tipoNuevo == 2) {
                    duplicarVA.setTipoentidad(listTiposEntidades.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarTipoEntidadVA");
                }
                listTiposEntidades.clear();
                getListTiposEntidades();
            } else {
                context.update("form:TipoEntidadDialogo");
                context.execute("TipoEntidadDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaTipoEntidadVA");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarTipoEntidadVA");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaA.getTercerosucursal().getTercero().setNombre(terceros);
            } else if (tipoNuevo == 2) {
                duplicarVA.getTercerosucursal().getTercero().setNombre(terceros);
            }
            for (int i = 0; i < listTerceros.size(); i++) {
                if (listTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaA.getTercerosucursal().setTercero(listTerceros.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaTerceroVA");
                } else if (tipoNuevo == 2) {
                    duplicarVA.getTercerosucursal().setTercero(listTerceros.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarTerceroVA");
                }
                listTerceros.clear();
                getListTerceros();
            } else {
                context.update("form:TerceroDialogoVA");
                context.execute("TerceroDialogoVA.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaTerceroVA");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicadoTerceroVA");
                }
            }
        }
    }

    //Ubicacion Celda.
    /**
     * Metodo que obtiene la posicion dentro de la tabla VigenciasLocalizaciones
     *
     * @param indice Fila de la tabla
     * @param celda Columna de la tabla
     */
    public void cambiarIndice(VigenciasSueldos vSueldo, int celda) {
        vigenciaSueldoSeleccionada = vSueldo;
        //if (listVABorrar.isEmpty() && listVACrear.isEmpty() && listVAModificar.isEmpty()) {

        if (permitirIndex) {
            if (cambioVigenciaA == false) {
                cualCelda = celda;
                fechaRetro = vigenciaSueldoSeleccionada.getFechavigenciaretroactivo();
                fechaVig = vigenciaSueldoSeleccionada.getFechavigencia();

                if (cualCelda == 1) {
                    activarLOV = false;
                    RequestContext.getCurrentInstance().update("form:listaValores");
                    motivoCambioSueldo = vigenciaSueldoSeleccionada.getMotivocambiosueldo().getNombre();
                } else if (cualCelda == 2) {
                    activarLOV = false;
                    RequestContext.getCurrentInstance().update("form:listaValores");
                    tiposSueldos = vigenciaSueldoSeleccionada.getTiposueldo().getDescripcion();
                } else {
                    activarLOV = true;
                    RequestContext.getCurrentInstance().update("form:listaValores");
                }
                listVigenciasAfiliaciones = null;
                getListVigenciasAfiliaciones();
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:guardarCambiosVigenciasAfiliaciones");
                RequestContext.getCurrentInstance().execute("guardarCambiosVigenciasAfiliaciones.show()");
            }

            if (banderaVA == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                vAFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaInicial");
                vAFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                vAFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaFinal");
                vAFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                vATercero = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATercero");
                vATercero.setFilterStyle("display: none; visibility: hidden;");
                vATipoEntidad = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATipoEntidad");
                vATipoEntidad.setFilterStyle("display: none; visibility: hidden;");
                vAValor = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAValor");
                vAValor.setFilterStyle("display: none; visibility: hidden;");
                altoTabla2 = "105";
                banderaVA = 0;
                filtrarVigenciasAfiliaciones = null;
                tipoListaVA = 0;
            }
            RequestContext.getCurrentInstance().update("form:datosVAVigencia");
        }
    }

    /**
     * Metodo que obtiene la posicion dentro de la tabla VigenciasProrrateos
     *
     * @param indice Fila de la tabla
     * @param celda Columna de la tabla
     */
    public void cambiarIndiceVA(VigenciasAfiliaciones vAfiliacion, int celda) {
        vigenciaAfiliacioneSeleccionada = vAfiliacion;
        if (permitirIndexVA) {
            cualCeldaVA = celda;
            //fechaFin = vigenciaAfiliacioneSeleccionada.getFechafinal();
            fechaIni = vigenciaAfiliacioneSeleccionada.getFechainicial();
            if (cualCeldaVA == 2) {
                activarLOV = false;
                RequestContext.getCurrentInstance().update("form:listaValores");
                terceros = vigenciaAfiliacioneSeleccionada.getTercerosucursal().getTercero().getNombre();
            } else if (cualCeldaVA == 3) {
                activarLOV = false;
                RequestContext.getCurrentInstance().update("form:listaValores");
                tiposEntidades = vigenciaAfiliacioneSeleccionada.getTipoentidad().getNombre();
            } else {
                activarLOV = true;
                RequestContext.getCurrentInstance().update("form:listaValores");
            }

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                vSFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSFechaVigencia");
                vSFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
                vSMotivoCambioSueldo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSMotivoCambioSueldo");
                vSMotivoCambioSueldo.setFilterStyle("display: none; visibility: hidden;");
                vSTipoSueldo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSTipoSueldo");
                vSTipoSueldo.setFilterStyle("display: none; visibility: hidden;");
                vSVigenciaRetroactivo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSVigenciaRetroactivo");
                vSVigenciaRetroactivo.setFilterStyle("display: none; visibility: hidden;");
                vSValor = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSValor");
                vSValor.setFilterStyle("display: none; visibility: hidden;");
                vSObservaciones = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSObservaciones");
                vSObservaciones.setFilterStyle("display: none; visibility: hidden;");
                vSRetroactivo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSRetroactivo");
                vSRetroactivo.setFilterStyle("display: none; visibility: hidden;");
                altoTabla1 = "105";
                RequestContext.getCurrentInstance().update("form:datosVSEmpleado");
                bandera = 0;
                filtrarVigenciasSueldos = null;
                tipoLista = 0;
            }
        }
    }

    public void guardadoGeneral() {
        guardarCambiosVS();
        //if (listVigenciasAfiliaciones != null) {
        guardarCambiosVA();
        //}
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:ACEPTAR");
        FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().update("form:growl");
    }

    /**
     * Metodo que guarda los cambios efectuados en la pagina
     * VigenciasLocalizaciones
     */
    public void guardarCambiosVS() {
        if (guardado == false) {
            if (!listVSBorrar.isEmpty()) {
                for (int i = 0; i < listVSBorrar.size(); i++) {
                    if (listVSBorrar.get(i).getMotivocambiosueldo().getSecuencia() == null) {
                        listVSBorrar.get(i).setMotivocambiosueldo(null);
                    }
                    if (listVSBorrar.get(i).getTiposueldo().getSecuencia() == null) {
                        listVSBorrar.get(i).setTiposueldo(null);
                    }
                    administrarVigenciasSueldos.borrarVS(listVSBorrar.get(i));
                }
                listVSBorrar.clear();
            }
            if (!listVSCrear.isEmpty()) {
                for (int i = 0; i < listVSCrear.size(); i++) {
                    if (listVSCrear.get(i).getMotivocambiosueldo().getSecuencia() == null) {
                        listVSCrear.get(i).setMotivocambiosueldo(null);
                    }
                    if (listVSCrear.get(i).getTiposueldo().getSecuencia() == null) {
                        listVSCrear.get(i).setTiposueldo(null);
                    }
                    administrarVigenciasSueldos.crearVS(listVSCrear.get(i));
                }
                listVSCrear.clear();
            }
            if (!listVSModificar.isEmpty()) {
                administrarVigenciasSueldos.modificarVS(listVSModificar);
                listVSModificar.clear();
            }

            if (listVigenciasSueldos != null) {
                modificarInfoRegistroS(listVigenciasSueldos.size());
            } else {
                modificarInfoRegistroS(0);
            }


            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosVSEmpleado");
            context.update("form:ACEPTAR");
            paraNuevaVSueldo = 0;

        }
        vigenciaSueldoSeleccionada = null;
    }

    /**
     * Metodo que guarda los cambios efectuados en la pagina VigenciasProrrateos
     */
    public void guardarCambiosVA() {
        if (guardado == false) {
            if (!listVABorrar.isEmpty()) {
                for (int i = 0; i < listVABorrar.size(); i++) {
                    if (listVABorrar.get(i).getTipoentidad().getSecuencia() == null) {
                        listVABorrar.get(i).setTipoentidad(null);
                    }
                    if (listVABorrar.get(i).getTercerosucursal().getTercero().getSecuencia() == null) {
                        listVABorrar.get(i).getTercerosucursal().setTercero(null);
                    }
                    administrarVigenciasSueldos.borrarVA(listVABorrar.get(i));
                }
                listVABorrar.clear();
            }
            if (!listVACrear.isEmpty()) {
                for (int i = 0; i < listVACrear.size(); i++) {
                    if (listVACrear.get(i).getTipoentidad().getSecuencia() == null) {
                        listVACrear.get(i).setTipoentidad(null);
                    }
                    if (listVACrear.get(i).getTercerosucursal().getTercero().getSecuencia() == null) {
                        listVACrear.get(i).getTercerosucursal().setTercero(null);
                    }
                    administrarVigenciasSueldos.crearVA(listVACrear.get(i));
                }
                listVACrear.clear();
            }
            if (!listVAModificar.isEmpty()) {
                administrarVigenciasSueldos.modificarVA(listVAModificar);
                listVAModificar.clear();
            }
            listVigenciasAfiliaciones = null;
            RequestContext context = RequestContext.getCurrentInstance();
            paraNuevaVSueldo = 0;

            if (listVigenciasAfiliaciones != null) {
                modificarInfoRegistroS(listVigenciasAfiliaciones.size());
            } else {
                modificarInfoRegistroS(0);
            }
        }
        cambioVigenciaA = false;
        vigenciaAfiliacioneSeleccionada = null;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosVAVigencia");
    }

    //CANCELAR MODIFICACIONES
    /**
     * Cancela las modificaciones realizas en la pagina
     */
    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            vSFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSFechaVigencia");
            vSFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
            vSMotivoCambioSueldo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSMotivoCambioSueldo");
            vSMotivoCambioSueldo.setFilterStyle("display: none; visibility: hidden;");
            vSTipoSueldo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSTipoSueldo");
            vSTipoSueldo.setFilterStyle("display: none; visibility: hidden;");
            vSVigenciaRetroactivo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSVigenciaRetroactivo");
            vSVigenciaRetroactivo.setFilterStyle("display: none; visibility: hidden;");
            vSValor = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSValor");
            vSValor.setFilterStyle("display: none; visibility: hidden;");
            vSObservaciones = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSObservaciones");
            vSObservaciones.setFilterStyle("display: none; visibility: hidden;");
            vSRetroactivo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSRetroactivo");
            vSRetroactivo.setFilterStyle("display: none; visibility: hidden;");
            altoTabla1 = "105";
            RequestContext.getCurrentInstance().update("form:datosVSEmpleado");
            bandera = 0;
            filtrarVigenciasSueldos = null;
            tipoLista = 0;
        }
        if (banderaVA == 1) {
            vAFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaInicial");
            vAFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            vAFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaFinal");
            vAFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            vATercero = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATercero");
            vATercero.setFilterStyle("display: none; visibility: hidden;");
            vATipoEntidad = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATipoEntidad");
            vATipoEntidad.setFilterStyle("display: none; visibility: hidden;");
            vAValor = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAValor");
            vAValor.setFilterStyle("display: none; visibility: hidden;");
            altoTabla2 = "105";
            RequestContext.getCurrentInstance().update("form:datosVAVigencia");
            banderaVA = 0;
            filtrarVigenciasAfiliaciones = null;
            tipoListaVA = 0;
        }
        listVSBorrar.clear();
        listVABorrar.clear();
        listVSCrear.clear();
        listVACrear.clear();
        listVSModificar.clear();
        listVAModificar.clear();
        paraNuevaVSueldo = 0;
        listVigenciasSueldos = null;
        listVigenciasAfiliaciones = null;
        cambioVigenciaA = false;
        mostrarActual = false;
        guardado = true;
        cambiosVS = false;
        vigenciaAfiliacioneSeleccionada = null;
        vigenciaSueldoSeleccionada = null;
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");

        getListVigenciasSueldos();
        //INICIALIZAR BOTONES NAVEGACION
        if (listVigenciasSueldos != null) {
            modificarInfoRegistroS(listVigenciasSueldos.size());
        } else {
            modificarInfoRegistroS(0);
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosVSEmpleado");
        context.update("form:totalConsultarActual");
        context.update("form:valorConsultarActual");
        context.update("form:datosVAVigencia");
        context.update("form:ACEPTAR");
    }

    /**
     * Cancela las modifaciones de la tabla vigencias prorrateos
     */
    public void cancelarModificacionVA() {
        if (banderaVA == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            vAFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaInicial");
            vAFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            vAFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaFinal");
            vAFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            vATercero = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATercero");
            vATercero.setFilterStyle("display: none; visibility: hidden;");
            vATipoEntidad = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATipoEntidad");
            vATipoEntidad.setFilterStyle("display: none; visibility: hidden;");
            vAValor = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAValor");
            vAValor.setFilterStyle("display: none; visibility: hidden;");
            altoTabla2 = "105";
            RequestContext.getCurrentInstance().update("form:datosVAVigencia");
            banderaVA = 0;
            filtrarVigenciasAfiliaciones = null;
            tipoListaVA = 0;
        }

        listVigenciasAfiliaciones = null;
        getListVigenciasSueldos();
        //INICIALIZAR BOTONES NAVEGACION
        if (listVigenciasAfiliaciones != null) {
            modificarInfoRegistroD(listVigenciasAfiliaciones.size());
        } else {
            modificarInfoRegistroD(0);
        }

        listVABorrar.clear();
        listVACrear.clear();
        listVAModificar.clear();
        vigenciaAfiliacioneSeleccionada = null;
        paraNuevaVSueldo = 0;
        listVigenciasAfiliaciones = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosVAVigencia");
        context.update("form:ACEPTAR");
        cambioVigenciaA = false;
    }

    //CREAR VL
    /**
     * Metodo que se encarga de agregar un nueva VigenciasLocalizaciones
     */
    public void agregarNuevaVS() {

        RequestContext context = RequestContext.getCurrentInstance();
        if ((nuevaVigenciaS.getFechavigencia() != null) && (nuevaVigenciaS.getMotivocambiosueldo().getSecuencia() != null) && (nuevaVigenciaS.getValor() != null)
                && (nuevaVigenciaS.getTiposueldo().getSecuencia() != null)) {

            if (nuevaVigenciaS.getFechavigenciaretroactivo() == null) {
                nuevaVigenciaS.setFechavigenciaretroactivo(nuevaVigenciaS.getFechavigencia());
            }

            int cont = 0;
            mensajeValidacion = "";
            if (listVigenciasSueldos != null) {
                for (int j = 0; j < listVigenciasSueldos.size(); j++) {
                    if (nuevaVigenciaS.getFechavigencia().equals(listVigenciasSueldos.get(j).getFechavigencia())) {
                        cont++;
                    }
                }
            }
            if (cont > 0) {
                mensajeValidacion = "FECHAS NO REPETIDAS";
                context.update("form:nuevaSFechasDuplicadas");
                context.execute("nuevaSFechasDuplicadas.show()");
            } else {
                if (validarFechasRegistroVigenciaSueldo(1)) {
                    if (bandera == 1) {
                        //CERRAR FILTRADO
                        FacesContext c = FacesContext.getCurrentInstance();
                        vSFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSFechaVigencia");
                        vSFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
                        vSMotivoCambioSueldo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSMotivoCambioSueldo");
                        vSMotivoCambioSueldo.setFilterStyle("display: none; visibility: hidden;");
                        vSTipoSueldo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSTipoSueldo");
                        vSTipoSueldo.setFilterStyle("display: none; visibility: hidden;");
                        vSVigenciaRetroactivo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSVigenciaRetroactivo");
                        vSVigenciaRetroactivo.setFilterStyle("display: none; visibility: hidden;");
                        vSValor = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSValor");
                        vSValor.setFilterStyle("display: none; visibility: hidden;");
                        vSObservaciones = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSObservaciones");
                        vSObservaciones.setFilterStyle("display: none; visibility: hidden;");
                        vSRetroactivo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSRetroactivo");
                        vSRetroactivo.setFilterStyle("display: none; visibility: hidden;");
                        altoTabla1 = "105";
                        RequestContext.getCurrentInstance().update("form:datosVSEmpleado");
                        bandera = 0;
                        filtrarVigenciasSueldos = null;
                        tipoLista = 0;
                    }
                    //AGREGAR REGISTRO A LA LISTA VIGENCIAS 
                    paraNuevaVSueldo++;
                    BigInteger var = BigInteger.valueOf(paraNuevaVSueldo);
                    nuevaVigenciaS.setSecuencia(var);
                    nuevaVigenciaS.setEmpleado(empleado);
                    nuevaVigenciaS.setFechasistema(fechaSis);
                    nuevaVigenciaS.setUnidadpago(new Unidades());
                    BigInteger secuen = BigInteger.valueOf(19669);
                    nuevaVigenciaS.getUnidadpago().setSecuencia(secuen);

                    listVSCrear.add(nuevaVigenciaS);
                    listVigenciasSueldos.add(nuevaVigenciaS);
                    vigenciaSueldoSeleccionada = listVigenciasSueldos.get(listVigenciasSueldos.size()-1);
                    activarLOV = true;
                    RequestContext.getCurrentInstance().update("form:listaValores");
                    nuevaVigenciaS = new VigenciasSueldos();
                    nuevaVigenciaS.setMotivocambiosueldo(new MotivosCambiosSueldos());
                    nuevaVigenciaS.setTiposueldo(new TiposSueldos());

                    modificarInfoRegistroS(listVigenciasSueldos.size());
                    context.update("form:datosVSEmpleado");
                    if (guardado) {
                        guardado = false;
                        context.update("form:ACEPTAR");
                    }
                    context.execute("NuevoRegistroVS.hide()");
                    if (mostrarActual) {
                        getValorTotal();
                        context.update("form:totalConsultarActual");
                        context.update("form:valorConsultarActual");
                    }
                    cambiosVS = true;
                } else {
                    context.execute("errorFechasVS.show()");
                }
            }
        } else {
            context.execute("negacionNuevaS.show()");
        }
    }
    //LIMPIAR NUEVO REGISTRO

    /**
     * Metodo que limpia las casillas de la nueva vigencia
     */
    public void limpiarNuevaVS() {
        nuevaVigenciaS = new VigenciasSueldos();
        nuevaVigenciaS.setMotivocambiosueldo(new MotivosCambiosSueldos());
        nuevaVigenciaS.setTiposueldo(new TiposSueldos());
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * Agrega una nueva Vigencia Prorrateo
     */
    public void agregarNuevaVA() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevaVigenciaA.getFechainicial() != null && nuevaVigenciaA.getTercerosucursal().getTercero().getSecuencia() != null && nuevaVigenciaA.getTipoentidad().getSecuencia() != null) {
            int cont = 0;
            mensajeValidacion = "";

            int tam = 0;
            if (listVigenciasAfiliaciones != null) {
                tam = listVigenciasAfiliaciones.size();
            }
            for (int j = 0; j < tam; j++) {
                if (nuevaVigenciaA.getFechainicial().equals(listVigenciasAfiliaciones.get(j).getFechainicial())) {
                    cont++;
                }
            }
            if (cont > 0) {
                mensajeValidacion = "FECHAS NO REPETIDAS";
                context.update("form:nuevaAFechasDuplicadas");
                context.execute("nuevaAFechasDuplicadas.show()");
            } else {

                if (validarFechasRegistroVigenciaAfiliaciones(1)) {
                    cambioVigenciaA = true;
                    //CERRAR FILTRADO
                    if (banderaVA == 1) {
                        FacesContext c = FacesContext.getCurrentInstance();
                        vAFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaInicial");
                        vAFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                        vAFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaFinal");
                        vAFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                        vATercero = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATercero");
                        vATercero.setFilterStyle("display: none; visibility: hidden;");
                        vATipoEntidad = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATipoEntidad");
                        vATipoEntidad.setFilterStyle("display: none; visibility: hidden;");
                        vAValor = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAValor");
                        vAValor.setFilterStyle("display: none; visibility: hidden;");
                        altoTabla2 = "105";
                        RequestContext.getCurrentInstance().update("form:datosVAVigencia");
                        banderaVA = 0;
                        filtrarVigenciasAfiliaciones = null;
                        tipoListaVA = 0;
                    }
                    //AGREGAR REGISTRO A LA LISTA VIGENCIAS
                    paraNuevaVSueldo++;
                    nuevaVSueldoSecuencia = BigInteger.valueOf(paraNuevaVSueldo);
                    BigDecimal var = BigDecimal.valueOf(paraNuevaVSueldo);
                    nuevaVigenciaA.setSecuencia(nuevaVSueldoSecuencia);
                    nuevaVigenciaA.setEmpleado(empleado);
                    nuevaVigenciaA.setVigenciasueldo(vigenciaSueldoSeleccionada);
                    listVACrear.add(nuevaVigenciaA);
                    listVigenciasAfiliaciones.add(nuevaVigenciaA);
                    vigenciaAfiliacioneSeleccionada = listVigenciasAfiliaciones.get(listVigenciasAfiliaciones.indexOf(nuevaVigenciaA));
                    modificarInfoRegistroD(listVigenciasAfiliaciones.size());
                    activarLOV = true;
                    RequestContext.getCurrentInstance().update("form:listaValores");
                    //
                    nuevaVigenciaA = new VigenciasAfiliaciones();
                    nuevaVigenciaA.setTipoentidad(new TiposEntidades());
                    nuevaVigenciaA.setTercerosucursal(new TercerosSucursales());
                    context.update("form:datosVAVigencia");
                    context.execute("NuevoRegistroVA.hide()");
                    if (guardado) {
                        guardado = false;
                        context.update("form:ACEPTAR");
                    }
                    vigenciaAfiliacioneSeleccionada = null;
                } else {
                    context.execute("errorFechasVA.show()");
                }
            }
        } else {
            context.execute("negacionNuevaVA.show()");
        }
    }

    /**
     * Limpia los elementos de una nueva vigencia prorrateo
     */
    public void limpiarNuevaVA() {
        nuevaVigenciaA = new VigenciasAfiliaciones();
        nuevaVigenciaA.setTipoentidad(new TiposEntidades());
        nuevaVigenciaA.setTercerosucursal(new TercerosSucursales());
    }

    //DUPLICAR VL
    /**
     * Metodo que verifica que proceso de duplicar se genera con respecto a la
     * posicion en la pagina que se tiene
     */
    public void verificarDuplicarVigencia() {
        RequestContext context = RequestContext.getCurrentInstance();
        //Si no hay registro selecciionado
        if (vigenciaSueldoSeleccionada == null && vigenciaAfiliacioneSeleccionada == null) {
            context.execute("seleccionarRegistro.show()");
        } else {
            if (vigenciaSueldoSeleccionada != null && vigenciaAfiliacioneSeleccionada != null) {
                duplicarVigenciaA();
            } else if (vigenciaSueldoSeleccionada != null) {
                duplicarVigenciaS();
            }
        }
    }

    /**
     * Duplica una nueva vigencia localizacion
     */
    public void duplicarVigenciaS() {
        if (vigenciaSueldoSeleccionada != null) {
            duplicarVS = new VigenciasSueldos();
            paraNuevaVSueldo++;
            BigInteger var = BigInteger.valueOf(paraNuevaVSueldo);
            duplicarVS.setEmpleado(vigenciaSueldoSeleccionada.getEmpleado());
            duplicarVS.setFechavigencia(vigenciaSueldoSeleccionada.getFechavigencia());
            duplicarVS.setMotivocambiosueldo(vigenciaSueldoSeleccionada.getMotivocambiosueldo());
            duplicarVS.setTiposueldo(vigenciaSueldoSeleccionada.getTiposueldo());
            duplicarVS.setRetroactivo(vigenciaSueldoSeleccionada.getRetroactivo());
            duplicarVS.setUnidadpago(vigenciaSueldoSeleccionada.getUnidadpago());
            duplicarVS.setFechasistema(vigenciaSueldoSeleccionada.getFechasistema());
            if (vigenciaSueldoSeleccionada.getRetroactivo() == null || vigenciaSueldoSeleccionada.getRetroactivo().equals("N")) {
                duplicarVS.setCheckRetroactivo(false);
            } else {
                duplicarVS.setCheckRetroactivo(true);
            }
            duplicarVS.setFechavigenciaretroactivo(vigenciaSueldoSeleccionada.getFechavigenciaretroactivo());
            duplicarVS.setValor(vigenciaSueldoSeleccionada.getValor());
            duplicarVS.setObservaciones(vigenciaSueldoSeleccionada.getObservaciones());

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:duplicarVS");
            context.execute("DuplicarRegistroVS.show()");
        }
    }

    /**
     * Metodo que confirma el duplicado y actualiza los datos de la tabla
     * VigenciasLocalizaciones
     */
    public void confirmarDuplicarS() {
        RequestContext context = RequestContext.getCurrentInstance();
        if ((duplicarVS.getFechavigencia() != null) && (duplicarVS.getValor() != null)
                && (duplicarVS.getTiposueldo().getSecuencia() != null) && (duplicarVS.getMotivocambiosueldo().getSecuencia() != null)) {

            if (duplicarVS.getFechavigenciaretroactivo() == null) {
                duplicarVS.setFechavigenciaretroactivo(duplicarVS.getFechavigencia());
            }

            int cont = 0;
            mensajeValidacion = "";
            for (int j = 0; j < listVigenciasSueldos.size(); j++) {
                if (duplicarVS.getFechavigencia().equals(listVigenciasSueldos.get(j).getFechavigencia())) {
                    cont++;
                }
            }
            if (cont > 0) {
                mensajeValidacion = "FECHAS NO REPETIDAS";
                context.update("form:duplicarSFechasDuplicadas");
                context.execute("duplicarSFechasDuplicadas.show()");
            } else {
                if (validarFechasRegistroVigenciaSueldo(2)) {
                    paraNuevaVSueldo++;
                    BigInteger var = BigInteger.valueOf(paraNuevaVSueldo);
                    duplicarVS.setSecuencia(var);
                    listVigenciasSueldos.add(duplicarVS);
                    listVSCrear.add(duplicarVS);
                    vigenciaSueldoSeleccionada = listVigenciasSueldos.get(listVigenciasSueldos.indexOf(duplicarVS));
                    modificarInfoRegistroS(listVigenciasSueldos.size());
                    context.update("form:datosVSEmpleado");
                    if (guardado) {
                        guardado = false;
                        context.update("form:ACEPTAR");
                    }
                    if (bandera == 1) {
                        //CERRAR FILTRADO
                        FacesContext c = FacesContext.getCurrentInstance();
                        vSFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSFechaVigencia");
                        vSFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
                        vSMotivoCambioSueldo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSMotivoCambioSueldo");
                        vSMotivoCambioSueldo.setFilterStyle("display: none; visibility: hidden;");
                        vSTipoSueldo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSTipoSueldo");
                        vSTipoSueldo.setFilterStyle("display: none; visibility: hidden;");
                        vSVigenciaRetroactivo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSVigenciaRetroactivo");
                        vSVigenciaRetroactivo.setFilterStyle("display: none; visibility: hidden;");
                        vSValor = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSValor");
                        vSValor.setFilterStyle("display: none; visibility: hidden;");
                        vSObservaciones = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSObservaciones");
                        vSObservaciones.setFilterStyle("display: none; visibility: hidden;");
                        vSRetroactivo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSRetroactivo");
                        vSRetroactivo.setFilterStyle("display: none; visibility: hidden;");
                        altoTabla1 = "105";
                        RequestContext.getCurrentInstance().update("form:datosVSEmpleado");
                        bandera = 0;
                        filtrarVigenciasSueldos = null;
                        tipoLista = 0;
                    }
                    cambiosVS = true;
                    context.execute("DuplicarRegistroVS.hide()");
                    duplicarVS = new VigenciasSueldos();
                } else {
                    context.execute("errorFechasVS.show()");
                }
            }
        } else {
            context.execute("negacionNuevaS.show()");
        }
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }
    //LIMPIAR DUPLICAR

    /**
     * Metodo que limpia los datos de un duplicar Vigencia Localizacion
     */
    public void limpiarduplicarVS() {
        duplicarVS = new VigenciasSueldos();
        duplicarVS.setMotivocambiosueldo(new MotivosCambiosSueldos());
        duplicarVS.setTiposueldo(new TiposSueldos());
    }

    ///////////////////////////////////////////////////////////////
    /**
     * Duplica una registro de VigenciaProrrateos
     */
    public void duplicarVigenciaA() {

        if (vigenciaAfiliacioneSeleccionada != null) {
            duplicarVA = new VigenciasAfiliaciones();

            duplicarVA.setVigenciasueldo(vigenciaSueldoSeleccionada);
            duplicarVA.setEmpleado(vigenciaSueldoSeleccionada.getEmpleado());
            duplicarVA.setFechainicial(vigenciaAfiliacioneSeleccionada.getFechainicial());
            duplicarVA.setFechafinal(vigenciaAfiliacioneSeleccionada.getFechafinal());
            duplicarVA.setTercerosucursal(vigenciaAfiliacioneSeleccionada.getTercerosucursal());
            //duplicarVA.getTercerosucursal().setCodigosucursal(vigenciaAfiliacioneSeleccionada.getTercerosucursal().getCodigosucursal());
            duplicarVA.setTipoentidad(vigenciaAfiliacioneSeleccionada.getTipoentidad());
            duplicarVA.setValor(vigenciaAfiliacioneSeleccionada.getValor());

            cambioVigenciaA = true;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:duplicadoVA");
            context.execute("DuplicadoRegistroVA.show()");
        }
    }

    /**
     * Metodo que confirma el duplicado y actualiza los datos de la tabla
     * VigenciaProrrateo
     */
    public void confirmarDuplicarVA() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (duplicarVA.getFechainicial() != null && duplicarVA.getTercerosucursal().getTercero().getSecuencia() != null && duplicarVA.getTipoentidad().getSecuencia() != null) {
            int cont = 0;
            mensajeValidacion = "";
            for (int j = 0; j < listVigenciasAfiliaciones.size(); j++) {
                if (duplicarVA.getFechainicial().equals(listVigenciasAfiliaciones.get(j).getFechainicial())) {
                    cont++;
                }
            }
            if (cont > 0) {
                mensajeValidacion = "FECHAS NO REPETIDAS";
                context.update("form:duplicarAFechasDuplicadas");
                context.execute("duplicarAFechasDuplicadas.show()");
            } else {
                if (validarFechasRegistroVigenciaAfiliaciones(2)) {
                    paraNuevaVSueldo++;
                    BigInteger var = BigInteger.valueOf(paraNuevaVSueldo);
                    duplicarVA.setSecuencia(var);
                    cambioVigenciaA = true;
                    listVigenciasAfiliaciones.add(duplicarVA);
                    listVACrear.add(duplicarVA);
                    vigenciaAfiliacioneSeleccionada = listVigenciasAfiliaciones.get(listVigenciasAfiliaciones.indexOf(duplicarVA));
                    modificarInfoRegistroD(listVigenciasAfiliaciones.size());
                    context.update("form:datosVAVigencia");
                    context.execute("DuplicadoRegistroVA.hide()");
                    if (guardado) {
                        guardado = false;
                        context.update("form:ACEPTAR");
                    }
                    if (banderaVA == 1) {
                        //CERRAR FILTRADO
                        FacesContext c = FacesContext.getCurrentInstance();
                        vAFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaInicial");
                        vAFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                        vAFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaFinal");
                        vAFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                        vATercero = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATercero");
                        vATercero.setFilterStyle("display: none; visibility: hidden;");
                        vATipoEntidad = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATipoEntidad");
                        vATipoEntidad.setFilterStyle("display: none; visibility: hidden;");
                        vAValor = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAValor");
                        vAValor.setFilterStyle("display: none; visibility: hidden;");
                        altoTabla2 = "105";
                        RequestContext.getCurrentInstance().update("form:datosVAVigencia");
                        banderaVA = 0;
                        filtrarVigenciasAfiliaciones = null;
                        tipoListaVA = 0;
                    }
                    duplicarVA = new VigenciasAfiliaciones();
                } else {
                    context.execute("errorFechasVA.show()");
                }
            }
        } else {
            context.execute("negacionNuevaVA.show()");
        }
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    /**
     * Limpia los elementos del duplicar registro Vigencia Prorrateo
     */
    public void limpiarduplicarVA() {
        duplicarVA = new VigenciasAfiliaciones();
        duplicarVA.setTipoentidad(new TiposEntidades());
        duplicarVA.setTercerosucursal(new TercerosSucursales());

    }

    /**
     * Valida que registro se elimina de que tabla con respecto a la posicion en
     * la pagina
     */
    public void validarBorradoVigencia() {
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        RequestContext context = RequestContext.getCurrentInstance();
        //Si no hay registro selecciionado
        if (vigenciaSueldoSeleccionada == null && vigenciaAfiliacioneSeleccionada == null) {
            context.execute("seleccionarRegistro.show()");
        } else {
            if (vigenciaAfiliacioneSeleccionada != null) {
                borrarVA();
            } else if (vigenciaSueldoSeleccionada != null) {
                if (listVigenciasAfiliaciones == null) {
                    borrarVS();
                } else if (listVigenciasAfiliaciones.isEmpty()) {
                    borrarVS();
                } else {
                    context.update("form:negacionBorradoVS");
                    context.execute("negacionBorradoVS.show()");
                }
            }
        }
    }

    //BORRAR VL
    /**
     * Metodo que borra una vigencia localizacion
     */
    public void borrarVS() {
        if (!listVSModificar.isEmpty() && listVSModificar.contains(vigenciaSueldoSeleccionada)) {
            int modIndex = listVSModificar.indexOf(vigenciaSueldoSeleccionada);
            listVSModificar.remove(modIndex);
            listVSBorrar.add(vigenciaSueldoSeleccionada);
        } else if (!listVSCrear.isEmpty() && listVSCrear.contains(vigenciaSueldoSeleccionada)) {
            int crearIndex = listVSCrear.indexOf(vigenciaSueldoSeleccionada);
            listVSCrear.remove(crearIndex);
        } else {
            listVSBorrar.add(vigenciaSueldoSeleccionada);
        }
        listVigenciasSueldos.remove(vigenciaSueldoSeleccionada);
        if (tipoLista == 1) {
            System.out.println("Entro a borrar de la lista filtrada");
            filtrarVigenciasSueldos.remove(vigenciaSueldoSeleccionada);
        }
        modificarInfoRegistroS(listVigenciasSueldos.size());
        RequestContext context = RequestContext.getCurrentInstance();
        if (mostrarActual) {
            getValorTotal();
            context.update("form:totalConsultarActual");
            context.update("form:valorConsultarActual");
        }
        vigenciaSueldoSeleccionada = null;
        cambiosVS = true;
        if (guardado) {
            guardado = false;
            context.update("form:ACEPTAR");
        }
        context.update("form:datosVSEmpleado");
    }

    /**
     * Metodo que borra una vigencia prorrateo
     */
    public void borrarVA() {
        System.out.println("entro en borrarVA()");
        cambioVigenciaA = true;
        if (!listVAModificar.isEmpty() && listVAModificar.contains(vigenciaAfiliacioneSeleccionada)) {
            int modIndex = listVAModificar.indexOf(vigenciaAfiliacioneSeleccionada);
            listVAModificar.remove(modIndex);
            listVABorrar.add(vigenciaAfiliacioneSeleccionada);
        } else if (!listVACrear.isEmpty() && listVACrear.contains(vigenciaAfiliacioneSeleccionada)) {
            int crearIndex = listVACrear.indexOf(vigenciaAfiliacioneSeleccionada);
            listVACrear.remove(crearIndex);
        } else {
            listVABorrar.add(vigenciaAfiliacioneSeleccionada);
        }
        listVigenciasAfiliaciones.remove(vigenciaAfiliacioneSeleccionada);
        if (tipoLista == 1) {
            filtrarVigenciasAfiliaciones.remove(vigenciaAfiliacioneSeleccionada);
        }
        modificarInfoRegistroD(listVigenciasAfiliaciones.size());

        vigenciaAfiliacioneSeleccionada = null;
        if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosVAVigencia");
    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    /**
     * Metodo que activa el filtrado por medio de la opcion en el toolbar o por
     * medio de la tecla Crtl+F11
     */
    public void activarCtrlF11() {
        if (vigenciaSueldoSeleccionada != null) {
            filtradoVigenciaSueldo();
            vigenciaSueldoSeleccionada = null;
        }
        if (vigenciaAfiliacioneSeleccionada != null) {
            filtradoVigenciaAfiliacion();
            vigenciaAfiliacioneSeleccionada = null;
        }
    }

    /**
     * Metodo que acciona el filtrado de la tabla vigencia localizacion
     */
    public void filtradoVigenciaSueldo() {
        if (vigenciaSueldoSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 0) {
                vSFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSFechaVigencia");
                vSFechaVigencia.setFilterStyle("width: 85%");
                vSMotivoCambioSueldo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSMotivoCambioSueldo");
                vSMotivoCambioSueldo.setFilterStyle("width: 85%");
                vSTipoSueldo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSTipoSueldo");
                vSTipoSueldo.setFilterStyle("width: 85%");
                vSVigenciaRetroactivo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSVigenciaRetroactivo");
                vSVigenciaRetroactivo.setFilterStyle("width: 85%");
                vSValor = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSValor");
                vSValor.setFilterStyle("width: 85%");
                vSObservaciones = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSObservaciones");
                vSObservaciones.setFilterStyle("width: 85%");
                vSRetroactivo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSRetroactivo");
                vSRetroactivo.setFilterStyle("width: 85%");
                altoTabla1 = "85";
                RequestContext.getCurrentInstance().update("form:datosVSEmpleado");
                bandera = 1;
            } else if (bandera == 1) {
                vSFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSFechaVigencia");
                vSFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
                vSMotivoCambioSueldo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSMotivoCambioSueldo");
                vSMotivoCambioSueldo.setFilterStyle("display: none; visibility: hidden;");
                vSTipoSueldo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSTipoSueldo");
                vSTipoSueldo.setFilterStyle("display: none; visibility: hidden;");
                vSVigenciaRetroactivo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSVigenciaRetroactivo");
                vSVigenciaRetroactivo.setFilterStyle("display: none; visibility: hidden;");
                vSValor = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSValor");
                vSValor.setFilterStyle("display: none; visibility: hidden;");
                vSObservaciones = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSObservaciones");
                vSObservaciones.setFilterStyle("display: none; visibility: hidden;");
                vSRetroactivo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSRetroactivo");
                vSRetroactivo.setFilterStyle("display: none; visibility: hidden;");
                altoTabla1 = "105";
                RequestContext.getCurrentInstance().update("form:datosVSEmpleado");
                bandera = 0;
                filtrarVigenciasSueldos = null;
                tipoLista = 0;
            }
        }
    }

    /**
     * Metodo que acciona el filtrado de la tabla vigencia prorrateo
     */
    public void filtradoVigenciaAfiliacion() {
        if (vigenciaAfiliacioneSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (banderaVA == 0) {
                //Columnas Tabla VPP
                vAFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaInicial");
                vAFechaInicial.setFilterStyle("width: 85%");
                vAFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaFinal");
                vAFechaFinal.setFilterStyle("width: 85%");
                vATercero = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATercero");
                vATercero.setFilterStyle("width: 85%");
                vATipoEntidad = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATipoEntidad");
                vATipoEntidad.setFilterStyle("width: 85%");
                vAValor = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAValor");
                vAValor.setFilterStyle("width: 85%");
                altoTabla2 = "85";
                RequestContext.getCurrentInstance().update("form:datosVAVigencia");
                banderaVA = 1;
            } else if (banderaVA == 1) {
                vAFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaInicial");
                vAFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                vAFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaFinal");
                vAFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                vATercero = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATercero");
                vATercero.setFilterStyle("display: none; visibility: hidden;");
                vATipoEntidad = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATipoEntidad");
                vATipoEntidad.setFilterStyle("display: none; visibility: hidden;");
                vAValor = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAValor");
                vAValor.setFilterStyle("display: none; visibility: hidden;");
                altoTabla2 = "105";
                RequestContext.getCurrentInstance().update("form:datosVAVigencia");
                banderaVA = 0;
                filtrarVigenciasAfiliaciones = null;
                tipoListaVA = 0;
            }
        }
    }

    //SALIR
    /**
     * Metodo que cierra la sesion y limpia los datos en la pagina
     */
    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        RequestContext context = RequestContext.getCurrentInstance();
        if (bandera == 1) {
            vSFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSFechaVigencia");
            vSFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
            vSMotivoCambioSueldo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSMotivoCambioSueldo");
            vSMotivoCambioSueldo.setFilterStyle("display: none; visibility: hidden;");
            vSTipoSueldo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSTipoSueldo");
            vSTipoSueldo.setFilterStyle("display: none; visibility: hidden;");
            vSVigenciaRetroactivo = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSVigenciaRetroactivo");
            vSVigenciaRetroactivo.setFilterStyle("display: none; visibility: hidden;");
            vSValor = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSValor");
            vSValor.setFilterStyle("display: none; visibility: hidden;");
            vSObservaciones = (Column) c.getViewRoot().findComponent("form:datosVSEmpleado:vSObservaciones");
            vSObservaciones.setFilterStyle("display: none; visibility: hidden;");
            altoTabla1 = "105";
            context.update("form:datosVSEmpleado");
            bandera = 0;
            filtrarVigenciasSueldos = null;
            tipoLista = 0;
        }
        if (banderaVA == 1) {
            vAFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaInicial");
            vAFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            vAFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaFinal");
            vAFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            vATercero = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATercero");
            vATercero.setFilterStyle("display: none; visibility: hidden;");
            vATipoEntidad = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATipoEntidad");
            vATipoEntidad.setFilterStyle("display: none; visibility: hidden;");
            vAValor = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAValor");
            vAValor.setFilterStyle("display: none; visibility: hidden;");
            altoTabla2 = "105";
            context.update("form:datosVAVigencia");
            banderaVA = 0;
            filtrarVigenciasAfiliaciones = null;
            tipoListaVA = 0;
        }

        listVSBorrar.clear();
        listVSCrear.clear();
        listVSModificar.clear();
        vigenciaSueldoSeleccionada = null;
        vigenciaAfiliacioneSeleccionada = null;
        paraNuevaVSueldo = 0;
        listVigenciasSueldos = null;
        listVigenciasAfiliaciones = null;
        guardado = true;
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        context.update("form:ACEPTAR");
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
        if (tt == 0) {
            if (LND == 0) {
                vigenciaSueldoSeleccionada = (VigenciasSueldos) elemento;
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dlg == 0) {
                if (listMotivosCambiosSueldos != null) {
                    motivoCambioSueldoSeleccionado = listMotivosCambiosSueldos.get(0);
                    modificarInfoRegistroMCS(listMotivosCambiosSueldos.size());
                } else {
                    modificarInfoRegistroMCS(0);
                }
                context.update("form:MotivoCambioSueldoDialogo");
                context.execute("MotivoCambioSueldoDialogo.show()");
            } else if (dlg == 1) {
                if (listTiposSueldos != null) {
                    tipoSueldoSeleccionado = listTiposSueldos.get(0);
                    modificarInfoRegistroTS(listTiposSueldos.size());
                } else {
                    modificarInfoRegistroTS(0);
                }
                context.update("form:TipoSueldoDialogo");
                context.execute("TipoSueldoDialogo.show()");
            }
            listVigenciasAfiliaciones = null;
            getListVigenciasAfiliaciones();
        }
        if (tt == 1) {
            if (LND == 0) {
                vigenciaAfiliacioneSeleccionada = (VigenciasAfiliaciones) elemento;
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dlg == 0) {
                if (listTerceros != null) {
                    terceroSeleccionado = listTerceros.get(0);
                    modificarInfoRegistroT(listTerceros.size());
                } else {
                    modificarInfoRegistroT(0);
                }
                context.update("form:TerceroDialogo");
                context.execute("TerceroDialogo.show()");
            } else if (dlg == 1) {
                if (listTiposEntidades != null) {
                    tipoEntidadSeleccionado = listTiposEntidades.get(0);
                    modificarInfoRegistroTE(listTiposEntidades.size());
                } else {
                    modificarInfoRegistroTE(0);
                }
                context.update("form:TipoEntidadDialogo");
                context.execute("TipoEntidadDialogo.show()");
            }
        }
        activarLOV = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    //LISTA DE VALORES DINAMICA
    /**
     * Metodo que activa la lista de valores de todas las tablas con respecto al
     * index activo y la columna activa
     */
    public void listaValoresBoton() {

        RequestContext context = RequestContext.getCurrentInstance(); //Si no hay registro selecciionado 
        if (vigenciaSueldoSeleccionada == null && vigenciaAfiliacioneSeleccionada == null) {
            context.execute("seleccionarRegistro.show()");
        } else if (vigenciaAfiliacioneSeleccionada != null) {
            if (cualCeldaVA == 2) {
                if (listTerceros != null) {
                    terceroSeleccionado = listTerceros.get(0);
                    modificarInfoRegistroT(listTerceros.size());
                } else {
                    modificarInfoRegistroT(0);
                }
                context.update("form:TerceroDialogo");
                context.execute("TerceroDialogo.show()");
                tipoActualizacion = 0;
            }
            if (cualCeldaVA == 3) {
                if (listTiposEntidades != null) {
                    tipoEntidadSeleccionado = listTiposEntidades.get(0);
                    modificarInfoRegistroTE(listTiposEntidades.size());
                } else {
                    modificarInfoRegistroTE(0);
                }
                context.update("form:TipoEntidadDialogo");
                context.execute("TipoEntidadDialogo.show()");
                tipoActualizacion = 0;
            }
        } else if (vigenciaSueldoSeleccionada != null) {
            if (cualCelda == 1) {
                if (listMotivosCambiosSueldos != null) {
                    motivoCambioSueldoSeleccionado = listMotivosCambiosSueldos.get(0);
                    modificarInfoRegistroMCS(listMotivosCambiosSueldos.size());
                } else {
                    modificarInfoRegistroMCS(0);
                }
                context.update("form:MotivoCambioSueldoDialogo");
                context.execute("MotivoCambioSueldoDialogo.show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 2) {
                if (listTiposSueldos != null) {
                    tipoSueldoSeleccionado = listTiposSueldos.get(0);
                    modificarInfoRegistroTS(listTiposSueldos.size());
                } else {
                    modificarInfoRegistroTS(0);
                }
                context.update("form:TipoSueldoDialogo");
                context.execute("TipoSueldoDialogo.show()");
                tipoActualizacion = 0;
            }
        }
    }
    //LOVS
    //Estructuras

    /**
     * Metodo que actualiza la estructura seleccionada (vigencia localizacion)
     */
    public void actualizarMotivoCambioSueldo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {// Si se trabaja sobre la tabla y no sobre un dialogo
            vigenciaSueldoSeleccionada.setMotivocambiosueldo(motivoCambioSueldoSeleccionado);
            if (!listVSCrear.contains(vigenciaSueldoSeleccionada)) {
                if (listVSModificar.isEmpty()) {
                    listVSModificar.add(vigenciaSueldoSeleccionada);
                } else if (!listVSModificar.contains(vigenciaSueldoSeleccionada)) {
                    listVSModificar.add(vigenciaSueldoSeleccionada);
                }
            }

            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
            permitirIndex = true;
            cambiosVS = true;
        } else if (tipoActualizacion == 1) {//Para crear un registro
            nuevaVigenciaS.setMotivocambiosueldo(motivoCambioSueldoSeleccionado);
            context.update("formularioDialogos:nuevaVS");
        } else if (tipoActualizacion == 2) {//Para duplicar un registro
            duplicarVS.setMotivocambiosueldo(motivoCambioSueldoSeleccionado);
            context.update("formularioDialogos:duplicarVS");
        }
        filtrarMotivosCambiosSueldos = null;
        motivoCambioSueldoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        context.update("form:datosVSEmpleado");
        context.reset("form:lovMotivoCambioSueldo:globalFilter");
        context.execute("lovMotivoCambioSueldo.clearFilters()");
        context.execute("MotivoCambioSueldoDialogo.hide()");
    }

    /**
     * Metodo que cancela los cambios sobre estructura (vigencia localizacion)
     */
    public void cancelarCambioMotivoCambioSueldo() {
        filtrarMotivosCambiosSueldos = null;
        motivoCambioSueldoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovMotivoCambioSueldo:globalFilter");
        context.execute("lovMotivoCambioSueldo.clearFilters()");
        context.execute("MotivoCambioSueldoDialogo.hide()");
    }

    //Motivo Localizacion
    /**
     * Metodo que actualiza el motivo localizacion seleccionado (vigencia
     * localizacion)
     */
    public void actualizarTipoEntidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {// Si se trabaja sobre la tabla y no sobre un dialogo
            vigenciaAfiliacioneSeleccionada.setTipoentidad(tipoEntidadSeleccionado);
            if (!listVACrear.contains(vigenciaAfiliacioneSeleccionada)) {
                if (listVAModificar.isEmpty()) {
                    listVAModificar.add(vigenciaAfiliacioneSeleccionada);
                } else if (!listVAModificar.contains(vigenciaAfiliacioneSeleccionada)) {
                    listVAModificar.add(vigenciaAfiliacioneSeleccionada);
                }
            }

            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
            cambioVigenciaA = true;
            permitirIndex = true;
            context.update("form:datosVAVigencia");
        } else if (tipoActualizacion == 1) {//Para crear un registro
            nuevaVigenciaA.setTipoentidad(tipoEntidadSeleccionado);
            context.update("formularioDialogos:nuevaVA");
        } else if (tipoActualizacion == 2) {//Para duplicar un registro
            duplicarVA.setTipoentidad(tipoEntidadSeleccionado);
            context.update("formularioDialogos:duplicadoVA");
        }
        filtrarTiposEntidades = null;
        tipoEntidadSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        context.update("form:datosVAVigencia");
        context.reset("form:lovTipoEntidad:globalFilter");
        context.execute("lovTipoEntidad.clearFilters()");
        context.execute("TipoEntidadDialogo.hide()");
    }

    /**
     * Metodo que cancela la seleccion del motivo localizacion (vigencia
     * localizacion)
     */
    public void cancelarCambioTipoEntidad() {
        filtrarTiposEntidades = null;
        tipoEntidadSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndexVA = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoEntidad:globalFilter");
        context.execute("lovTipoEntidad.clearFilters()");
        context.execute("TipoEntidadDialogo.hide()");
    }

    //Motivo Localizacion
    /**
     * Metodo que actualiza el proyecto seleccionado (vigencia localizacion)
     */
    public void actualizarTerceros() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {//No se crea ni se duplica ningun registro
            vigenciaAfiliacioneSeleccionada.setTercerosucursal(new TercerosSucursales());
            vigenciaAfiliacioneSeleccionada.getTercerosucursal().setTercero(new Terceros());
            vigenciaAfiliacioneSeleccionada.getTercerosucursal().setTercero(terceroSeleccionado);
            int posicion = -1;
            List<TercerosSucursales> listTercerosSucursales = administrarVigenciasSueldos.listTercerosSucursales();
            //Se recorre la lista de tercerosSucursales para buscar los datos del tercero seleccionado
            for (int i = 0; i < listTercerosSucursales.size(); i++) {
                if (listTercerosSucursales.get(i).getTercero().getNombre().equalsIgnoreCase(terceroSeleccionado.getNombre())) {
                    posicion = i;
                }
            }
            if (posicion != -1) {
                // Se Asignan los datos de tercerosSucursales a la vigencia Afiliacion
                vigenciaAfiliacioneSeleccionada.setTercerosucursal(listTercerosSucursales.get(posicion));
            }

            if (!listVACrear.contains(vigenciaAfiliacioneSeleccionada)) {
                if (listVAModificar.isEmpty()) {
                    listVAModificar.add(vigenciaAfiliacioneSeleccionada);
                } else if (!listVAModificar.contains(vigenciaAfiliacioneSeleccionada)) {
                    listVAModificar.add(vigenciaAfiliacioneSeleccionada);
                }
            }

            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
            cambioVigenciaA = true;
            permitirIndexVA = true;
            context.update("form:datosVAVigencia");
        } else if (tipoActualizacion == 1) {//Para crear un registro
            boolean banderaEncuentra = false;
            int posicion = -1;
            List<TercerosSucursales> listTercerosSucursales = administrarVigenciasSueldos.tercerosSucursales(terceroSeleccionado.getSecuencia());
            for (int i = 0; i < listTercerosSucursales.size(); i++) {
                if (listTercerosSucursales.get(i).getTercero().getSecuencia().compareTo(terceroSeleccionado.getSecuencia()) == 0) {
                    banderaEncuentra = true;
                    posicion = i;
                }
            }
            if ((banderaEncuentra) && (posicion != -1)) {
                nuevaVigenciaA.setTercerosucursal(listTercerosSucursales.get(posicion));
            }
            context.update("formularioDialogos:nuevaVA");
        } else if (tipoActualizacion == 2) {//Para duplicar un registro
            boolean banderaEncuentra = false;
            int posicion = -1;
            List<TercerosSucursales> listTercerosSucursales = administrarVigenciasSueldos.tercerosSucursales(terceroSeleccionado.getSecuencia());
            for (int i = 0; i < listTercerosSucursales.size(); i++) {
                if (listTercerosSucursales.get(i).getTercero().getSecuencia() == terceroSeleccionado.getSecuencia()) {
                    banderaEncuentra = true;
                    posicion = i;
                }
            }
            if ((banderaEncuentra) && (posicion != -1)) {
                duplicarVA.setTercerosucursal(listTercerosSucursales.get(posicion));
            }
            context.update("formularioDialogos:duplicadoVA");
        }
        filtrarTerceros = null;
        terceroSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        context.update("form:datosVAVigencia");
        context.reset("form:lovTercero:globalFilter");
        context.execute("lovTercero.clearFilters()");
        context.execute("TerceroDialogo.hide()");
    }

    /**
     * Metodo que cancela la seleccion del proyecto (vigencia localizacion)
     */
    public void cancelarCambioTerceros() {
        filtrarTerceros = null;
        terceroSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndexVA = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTercero:globalFilter");
        context.execute("lovTercero.clearFilters()");
        context.execute("TerceroDialogo.hide()");
    }

    ///////////////////////////////////////////////////////////////////////
    /**
     * Metodo que actualiza el centro costo seleccionado (vigencia prorrateo)
     */
    public void actualizarTipoSueldo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {// Si se trabaja sobre la tabla y no sobre un dialogo
            vigenciaSueldoSeleccionada.setTiposueldo(tipoSueldoSeleccionado);
            if (!listVSCrear.contains(vigenciaSueldoSeleccionada)) {
                if (listVSModificar.isEmpty()) {
                    listVSModificar.add(vigenciaSueldoSeleccionada);
                } else if (!listVSModificar.contains(vigenciaSueldoSeleccionada)) {
                    listVSModificar.add(vigenciaSueldoSeleccionada);
                }
            }

            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
            permitirIndexVA = true;
            cambiosVS = true;
        } else if (tipoActualizacion == 1) {//Para crear un registro
            nuevaVigenciaS.setTiposueldo(tipoSueldoSeleccionado);
            context.update("formularioDialogos:nuevaVS");
        } else if (tipoActualizacion == 2) {//Para duplicar un registro
            duplicarVS.setTiposueldo(tipoSueldoSeleccionado);
            context.update("formularioDialogos:duplicarVS");
        }
        filtrarTiposSueldos = null;
        tipoSueldoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;

        context.update("form:datosVSEmpleado");
        context.reset("form:lovTipoSueldo:globalFilter");
        context.execute("lovTipoSueldo.clearFilters()");
        context.execute("TipoSueldoDialogo.hide()");
    }

    /**
     * Cancela la seleccion del centro costo (vigencia prorrateo)
     */
    public void cancelarCambioTipoSueldo() {
        filtrarTiposSueldos = null;
        tipoSueldoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;

        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoSueldo:globalFilter");
        context.execute("lovTipoSueldo.clearFilters()");
        context.execute("TipoSueldoDialogo.hide()");
    }

    //MOSTRAR DATOS CELDA
    /**
     * Metodo que muestra los dialogos de editar con respecto a la lista real o
     * la lista filtrada y a la columna
     */
    public void editarCelda() {

        RequestContext context = RequestContext.getCurrentInstance(); //Si no hay registro selecciionado 
        if (vigenciaSueldoSeleccionada == null && vigenciaAfiliacioneSeleccionada == null) {
            context.execute("seleccionarRegistro.show()");
        } else {
            if (vigenciaSueldoSeleccionada != null) {
                editarVS = vigenciaSueldoSeleccionada;

                if (cualCelda
                        == 0) {
                    context.update("formularioDialogos:editarFechaVigenciaD");
                    context.execute("editarFechaVigenciaD.show()");
                    cualCelda = -1;
                } else if (cualCelda == 1) {
                    context.update("formularioDialogos:editarMotivoCambioSueldoD");
                    context.execute("editarMotivoCambioSueldoD.show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    context.update("formularioDialogos:editarTipoSueldoD");
                    context.execute("editarTipoSueldoD.show()");
                    cualCelda = -1;
                } else if (cualCelda == 4) {
                    context.update("formularioDialogos:editarVigenciaRetroactivo");
                    context.execute("editarVigenciaRetroactivo.show()");
                    cualCelda = -1;
                } else if (cualCelda == 5) {
                    context.update("formularioDialogos:editarValorD");
                    context.execute("editarValorD.show()");
                    cualCelda = -1;
                } else if (cualCelda == 6) {
                    context.update("formularioDialogos:editarObservacionesD");
                    context.execute("editarObservacionesD.show()");
                    cualCelda = -1;
                }
            }
            if (vigenciaAfiliacioneSeleccionada != null) {
                editarVA = vigenciaAfiliacioneSeleccionada;

                if (cualCeldaVA == 0) {
                    context.update("formularioDialogos:editarFechaInicialVAD");
                    context.execute("editarFechaInicialVAD.show()");
                    cualCeldaVA = -1;
                } else if (cualCeldaVA == 1) {
                    context.update("formularioDialogos:editarFechaFinalVAD");
                    context.execute("editarFechaFinalVAD.show()");
                    cualCeldaVA = -1;
                } else if (cualCeldaVA == 2) {
                    context.update("formularioDialogos:editarTerceroVAD");
                    context.execute("editarTerceroVAD.show()");
                    cualCeldaVA = -1;
                } else if (cualCeldaVA == 3) {
                    context.update("formularioDialogos:editarTipoEntidadVAD");
                    context.execute("editarTipoEntidadVAD" + ".show()");
                    cualCeldaVA =
                            -1;
                } else if (cualCeldaVA == 4) {
                    context.update("formularioDialogos:editarValorVAD");
                    context.execute("editarValorVAD.show()");
                    cualCeldaVA = -1;
                }
            }
        }
    }

    /**
     * Metodo que activa el boton aceptar de la pagina y los dialogos
     */
    public void activarAceptar() {
        aceptar = false;
    }
    //EXPORTAR

    /**
     * Selecciona la tabla a exportar XML con respecto al index activo
     *
     * @return Nombre del dialogo a exportar en XML
     */
    public String exportXML() {
        if (vigenciaSueldoSeleccionada != null) {
            nombreTabla = ":formExportarVS:datosVSEmpleadoExportar";
            nombreXML = "VigenciasSueldosXML";
        }
        if (vigenciaAfiliacioneSeleccionada != null) {
            nombreTabla = ":formExportarVA:datosVAVigenciaExportar";
            nombreXML = "VigenciasAfiliacionesXML";
        }
        return nombreTabla;
    }

    /**
     * Valida la tabla a exportar en PDF con respecto al index activo
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void validarExportPDF() throws IOException {
        if (vigenciaSueldoSeleccionada != null) {
            exportPDFVS();
        }
        if (vigenciaAfiliacioneSeleccionada != null) {
            exportPDFVA();
        }
    }

    /**
     * Metodo que exporta datos a PDF Vigencia Localizacion
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportPDFVS() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportarVS:datosVSEmpleadoExportar");
        FacesContext context = c;
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "VigenciasSueldosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    /**
     * Metodo que exporta datos a PDF Vigencia Prorrateo
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportPDFVA() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportarVA:datosVAVigenciaExportar");
        FacesContext context = c;
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "VigenciasAfiliacionesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    /**
     * Verifica que tabla exportar XLS con respecto al index activo
     *
     * @throws IOException
     */
    public void verificarExportXLS() throws IOException {
        if (vigenciaSueldoSeleccionada != null) {
            exportXLSVS();
        }
        if (vigenciaAfiliacioneSeleccionada != null) {
            exportXLSVA();
        }
    }

    /**
     * Metodo que exporta datos a XLS Vigencia Sueldos
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportXLSVS() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportarVS:datosVSEmpleadoExportar");
        FacesContext context = c;
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "VigenciasSueldosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    /**
     * Metodo que exporta datos a XLS Vigencia Afiliaciones
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportXLSVA() throws IOException {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportarVA:datosVAVigenciaExportar");
        FacesContext context = c;
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "VigenciasAfiliacionesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void cambioConsultaActual() {
        if (cambiosVS == false) {
            mostrarActual = true;
            listVigenciasSueldos = new ArrayList<VigenciasSueldos>();
            listVigenciasSueldos = (List<VigenciasSueldos>) administrarVigenciasSueldos.VigenciasSueldosActualesEmpleado(empleado.getSecuencia());
            getValorTotal();
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosVSEmpleado");
            context.update("form:datosVAVigencia");
            context.update("form:totalConsultarActual");
            context.update("form:valorConsultarActual");
        }
        if (cambiosVS) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:confirmarGuardar");
            context.execute("confirmarGuardar.show()");
        }
    }

    public void cambioConsultaGlobal() {
        if (cambiosVS == false) {
            mostrarActual = false;
            valorTotal = 0;
            listVigenciasSueldos = null;
            getListVigenciasSueldos();
            getValorTotal();
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosVSEmpleado");
            context.update("form:datosVAVigencia");
            context.update("form:totalConsultarActual");
            context.update("form:valorConsultarActual");
        }
        if (cambiosVS) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:confirmarGuardar");
            context.execute("confirmarGuardar.show()");
        }
    }

    public String visibilidadTotal() {
        String retorno = null;
        if (mostrarActual == false) {
            retorno = "hidden";
        }
        if (mostrarActual) {
            retorno = "visible";
        }
        return retorno;
    }
    //METODO RASTROS PARA LAS TABLAS EN EMPLVIGENCIASUELDOS

    public void verificarRastroTabla() {
        //Cuando no se ha seleccionado ningun registro:
        if (vigenciaSueldoSeleccionada == null && vigenciaAfiliacioneSeleccionada == null) {
            //Dialogo para seleccionar el rastro Historico de la tabla deseada
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("verificarRastrosTablas.show()");
        } else {
            //Cuando se selecciono registro:            
            if (vigenciaSueldoSeleccionada != null && vigenciaAfiliacioneSeleccionada != null) {
                verificarRastroVigenciaAfiliacion();
            } else if (vigenciaSueldoSeleccionada != null) {
                verificarRastroVigenciaSueldo();
            }
        }
    }

    //Verificar Rastro Vigencia Sueldos
    public void verificarRastroVigenciaSueldo() {
        RequestContext context = RequestContext.getCurrentInstance();
        int resultado = administrarRastros.obtenerTabla(vigenciaSueldoSeleccionada.getSecuencia(), "VIGENCIASSUELDOS");
        backUp = vigenciaSueldoSeleccionada.getSecuencia();
        if (resultado == 1) {
            context.execute("errorObjetosDB.show()");
        } else if (resultado == 2) {
            nombreTablaRastro = "VigenciasSueldos";
            msnConfirmarRastro = "La tabla VIGENCIASSUELDOS tiene rastros para el registro seleccionado, ¿desea continuar?";
            context.update("form:msnConfirmarRastro");
            context.execute("confirmarRastro.show()");
        } else if (resultado == 3) {
            context.execute("errorRegistroRastro.show()");
        } else if (resultado == 4) {
            context.execute("errorTablaConRastro.show()");
        } else if (resultado == 5) {
            context.execute("errorTablaSinRastro.show()");
        }
    }
    //Verificar Rastro Vigencia Sueldos Historico

    public void verificarRastroVigenciaSueldoHist() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (administrarRastros.verificarHistoricosTabla("VIGENCIASSUELDOS")) {
            nombreTablaRastro = "VigenciasSueldos";
            msnConfirmarRastroHistorico = "La tabla VIGENCIASSUELDOS tiene rastros historicos, ¿Desea continuar?";
            context.update("form:confirmarRastroHistorico");
            context.execute("confirmarRastroHistorico.show()");
        } else {
            context.execute("errorRastroHistorico.show()");
        }
    }

    //Verificar Rastro Vigencia Sueldos
    public void verificarRastroVigenciaAfiliacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        int resultado = administrarRastros.obtenerTabla(vigenciaAfiliacioneSeleccionada.getSecuencia(), "VIGENCIASAFILIACIONES");
        backUp = vigenciaAfiliacioneSeleccionada.getSecuencia();
        if (resultado == 1) {
            context.execute("errorObjetosDB.show()");
        } else if (resultado == 2) {
            nombreTablaRastro = "VigenciasAfiliaciones";
            msnConfirmarRastro = "La tabla VIGENCIASAFILIACIONES tiene rastros para el registro seleccionado, ¿desea continuar?";
            context.update("form:msnConfirmarRastro");
            context.execute("confirmarRastro.show()");
        } else if (resultado == 3) {
            context.execute("errorRegistroRastro.show()");
        } else if (resultado == 4) {
            context.execute("errorTablaConRastro.show()");
        } else if (resultado == 5) {
            context.execute("errorTablaSinRastro.show()");
        }
    }
    //Verificar Rastro Vigencia Afiliaciones Historico

    public void verificarRastroVigenciaAfiliacionHist() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (administrarRastros.verificarHistoricosTabla("VIGENCIASAFILIACIONES")) {
            nombreTablaRastro = "VigenciasAfiliaciones";
            msnConfirmarRastroHistorico = "La tabla VIGENCIASAFILIACIONES tiene rastros historicos, ¿Desea continuar?";
            context.update("form:confirmarRastroHistorico");
            context.execute("confirmarRastroHistorico.show()");
        } else {
            context.execute("errorRastroHistorico.show()");
        }
    }

    public void limpiarMSNRastros() {
        msnConfirmarRastro = "";
        msnConfirmarRastroHistorico = "";
        nombreTablaRastro = "";
    }

    private void modificarInfoRegistroS(int valor) {
        infoRegistroS = String.valueOf(valor);
    }

    public void anularLOV() {
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }
    //EVENTO FILTRAR

    /**
     * Evento que cambia la lista real a la filtrada
     */
    public void eventoFiltrarS() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        vigenciaSueldoSeleccionada = null;
        modificarInfoRegistroS(filtrarVigenciasSueldos.size());
        RequestContext.getCurrentInstance().update("form:informacionRegistroS");
    }

    public void eventoFiltrarD() {
        if (tipoListaVA == 0) {
            tipoListaVA = 1;
        }
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        vigenciaAfiliacioneSeleccionada = null;
        modificarInfoRegistroD(filtrarVigenciasAfiliaciones.size());
        RequestContext.getCurrentInstance().update("form:informacionRegistroD");
    }

    public void eventoFiltrarMCS() {
        modificarInfoRegistroMCS(filtrarMotivosCambiosSueldos.size());
        RequestContext.getCurrentInstance().update("form:infoRegistroMotivoCambioSueldo");
    }

    public void eventoFiltrarT() {
        modificarInfoRegistroT(filtrarTerceros.size());
        RequestContext.getCurrentInstance().update("form:infoRegistroTercero");
    }

    public void eventoFiltrarTE() {
        modificarInfoRegistroTE(filtrarTiposEntidades.size());
        RequestContext.getCurrentInstance().update("form:infoRegistroTipoEntidad");
    }

    public void eventoFiltrarTS() {
        modificarInfoRegistroTS(filtrarTiposSueldos.size());
        RequestContext.getCurrentInstance().update("form:infoRegistroTipoSueldo");
    }

    private void modificarInfoRegistroD(int valor) {
        infoRegistroD = String.valueOf(valor);
    }

    private void modificarInfoRegistroMCS(int valor) {
        infoRegistroMotivoCambioSueldo = String.valueOf(valor);
    }

    private void modificarInfoRegistroT(int valor) {
        infoRegistroTercero = String.valueOf(valor);
    }

    private void modificarInfoRegistroTE(int valor) {
        infoRegistroTipoEntidad = String.valueOf(valor);
    }

    private void modificarInfoRegistroTS(int valor) {
        infoRegistroTipoSueldo = String.valueOf(valor);
    }

    //GET - SET 
    public List<VigenciasSueldos> getListVigenciasSueldos() {
        try {
            if (listVigenciasSueldos == null) {
                listVigenciasSueldos = new ArrayList<VigenciasSueldos>();
                listVigenciasSueldos = (List<VigenciasSueldos>) administrarVigenciasSueldos.VigenciasSueldosEmpleado(empleado.getSecuencia());
            }
            return listVigenciasSueldos;
        } catch (Exception e) {
            System.out.println("Error getVigenciaLocalizaciones " + e.toString());
            return null;
        }
    }

    public void recordarSeleccionVA() {
        if (vigenciaAfiliacioneSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tabla = (DataTable) c.getViewRoot().findComponent("form:datosVAVigencia");
            tabla.setSelection(vigenciaAfiliacioneSeleccionada);
        }
    }

    public void recordarSeleccionVS() {
        if (vigenciaSueldoSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tabla = (DataTable) c.getViewRoot().findComponent("form:datosVSEmpleado");
            tabla.setSelection(vigenciaSueldoSeleccionada);
        }
    }

    public void validarNuevaVA() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaSueldoSeleccionada != null) {
            context.execute("NuevoRegistroVA.show()");
            context.execute("NuevoRegistroPagina.hide()");
        } else {
            context.execute("seleccionarRegistroSel.show()");
        }
    }

    public void setListVigenciasSueldos(List<VigenciasSueldos> listVigenciasSueldos) {
        this.listVigenciasSueldos = listVigenciasSueldos;
    }

    public List<VigenciasSueldos> getFiltrarVigenciasSueldos() {
        return filtrarVigenciasSueldos;
    }

    public void setFiltrarVigenciasSueldos(List<VigenciasSueldos> filtrarVigenciasSueldos) {
        this.filtrarVigenciasSueldos = filtrarVigenciasSueldos;
    }

    public List<VigenciasAfiliaciones> getListVigenciasAfiliaciones() {
        if (vigenciaSueldoSeleccionada != null) {
            if (listVigenciasAfiliaciones == null || listVigenciasAfiliaciones.isEmpty()) {
                listVigenciasAfiliaciones = administrarVigenciasSueldos.VigenciasAfiliacionesVigencia(vigenciaSueldoSeleccionada.getSecuencia());
            }
            return listVigenciasAfiliaciones;
        } else {
            listVigenciasAfiliaciones = new ArrayList<VigenciasAfiliaciones>();
            return listVigenciasAfiliaciones;
        }
    }

    public void setListVigenciasAfiliaciones(List<VigenciasAfiliaciones> listVigenciasAfiliaciones) {
        this.listVigenciasAfiliaciones = listVigenciasAfiliaciones;
    }

    public List<VigenciasAfiliaciones> getFiltrarVigenciasAfiliaciones() {
        return filtrarVigenciasAfiliaciones;
    }

    public void setFiltrarVigenciasAfiliaciones(List<VigenciasAfiliaciones> filtrarVigenciasAfiliaciones) {
        this.filtrarVigenciasAfiliaciones = filtrarVigenciasAfiliaciones;
    }

    public List<TiposSueldos> getListTiposSueldos() {
        if (listTiposSueldos == null) {
            listTiposSueldos = administrarVigenciasSueldos.tiposSueldos();
        }
        return listTiposSueldos;
    }

    public void setListTiposSueldos(List<TiposSueldos> listTiposSueldos) {
        this.listTiposSueldos = listTiposSueldos;
    }

    public TiposSueldos getTipoSueldoSeleccionado() {
        return tipoSueldoSeleccionado;
    }

    public void setTipoSueldoSeleccionado(TiposSueldos tipoSueldoSeleccionado) {
        this.tipoSueldoSeleccionado = tipoSueldoSeleccionado;
    }

    public List<TiposSueldos> getFiltrarTiposSueldos() {
        return filtrarTiposSueldos;
    }

    public void setFiltrarTiposSueldos(List<TiposSueldos> filtrarTiposSueldos) {
        this.filtrarTiposSueldos = filtrarTiposSueldos;
    }

    public List<MotivosCambiosSueldos> getListMotivosCambiosSueldos() {
        if (listMotivosCambiosSueldos == null) {
            listMotivosCambiosSueldos = administrarVigenciasSueldos.motivosCambiosSueldos();
        }
        return listMotivosCambiosSueldos;
    }

    public void setListMotivosCambiosSueldos(List<MotivosCambiosSueldos> listMotivosCambiosSueldos) {
        this.listMotivosCambiosSueldos = listMotivosCambiosSueldos;
    }

    public MotivosCambiosSueldos getMotivoCambioSueldoSeleccionado() {
        return motivoCambioSueldoSeleccionado;
    }

    public void setMotivoCambioSueldoSeleccionado(MotivosCambiosSueldos motivoCambioSueldoSeleccionado) {
        this.motivoCambioSueldoSeleccionado = motivoCambioSueldoSeleccionado;
    }

    public List<MotivosCambiosSueldos> getFiltrarMotivosCambiosSueldos() {
        return filtrarMotivosCambiosSueldos;
    }

    public void setFiltrarMotivosCambiosSueldos(List<MotivosCambiosSueldos> filtrarMotivosCambiosSueldos) {
        this.filtrarMotivosCambiosSueldos = filtrarMotivosCambiosSueldos;
    }

    public List<TiposEntidades> getListTiposEntidades() {
        if (listTiposEntidades == null) {
            listTiposEntidades = administrarVigenciasSueldos.tiposEntidades();
        }
        return listTiposEntidades;
    }

    public void setListTiposEntidades(List<TiposEntidades> listTiposEntidades) {
        this.listTiposEntidades = listTiposEntidades;
    }

    public TiposEntidades getTipoEntidadSeleccionado() {
        return tipoEntidadSeleccionado;
    }

    public void setTipoEntidadSeleccionado(TiposEntidades tipoEntidadSeleccionado) {
        this.tipoEntidadSeleccionado = tipoEntidadSeleccionado;
    }

    public List<TiposEntidades> getFiltrarTiposEntidades() {
        return filtrarTiposEntidades;
    }

    public void setFiltrarTiposEntidades(List<TiposEntidades> filtrarTiposEntidades) {
        this.filtrarTiposEntidades = filtrarTiposEntidades;
    }

    public List<Terceros> getListTerceros() {
        if (listTerceros == null) {
            listTerceros = administrarVigenciasSueldos.terceros();
        }
        return listTerceros;
    }

    public void setListTerceros(List<Terceros> listTerceros) {
        this.listTerceros = listTerceros;
    }

    public Terceros getTerceroSeleccionado() {
        return terceroSeleccionado;
    }

    public void setTerceroSeleccionado(Terceros terceroSeleccionado) {
        this.terceroSeleccionado = terceroSeleccionado;
    }

    public List<Terceros> getFiltrarTerceros() {
        return filtrarTerceros;
    }

    public void setFiltrarTerceros(List<Terceros> filtrarTerceros) {
        this.filtrarTerceros = filtrarTerceros;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public List<VigenciasSueldos> getListVSModificar() {
        return listVSModificar;
    }

    public void setListVSModificar(List<VigenciasSueldos> listVSModificar) {
        this.listVSModificar = listVSModificar;
    }

    public List<VigenciasAfiliaciones> getListVAModificar() {
        return listVAModificar;
    }

    public void setListVAModificar(List<VigenciasAfiliaciones> listVAModificar) {
        this.listVAModificar = listVAModificar;
    }

    public VigenciasSueldos getNuevaVigencia() {
        return nuevaVigenciaS;
    }

    public void setNuevaVigencia(VigenciasSueldos nuevaVigencia) {
        this.nuevaVigenciaS = nuevaVigencia;
    }

    public VigenciasAfiliaciones getNuevaVigenciaA() {
        return nuevaVigenciaA;
    }

    public void setNuevaVigenciaA(VigenciasAfiliaciones nuevaVigenciaA) {
        this.nuevaVigenciaA = nuevaVigenciaA;
    }

    public List<VigenciasSueldos> getListVSCrear() {
        return listVSCrear;
    }

    public void setListVSCrear(List<VigenciasSueldos> listVSCrear) {
        this.listVSCrear = listVSCrear;
    }

    public List<VigenciasSueldos> getListVSBorrar() {
        return listVSBorrar;
    }

    public void setListVSBorrar(List<VigenciasSueldos> listVSBorrar) {
        this.listVSBorrar = listVSBorrar;
    }

    public List<VigenciasAfiliaciones> getListVABorrar() {
        return listVABorrar;
    }

    public void setListVABorrar(List<VigenciasAfiliaciones> listVABorrar) {
        this.listVABorrar = listVABorrar;
    }

    public VigenciasSueldos getEditarVS() {
        return editarVS;
    }

    public void setEditarVS(VigenciasSueldos editarVS) {
        this.editarVS = editarVS;
    }

    public VigenciasAfiliaciones getEditarVA() {
        return editarVA;
    }

    public void setEditarVA(VigenciasAfiliaciones editarVA) {
        this.editarVA = editarVA;
    }

    public VigenciasSueldos getDuplicarVS() {
        return duplicarVS;
    }

    public void setDuplicarVS(VigenciasSueldos duplicarVS) {
        this.duplicarVS = duplicarVS;
    }

    public VigenciasAfiliaciones getDuplicarVA() {
        return duplicarVA;
    }

    public void setDuplicarVA(VigenciasAfiliaciones duplicarVA) {
        this.duplicarVA = duplicarVA;
    }

    public List<VigenciasAfiliaciones> getListVACrear() {
        return listVACrear;
    }

    public void setListVACrear(List<VigenciasAfiliaciones> listVACrear) {
        this.listVACrear = listVACrear;
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

    public boolean isRetroactivo() {
        return retroactivo;
    }

    public void setRetroactivo(boolean retroactivo) {
        this.retroactivo = retroactivo;
    }

    public boolean isMostrarActual() {

        return mostrarActual;
    }

    public Date getFechaSis() {
        return fechaSis;
    }

    public void setFechaSis(Date fechaSis) {
        this.fechaSis = fechaSis;
    }

    public void setMostrarActual(boolean mostrarActual) {
        this.mostrarActual = mostrarActual;
    }

    public int getValorTotal() {
        valorTotal = 0;
        if (listVigenciasSueldos != null) {
            for (int i = 0; i < listVigenciasSueldos.size(); i++) {
                valorTotal = valorTotal + listVigenciasSueldos.get(i).getValor().intValue();
            }
        }
//        RequestContext context = RequestContext.getCurrentInstance();
//        context.update("form:totalConsultarActual");
//        context.update("form:valorConsultarActual");
        return valorTotal;
    }

    public void setValorTotal(int valorTotal) {
        this.valorTotal = valorTotal;
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

    public boolean isGuardado() {
        return guardado;
    }

    public String getAltoTabla1() {
        return altoTabla1;
    }

    public String getAltoTabla2() {
        return altoTabla2;
    }

    public VigenciasSueldos getVigenciaSueldoSeleccionada() {
        return vigenciaSueldoSeleccionada;
    }

    public void setVigenciaSueldoSeleccionada(VigenciasSueldos vigenciaSueldoSeleccionada) {
        this.vigenciaSueldoSeleccionada = vigenciaSueldoSeleccionada;
    }

    public VigenciasAfiliaciones getVigenciaAfiliacioneSeleccionada() {
        return vigenciaAfiliacioneSeleccionada;
    }

    public void setVigenciaAfiliacioneSeleccionada(VigenciasAfiliaciones vigenciaAfiliacioneSeleccionada) {
        this.vigenciaAfiliacioneSeleccionada = vigenciaAfiliacioneSeleccionada;
    }

    public String getInfoRegistroTipoSueldo() {
        return infoRegistroTipoSueldo;
    }

    public String getInfoRegistroMotivoCambioSueldo() {
        return infoRegistroMotivoCambioSueldo;
    }

    public String getInfoRegistroTercero() {
        return infoRegistroTercero;
    }

    public String getInfoRegistroTipoEntidad() {
        return infoRegistroTipoEntidad;
    }

    public Unidades getUniPago() {
        return uniPago;
    }

    public void setUniPago(Unidades uniPago) {
        this.uniPago = uniPago;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public String getInfoRegistroD() {
        return infoRegistroD;
    }

    public String getInfoRegistroS() {
        return infoRegistroS;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }
}
