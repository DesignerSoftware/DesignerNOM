package Controlador;


import Entidades.CentrosCostos;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.Parametros;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.TiposTrabajadores;
import Entidades.Usuarios;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarParametrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
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

@ManagedBean
@SessionScoped
public class ControlParametro implements Serializable {

    @EJB
    AdministrarParametrosInterface administrarParametros;
    private ParametrosEstructuras parametroLiquidacion;
    private List<Estructuras> lovEstructuras, filtradoLovEstructuras;
    private Estructuras seleccionEstructura;
    private List<TiposTrabajadores> lovTiposTrabajadores, filtradoLovTiposTrabajadores;
    private TiposTrabajadores seleccionTipoTrabajador;
    private List<Procesos> lovProcesos, filtradoLovProcesos;
    private Procesos seleccionProcesos;
    private List<Empresas> listaEmpresas;
    //OTROS
    private boolean aceptar, guardado, cambiosEmpleadosParametros, borrarTodos, cambiosParametros;
    private int index, tipoLista, cualCelda, bandera, editor;
    private String altoTabla;
    private Integer empleadosParametrizados;
    private Date fechaCorte, fechaInicial, fechaFinal;
    private String strFechaCorte, strFechaInicial, strFechaFinal, tipoGuardado;
    private final SimpleDateFormat formatoFecha, formatoFechaDias;
    //AUTOCOMPLETAR
    private String nombreEstructura, nombreTipoTrabajador, nombreProceso;
    //EMPLEADOS PARA LIQUIDAR
    private List<Parametros> empleadosParametros;
    private List<Parametros> filtradoEmpleadosParametros;
    private Parametros editarEmpleadosParametros;
    private Parametros empleadoParametroSeleccionado;
    //COLUMNAS
    private Column FechaDesde, FechaHasta, Codigo, pApellido, sApellido, nombre, estadoParametro;
    //LOV EMPLEADOS
    private List<Empleados> lovEmpleados;
    private List<Empleados> filtradoLovEmpleados;
    private Empleados seleccionEmpleado;
    //ADICIONAR Y/O ELIMINAR EMPLEADOS DE PARAMERTROS
    private final List<Parametros> listaCrearParametros;
    private final List<Parametros> listaBorrarParametros;
    private BigInteger l;
    private int k;
    //
    private String infoRegistroEmpleado, infoRegistroProceso, infoRegistroTipoTrabajador, infoRegistroEstructura;
    //
    private String infoRegistro;

    public ControlParametro() {
        aceptar = true;
        guardado = true;
        cambiosEmpleadosParametros = false;
        bandera = 0;
        editor = 0;
        index = -1;
        cualCelda = -1;
        tipoLista = 0;
        listaBorrarParametros = new ArrayList<Parametros>();
        altoTabla = "190";
        cambiosParametros = false;
        formatoFecha = new SimpleDateFormat("dd/MMM/yyyy");
        formatoFechaDias = new SimpleDateFormat("dd/MM/yyyy");
        k = 0;
        listaCrearParametros = new ArrayList<Parametros>();
        empleadoParametroSeleccionado = null;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarParametros.obtenerConexion(ses.getId());
            empleadosParametros = null;
            getEmpleadosParametros();
            if (empleadosParametros != null) {
                if (!empleadosParametros.isEmpty()) {
                    empleadoParametroSeleccionado = empleadosParametros.get(0);
                }
            }
            contarRegistros();
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void llamarLov() {
        FacesContext contexto = FacesContext.getCurrentInstance();
        RequestContext context = RequestContext.getCurrentInstance();
        Map<String, String> map = contexto.getExternalContext().getRequestParameterMap();
        String LOV = map.get("LOV");
        if (LOV.equals("ESTRUCTURA")) {
            cargarLovEstrucuras();
            contarRegistrosLovEstr(0);
            RequestContext.getCurrentInstance().update("formularioDialogos:estructurasDialogo");
            RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').show()");
        } else if (LOV.equals("TIPO TRABAJADOR")) {
            cargarLovTiposTrabajadores();
            contarRegistrosLovTT(0);
            RequestContext.getCurrentInstance().update("formularioDialogos:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
        } else if (LOV.equals("PROCESO")) {
            cargarLovProcesos();
            contarRegistrosLovProc(0);
            RequestContext.getCurrentInstance().update("formularioDialogos:ProcesosDialogo");
            RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
        }
    }

    public void ubicacionCampo() {
        FacesContext contexto = FacesContext.getCurrentInstance();
        RequestContext context = RequestContext.getCurrentInstance();
        Map<String, String> map = contexto.getExternalContext().getRequestParameterMap();
        String campo = map.get("CAMPO");
        editor = 0;
        if (campo.equals("FECHA DESDE")) {
            cualCelda = 0;
        } else if (campo.equals("FECHA HASTA")) {
            cualCelda = 1;
        } else if (campo.equals("FECHA CORTE")) {
            cualCelda = 2;
        } else if (campo.equals("CODIGO CC")) {
            cualCelda = 4;
        } else if (campo.equals("NOMBRE CC")) {
            cualCelda = 5;
        } else if (campo.equals("DESDE")) {
            cualCelda = 8;
        } else if (campo.equals("HASTA")) {
            cualCelda = 9;
        } else if (campo.equals("CODIGO")) {
            cualCelda = 10;
        } else if (campo.equals("PAPELLIDO")) {
            cualCelda = 11;
        } else if (campo.equals("SAPELLIDO")) {
            cualCelda = 12;
        } else if (campo.equals("NOMBRE")) {
            cualCelda = 13;
        } else if (campo.equals("ESTADO")) {
            cualCelda = 14;
        }
    }

    public void ubicacionCampoTabla() {
        FacesContext contexto = FacesContext.getCurrentInstance();
        Map<String, String> map = contexto.getExternalContext().getRequestParameterMap();
        String campo = map.get("CAMPO");
        String indice = map.get("INDICE");
        index = Integer.parseInt(indice);
        editor = 1;
        if (campo.equals("DESDE")) {
            cualCelda = 8;
        } else if (campo.equals("HASTA")) {
            cualCelda = 9;
        } else if (campo.equals("CODIGO")) {
            cualCelda = 10;
        } else if (campo.equals("PAPELLIDO")) {
            cualCelda = 11;
        } else if (campo.equals("SAPELLIDO")) {
            cualCelda = 12;
        } else if (campo.equals("NOMBRE")) {
            cualCelda = 13;
        } else if (campo.equals("ESTADO")) {
            cualCelda = 14;
        }
    }

    public void actualizarEstructura() {
        RequestContext context = RequestContext.getCurrentInstance();
        parametroLiquidacion.setEstructura(seleccionEstructura);
        RequestContext.getCurrentInstance().update("form:Estructura");
        RequestContext.getCurrentInstance().update("form:codigoCC");
        RequestContext.getCurrentInstance().update("form:nombreCC");
        filtradoLovEstructuras = null;
        seleccionEstructura = null;
        aceptar = true;
        guardado = false;
        cambiosParametros = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formularioDialogos:estructurasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lOVEstructuras");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");

        context.reset("formularioDialogos:lOVEstructuras:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lOVEstructuras').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').hide()");
    }

    public void cancelarCambioEstructura() {
        filtradoLovEstructuras = null;
        seleccionEstructura = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:estructurasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lOVEstructuras");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
        context.reset("formularioDialogos:lOVEstructuras:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lOVEstructuras').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').hide()");
    }

    public void actualizarTipoTrabajador() {
        RequestContext context = RequestContext.getCurrentInstance();
        parametroLiquidacion.setTipotrabajador(seleccionTipoTrabajador);
        RequestContext.getCurrentInstance().update("form:tipoTrabajador");
        filtradoLovTiposTrabajadores = null;
        seleccionTipoTrabajador = null;
        aceptar = true;
        guardado = false;
        cambiosParametros = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formularioDialogos:TipoTrabajadorDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTipoTrabajador");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTT");

        context.reset("formularioDialogos:lovTipoTrabajador:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').hide()");
    }

    public void cancelarCambioTipoTrabajador() {
        filtradoLovTiposTrabajadores = null;
        seleccionTipoTrabajador = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:TipoTrabajadorDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTipoTrabajador");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTT");
        context.reset("formularioDialogos:lovTipoTrabajador:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').hide()");
    }

    public void actualizarProceso() {
        RequestContext context = RequestContext.getCurrentInstance();
        parametroLiquidacion.setProceso(seleccionProcesos);
        RequestContext.getCurrentInstance().update("form:proceso");
        filtradoLovProcesos = null;
        seleccionProcesos = null;
        aceptar = true;
        guardado = false;
        cambiosParametros = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

        RequestContext.getCurrentInstance().update("formularioDialogos:ProcesosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovProcesos");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarP");

        context.reset("formularioDialogos:lovProcesos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovProcesos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').hide()");
    }

    public void cancelarCambioProceso() {
        filtradoLovProcesos = null;
        seleccionProcesos = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:ProcesosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovProcesos");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarP");
        context.reset("formularioDialogos:lovProcesos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovProcesos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').hide()");
    }

    public void seleccionarEmpleado() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (empleadosParametros != null) {
            System.out.println("empleadosParametros.size() : " + empleadosParametros.size());
            if (!empleadosParametros.isEmpty()) {
                System.out.println("seleccionEmpleado.getSecuencia() : " + seleccionEmpleado.getSecuencia());
                System.out.println("Empleado : " + seleccionEmpleado.getPersona().getNombreCompleto());
                int control = 0;
                for (int i = 0; i < empleadosParametros.size(); i++) {
                    if (empleadosParametros.get(i).getEmpleado().getSecuencia().equals(seleccionEmpleado.getSecuencia())) {
                        System.out.println("empleadosParametros.get(i).getEmpleado().getSecuencia() : " + empleadosParametros.get(i).getEmpleado().getSecuencia());
                        System.out.println("empleadosParametros.get(i) Empleado() : " + empleadosParametros.get(i).getEmpleado().getPersona().getNombreCompleto());
                        System.out.println("/////////////////////////////////////////////////////////////");
                        control++;
                        break;
                    }
                }
                if (control == 0) {
                    agregarParametro();
                } else {
                    RequestContext.getCurrentInstance().execute("PF('errorSeleccionEmpleado').show()");
                    aceptar = true;
                }
            } else {
                agregarParametro();
            }
        } else {
            empleadosParametros = new ArrayList<Parametros>();
            agregarParametro();
        }
        RequestContext.getCurrentInstance().update("form:empleadosParametros");
        contarRegistros();
        filtradoLovEmpleados = null;
        seleccionEmpleado = null;
        aceptar = true;
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:quitarTodos");

        RequestContext.getCurrentInstance().update("formularioDialogos:buscarEmpleadoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpleados");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarEm");

        context.reset("formularioDialogos:lovEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('buscarEmpleadoDialogo').hide()");
    }

    public void cancelarSeleccionEmpleado() {
        filtradoLovEmpleados = null;
        seleccionEmpleado = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();

        RequestContext.getCurrentInstance().update("formularioDialogos:buscarEmpleadoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpleados");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarEm");
        context.reset("formularioDialogos:lovEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('buscarEmpleadoDialogo').hide()");
    }

    public void agregarParametro() {
        System.out.println("Entro en agregarParametro()");
        k++;
        l = BigInteger.valueOf(k);
        Parametros parametro = new Parametros();
        parametro.setSecuencia(l);
        parametro.setParametroestructura(parametroLiquidacion);
        parametro.setEmpleado(seleccionEmpleado);
        listaCrearParametros.add(parametro);
        empleadosParametros.add(parametro);
        if (tipoLista == 1) {
            filtradoEmpleadosParametros.add(parametro);
        }
        if (cambiosEmpleadosParametros == false) {
            cambiosEmpleadosParametros = true;
        }
        borrarTodos = false;
    }

    public void valoresBackupAutocompletar(String Campo) {
        editor = 0;
        if (Campo.equals("ESTRUCTURA")) {
            cualCelda = 3;
            nombreEstructura = parametroLiquidacion.getEstructura().getNombre();
        } else if (Campo.equals("TIPO TRABAJADOR")) {
            cualCelda = 6;
            nombreTipoTrabajador = parametroLiquidacion.getTipotrabajador().getNombre();
        } else if (Campo.equals("PROCESO")) {
            cualCelda = 7;
            nombreProceso = parametroLiquidacion.getProceso().getDescripcion();
        }
    }

    public void autocompletar(String confirmarCambio, String valorConfirmar) {
        RequestContext context = RequestContext.getCurrentInstance();
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        if (confirmarCambio.equalsIgnoreCase("ESTRUCTURA")) {
            if (!valorConfirmar.isEmpty()) {
                parametroLiquidacion.getEstructura().setNombre(nombreEstructura);
                cargarLovEstrucuras();
                if (lovEstructuras != null) {
                    for (int i = 0; i < lovEstructuras.size(); i++) {
                        if (lovEstructuras.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                    if (coincidencias == 1) {
                        parametroLiquidacion.setEstructura(lovEstructuras.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("form:Estructura");
                        RequestContext.getCurrentInstance().update("form:codigoCC");
                        RequestContext.getCurrentInstance().update("form:nombreCC");
                        guardado = false;
                        cambiosParametros = true;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    } else {
                        cargarLovEstrucuras();
                        contarRegistrosLovEstr(0);
                        RequestContext.getCurrentInstance().update("formularioDialogos:estructurasDialogo");
                        RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').show()");
                        RequestContext.getCurrentInstance().update("form:Estructura");
                    }
                } else {
                    RequestContext.getCurrentInstance().execute("PF('lovVacia').show()");
                    //NO HAY ELEMENTOS EN LA LISTA DE VALORES
                }
            } else {
                guardado = false;
                cambiosParametros = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                parametroLiquidacion.setEstructura(new Estructuras());
                RequestContext.getCurrentInstance().update("form:Estructura");
                RequestContext.getCurrentInstance().update("form:codigoCC");
                RequestContext.getCurrentInstance().update("form:nombreCC");
            }

        } else if (confirmarCambio.equalsIgnoreCase("TIPO TRABAJADOR")) {
            if (!valorConfirmar.isEmpty()) {
                parametroLiquidacion.getTipotrabajador().setNombre(nombreTipoTrabajador);
                cargarLovTiposTrabajadores();
                if (lovTiposTrabajadores != null) {
                    for (int i = 0; i < lovTiposTrabajadores.size(); i++) {
                        if (lovTiposTrabajadores.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                    if (coincidencias == 1) {
                        parametroLiquidacion.setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("form:tipoTrabajador");
                        guardado = false;
                        cambiosParametros = true;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    } else {
                        cargarLovTiposTrabajadores();
                        contarRegistrosLovTT(0);
                        RequestContext.getCurrentInstance().update("formularioDialogos:TipoTrabajadorDialogo");
                        RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
                        RequestContext.getCurrentInstance().update("form:tipoTrabajador");
                    }
                } else {
                    RequestContext.getCurrentInstance().execute("PF('lovVacia').show()");
                    //NO HAY ELEMENTOS EN LA LISTA DE VALORES
                }
            } else {
                guardado = false;
                cambiosParametros = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                parametroLiquidacion.setTipotrabajador(new TiposTrabajadores());
                RequestContext.getCurrentInstance().update("form:tipoTrabajador");
            }
        } else if (confirmarCambio.equalsIgnoreCase("PROCESO")) {
            parametroLiquidacion.getProceso().setDescripcion(nombreProceso);
            cargarLovProcesos();
            if (lovProcesos != null) {
                for (int i = 0; i < lovProcesos.size(); i++) {
                    if (lovProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroLiquidacion.setProceso(lovProcesos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("form:proceso");
                    guardado = false;
                    cambiosParametros = true;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    cargarLovProcesos();
                    contarRegistrosLovProc(0);
                    RequestContext.getCurrentInstance().update("formularioDialogos:ProcesosDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
                    RequestContext.getCurrentInstance().update("form:proceso");
                }
            } else {
                RequestContext.getCurrentInstance().execute("PF('lovVacia').show()");
                //NO HAY ELEMENTOS EN LA LISTA DE VALORES
            }
        }
    }

    public void validarGuardado(String tipoG) {
        System.out.println("Entre a validarGuardado");
        tipoGuardado = tipoG;
        RequestContext context = RequestContext.getCurrentInstance();
        if (guardado == false) {
            if (parametroLiquidacion.getFechadesdecausado() != null) {
                if (parametroLiquidacion.getFechahastacausado() != null) {
                    if (parametroLiquidacion.getFechasistema() != null) {
                        if (parametroLiquidacion.getProceso().getSecuencia() != null) {
                            int comprar = parametroLiquidacion.getFechadesdecausado().compareTo(parametroLiquidacion.getFechahastacausado());
                            if (comprar < 0) {
                                Calendar c = Calendar.getInstance();
                                c.setTime(parametroLiquidacion.getFechadesdecausado());
                                int dia = c.get(Calendar.DAY_OF_MONTH);
                                if (dia == 1 || dia == 16) {
                                    c.setTime(parametroLiquidacion.getFechahastacausado());
                                    dia = c.get(Calendar.DAY_OF_MONTH);
                                    int ultimoDia = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                                    if (dia == 15 || dia == ultimoDia) {
                                        c.setTime(parametroLiquidacion.getFechasistema());
                                        dia = c.get(Calendar.DAY_OF_MONTH);
                                        ultimoDia = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                                        if (dia == 1 || dia == 15 || dia == 16 || dia == ultimoDia) {
                                            if (compararFechas() == true) {
                                                if (conteoDias_Proceso() == true) {
                                                    guardarCambios();
                                                } else {
                                                    RequestContext.getCurrentInstance().execute("PF('errorDias').show();");
                                                }
                                            } else {
                                                RequestContext.getCurrentInstance().update("formularioDialogos:errorFechas");
                                                RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
                                            }
                                        } else {
                                            RequestContext.getCurrentInstance().execute("PF('errorDiaFechaCorte').show();");
                                        }
                                    } else {
                                        RequestContext.getCurrentInstance().execute("PF('errorDiaFechaHasta').show();");
                                    }
                                } else {
                                    RequestContext.getCurrentInstance().execute("PF('errorDiaFechaDesde').show();");
                                }
                            } else {
                                RequestContext.getCurrentInstance().execute("PF('errorFechaHasta_Desde').show();");
                            }
                        } else {
                            RequestContext.getCurrentInstance().execute("PF('errorProceso').show();");
                        }
                    } else {
                        RequestContext.getCurrentInstance().execute("PF('errorFechaCorte').show();");
                    }
                } else {
                    RequestContext.getCurrentInstance().execute("PF('errorFechaHasta').show();");
                }
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorFechaDesde').show();");
            }
        }
        if (guardado == true && tipoGuardado.equals("ADICIONAR EMPLEADOS")) {
            guardarCambios();
        }
    }

    public boolean conteoDias_Proceso() {
        Integer diferencia;
        Short codigoProceso;
        Date fechaDesde, fechaHasta;
        fechaDesde = parametroLiquidacion.getFechadesdecausado();
        fechaHasta = parametroLiquidacion.getFechahastacausado();
        diferencia = administrarParametros.diferenciaDias(formatoFechaDias.format(fechaDesde), formatoFechaDias.format(fechaHasta));
        codigoProceso = parametroLiquidacion.getProceso().getCodigo();
        if (diferencia <= 30) {
            return true;
        } else if (codigoProceso != 6 && diferencia > 30) {
            return false;
        } else {
            return true;
        }
    }

    public void guardarCambios() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cambiosParametros == true) {
                if (parametroLiquidacion.getTipotrabajador().getSecuencia() == null) {
                    parametroLiquidacion.setTipotrabajador(null);
                }
                if (parametroLiquidacion.getEstructura().getSecuencia() == null) {
                    parametroLiquidacion.setEstructura(null);
                }
                administrarParametros.crearParametroEstructura(parametroLiquidacion);
                parametroLiquidacion = null;
                getParametroLiquidacion();
                cambiosParametros = false;
            }
            if (cambiosEmpleadosParametros == true) {
                if (!listaBorrarParametros.isEmpty()) {
                    administrarParametros.eliminarParametros(listaBorrarParametros);
                    listaBorrarParametros.clear();
                }
                if (!listaCrearParametros.isEmpty()) {
                    Usuarios au = administrarParametros.usuarioActual();
                    Date fechaDesde = parametroLiquidacion.getFechadesdecausado();
                    Date fechaHasta = parametroLiquidacion.getFechahastacausado();
                    Date fechaSistema = parametroLiquidacion.getFechasistema();
                    Procesos proceso = parametroLiquidacion.getProceso();
                    for (int i = 0; i < listaCrearParametros.size(); i++) {
                        listaCrearParametros.get(i).setFechadesdecausado(fechaDesde);
                        listaCrearParametros.get(i).setFechahastacausado(fechaHasta);
                        listaCrearParametros.get(i).setFechasistema(fechaSistema);
                        listaCrearParametros.get(i).setProceso(proceso);
                        listaCrearParametros.get(i).setUsuario(au);
                        if (listaCrearParametros.get(i).getParametroestructura().getTipotrabajador().getSecuencia() == null) {
                            listaCrearParametros.get(i).getParametroestructura().setTipotrabajador(null);
                        }
                    }
                    administrarParametros.crearParametros(listaCrearParametros);
                    listaCrearParametros.clear();
                }
                cambiosEmpleadosParametros = false;
            }
            if (tipoGuardado.equals("ADICIONAR EMPLEADOS")) {
                if (consultarEmpleadosParametrizados() == true) {
                    adicionarEmpleados();
                } else {
                    RequestContext.getCurrentInstance().update("formularioDialogos:confirmarAdicionarEmpleados");
                    RequestContext.getCurrentInstance().execute("PF('confirmarAdicionarEmpleados').show()");
                }
            } else if (tipoGuardado.equals("GUARDADO RAPIDO")) {
                empleadosParametros = null;
                getEmpleadosParametros();
                contarRegistros();
                guardado = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:empleadosParametros");
                RequestContext.getCurrentInstance().update("form:panelParametro");
                FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            } else if (tipoGuardado.equals("GUARDADO SALIR")) {
                salir();
                RequestContext.getCurrentInstance().execute("PF('salirGuardado();");
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (editor == 0) {
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaDesde");
                RequestContext.getCurrentInstance().execute("PF('editarFechaDesde').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaHasta");
                RequestContext.getCurrentInstance().execute("PF('editarFechaHasta').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaCorte");
                RequestContext.getCurrentInstance().execute("PF('editarFechaCorte').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEstructura");
                RequestContext.getCurrentInstance().execute("PF('editarEstructura').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoCC");
                RequestContext.getCurrentInstance().execute("PF('editarCodigoCC').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreCentroCosto");
                RequestContext.getCurrentInstance().execute("PF('editarNombreCentroCosto').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoTrabajador");
                RequestContext.getCurrentInstance().execute("PF('editarTipoTrabajador').show()");
                cualCelda = -1;
            } else if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarProceso");
                RequestContext.getCurrentInstance().execute("PF('editarProceso').show()");
                cualCelda = -1;
            }
        } else {
            if (index >= 0) {
                if (tipoLista == 0) {
                    editarEmpleadosParametros = empleadosParametros.get(index);
                }
                if (tipoLista == 1) {
                    editarEmpleadosParametros = filtradoEmpleadosParametros.get(index);
                }

                if (cualCelda == 8) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarDesde");
                    RequestContext.getCurrentInstance().execute("PF('editarDesde').show()");
                    cualCelda = -1;
                } else if (cualCelda == 9) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarHasta");
                    RequestContext.getCurrentInstance().execute("PF('editarHasta').show()");
                    cualCelda = -1;
                } else if (cualCelda == 10) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigo");
                    RequestContext.getCurrentInstance().execute("PF('editarCodigo').show()");
                    cualCelda = -1;
                } else if (cualCelda == 11) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarPApellido");
                    RequestContext.getCurrentInstance().execute("PF('editarPApellido').show()");
                    cualCelda = -1;
                } else if (cualCelda == 12) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarSApellido");
                    RequestContext.getCurrentInstance().execute("PF('editarSApellido').show()");
                    cualCelda = -1;
                } else if (cualCelda == 13) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNombre");
                    RequestContext.getCurrentInstance().execute("PF('editarNombre').show()");
                    cualCelda = -1;
                } else if (cualCelda == 14) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarEstado");
                    RequestContext.getCurrentInstance().execute("PF('editarEstado').show()");
                    cualCelda = -1;
                }
            }
            index = -1;
        }
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (cualCelda == 3) {
            cargarLovEstrucuras();
            contarRegistrosLovEstr(0);
            RequestContext.getCurrentInstance().update("formularioDialogos:estructurasDialogo");
            RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').show()");
        } else if (cualCelda == 6) {
            cargarLovTiposTrabajadores();
            contarRegistrosLovTT(0);
            RequestContext.getCurrentInstance().update("formularioDialogos:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
        } else if (cualCelda == 7) {
            cargarLovProcesos();
            contarRegistrosLovProc(0);
            RequestContext.getCurrentInstance().update("formularioDialogos:ProcesosDialogo");
            RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
        }
    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    public void activarCtrlF11() {
        if (bandera == 0) {
            FechaDesde = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:empleadosParametros:FechaDesde");
            FechaDesde.setFilterStyle("width: 85% !important;");
            FechaHasta = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:empleadosParametros:FechaHasta");
            FechaHasta.setFilterStyle("width: 85% !important;");
            Codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:empleadosParametros:Codigo");
            Codigo.setFilterStyle("width: 85% !important;");
            pApellido = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:empleadosParametros:pApellido");
            pApellido.setFilterStyle("width: 85% !important;");
            sApellido = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:empleadosParametros:sApellido");
            sApellido.setFilterStyle("width: 85% !important;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:empleadosParametros:nombre");
            nombre.setFilterStyle("width: 85% !important;");
            estadoParametro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:empleadosParametros:estadoParametro");
            estadoParametro.setFilterStyle("width: 85% !important;");
            altoTabla = "170";
            RequestContext.getCurrentInstance().update("form:empleadosParametros");
            bandera = 1;
        } else {
            cerrarFiltrado();
        }
        contarRegistros();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:empleadosParametrosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "EmpleadosParametrosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:empleadosParametrosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "EmpleadosParametrosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
    }

    public void cambiosCampos() {
        guardado = false;
        cambiosParametros = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:empleadosParametros");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void eliminarParametros(int indice) {
        if (indice >= 0) {
            if (tipoLista == 0) {
                if (!listaCrearParametros.isEmpty() && listaCrearParametros.contains(empleadosParametros.get(indice))) {
                    int crearIndex = listaCrearParametros.indexOf(empleadosParametros.get(indice));
                    listaCrearParametros.remove(crearIndex);
                } else {
                    listaBorrarParametros.add(empleadosParametros.get(indice));
                }
                empleadosParametros.remove(indice);
            } else if (tipoLista == 1) {
                if (!listaCrearParametros.isEmpty() && listaCrearParametros.contains(filtradoEmpleadosParametros.get(indice))) {
                    int crearIndex = listaCrearParametros.indexOf(filtradoEmpleadosParametros.get(indice));
                    listaCrearParametros.remove(crearIndex);
                } else {
                    listaBorrarParametros.add(filtradoEmpleadosParametros.get(indice));
                }
                int EPIndex = empleadosParametros.indexOf(filtradoEmpleadosParametros.get(indice));
                empleadosParametros.remove(EPIndex);
                filtradoEmpleadosParametros.remove(indice);

            }
            cambiosEmpleadosParametros = true;
            guardado = false;
            RequestContext context = RequestContext.getCurrentInstance();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:empleadosParametros");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (empleadosParametros.isEmpty()) {
                borrarTodos = true;
                RequestContext.getCurrentInstance().update("form:quitarTodos");
            }
        }
    }

    public void adicionarEmpleados() {
        System.out.println("Entre a Adicionar Empleados");
        if (parametroLiquidacion != null) {
            administrarParametros.adicionarEmpleados(parametroLiquidacion.getSecuencia());
        }
        empleadosParametros = null;
        getEmpleadosParametros();
        FacesMessage msg;
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext contexto = FacesContext.getCurrentInstance();
        if (empleadosParametros != null && !empleadosParametros.isEmpty()) {
            msg = new FacesMessage("Información", "Se adicionaron los empleados exitosamente");
            contexto.addMessage(null, msg);
        } else {
            msg = new FacesMessage("Información", "No hay empleados que cumplan con los criterios parametrizados.");
            contexto.addMessage(null, msg);
        }
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:growl");
        RequestContext.getCurrentInstance().update("form:empleadosParametros");
        RequestContext.getCurrentInstance().update("form:quitarTodos");
    }

    public boolean consultarEmpleadosParametrizados() {
        empleadosParametrizados = administrarParametros.empleadosParametrizados(parametroLiquidacion.getProceso().getSecuencia());
        if (empleadosParametrizados == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean compararFechas() {
        fechaCorte = parametroLiquidacion.getFechasistema();
        fechaInicial = parametroLiquidacion.getEstructura().getCentrocosto().getEmpresa().getControlinicioperiodoactivo();
        fechaFinal = parametroLiquidacion.getEstructura().getCentrocosto().getEmpresa().getControlfinperiodoactivo();
        if (fechaInicial.compareTo(fechaCorte) > 0 && fechaFinal.compareTo(fechaCorte) < 0) {
            return true;
        } else if (fechaInicial.compareTo(fechaCorte) == 0 || fechaFinal.compareTo(fechaCorte) == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void seguirErrorFechas() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (conteoDias_Proceso() == true) {
            guardarCambios();
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorDias').show();");
        }
    }

    public void cancelarModificacion() {
        cerrarFiltrado();
        parametroLiquidacion = null;
        getParametroLiquidacion();
        empleadosParametros = null;
        getEmpleadosParametros();
        cambiosEmpleadosParametros = false;
        lovEstructuras = null;
        lovProcesos = null;
        lovTiposTrabajadores = null;
        lovEmpleados = null;
        guardado = true;
        cambiosParametros = false;
        listaBorrarParametros.clear();
        listaCrearParametros.clear();
        listaBorrarParametros.clear();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:empleadosParametros");

        RequestContext.getCurrentInstance().update("form:fechaDesde");
        RequestContext.getCurrentInstance().update("form:fechaHasta");
        RequestContext.getCurrentInstance().update("form:fechaCorte");
        RequestContext.getCurrentInstance().update("form:Estructura");
        RequestContext.getCurrentInstance().update("form:codigoCC");
        RequestContext.getCurrentInstance().update("form:nombreCC");
        RequestContext.getCurrentInstance().update("form:tipoTrabajador");
        RequestContext.getCurrentInstance().update("form:proceso");
    }

    public void salir() {
        cerrarFiltrado();
        parametroLiquidacion = null;
        empleadosParametros = null;
        cambiosEmpleadosParametros = false;
        lovEstructuras = null;
        lovProcesos = null;
        lovTiposTrabajadores = null;
        lovEmpleados = null;
        guardado = true;
        cambiosParametros = false;
        listaBorrarParametros.clear();
        listaCrearParametros.clear();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        getEmpleadosParametros();
        contarRegistros();
        guardado = true;
    }

    private void cerrarFiltrado() {
        if (bandera == 1) {
            FechaDesde = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:empleadosParametros:FechaDesde");
            FechaDesde.setFilterStyle("display: none; visibility: hidden;");
            FechaHasta = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:empleadosParametros:FechaHasta");
            FechaHasta.setFilterStyle("display: none; visibility: hidden;");
            Codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:empleadosParametros:Codigo");
            Codigo.setFilterStyle("display: none; visibility: hidden;");
            pApellido = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:empleadosParametros:pApellido");
            pApellido.setFilterStyle("display: none; visibility: hidden;");
            sApellido = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:empleadosParametros:sApellido");
            sApellido.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:empleadosParametros:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            estadoParametro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:empleadosParametros:estadoParametro");
            estadoParametro.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "190";
            RequestContext.getCurrentInstance().update("form:empleadosParametros");
            bandera = 0;
            filtradoEmpleadosParametros = null;
            tipoLista = 0;
        }
    }

    public void borrarParametros() {
        empleadosParametros = null;
        getEmpleadosParametros();
        contarRegistros();
        listaBorrarParametros.clear();
        listaCrearParametros.clear();
        cambiosEmpleadosParametros = false;
        administrarParametros.borrarParametros(parametroLiquidacion.getSecuencia());
        if (cambiosParametros == true) {
            guardado = false;
        } else {
            guardado = true;
        }
        parametroLiquidacion = null;
        getEmpleadosParametros();
        getParametroLiquidacion();
        empleadosParametros = null;
        contarRegistros();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:empleadosParametros");
        RequestContext.getCurrentInstance().update("form:quitarTodos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        empleadoParametroSeleccionado = null;
        contarRegistros();
    }

    public void eventoFiltrarEstr() {
        contarRegistrosLovEstr(1);
    }

    public void eventoFiltrarTT() {
        contarRegistrosLovTT(1);
    }

    public void eventoFiltrarProc() {
        contarRegistrosLovProc(1);
    }

    public void eventoFiltrarEmpl() {
        contarRegistrosLovEmpl(1);
    }

    public void contarRegistros() {
        if (tipoLista == 1) {
            infoRegistro = String.valueOf(filtradoEmpleadosParametros.size());
        } else if (empleadosParametros != null) {
            infoRegistro = String.valueOf(empleadosParametros.size());
        } else {
            infoRegistro = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosLovEstr(int tipoListaLOV) {
        if (tipoListaLOV == 1) {
            infoRegistroEstructura = String.valueOf(filtradoLovEstructuras.size());
        } else if (lovEstructuras != null) {
            infoRegistroEstructura = String.valueOf(lovEstructuras.size());
        } else {
            infoRegistroEstructura = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEstructura");
    }

    public void contarRegistrosLovTT(int tipoListaLOV) {
        if (tipoListaLOV == 1) {
            infoRegistroTipoTrabajador = String.valueOf(filtradoLovTiposTrabajadores.size());
        } else if (lovTiposTrabajadores != null) {
            infoRegistroTipoTrabajador = String.valueOf(lovTiposTrabajadores.size());
        } else {
            infoRegistroTipoTrabajador = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTipoTrabajador");
    }

    public void contarRegistrosLovProc(int tipoListaLOV) {
        if (tipoListaLOV == 1) {
            infoRegistroProceso = String.valueOf(filtradoLovProcesos.size());
        } else if (lovProcesos != null) {
            infoRegistroProceso = String.valueOf(lovProcesos.size());
        } else {
            infoRegistroProceso = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroProceso");
    }

    public void contarRegistrosLovEmpl(int tipoListaLOV) {
        if (tipoListaLOV == 1) {
            infoRegistroEmpleado = String.valueOf(filtradoLovEmpleados.size());
        } else if (lovEmpleados != null) {
            infoRegistroEmpleado = String.valueOf(lovEmpleados.size());
        } else {
            infoRegistroEmpleado = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpleado");
//        RequestContext.getCurrentInstance().update("formularioDialogos:buscarEmpleadoDialogo");
//        RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpleados");
    }

///////////  Cargar listas de valores:  //////////
    public void cargarLovEmpleados() {
        if (lovEmpleados == null) {
            lovEmpleados = administrarParametros.empleadosLov();
        }
        contarRegistrosLovEmpl(0);
        RequestContext.getCurrentInstance().update("formularioDialogos:buscarEmpleadoDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpleados");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarEm");
    }

    public void cargarLovProcesos() {
        if (lovProcesos == null) {
            lovProcesos = administrarParametros.lovProcesos();
        }
    }

    public void cargarLovTiposTrabajadores() {
        if (lovTiposTrabajadores == null) {
            lovTiposTrabajadores = administrarParametros.lovTiposTrabajadores();
        }
    }

    public void cargarLovEstrucuras() {
        if (lovEstructuras == null) {
            lovEstructuras = administrarParametros.lovEstructuras();
        }
    }

    //GETTER AND SETTER
    public ParametrosEstructuras getParametroLiquidacion() {
        if (parametroLiquidacion == null) {
            parametroLiquidacion = administrarParametros.parametrosLiquidacion();
            if (parametroLiquidacion == null) {
                Usuarios au = administrarParametros.usuarioActual();
                parametroLiquidacion = new ParametrosEstructuras();
                parametroLiquidacion.setUsuario(au);
                parametroLiquidacion.setProceso(new Procesos());
                parametroLiquidacion.setSecuencia(BigInteger.valueOf(0));
            }
        }
        return parametroLiquidacion;
    }

    public void setParametroLiquidacion(ParametrosEstructuras parametroLiquidacion) {
        this.parametroLiquidacion = parametroLiquidacion;
    }

    public List<Estructuras> getLovEstructuras() {
        return lovEstructuras;
    }

    public void setLovEstructuras(List<Estructuras> lovEstructuras) {
        this.lovEstructuras = lovEstructuras;
    }

    public List<Estructuras> getFiltradoLovEstructuras() {
        return filtradoLovEstructuras;
    }

    public void setFiltradoLovEstructuras(List<Estructuras> filtradoLovEstructuras) {
        this.filtradoLovEstructuras = filtradoLovEstructuras;
    }

    public Estructuras getSeleccionEstructura() {
        return seleccionEstructura;
    }

    public void setSeleccionEstructura(Estructuras seleccionEstructura) {
        this.seleccionEstructura = seleccionEstructura;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    //GETS & SETS DE LOVTIPOSTRABAJADORES
    public List<TiposTrabajadores> getLovTiposTrabajadores() {
        return lovTiposTrabajadores;
    }

    public void setLovTiposTrabajadores(List<TiposTrabajadores> lovTiposTrabajadores) {
        this.lovTiposTrabajadores = lovTiposTrabajadores;
    }

    public List<TiposTrabajadores> getFiltradoLovTiposTrabajadores() {
        return filtradoLovTiposTrabajadores;
    }

    public void setFiltradoLovTiposTrabajadores(List<TiposTrabajadores> filtradoLovTiposTrabajadores) {
        this.filtradoLovTiposTrabajadores = filtradoLovTiposTrabajadores;
    }

    //GETS & SETS DE SeleccionTipoTrabajador
    public TiposTrabajadores getSeleccionTipoTrabajador() {
        return seleccionTipoTrabajador;
    }

    public void setSeleccionTipoTrabajador(TiposTrabajadores seleccionTipoTrabajador) {
        this.seleccionTipoTrabajador = seleccionTipoTrabajador;
    }

    //GETS & SETS DE LovProcesos
    public List<Procesos> getLovProcesos() {
        return lovProcesos;
    }

    public void setLovProcesos(List<Procesos> lovProcesos) {
        this.lovProcesos = lovProcesos;
    }

    public List<Procesos> getFiltradoLovProcesos() {
        return filtradoLovProcesos;
    }

    public void setFiltradoLovProcesos(List<Procesos> filtradoLovProcesos) {
        this.filtradoLovProcesos = filtradoLovProcesos;
    }

    public Procesos getSeleccionProcesos() {
        return seleccionProcesos;
    }

    public void setSeleccionProcesos(Procesos seleccionProcesos) {
        this.seleccionProcesos = seleccionProcesos;
    }

    public List<Parametros> getEmpleadosParametros() {
        if (empleadosParametros == null) {
            empleadosParametros = administrarParametros.empleadosParametros();
            if (empleadosParametros != null && !empleadosParametros.isEmpty()) {
                for (int i = 0; i < empleadosParametros.size(); i++) {
                    empleadosParametros.get(i).setEstadoParametro(administrarParametros.estadoParametro(empleadosParametros.get(i).getSecuencia()));
                }
                borrarTodos = false;
            } else {
                borrarTodos = true;
            }
        }
        return empleadosParametros;
    }

    public void setEmpleadosParametros(List<Parametros> empleadosParametros) {
        this.empleadosParametros = empleadosParametros;
    }

    public List<Parametros> getFiltradoEmpleadosParametros() {
        return filtradoEmpleadosParametros;
    }

    public void setFiltradoEmpleadosParametros(List<Parametros> filtradoEmpleadosParametros) {
        this.filtradoEmpleadosParametros = filtradoEmpleadosParametros;
    }

    public Parametros getEditarEmpleadosParametros() {
        return editarEmpleadosParametros;
    }

    public Integer getEmpleadosParametrizados() {
        return empleadosParametrizados;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public boolean isBorrarTodos() {
        if (empleadosParametros != null && !empleadosParametros.isEmpty()) {
            borrarTodos = false;
        } else {
            borrarTodos = true;
        }
        return borrarTodos;
    }

    public List<Empresas> getListaEmpresas() {
        return listaEmpresas;
    }

    public void setListaEmpresas(List<Empresas> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }

    public String getStrFechaCorte() {
        if (fechaCorte != null) {
            strFechaCorte = formatoFecha.format(fechaCorte).toUpperCase();
        }
        return strFechaCorte;
    }

    public String getStrFechaInicial() {
        if (fechaInicial != null) {
            strFechaInicial = formatoFecha.format(fechaInicial).toUpperCase();
        }
        return strFechaInicial;
    }

    public String getStrFechaFinal() {
        if (fechaFinal != null) {
            strFechaFinal = formatoFecha.format(fechaFinal).toUpperCase();
        }
        return strFechaFinal;
    }

    public List<Empleados> getLovEmpleados() {
        return lovEmpleados;
    }

    public void setLovEmpleados(List<Empleados> lovEmpleados) {
        this.lovEmpleados = lovEmpleados;
    }

    public List<Empleados> getFiltradoLovEmpleados() {
        return filtradoLovEmpleados;
    }

    public void setFiltradoLovEmpleados(List<Empleados> filtradoLovEmpleados) {
        this.filtradoLovEmpleados = filtradoLovEmpleados;
    }

    public Empleados getSeleccionEmpleado() {
        return seleccionEmpleado;
    }

    public void setSeleccionEmpleado(Empleados seleccionEmpleado) {
        this.seleccionEmpleado = seleccionEmpleado;
    }

    public Parametros getEmpleadoParametroSeleccionado() {
        return empleadoParametroSeleccionado;
    }

    public void setEmpleadoParametroSeleccionado(Parametros empleadoParametroSeleccionado) {
        this.empleadoParametroSeleccionado = empleadoParametroSeleccionado;
    }

    public String getInfoRegistroEmpleado() {
        return infoRegistroEmpleado;
    }

    public void setInfoRegistroEmpleado(String infoRegistroEmpleado) {
        this.infoRegistroEmpleado = infoRegistroEmpleado;
    }

    public String getInfoRegistroProceso() {
        return infoRegistroProceso;
    }

    public void setInfoRegistroProceso(String infoRegistroProceso) {
        this.infoRegistroProceso = infoRegistroProceso;
    }

    public String getInfoRegistroTipoTrabajador() {
        return infoRegistroTipoTrabajador;
    }

    public void setInfoRegistroTipoTrabajador(String infoRegistroTipoTrabajador) {
        this.infoRegistroTipoTrabajador = infoRegistroTipoTrabajador;
    }

    public String getInfoRegistroEstructura() {
        return infoRegistroEstructura;
    }

    public void setInfoRegistroEstructura(String infoRegistroEstructura) {
        this.infoRegistroEstructura = infoRegistroEstructura;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }
}
