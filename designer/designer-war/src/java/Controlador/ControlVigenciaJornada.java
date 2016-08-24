package Controlador;

import Entidades.*;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciasJornadasInterface;
import java.io.IOException;
import java.io.Serializable;
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
public class ControlVigenciaJornada implements Serializable {

    @EJB
    AdministrarVigenciasJornadasInterface administrarVigenciasJornadas;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //Vigencias Localizaciones
    private List<VigenciasJornadas> listVigenciasJornadas;
    private List<VigenciasJornadas> filtrarVJ;
    private VigenciasJornadas vigenciaJornadaSeleccionada;
    //Tipos Descansos
    private List<TiposDescansos> listTiposDescansos;
    private TiposDescansos tipoDescansoSeleccionado;
    private List<TiposDescansos> filtrarTiposDescansos;
    //Jornadas Laborales
    private List<JornadasLaborales> listJornadasLaborales;
    private JornadasLaborales jornadaLaboralSeleccionada;
    private List<JornadasLaborales> filtrarJornadasLaborales;
    //Empleado
    private Empleados empleado;
    //Tipo Actualizacion
    private int tipoActualizacion;
    //Activo/Desactivo Crtl + F11
    private int bandera;
    //Activo/Desactivo VP Crtl + F11
    private int banderaVCT;
    //Activo/Desactivo VPP Crtl + F11
    private int banderaVCD;
    //VigenciasCompensaciones Tiempo
    private List<VigenciasCompensaciones> listVigenciasCompensacionesTiempo;
    private List<VigenciasCompensaciones> filtrarVigenciasCompensacionesTiempo;
    private VigenciasCompensaciones vigenciaTiempoSeleccionada;
    // VigenciasCompensaciones Dinero
    private List<VigenciasCompensaciones> listVigenciasCompensacionesDinero;
    private List<VigenciasCompensaciones> filtrarVigenciasCompensacionesDinero;
    private VigenciasCompensaciones vigenciaDineroSeleccionada;
    //Columnas Tabla VL
    private Column vJFechaVigencia, vJNombreJornada, vJTipoDescanso;
    //Columnas Tabla VP
    private Column vCTFechaInicial, vCTFechaFinal, vCTComentario;
    //Columnas Tabla VPP
    private Column vCDComentario, vCDFechaInicial, vCDFechaFinal;
    //Otros
    private boolean aceptar;
    //private int index;
    //modificar
    private List<VigenciasJornadas> listVJModificar;
    private List<VigenciasCompensaciones> listVCTModificar;
    private List<VigenciasCompensaciones> listVCDModificar;
    private boolean guardado;
    //crear VL
    public VigenciasJornadas nuevaVigencia;
    //crear VP
    public VigenciasCompensaciones nuevaVigenciaCT;
    //crear VPP
    public VigenciasCompensaciones nuevaVigenciaCD;
    private List<VigenciasJornadas> listVJCrear;
    private BigInteger nuevaVJornadaSecuencia;
    private int paraNuevaVJornada;
    //borrar VL
    private List<VigenciasJornadas> listVJBorrar;
    private List<VigenciasCompensaciones> listVCTBorrar;
    private List<VigenciasCompensaciones> listVCDBorrar;
    //editar celda
    private VigenciasJornadas editarVJ;
    private VigenciasCompensaciones editarVCT;
    private VigenciasCompensaciones editarVCD;
    private int cualCelda, tipoLista;
    //private boolean cambioEditor, aceptarEditar;
    //duplicar
    private VigenciasJornadas duplicarVJ;
    //Autocompletar
    private boolean permitirIndex, permitirIndexVCT, permitirIndexVCD;
    //Variables Autompletar
    private String nombreJornada, nombreTipoDescanso;
    //Indices VigenciaProrrateo / VigenciaProrrateoProyecto
    //private int indexVCT, indexVCD;
    //Indice de celdas VigenciaProrrateo / VigenciaProrrateoProyecto
    private int cualCeldaVCT, cualCeldaVCD, tipoListaVCT, tipoListaVCD;
    //Index Auxiliar Para Nuevos Registros
    private int indexAuxVJ;
    //Duplicar Vigencia Prorrateo
    private VigenciasCompensaciones duplicarVCT;
    //Duplicar Vigencia Prorrateo Proyecto
    private VigenciasCompensaciones duplicarVCD;
    //Lista Vigencia Prorrateo Crear
    private List<VigenciasCompensaciones> listVCTCrear;
    //Lista Vigencia Prorrateo Proyecto Crear
    private List<VigenciasCompensaciones> listVCDCrear;
    private String nombreXML;
    private String nombreTabla;
    //Banderas Boolean de operaciones sobre vigencias prorrateos y vigencias prorrateos proyectos
    private String msnConfirmarRastro, msnConfirmarRastroHistorico;
    private BigInteger backUp;
    private String nombreTablaRastro;
    private Date fechaParametro;
    private Date fechaVigenciaVJ;
    //ALTO TABLAS
    private String altoTabla1;
    private String altoTabla2;
    private String altoTabla3;
    private boolean cambiosJornada, cambiosDinero, cambiosTiempo;
    private String infoRegistroVJ, infoRegistroVT, infoRegistroVD;
    private String infoRegistroJornadaLaboral, infoRegistroTipoDescanso;
    //validacion
    private String mensajeValidacion;
    //
    private DataTable tablaC;
    private boolean activarLOV;

    public ControlVigenciaJornada() {
        cambiosJornada = false;
        cambiosDinero = false;
        cambiosTiempo = false;
        backUp = null;
        msnConfirmarRastro = "";
        msnConfirmarRastroHistorico = "";
        nombreTablaRastro = "";

        listTiposDescansos = null;
        listJornadasLaborales = null;

        listVigenciasJornadas = null;
        listVigenciasCompensacionesTiempo = null;
        listVigenciasCompensacionesDinero = null;

        empleado = new Empleados();
        //Otros
        aceptar = true;
        //borrar aficiones
        listVJBorrar = new ArrayList<VigenciasJornadas>();
        listVCTBorrar = new ArrayList<VigenciasCompensaciones>();
        listVCDBorrar = new ArrayList<VigenciasCompensaciones>();
        //crear aficiones
        listVJCrear = new ArrayList<VigenciasJornadas>();
        paraNuevaVJornada = 0;
        //modificar aficiones
        listVJModificar = new ArrayList<VigenciasJornadas>();
        listVCTModificar = new ArrayList<VigenciasCompensaciones>();
        listVCDModificar = new ArrayList<VigenciasCompensaciones>();
        //editar
        editarVJ = new VigenciasJornadas();
        editarVCT = new VigenciasCompensaciones();
        editarVCD = new VigenciasCompensaciones();
        //cambioEditor = false;
        //aceptarEditar = true;
        cualCelda = -1;
        tipoLista = 0;
        tipoListaVCT = 0;
        tipoListaVCD = 0;
        //guardar
        guardado = true;
        //Crear VC
        nuevaVigencia = new VigenciasJornadas();
        nuevaVigencia.setJornadatrabajo(new JornadasLaborales());
        nuevaVigencia.setTipodescanso(new TiposDescansos());
        bandera = 0;
        banderaVCT = 0;
        banderaVCD = 0;
        permitirIndex = true;
        permitirIndexVCT = true;
        permitirIndexVCD = true;
        vigenciaTiempoSeleccionada = null;
        vigenciaJornadaSeleccionada = null;
        vigenciaDineroSeleccionada = null;
        cualCeldaVCT = -1;
        cualCeldaVCD = -1;

        nuevaVigenciaCT = new VigenciasCompensaciones();
        nuevaVigenciaCD = new VigenciasCompensaciones();
        listVCTCrear = new ArrayList<VigenciasCompensaciones>();
        listVCDCrear = new ArrayList<VigenciasCompensaciones>();

        nombreTabla = ":formExportarVJ:datosVJEmpleadoExportar";
        nombreXML = "VigenciasJornadasXML";

        altoTabla1 = "115";
        altoTabla2 = "115";
        altoTabla3 = "115";

        mensajeValidacion = "";
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarVigenciasJornadas.obtenerConexion(ses.getId());
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
        getListVigenciasJornadas();
        if (listVigenciasJornadas != null) {
            if (!listVigenciasJornadas.isEmpty()) {
                vigenciaJornadaSeleccionada = listVigenciasJornadas.get(0);
            }
        }
        getListVigenciasCompensacionesDinero();
        getListVigenciasCompensacionesTiempo();
        contarRegistrosVJ();
        contarRegistrosVD();
        contarRegistrosVT();
    }

    /**
     * Modifica los elementos de la tabla VigenciaLocalizacion que no usan
     * autocomplete
     *
     * @param indice Fila donde se efectu el cambio
     */
    public void modificarVJ() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listVJCrear.contains(vigenciaJornadaSeleccionada)) {
            if (listVJModificar.isEmpty()) {
                listVJModificar.add(vigenciaJornadaSeleccionada);
            } else if (!listVJModificar.contains(vigenciaJornadaSeleccionada)) {
                listVJModificar.add(vigenciaJornadaSeleccionada);
            }
            cambiosJornada = true;

            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
        }
    }

    public boolean validarFechasRegistroVJ(int i) {

        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (i == 0) {
            VigenciasJornadas auxiliar = null;
            auxiliar = vigenciaJornadaSeleccionada;

            if (auxiliar.getFechavigencia().after(fechaParametro)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        if (i == 1) {

            if (nuevaVigencia.getFechavigencia().after(fechaParametro)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarVJ.getFechavigencia().after(fechaParametro)) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        return retorno;
    }

    public void modificarFechasVJ(VigenciasJornadas vJornada, int c) {
        vigenciaJornadaSeleccionada = vJornada;

        if (vigenciaJornadaSeleccionada.getFechavigencia() != null) {
            boolean retorno = false;
            retorno = validarFechasRegistroVJ(0);
            if (retorno) {
                cambiarIndice(vigenciaJornadaSeleccionada, c);
                modificarVJ();
            } else {
                vigenciaJornadaSeleccionada.setFechavigencia(fechaVigenciaVJ);

                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:datosVJEmpleado");
                context.execute("errorFechas.show()");
            }
        } else {
            vigenciaJornadaSeleccionada.setFechavigencia(fechaVigenciaVJ);

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosVJEmpleado");
            context.execute("negacionNuevaVJ.show()");
        }
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    /**
     * Metodo que modifica los cambios efectuados en la tabla
     * VigenciasLocalizaciones de la pagina
     *
     * @param indice Fila en la cual se realizo el cambio
     */
    public void modificarVJ(VigenciasJornadas vJornada, String confirmarCambio, String valorConfirmar) {
        vigenciaJornadaSeleccionada = vJornada;

        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("NOMBREJORNADA")) {
            if (!valorConfirmar.isEmpty()) {
                vigenciaJornadaSeleccionada.getJornadatrabajo().setDescripcion(nombreJornada);

                for (int i = 0; i < listJornadasLaborales.size(); i++) {
                    if (listJornadasLaborales.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaJornadaSeleccionada.setJornadatrabajo(listJornadasLaborales.get(indiceUnicoElemento));

                } else {
                    permitirIndex = false;
                    context.update("form:JornadaLaboralDialogo");
                    context.execute("JornadaLaboralDialogo.show()");
                    tipoActualizacion = 0;
                }
            } else {
                vigenciaJornadaSeleccionada.setJornadatrabajo(new JornadasLaborales());

                //cambiosJornada = true;

                if (guardado) {
                    guardado = false;
                    context.update("form:ACEPTAR");
                }
            }
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
            RequestContext.getCurrentInstance().update("form:listaValores");
        } else if (confirmarCambio.equalsIgnoreCase("TIPODESCANSO")) {
            if (!valorConfirmar.isEmpty()) {
                vigenciaJornadaSeleccionada.getTipodescanso().setDescripcion(nombreTipoDescanso);

                for (int i = 0; i < listTiposDescansos.size(); i++) {
                    if (listTiposDescansos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    vigenciaJornadaSeleccionada.setTipodescanso(listTiposDescansos.get(indiceUnicoElemento));
                } else {
                    permitirIndex = false;
                    context.update("form:TiposDescansosDialogo");
                    context.execute("TiposDescansosDialogo.show()");
                    tipoActualizacion = 0;
                }
            } else {
                vigenciaJornadaSeleccionada.setTipodescanso(new TiposDescansos());

                //cambiosJornada = true;

                if (guardado) {
                    guardado = false;
                    context.update("form:ACEPTAR");
                }
            }
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
        }
        if (coincidencias == 1) {
            if (!listVJCrear.contains(vigenciaJornadaSeleccionada)) {

                if (listVJModificar.isEmpty()) {
                    listVJModificar.add(vigenciaJornadaSeleccionada);
                } else if (!listVJModificar.contains(vigenciaJornadaSeleccionada)) {
                    listVJModificar.add(vigenciaJornadaSeleccionada);
                }
                //cambiosJornada = true;

                if (guardado) {
                    guardado = false;
                    context.update("form:ACEPTAR");
                }
            }
        }
        context.update("form:datosVJEmpleado");
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * Modifica los elementos de la tabla VigenciaProrrateo que no usan
     * autocomplete
     *
     * @param indice Fila donde se efectu el cambio
     */
    public void modificarVCT(VigenciasCompensaciones vCompensacionT) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listVCTCrear.contains(vigenciaTiempoSeleccionada)) {

            if (listVCTModificar.isEmpty()) {
                listVCTModificar.add(vigenciaTiempoSeleccionada);
            } else if (!listVCTModificar.contains(vigenciaTiempoSeleccionada)) {
                listVCTModificar.add(vigenciaTiempoSeleccionada);
            }
            cambiosTiempo = true;
            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
        }
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        String auxiliar = vigenciaTiempoSeleccionada.getComentario();
        vigenciaTiempoSeleccionada.setComentario(auxiliar.toUpperCase());
    }

    public void modificarFechasVCT(VigenciasCompensaciones vCompensacionT, int c) {
        cambiarIndiceVCT(vCompensacionT, c);
        modificarVCT(vCompensacionT);
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * Modifica los elementos de la tabla VigenciaProrrateoProyectos que no usan
     * autocomplete
     */
    public void modificarVCD(VigenciasCompensaciones vCompensacionD) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listVCDCrear.contains(vigenciaDineroSeleccionada)) {

            if (listVCDModificar.isEmpty()) {
                listVCDModificar.add(vigenciaDineroSeleccionada);
            } else if (!listVCDModificar.contains(vigenciaDineroSeleccionada)) {
                listVCDModificar.add(vigenciaDineroSeleccionada);
            }
            cambiosDinero = true;
            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
        }
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        String auxiliar = vigenciaDineroSeleccionada.getComentario();
        vigenciaDineroSeleccionada.setComentario(auxiliar.toUpperCase());
    }

    public void modificarFechasVCD(VigenciasCompensaciones vCompensacionD, int c) {
        cambiarIndiceVCD(vCompensacionD, c);
        modificarVCD(vCompensacionD);
    }

    /**
     * Metodo que obtiene los valores de los dialogos para realizar los
     * autocomplete de los campos (VigenciaLocalizacion)
     *
     * @param tipoNuevo Tipo de operacion: Nuevo Registro - Duplicar Registro
     * @param Campo Campo que toma el cambio de autocomplete
     */
    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("NOMBREJORNADA")) {
            if (tipoNuevo == 1) {
                nombreJornada = nuevaVigencia.getJornadatrabajo().getDescripcion();
            } else if (tipoNuevo == 2) {
                nombreJornada = duplicarVJ.getJornadatrabajo().getDescripcion();
            }
        } else if (Campo.equals("TIPODESCANSO")) {
            if (tipoNuevo == 1) {
                nombreTipoDescanso = nuevaVigencia.getTipodescanso().getDescripcion();
            } else if (tipoNuevo == 2) {
                nombreTipoDescanso = duplicarVJ.getTipodescanso().getDescripcion();
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
        if (confirmarCambio.equalsIgnoreCase("NOMBREJORNADA")) {

            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevaVigencia.getJornadatrabajo().setDescripcion(nombreJornada);
                } else if (tipoNuevo == 2) {
                    duplicarVJ.getJornadatrabajo().setDescripcion(nombreJornada);
                }
                for (int i = 0; i < listJornadasLaborales.size(); i++) {
                    if (listJornadasLaborales.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevaVigencia.setJornadatrabajo(listJornadasLaborales.get(indiceUnicoElemento));
                        context.update("formularioDialogos:nuevaJornadaLaboral");
                    } else if (tipoNuevo == 2) {
                        duplicarVJ.setJornadatrabajo(listJornadasLaborales.get(indiceUnicoElemento));
                        context.update("formularioDialogos:duplicarJornadaLaboral");
                    }
                    listJornadasLaborales.clear();
                } else {
                    context.update("form:JornadaLaboralDialogo");
                    context.execute("JornadaLaboralDialogo.show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        context.update("formularioDialogos:nuevaJornadaLaboral");
                    } else if (tipoNuevo == 2) {
                        context.update("formularioDialogos:duplicarJornadaLaboral");
                    }
                }
            } else {
                listJornadasLaborales.clear();
                if (tipoNuevo == 1) {
                    nuevaVigencia.setJornadatrabajo(new JornadasLaborales());
                    context.update("formularioDialogos:nuevaJornadaLaboral");
                } else if (tipoNuevo == 2) {
                    duplicarVJ.setJornadatrabajo(new JornadasLaborales());
                    context.update("formularioDialogos:duplicarJornadaLaboral");
                }
            }

        } else if (confirmarCambio.equalsIgnoreCase("TIPODESCANSO")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevaVigencia.getTipodescanso().setDescripcion(nombreTipoDescanso);
                } else if (tipoNuevo == 2) {
                    duplicarVJ.getTipodescanso().setDescripcion(nombreTipoDescanso);
                }
                for (int i = 0; i < listTiposDescansos.size(); i++) {
                    if (listTiposDescansos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevaVigencia.setTipodescanso(listTiposDescansos.get(indiceUnicoElemento));
                        context.update("formularioDialogos:nuevaTipoDescanso");
                    } else if (tipoNuevo == 2) {
                        duplicarVJ.setTipodescanso(listTiposDescansos.get(indiceUnicoElemento));
                        context.update("formularioDialogos:duplicarTipoDescanso");
                    }
                    listTiposDescansos.clear();
                } else {
                    context.update("form:TiposDescansosDialogo");
                    context.execute("TiposDescansosDialogo.show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        context.update("formularioDialogos:nuevaTipoDescanso");
                    } else if (tipoNuevo == 2) {
                        context.update("formularioDialogos:duplicarTipoDescanso");
                    }
                }
            } else {
                listTiposDescansos.clear();
                if (tipoNuevo == 1) {
                    nuevaVigencia.setTipodescanso(new TiposDescansos());
                    context.update("formularioDialogos:nuevaTipoDescanso");
                } else if (tipoNuevo == 2) {
                    duplicarVJ.setTipodescanso(new TiposDescansos());
                    context.update("formularioDialogos:duplicarTipoDescanso");
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
    public void cambiarIndice(VigenciasJornadas vJornada, int celda) {
        System.out.println("vigenciaJornadaSeleccionada: " + vigenciaJornadaSeleccionada);
        vigenciaJornadaSeleccionada = vJornada;
        if (permitirIndex) {
            if ((cambiosDinero == false) && (cambiosTiempo == false)) {
                cualCelda = celda;
                fechaVigenciaVJ = vigenciaJornadaSeleccionada.getFechavigencia();
                if (cualCelda == 1) {
                    activarLOV = false;
                    RequestContext.getCurrentInstance().update("form:listaValores");
                    nombreJornada = vigenciaJornadaSeleccionada.getJornadatrabajo().getDescripcion();
                } else if (cualCelda == 2) {
                    activarLOV = false;
                    RequestContext.getCurrentInstance().update("form:listaValores");
                    nombreTipoDescanso = vigenciaJornadaSeleccionada.getTipodescanso().getDescripcion();
                } else {
                    activarLOV = true;
                    RequestContext.getCurrentInstance().update("form:listaValores");
                }

                listVigenciasCompensacionesTiempo = null;
                listVigenciasCompensacionesTiempo = getListVigenciasCompensacionesTiempo();
                listVigenciasCompensacionesDinero = null;
                listVigenciasCompensacionesDinero = getListVigenciasCompensacionesDinero();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("confirmarGuardarSinSalida.show()");
            }

        }
        FacesContext c = FacesContext.getCurrentInstance();
        if (banderaVCT == 1) {
            vCTFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTFechaInicial");
            vCTFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            vCTFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTFechaFinal");
            vCTFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            vCTComentario = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTComentario");
            vCTComentario.setFilterStyle("display: none; visibility: hidden;");
            altoTabla2 = "115";
            RequestContext.getCurrentInstance().update("form:datosVigenciaCT");
            banderaVCT = 0;
            filtrarVigenciasCompensacionesTiempo = null;
            tipoListaVCT = 0;
        }
        if (banderaVCD == 1) {
            vCDComentario = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDComentario");
            vCDComentario.setFilterStyle("display: none; visibility: hidden;");
            vCDFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDFechaInicial");
            vCDFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            vCDFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDFechaFinal");
            vCDFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            altoTabla3 = "115";
            RequestContext.getCurrentInstance().update("form:datosVigenciaCD");
            banderaVCD = 0;
            filtrarVigenciasCompensacionesDinero = null;
            tipoListaVCD = 0;
        }
        vigenciaTiempoSeleccionada = null;
        vigenciaDineroSeleccionada = null;
        RequestContext.getCurrentInstance().execute("datosVigenciaCT.unselectAllRows()");
        RequestContext.getCurrentInstance().execute("datosVigenciaCD.unselectAllRows()");
        RequestContext.getCurrentInstance().update("form:datosVigenciaCT");
        RequestContext.getCurrentInstance().update("form:datosVigenciaCD");
        contarRegistrosVD();
        RequestContext.getCurrentInstance().update("form:informacionRegistroVD");
        contarRegistrosVT();
        RequestContext.getCurrentInstance().update("form:informacionRegistroVT");
    }

    /**
     * Metodo que obtiene la posicion dentro de la tabla VigenciasProrrateos
     *
     * @param indice Fila de la tabla
     * @param celda Columna de la tabla
     */
    public void cambiarIndiceVCT(VigenciasCompensaciones vCompensacionT, int celda) {
        if (permitirIndexVCT) {
            vigenciaTiempoSeleccionada = vCompensacionT;
            cualCeldaVCT = celda;
        }
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            vJFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJFechaVigencia");
            vJFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
            vJNombreJornada = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJNombreJornada");
            vJNombreJornada.setFilterStyle("display: none; visibility: hidden;");
            vJTipoDescanso = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJTipoDescanso");
            vJTipoDescanso.setFilterStyle("display: none; visibility: hidden;");
            altoTabla1 = "115";
            RequestContext.getCurrentInstance().update("form:datosVJEmpleado");
            bandera = 0;
            filtrarVJ = null;
            tipoLista = 0;
        }
        if (banderaVCD == 1) {
            vCDComentario = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDComentario");
            vCDComentario.setFilterStyle("display: none; visibility: hidden;");
            vCDFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDFechaInicial");
            vCDFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            vCDFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDFechaFinal");
            vCDFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            altoTabla3 = "115";
            banderaVCD = 0;
            filtrarVigenciasCompensacionesDinero = null;
            tipoListaVCD = 0;
        }
        vigenciaDineroSeleccionada = null;
        RequestContext.getCurrentInstance().execute("datosVigenciaCD.unselectAllRows()");
        RequestContext.getCurrentInstance().update("form:datosVigenciaCD");
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    /**
     * Metodo que obtiene la posicion dentro de la tabla
     * VigenciasProrrateosProyectos
     *
     * @param indice Fila de la tabla
     * @param celda Columna de la tabla
     */
    public void cambiarIndiceVCD(VigenciasCompensaciones vCompensacionD, int celda) {
        if (permitirIndexVCD) {
            vigenciaDineroSeleccionada = vCompensacionD;
            cualCeldaVCD = celda;
        }
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            vJFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJFechaVigencia");
            vJFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
            vJNombreJornada = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJNombreJornada");
            vJNombreJornada.setFilterStyle("display: none; visibility: hidden;");
            vJTipoDescanso = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJTipoDescanso");
            vJTipoDescanso.setFilterStyle("display: none; visibility: hidden;");
            altoTabla1 = "115";
            RequestContext.getCurrentInstance().update("form:datosVJEmpleado");
            bandera = 0;
            filtrarVJ = null;
            tipoLista = 0;
        }
        if (banderaVCT == 1) {
            vCTFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTFechaInicial");
            vCTFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            vCTFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTFechaFinal");
            vCTFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            vCTComentario = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTComentario");
            vCTComentario.setFilterStyle("display: none; visibility: hidden;");
            altoTabla2 = "115";
            RequestContext.getCurrentInstance().update("form:datosVigenciaCT");
            banderaVCT = 0;
            filtrarVigenciasCompensacionesTiempo = null;
            tipoListaVCT = 0;
        }
        vigenciaTiempoSeleccionada = null;
        RequestContext.getCurrentInstance().execute("datosVigenciaCT.unselectAllRows()");
        RequestContext.getCurrentInstance().update("form:datosVigenciaCT");
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }
    //GUARDAR

    /**
     * Metodo de guardado general para la pagina
     */
    public void guardadoGeneral() {
        if (cambiosJornada) {
            guardarCambiosVJ();
        }
        if (cambiosTiempo) {
            guardarCambiosVCT();
        }
        if (cambiosDinero) {
            guardarCambiosVCD();
        }
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:ACEPTAR");
    }

    /**
     * Metodo que guarda los cambios efectuados en la pagina
     * VigenciasLocalizaciones
     */
    public void guardarCambiosVJ() {
        if (cambiosJornada) {
            if (!listVJBorrar.isEmpty()) {
                for (int i = 0; i < listVJBorrar.size(); i++) {
                    if (listVJBorrar.get(i).getJornadatrabajo().getSecuencia() == null) {
                        listVJBorrar.get(i).setJornadatrabajo(null);
                    }
                    if (listVJBorrar.get(i).getTipodescanso().getSecuencia() == null) {
                        listVJBorrar.get(i).setTipodescanso(null);
                    }
                    administrarVigenciasJornadas.borrarVJ(listVJBorrar.get(i));
                }
                listVJBorrar.clear();
            }
            if (!listVJCrear.isEmpty()) {
                for (int i = 0; i < listVJCrear.size(); i++) {
                    if (listVJCrear.get(i).getJornadatrabajo().getSecuencia() == null) {
                        listVJCrear.get(i).setJornadatrabajo(null);
                    }
                    if (listVJCrear.get(i).getTipodescanso().getSecuencia() == null) {
                        listVJCrear.get(i).setTipodescanso(null);
                    }
                    administrarVigenciasJornadas.crearVJ(listVJCrear.get(i));
                }
                listVJCrear.clear();
            }
            if (!listVJModificar.isEmpty()) {
                administrarVigenciasJornadas.modificarVJ(listVJModificar);
                listVJModificar.clear();
            }
            listVigenciasJornadas = null;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosVJEmpleado");
            paraNuevaVJornada = 0;
            guardado = true;
            context.update("form:ACEPTAR");
            cambiosJornada = false;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos de Jornadas con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            contarRegistrosVJ();
            context.update("form:informacionRegistroVJ");

        }
        vigenciaJornadaSeleccionada = null;
    }

    /**
     * Metodo que guarda los cambios efectuados en la pagina VigenciasProrrateos
     */
    public void guardarCambiosVCT() {
        if (cambiosTiempo) {
            if (!listVCTBorrar.isEmpty()) {
                for (int i = 0; i < listVCTBorrar.size(); i++) {
                    administrarVigenciasJornadas.borrarVC(listVCTBorrar.get(i));
                }
                listVCTBorrar.clear();
            }
            if (!listVCTCrear.isEmpty()) {
                for (int i = 0; i < listVCTCrear.size(); i++) {
                    administrarVigenciasJornadas.crearVC(listVCTCrear.get(i));
                }
                listVCTCrear.clear();
            }
            if (!listVCTModificar.isEmpty()) {
                administrarVigenciasJornadas.modificarVC(listVCTModificar);
                listVCTModificar.clear();
            }
            listVigenciasCompensacionesTiempo = null;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosVigenciaCT");
            paraNuevaVJornada = 0;
            guardado = true;
            context.update("form:ACEPTAR");
            cambiosTiempo = false;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos de Descanso Tiempo con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
            contarRegistrosVT();
            context.update("form:informacionRegistroVT");
        }
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        vigenciaTiempoSeleccionada = null;
    }

    /**
     * Metodo que guarda los cambios efectuados en la pagina
     * VigenciasProrrateosProyectos
     */
    public void guardarCambiosVCD() {
        if (cambiosDinero) {
            if (!listVCDBorrar.isEmpty()) {
                for (int i = 0; i < listVCDBorrar.size(); i++) {
                    administrarVigenciasJornadas.borrarVC(listVCDBorrar.get(i));
                }
                listVCDBorrar.clear();
            }
            if (!listVCDCrear.isEmpty()) {
                for (int i = 0; i < listVCDCrear.size(); i++) {
                    administrarVigenciasJornadas.crearVC(listVCDCrear.get(i));
                }
                listVCDCrear.clear();
            }
            if (!listVCDModificar.isEmpty()) {
                administrarVigenciasJornadas.modificarVC(listVCDModificar);
                listVCDModificar.clear();
            }
            listVigenciasCompensacionesDinero = null;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosVigenciaCD");
            paraNuevaVJornada = 0;
            guardado = true;
            context.update("form:ACEPTAR");
            cambiosDinero = false;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos de Descanso Dinero con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
            contarRegistrosVD();
            context.update("form:informacionRegistroVD");
        }
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        vigenciaDineroSeleccionada = null;
    }
    //CANCELAR MODIFICACIONES

    /**
     * Cancela las modificaciones realizas en la pagina
     */
    public void cancelarModificacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            vJFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJFechaVigencia");
            vJFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
            vJNombreJornada = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJNombreJornada");
            vJNombreJornada.setFilterStyle("display: none; visibility: hidden;");
            vJTipoDescanso = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJTipoDescanso");
            vJTipoDescanso.setFilterStyle("display: none; visibility: hidden;");
            altoTabla1 = "115";
            context.update("form:datosVJEmpleado");
            bandera = 0;
            filtrarVJ = null;
            tipoLista = 0;
        }
        if (banderaVCT == 1) {
            vCTFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTFechaInicial");
            vCTFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            vCTFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTFechaFinal");
            vCTFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            vCTComentario = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTComentario");
            vCTComentario.setFilterStyle("display: none; visibility: hidden;");
            altoTabla2 = "115";
            context.update("form:datosVigenciaCT");
            banderaVCT = 0;
            filtrarVigenciasCompensacionesTiempo = null;
            tipoListaVCT = 0;
        }
        if (banderaVCD == 1) {
            vCDComentario = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDComentario");
            vCDComentario.setFilterStyle("display: none; visibility: hidden;");
            vCDFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDFechaInicial");
            vCDFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            vCDFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDFechaFinal");
            vCDFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            altoTabla3 = "115";
            context.update("form:datosVigenciaCD");
            banderaVCD = 0;
            filtrarVigenciasCompensacionesDinero = null;
            tipoListaVCD = 0;
        }
        listVJBorrar.clear();
        listVCTBorrar.clear();
        listVCDBorrar.clear();
        listVJCrear.clear();
        listVCTCrear.clear();
        listVCDCrear.clear();
        listVJModificar.clear();
        listVCTModificar.clear();
        listVCDModificar.clear();
        vigenciaJornadaSeleccionada = null;
        vigenciaTiempoSeleccionada = null;
        vigenciaDineroSeleccionada = null;
        paraNuevaVJornada = 0;
        listVigenciasJornadas = null;
        listVigenciasCompensacionesTiempo = null;
        listVigenciasCompensacionesDinero = null;
        guardado = true;
        cambiosDinero = false;
        cambiosJornada = false;
        cambiosTiempo = false;
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        getListVigenciasJornadas();
        getListVigenciasCompensacionesDinero();
        getListVigenciasCompensacionesTiempo();
        contarRegistrosVJ();
        context.update("form:informacionRegistroVJ");
        contarRegistrosVD();
        context.update("form:informacionRegistroVD");
        contarRegistrosVT();
        context.update("form:informacionRegistroVT");
        context.update("form:datosVJEmpleado");
        context.update("form:datosVigenciaCT");
        context.update("form:datosVigenciaCD");
        context.update("form:ACEPTAR");
    }

    /**
     * Cancela las modifaciones de la tabla vigencias prorrateos
     */
    public void cancelarModificacionVCT() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (banderaVCT == 1) {
            vCTFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTFechaInicial");
            vCTFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            vCTFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTFechaFinal");
            vCTFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            vCTComentario = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTComentario");
            vCTComentario.setFilterStyle("display: none; visibility: hidden;");
            altoTabla2 = "115";
            RequestContext.getCurrentInstance().update("form:datosVigenciaCT");
            banderaVCT = 0;
            filtrarVigenciasCompensacionesTiempo = null;
            tipoListaVCT = 0;
        }
        listVCTBorrar.clear();
        listVCTCrear.clear();
        listVCTModificar.clear();
        vigenciaTiempoSeleccionada = null;
        paraNuevaVJornada = 0;
        listVigenciasCompensacionesTiempo = null;
        guardado = true;
        cambiosTiempo = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosVigenciaCT");
        context.update("form:ACEPTAR");
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    /**
     * Cancela las modifaciones de la tabla vigencias prorrateos proyectos
     */
    public void cancelarModificacionVCD() {
        if (banderaVCD == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            vCDComentario = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDComentario");
            vCDComentario.setFilterStyle("display: none; visibility: hidden;");
            vCDFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDFechaInicial");
            vCDFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            vCDFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDFechaFinal");
            vCDFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            altoTabla3 = "115";
            RequestContext.getCurrentInstance().update("form:datosVigenciaCD");
            banderaVCD = 0;
            filtrarVigenciasCompensacionesDinero = null;
            tipoListaVCD = 0;
        }
        listVCDBorrar.clear();
        listVCDCrear.clear();
        listVCDModificar.clear();
        vigenciaDineroSeleccionada = null;
        paraNuevaVJornada = 0;
        listVigenciasCompensacionesDinero = null;
        guardado = true;
        cambiosDinero = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosVigenciaCD");
        context.update("form:ACEPTAR");
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    //MOSTRAR DATOS CELDA
    /**
     * Metodo que muestra los dialogos de editar con respecto a la lista real o
     * la lista filtrada y a la columna
     */
    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        //Si no hay registro selecciionado
        if (vigenciaJornadaSeleccionada == null && vigenciaDineroSeleccionada == null && vigenciaTiempoSeleccionada == null) {
            context.execute("seleccionarRegistro.show()");
        } else {
            if (vigenciaTiempoSeleccionada != null) {
                editarVCT = vigenciaTiempoSeleccionada;

                if (cualCeldaVCT == 0) {
                    context.update("formularioDialogos:editarFechaInicialVCT");
                    context.execute("editarFechaInicialVCT.show()");
                    cualCeldaVCT = -1;
                } else if (cualCeldaVCT == 1) {
                    context.update("formularioDialogos:editarFechaFinalVCT");
                    context.execute("editarFechaFinalVCT.show()");
                    cualCeldaVCT = -1;
                } else if (cualCeldaVCT == 2) {
                    context.update("formularioDialogos:editarComentarioVCT");
                    context.execute("editarComentarioVCT.show()");
                    cualCeldaVCT = -1;
                }
            } else if (vigenciaDineroSeleccionada != null) {
                editarVCD = vigenciaDineroSeleccionada;

                if (cualCeldaVCD == 0) {
                    context.update("formularioDialogos:editarFechaInicialVCD");
                    context.execute("editarFechaInicialVCD.show()");
                    cualCeldaVCD = -1;
                } else if (cualCeldaVCD == 1) {
                    context.update("formularioDialogos:editarFechaFinalVCD");
                    context.execute("editarFechaFinalVCD.show()");
                    cualCeldaVCD = -1;
                } else if (cualCeldaVCD == 2) {
                    context.update("formularioDialogos:editarComentarioVCD");
                    context.execute("editarComentarioVCD.show()");
                    cualCeldaVCD = -1;
                }
            } else if (vigenciaJornadaSeleccionada != null) {
                editarVJ = vigenciaJornadaSeleccionada;

                if (cualCelda == 0) {
                    context.update("formularioDialogos:editarFechaVigencia");
                    context.execute("editarFechaVigencia.show()");
                    cualCelda = -1;
                } else if (cualCelda == 1) {
                    context.update("formularioDialogos:editarNombreJornada");
                    context.execute("editarNombreJornada.show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    context.update("formularioDialogos:editarTipoDescanso");
                    context.execute("editarTipoDescanso.show()");
                    cualCelda = -1;
                }
            }
        }
    }
    //CREAR VL

    /**
     * Metodo que se encarga de agregar un nueva VigenciasLocalizaciones
     */
    public void validarCualTabla(int tabla) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tabla == 1) {
            //Dialogo de nuevo registro vigencias localizaciones
            context.update("form:NuevoRegistroVJ");
            context.execute("NuevoRegistroVJ.show()");
        }
        if (tabla == 2) {
            if (vigenciaJornadaSeleccionada != null) {
                nuevaVigenciaCT = new VigenciasCompensaciones();
                context.update("form:NuevoRegistroVCT");
                context.execute("NuevoRegistroVCT.show()");
            } else {
                context.execute("seleccionarRegistroJT.show()");
            }
        }
        if (tabla == 3) {
            if (vigenciaJornadaSeleccionada != null) {
                nuevaVigenciaCD = new VigenciasCompensaciones();
                context.update("form:NuevoRegistroVCD");
                context.execute("NuevoRegistroVCD.show()");
            } else {
                context.execute("seleccionarRegistroJT.show()");
            }
        }
    }

    public void agregarNuevaVJ() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevaVigencia.getFechavigencia() != null && nuevaVigencia.getJornadatrabajo().getSecuencia() != null) {
            int cont = 0;
            mensajeValidacion = "";
            for (int j = 0; j < listVigenciasJornadas.size(); j++) {
                if (nuevaVigencia.getFechavigencia().equals(listVigenciasJornadas.get(j).getFechavigencia())) {
                    cont++;
                }
            }
            if (cont > 0) {
                mensajeValidacion = "FECHAS NO REPETIDAS";
                context.update("form:validarNuevoFechas");
                context.execute("validarNuevoFechas.show()");
            } else {
                if (validarFechasRegistroVJ(1)) {
                    cambiosJornada = true;
                    if (bandera == 1) {
                        //CERRAR FILTRADO
                        FacesContext c = FacesContext.getCurrentInstance();
                        vJFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJFechaVigencia");
                        vJFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
                        vJNombreJornada = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJNombreJornada");
                        vJNombreJornada.setFilterStyle("display: none; visibility: hidden;");
                        vJTipoDescanso = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJTipoDescanso");
                        vJTipoDescanso.setFilterStyle("display: none; visibility: hidden;");
                        altoTabla1 = "115";
                        RequestContext.getCurrentInstance().update("form:datosVJEmpleado");
                        bandera = 0;
                        filtrarVJ = null;
                        tipoLista = 0;
                    }
                    //AGREGAR REGISTRO A LA LISTA VIGENCIAS 
                    paraNuevaVJornada++;
                    nuevaVJornadaSecuencia = BigInteger.valueOf(paraNuevaVJornada);
                    nuevaVigencia.setSecuencia(nuevaVJornadaSecuencia);
                    nuevaVigencia.setEmpleado(empleado);
                    listVJCrear.add(nuevaVigencia);
                    listVigenciasJornadas.add(nuevaVigencia);
                    modificarInfoRegistroVJ(listVigenciasJornadas.size());
                    vigenciaJornadaSeleccionada = listVigenciasJornadas.get(listVigenciasJornadas.size() - 1);
                    activarLOV = true;
                    RequestContext.getCurrentInstance().update("form:listaValores");
                    context.update("form:informacionRegistroVJ");
                    context.update("form:datosVJEmpleado");
                    if (guardado) {
                        guardado = false;
                        context.update("form:ACEPTAR");
                    }
                    context.execute("NuevoRegistroVJ.hide()");
                    nuevaVigencia = new VigenciasJornadas();
                    nuevaVigencia.setJornadatrabajo(new JornadasLaborales());
                    nuevaVigencia.setTipodescanso(new TiposDescansos());
                } else {
                    context.execute("errorFechas.show()");
                }
            }
        } else {
            context.execute("negacionNuevaVJ.show()");
        }
    }
    //LIMPIAR NUEVO REGISTRO

    /**
     * Metodo que limpia las casillas de la nueva vigencia
     */
    public void limpiarNuevaVJ() {
        nuevaVigencia = new VigenciasJornadas();
        nuevaVigencia.setJornadatrabajo(new JornadasLaborales());
        nuevaVigencia.setTipodescanso(new TiposDescansos());
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * Agrega una nueva Vigencia Prorrateo
     */
    public void agregarNuevaVCT() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevaVigenciaCT.getFechafinal() != null && nuevaVigenciaCT.getFechainicial() != null) {
            if (nuevaVigenciaCT.getFechafinal().after(nuevaVigenciaCT.getFechainicial())) {
                int cont = 0;
                mensajeValidacion = "";
                for (int j = 0; j < listVigenciasCompensacionesTiempo.size(); j++) {
                    if (nuevaVigenciaCT.getFechainicial().equals(listVigenciasCompensacionesTiempo.get(j).getFechainicial())) {
                        cont++;
                    }
                }
                if (cont > 0) {
                    mensajeValidacion = "FECHAS INICIALES NO REPETIDAS";
                    context.update("form:validarNuevoFechas");
                    context.execute("validarNuevoFechas.show()");
                } else {
                    //CERRAR FILTRADO
                    cambiosTiempo = true;
                    if (banderaVCT == 1) {
                        FacesContext c = FacesContext.getCurrentInstance();
                        vCTFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTFechaInicial");
                        vCTFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                        vCTFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTFechaFinal");
                        vCTFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                        vCTComentario = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTComentario");
                        vCTComentario.setFilterStyle("display: none; visibility: hidden;");
                        altoTabla2 = "115";
                        RequestContext.getCurrentInstance().update("form:datosVigenciaCT");
                        banderaVCT = 0;
                        filtrarVigenciasCompensacionesTiempo = null;
                        tipoListaVCT = 0;
                    }
                    //AGREGAR REGISTRO A LA LISTA VIGENCIAS
                    paraNuevaVJornada++;
                    nuevaVJornadaSecuencia = BigInteger.valueOf(paraNuevaVJornada);
                    nuevaVigenciaCT.setSecuencia(nuevaVJornadaSecuencia);
                    nuevaVigenciaCT.setVigenciajornada(listVigenciasJornadas.get(indexAuxVJ));
                    nuevaVigenciaCT.setTipocompensacion("DESCANSO");
                    nuevaVigenciaCT.setNovedadturnorotativo(null);
                    if (nuevaVigenciaCT.getComentario() != null) {
                        String auxiliar = nuevaVigenciaCT.getComentario();
                        nuevaVigenciaCT.setComentario(auxiliar.toUpperCase());
                    }
                    listVCTCrear.add(nuevaVigenciaCT);
                    if (listVigenciasCompensacionesTiempo == null) {
                        listVigenciasCompensacionesTiempo = new ArrayList<VigenciasCompensaciones>();
                    }
                    listVigenciasCompensacionesTiempo.add(nuevaVigenciaCT);
                    vigenciaTiempoSeleccionada = listVigenciasCompensacionesTiempo.get(listVigenciasCompensacionesTiempo.size() - 1);
                    activarLOV = true;
                    RequestContext.getCurrentInstance().update("form:listaValores");
                    modificarInfoRegistroVT(listVigenciasCompensacionesTiempo.size());
                    context.update("form:informacionRegistroVT");
                    nuevaVigenciaCT = new VigenciasCompensaciones();
                    context.execute("NuevoRegistroVCT.hide();");
                    context.update("form:datosVigenciaCT");
                    if (guardado) {
                        guardado = false;
                        context.update("form:ACEPTAR");
                    }
                }
            } else {
                context.execute("errorFechaDescanso.show()");
            }

        } else {
            context.execute("errorNullDescanso.show()");
        }
    }

    /**
     * Limpia los elementos de una nueva vigencia prorrateo
     */
    public void limpiarNuevaVCT() {
        nuevaVigenciaCT = new VigenciasCompensaciones();
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * Agrega una nueva vigencia prorrateo proyecto
     */
    public void agregarNuevaVCD() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevaVigenciaCD.getFechafinal() != null && nuevaVigenciaCD.getFechainicial() != null) {
            if (nuevaVigenciaCD.getFechafinal().after(nuevaVigenciaCD.getFechainicial())) {
                int cont = 0;
                mensajeValidacion = "";
                for (int j = 0; j < listVigenciasCompensacionesDinero.size(); j++) {
                    if (nuevaVigenciaCD.getFechainicial().equals(listVigenciasCompensacionesDinero.get(j).getFechainicial())) {
                        cont++;
                    }
                }
                if (cont > 0) {
                    mensajeValidacion = "FECHAS INICIALES NO REPETIDAS";
                    context.update("form:validarNuevoFechas");
                    context.execute("validarNuevoFechas.show()");
                } else {
                    cambiosDinero = true;
                    if (banderaVCT == 1) {
                        FacesContext c = FacesContext.getCurrentInstance();
                        vCDComentario = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDComentario");
                        vCDComentario.setFilterStyle("display: none; visibility: hidden;");
                        vCDFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDFechaInicial");
                        vCDFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                        vCDFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDFechaFinal");
                        vCDFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                        altoTabla3 = "115";
                        RequestContext.getCurrentInstance().update("form:datosVigenciaCD");
                        banderaVCD = 0;
                        filtrarVigenciasCompensacionesDinero = null;
                        tipoListaVCD = 0;
                    }
                    //AGREGAR REGISTRO A LA LISTA VIGENCIAS 
                    paraNuevaVJornada++;
                    nuevaVJornadaSecuencia = BigInteger.valueOf(paraNuevaVJornada);
                    nuevaVigenciaCD.setSecuencia(nuevaVJornadaSecuencia);
                    nuevaVigenciaCD.setVigenciajornada(listVigenciasJornadas.get(indexAuxVJ));
                    nuevaVigenciaCD.setTipocompensacion("DINERO");
                    nuevaVigenciaCD.setNovedadturnorotativo(null);
                    if (nuevaVigenciaCD.getComentario() != null) {
                        String auxiliar = nuevaVigenciaCD.getComentario();
                        nuevaVigenciaCD.setComentario(auxiliar.toUpperCase());
                    }
                    listVCDCrear.add(nuevaVigenciaCD);
                    if (listVigenciasCompensacionesDinero == null) {
                        listVigenciasCompensacionesDinero = new ArrayList<VigenciasCompensaciones>();
                    }
                    listVigenciasCompensacionesDinero.add(nuevaVigenciaCD);
                    vigenciaDineroSeleccionada = null;
                    vigenciaDineroSeleccionada = listVigenciasCompensacionesDinero.get(listVigenciasCompensacionesDinero.size() - 1);
                    activarLOV = true;
                    RequestContext.getCurrentInstance().update("form:listaValores");
                    modificarInfoRegistroVD(listVigenciasCompensacionesDinero.size());
                    context.update("form:informacionRegistroVD");
                    nuevaVigenciaCT = new VigenciasCompensaciones();
                    context.update("form:datosVigenciaCD");
                    context.execute("NuevoRegistroVCD.hide();");
                    if (guardado) {
                        guardado = false;
                        context.update("form:ACEPTAR");
                    }
                }
            } else {
                context.execute("errorFechaDescanso.show()");
            }
        } else {
            context.execute("errorNullDescanso.show()");
        }
    }

    /**
     * Elimina los datos de una nueva vigencia prorrateo proyecto
     */
    public void limpiarNuevaVCD() {
        nuevaVigenciaCD = new VigenciasCompensaciones();
    }
    //DUPLICAR VL

    /**
     * Metodo que verifica que proceso de duplicar se genera con respecto a la
     * posicion en la pagina que se tiene
     */
    public void verificarDuplicarVigencia() {
        RequestContext context = RequestContext.getCurrentInstance();
        //Si no hay registro selecciionado
        if (vigenciaJornadaSeleccionada == null && vigenciaDineroSeleccionada == null && vigenciaTiempoSeleccionada == null) {
            context.execute("seleccionarRegistro.show()");
        } else {
            if (vigenciaTiempoSeleccionada != null) {
                duplicarVigenciaCT();
            } else if (vigenciaDineroSeleccionada != null) {
                duplicarVigenciaCD();
            } else if (vigenciaJornadaSeleccionada != null) {
                duplicarVigenciaJ();
            }
        }
    }

    /**
     * Duplica una nueva vigencia localizacion
     */
    public void duplicarVigenciaJ() {
        if (vigenciaJornadaSeleccionada != null) {
            duplicarVJ = new VigenciasJornadas();
            paraNuevaVJornada++;
            nuevaVJornadaSecuencia = BigInteger.valueOf(paraNuevaVJornada);

            duplicarVJ.setEmpleado(vigenciaJornadaSeleccionada.getEmpleado());
            duplicarVJ.setFechavigencia(vigenciaJornadaSeleccionada.getFechavigencia());
            duplicarVJ.setJornadatrabajo(vigenciaJornadaSeleccionada.getJornadatrabajo());
            duplicarVJ.setTipodescanso(vigenciaJornadaSeleccionada.getTipodescanso());

            if (duplicarVJ.getJornadatrabajo() == null) {
                duplicarVJ.setJornadatrabajo(new JornadasLaborales());
            }
            if (duplicarVJ.getTipodescanso() == null) {
                duplicarVJ.setTipodescanso(new TiposDescansos());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:duplicarVJ");
            context.execute("DuplicarRegistroVJ.show()");
        }
    }

    /**
     * Metodo que confirma el duplicado y actualiza los datos de la tabla
     * VigenciasLocalizaciones
     */
    public void confirmarDuplicar() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (duplicarVJ.getFechavigencia() != null && duplicarVJ.getJornadatrabajo().getSecuencia() != null) {
            int cont = 0;
            mensajeValidacion = "";
            for (int j = 0; j < listVigenciasJornadas.size(); j++) {
                if (duplicarVJ.getFechavigencia().equals(listVigenciasJornadas.get(j).getFechavigencia())) {
                    cont++;
                }
            }
            if (cont > 0) {
                mensajeValidacion = "FECHAS NO REPETIDAS";
                context.update("form:validarNuevoFechas");
                context.execute("validarNuevoFechas.show()");
            } else {
                if (validarFechasRegistroVJ(2)) {
                    paraNuevaVJornada++;
                    nuevaVJornadaSecuencia = BigInteger.valueOf(paraNuevaVJornada);
                    duplicarVJ.setSecuencia(nuevaVJornadaSecuencia);
                    cambiosJornada = true;
                    listVigenciasJornadas.add(duplicarVJ);
                    listVJCrear.add(duplicarVJ);
                    vigenciaJornadaSeleccionada = listVigenciasJornadas.get(listVigenciasJornadas.size() - 1);
                    activarLOV = true;
                    RequestContext.getCurrentInstance().update("form:listaValores");
                    modificarInfoRegistroVJ(listVigenciasJornadas.size());
                    context.update("form:informacionRegistroVJ");
                    context.update("form:datosVJEmpleado");
                    context.execute("DuplicarRegistroVJ.hide()");
                    if (guardado) {
                        guardado = false;
                        context.update("form:ACEPTAR");
                    }
                    if (bandera == 1) {
                        //CERRAR FILTRADO
                        FacesContext c = FacesContext.getCurrentInstance();
                        vJFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJFechaVigencia");
                        vJFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
                        vJNombreJornada = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJNombreJornada");
                        vJNombreJornada.setFilterStyle("display: none; visibility: hidden;");
                        vJTipoDescanso = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJTipoDescanso");
                        vJTipoDescanso.setFilterStyle("display: none; visibility: hidden;");
                        altoTabla1 = "115";
                        RequestContext.getCurrentInstance().update("form:datosVJEmpleado");
                        bandera = 0;
                        filtrarVJ = null;
                        tipoLista = 0;
                    }
                    duplicarVJ = new VigenciasJornadas();
                } else {
                    context.execute("errorFechas.show()");
                }
            }
        } else {
            context.execute("negacionNuevaVJ.show()");

        }
    }
//LIMPIAR DUPLICAR

    /**
     * Metodo que limpia los datos de un duplicar Vigencia Localizacion
     */
    public void limpiarduplicarVJ() {
        duplicarVJ = new VigenciasJornadas();
        duplicarVJ.setJornadatrabajo(new JornadasLaborales());
        duplicarVJ.setTipodescanso(new TiposDescansos());
    }

    ///////////////////////////////////////////////////////////////
    /**
     * Duplica una registro de VigenciaProrrateos
     */
    public void duplicarVigenciaCT() {
        if (vigenciaTiempoSeleccionada != null) {
            duplicarVCT = new VigenciasCompensaciones();
            paraNuevaVJornada++;
            nuevaVJornadaSecuencia = BigInteger.valueOf(paraNuevaVJornada);

            duplicarVCT.setSecuencia(nuevaVJornadaSecuencia);
            duplicarVCT.setVigenciajornada(vigenciaTiempoSeleccionada.getVigenciajornada());
            duplicarVCT.setTipocompensacion(vigenciaTiempoSeleccionada.getTipocompensacion());
            duplicarVCT.setFechainicial(vigenciaTiempoSeleccionada.getFechainicial());
            duplicarVCT.setFechafinal(vigenciaTiempoSeleccionada.getFechafinal());
            duplicarVCT.setComentario(vigenciaTiempoSeleccionada.getComentario());

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:duplicadoVCT");
            context.execute("DuplicadoRegistroVCT.show()");
        }
    }

    /**
     * Metodo que confirma el duplicado y actualiza los datos de la tabla
     * VigenciaProrrateo
     */
    public void confirmarDuplicarVCT() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (duplicarVCT.getFechafinal() != null && duplicarVCT.getFechainicial() != null) {
            if (duplicarVCT.getFechafinal().after(duplicarVCT.getFechainicial())) {
                int cont = 0;
                mensajeValidacion = "";
                for (int j = 0; j < listVigenciasCompensacionesTiempo.size(); j++) {
                    if (duplicarVCT.getFechainicial().equals(listVigenciasCompensacionesTiempo.get(j).getFechainicial())) {
                        cont++;
                    }
                }
                if (cont > 0) {
                    mensajeValidacion = "FECHAS INICIALES NO REPETIDAS";
                    context.update("form:validarNuevoFechas");
                    context.execute("validarNuevoFechas.show()");
                } else {
                    cambiosTiempo = true;
                    if (duplicarVCT.getComentario() != null) {
                        String auxiliar = duplicarVCT.getComentario();
                        duplicarVCT.setComentario(auxiliar.toUpperCase());
                    }
                    listVigenciasCompensacionesTiempo.add(duplicarVCT);
                    listVCTCrear.add(duplicarVCT);
                    activarLOV = true;
                    RequestContext.getCurrentInstance().update("form:listaValores");
                    modificarInfoRegistroVT(listVigenciasCompensacionesTiempo.size());
                    context.update("form:informacionRegistroVT");
                    context.update("form:datosVigenciaCT");
                    vigenciaTiempoSeleccionada = listVigenciasCompensacionesTiempo.get(listVigenciasCompensacionesTiempo.size() - 1);
                    if (guardado) {
                        guardado = false;
                        context.update("form:ACEPTAR");
                    }
                    if (banderaVCT == 1) {
                        //CERRAR FILTRADO
                        FacesContext c = FacesContext.getCurrentInstance();
                        vCTFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTFechaInicial");
                        vCTFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                        vCTFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTFechaFinal");
                        vCTFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                        vCTComentario = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTComentario");
                        vCTComentario.setFilterStyle("display: none; visibility: hidden;");
                        altoTabla2 = "115";
                        RequestContext.getCurrentInstance().update("form:datosVigenciaCT");
                        banderaVCT = 0;
                        filtrarVigenciasCompensacionesTiempo = null;
                        tipoListaVCT = 0;
                    }
                    context.execute("DuplicadoRegistroVCT.hide()");
                    duplicarVCT = new VigenciasCompensaciones();
                }
            } else {
                context.execute("errorFechaDescanso.show()");
            }
        } else {
            context.execute("errorNullDescanso.show()");
        }
    }

    /**
     * Limpia los elementos del duplicar registro Vigencia Prorrateo
     */
    public void limpiarduplicarVCT() {
        duplicarVCT = new VigenciasCompensaciones();
    }

    ///////////////////////////////////////////////////////////////
    /**
     * Duplica un registo Vigencia Prorrateo Proyecto
     */
    public void duplicarVigenciaCD() {
        if (vigenciaDineroSeleccionada != null) {
            duplicarVCD = new VigenciasCompensaciones();
            paraNuevaVJornada++;
            nuevaVJornadaSecuencia = BigInteger.valueOf(paraNuevaVJornada);

            duplicarVCD.setSecuencia(nuevaVJornadaSecuencia);
            duplicarVCD.setVigenciajornada(vigenciaDineroSeleccionada.getVigenciajornada());
            duplicarVCD.setTipocompensacion(vigenciaDineroSeleccionada.getTipocompensacion());
            duplicarVCD.setFechafinal(vigenciaDineroSeleccionada.getFechafinal());
            duplicarVCD.setFechainicial(vigenciaDineroSeleccionada.getFechainicial());
            duplicarVCD.setComentario(vigenciaDineroSeleccionada.getComentario());

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:duplicarVCD");
            context.execute("DuplicarRegistroVCD.show()");
        }
    }

    /**
     * Metodo que confirma el duplicado y actualiza los datos de la tabla
     * Vigencia Prorrateo Proyecto
     */
    public void confirmarDuplicarVCD() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (duplicarVCD.getFechafinal() != null && duplicarVCD.getFechainicial() != null) {
            if (duplicarVCD.getFechafinal().after(duplicarVCD.getFechainicial())) {
                int cont = 0;
                mensajeValidacion = "";
                for (int j = 0; j < listVigenciasCompensacionesDinero.size(); j++) {
                    if (duplicarVCD.getFechainicial().equals(listVigenciasCompensacionesDinero.get(j).getFechainicial())) {
                        cont++;
                    }
                }
                if (cont > 0) {
                    mensajeValidacion = "FECHAS INICIALES NO REPETIDAS";
                    context.update("form:validarNuevoFechas");
                    context.execute("validarNuevoFechas.show()");
                } else {
                    cambiosDinero = true;
                    if (duplicarVCD.getComentario() != null) {
                        String auxiliar = duplicarVCD.getComentario();
                        duplicarVCD.setComentario(auxiliar.toUpperCase());
                    }
                    listVigenciasCompensacionesDinero.add(duplicarVCD);
                    listVCDCrear.add(duplicarVCD);
                    vigenciaDineroSeleccionada = listVigenciasCompensacionesDinero.get(listVigenciasCompensacionesDinero.size() - 1);
                    activarLOV = true;
                    RequestContext.getCurrentInstance().update("form:listaValores");
                    modificarInfoRegistroVD(listVigenciasCompensacionesDinero.size());
                    context.update("form:informacionRegistroVD");
                    context.update("form:datosVigenciaCD");
                    if (guardado) {
                        guardado = false;
                        context.update("form:ACEPTAR");
                    }
                    if (banderaVCD == 1) {
                        //CERRAR FILTRADO
                        FacesContext c = FacesContext.getCurrentInstance();
                        vCDComentario = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDComentario");
                        vCDComentario.setFilterStyle("display: none; visibility: hidden;");
                        vCDFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDFechaInicial");
                        vCDFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                        vCDFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDFechaFinal");
                        vCDFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                        altoTabla3 = "115";
                        RequestContext.getCurrentInstance().update("form:datosVigenciaCD");
                        banderaVCD = 0;
                        filtrarVigenciasCompensacionesDinero = null;
                        tipoListaVCD = 0;
                    }
                    context.execute("DuplicarRegistroVCD.hide()");
                    duplicarVCD = new VigenciasCompensaciones();
                }
            } else {
                context.execute("errorFechaDescanso.show()");
            }
        } else {
            context.execute("errorNullDescanso.show()");
        }
    }

    /**
     * Limpia el registro de duplicar Vigencia Prorrateo Proyecto
     */
    public void limpiarduplicarVCD() {
        duplicarVCD = new VigenciasCompensaciones();
    }

    /**
     * Valida que registro se elimina de que tabla con respecto a la posicion en
     * la pagina
     */
    public void validarBorradoVigencia() {
        RequestContext context = RequestContext.getCurrentInstance();
        //Si no hay registro selecciionado
        if (vigenciaJornadaSeleccionada == null && vigenciaDineroSeleccionada == null && vigenciaTiempoSeleccionada == null) {
            context.execute("seleccionarRegistro.show()");
        } else {
            if (vigenciaTiempoSeleccionada != null) {
                borrarVCT();
            } else if (vigenciaDineroSeleccionada != null) {
                borrarVCD();
            } else if (vigenciaJornadaSeleccionada != null) {
                int tam1 = 0;
                if (listVigenciasCompensacionesDinero != null) {
                    tam1 = listVigenciasCompensacionesDinero.size();
                }
                int tam2 = 0;
                if (listVigenciasCompensacionesTiempo != null) {
                    tam2 = listVigenciasCompensacionesTiempo.size();
                }
                if (tam1 == 0 && tam2 == 0) {
                    borrarVJ();
                } else {
                    context.update("form:negacionBorradoVJ");
                    context.execute("negacionBorradoVJ.show()");
                }
            }
        }
    }

    //BORRAR VL
    /**
     * Metodo que borra una vigencia localizacion
     */
    public void borrarVJ() {
        if (vigenciaJornadaSeleccionada != null) {
            cambiosJornada = true;
            if (!listVJModificar.isEmpty() && listVJModificar.contains(vigenciaJornadaSeleccionada)) {
                int modIndex = listVJModificar.indexOf(vigenciaJornadaSeleccionada);
                listVJModificar.remove(modIndex);
                listVJBorrar.add(vigenciaJornadaSeleccionada);
            } else if (!listVJCrear.isEmpty() && listVJCrear.contains(vigenciaJornadaSeleccionada)) {
                int crearIndex = listVJCrear.indexOf(vigenciaJornadaSeleccionada);
                listVJCrear.remove(crearIndex);
            } else {
                listVJBorrar.add(vigenciaJornadaSeleccionada);
            }
            listVigenciasJornadas.remove(vigenciaJornadaSeleccionada);
            if (tipoLista == 1) {
                filtrarVJ.remove(vigenciaJornadaSeleccionada);
            }
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            modificarInfoRegistroVJ(listVigenciasJornadas.size());
            RequestContext.getCurrentInstance().update("form:informacionRegistroVJ");


            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosVJEmpleado");
            vigenciaJornadaSeleccionada = null;

            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
        }
    }

    /**
     * Metodo que borra una vigencia prorrateo
     */
    public void borrarVCT() {
        if (vigenciaTiempoSeleccionada != null) {
            cambiosTiempo = true;
            if (!listVCTModificar.isEmpty() && listVCTModificar.contains(vigenciaTiempoSeleccionada)) {
                int modIndex = listVCTModificar.indexOf(vigenciaTiempoSeleccionada);
                listVCTModificar.remove(modIndex);
                listVCTBorrar.add(vigenciaTiempoSeleccionada);
            } else if (!listVCTCrear.isEmpty() && listVCTCrear.contains(vigenciaTiempoSeleccionada)) {
                int crearIndex = listVCTCrear.indexOf(vigenciaTiempoSeleccionada);
                listVCTCrear.remove(crearIndex);
            } else {
                listVCTBorrar.add(vigenciaTiempoSeleccionada);
            }
            listVigenciasCompensacionesTiempo.remove(vigenciaTiempoSeleccionada);
            if (tipoListaVCT == 1) {
                filtrarVigenciasCompensacionesTiempo.remove(vigenciaTiempoSeleccionada);
            }
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            modificarInfoRegistroVT(listVigenciasCompensacionesTiempo.size());
            RequestContext.getCurrentInstance().update("form:informacionRegistroVT");
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosVigenciaCT");
            vigenciaTiempoSeleccionada = null;
            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
        }
    }

    /**
     * Metodo que borra una vigencia prorrateo proyecto
     */
    public void borrarVCD() {
        if (vigenciaDineroSeleccionada != null) {
            cambiosDinero = true;
            if (!listVCDModificar.isEmpty() && listVCDModificar.contains(vigenciaDineroSeleccionada)) {
                int modIndex = listVCDModificar.indexOf(vigenciaDineroSeleccionada);
                listVCDModificar.remove(modIndex);
                listVCDBorrar.add(vigenciaDineroSeleccionada);
            } else if (!listVCDCrear.isEmpty() && listVCDCrear.contains(vigenciaDineroSeleccionada)) {
                int crearIndex = listVCDCrear.indexOf(vigenciaDineroSeleccionada);
                listVCDCrear.remove(crearIndex);
            } else {
                listVCDBorrar.add(vigenciaDineroSeleccionada);
            }
            listVigenciasCompensacionesDinero.remove(vigenciaDineroSeleccionada);
            if (tipoListaVCD == 1) {
                filtrarVigenciasCompensacionesDinero.remove(vigenciaDineroSeleccionada);
            }
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            modificarInfoRegistroVD(listVigenciasCompensacionesDinero.size());
            RequestContext.getCurrentInstance().update("form:informacionRegistroVD");

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosVigenciaCD");
            vigenciaDineroSeleccionada = null;
            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
        }
    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    /**
     * Metodo que activa el filtrado por medio de la opcion en el toolbar o por
     * medio de la tecla Crtl+F11
     */
    public void activarCtrlF11() {
        if (vigenciaJornadaSeleccionada != null) {
            filtradoVigenciaJornada();
        }
        if (vigenciaTiempoSeleccionada != null) {
            filtradoVigenciaCompensacionTiempo();
        }
        if (vigenciaDineroSeleccionada != null) {
            filtradoVigenciaCompensacionDinero();
        }
    }

    /**
     * Metodo que acciona el filtrado de la tabla vigencia localizacion
     */
    public void filtradoVigenciaJornada() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (vigenciaJornadaSeleccionada != null) {
            if (bandera == 0) {
                vJFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJFechaVigencia");
                vJFechaVigencia.setFilterStyle("width: 86%");
                vJNombreJornada = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJNombreJornada");
                vJNombreJornada.setFilterStyle("width: 86%");
                vJTipoDescanso = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJTipoDescanso");
                vJTipoDescanso.setFilterStyle("width: 86%");
                altoTabla1 = "91";
                RequestContext.getCurrentInstance().update("form:datosVJEmpleado");
                bandera = 1;
            } else if (bandera == 1) {
                vJFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJFechaVigencia");
                vJFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
                vJNombreJornada = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJNombreJornada");
                vJNombreJornada.setFilterStyle("display: none; visibility: hidden;");
                vJTipoDescanso = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJTipoDescanso");
                vJTipoDescanso.setFilterStyle("display: none; visibility: hidden;");
                altoTabla1 = "115";
                RequestContext.getCurrentInstance().update("form:datosVJEmpleado");
                bandera = 0;
                filtrarVJ = null;
                tipoLista = 0;
            }
        }
    }

    /**
     * Metodo que acciona el filtrado de la tabla vigencia prorrateo
     */
    public void filtradoVigenciaCompensacionTiempo() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (vigenciaTiempoSeleccionada != null) {
            if (banderaVCT == 0) {
                //Columnas Tabla VPP
                vCTFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTFechaInicial");
                vCTFechaInicial.setFilterStyle("width: 86%");
                vCTFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTFechaFinal");
                vCTFechaFinal.setFilterStyle("width: 86%");
                vCTComentario = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTComentario");
                vCTComentario.setFilterStyle("width: 86%");
                altoTabla2 = "91";
                RequestContext.getCurrentInstance().update("form:datosVigenciaCT");
                banderaVCT = 1;
            } else if (banderaVCT == 1) {
                vCTFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTFechaInicial");
                vCTFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                vCTFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTFechaFinal");
                vCTFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                vCTComentario = (Column) c.getViewRoot().findComponent("form:datosVigenciaCT:vCTComentario");
                vCTComentario.setFilterStyle("display: none; visibility: hidden;");
                altoTabla2 = "115";
                RequestContext.getCurrentInstance().update("form:datosVigenciaCT");
                banderaVCT = 0;
                filtrarVigenciasCompensacionesTiempo = null;
                tipoListaVCT = 0;
            }
        }
    }

    /**
     * Metodo que acciona el filtrado de la tabla vigencia prorrateo proyecto
     */
    public void filtradoVigenciaCompensacionDinero() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (vigenciaDineroSeleccionada != null) {
            //Columnas Tabla VPP
            if (banderaVCD == 0) {
                vCDComentario = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDComentario");
                vCDComentario.setFilterStyle("width: 86%");
                vCDFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDFechaInicial");
                vCDFechaInicial.setFilterStyle("width: 86%");
                vCDFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDFechaFinal");
                vCDFechaFinal.setFilterStyle("width: 86%");
                altoTabla3 = "91";
                RequestContext.getCurrentInstance().update("form:datosVigenciaCD");
                banderaVCD = 1;
            } else if (banderaVCD == 1) {
                vCDComentario = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDComentario");
                vCDComentario.setFilterStyle("display: none; visibility: hidden;");
                vCDFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDFechaInicial");
                vCDFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                vCDFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaCD:vCDFechaFinal");
                vCDFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                altoTabla3 = "115";
                RequestContext.getCurrentInstance().update("form:datosVigenciaCD");
                banderaVCD = 0;
                filtrarVigenciasCompensacionesDinero = null;
                tipoListaVCD = 0;
            }
        }
    }

    //SALIR
    /**
     * Metodo que cierra la sesion y limpia los datos en la pagina
     */
    public void salir() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            vJFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJFechaVigencia");
            vJFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
            vJNombreJornada = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJNombreJornada");
            vJNombreJornada.setFilterStyle("display: none; visibility: hidden;");
            vJTipoDescanso = (Column) c.getViewRoot().findComponent("form:datosVJEmpleado:vJTipoDescanso");
            vJTipoDescanso.setFilterStyle("display: none; visibility: hidden;");
            altoTabla1 = "115";
            context.update("form:datosVJEmpleado");
            bandera = 0;
            filtrarVJ = null;
            tipoLista = 0;
        }
        listVJBorrar.clear();
        listVJCrear.clear();
        listVJModificar.clear();
        vigenciaJornadaSeleccionada = null;
        vigenciaTiempoSeleccionada = null;
        vigenciaDineroSeleccionada = null;
        paraNuevaVJornada = 0;
        listVigenciasJornadas = null;
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
     * @param tt Tipo Tabla
     *
     */
    public void asignarIndex(int dlg, int LND, int tt) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tt == 0) {
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dlg == 0) { //getInfoRegistroJornadaLaboral();
                contarRegistrosJornadaL();
                context.update("form:JornadaLaboralDialogo");
                context.execute("JornadaLaboralDialogo.show()");
            } else if (dlg == 1) {
                //getInfoRegistroTipoDescanso(); contarRegistrosTipoD();
                context.update("form:TiposDescansosDialogo");
                context.execute("TiposDescansosDialogo.show()");
            }
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
        }
        contarRegistrosVD();
        context.update("form:informacionRegistroVD");
        contarRegistrosVT();
        context.update("form:informacionRegistroVT");
    }

    //LOVS //Estructuras /** Metodo que actualiza la estructura seleccionada
    public void actualizarJornadaLaboral() {

        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {//Si Se trabaja sobre la tabla y no en un dialogo
            //cambiosJornada = true;
            vigenciaJornadaSeleccionada.setJornadatrabajo(jornadaLaboralSeleccionada);
            if (!listVJCrear.contains(vigenciaJornadaSeleccionada)) {
                if (listVJModificar.isEmpty()) {
                    listVJModificar.add(vigenciaJornadaSeleccionada);
                } else if (!listVJModificar.contains(vigenciaJornadaSeleccionada)) {
                    listVJModificar.add(vigenciaJornadaSeleccionada);
                }
            }

            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
            context.update("form:datosVJEmpleado");
            permitirIndex = true;
            //cambiosJornada = true;
        } else if (tipoActualizacion == 1) {//Para crear un registro
            nuevaVigencia.setJornadatrabajo(jornadaLaboralSeleccionada);
            context.update("formularioDialogos:nuevaVJ");
        } else if (tipoActualizacion == 2) {//Para duplicar un registro
            duplicarVJ.setJornadatrabajo(jornadaLaboralSeleccionada);
            context.update("formularioDialogos:duplicarVJ");
        }
        filtrarJornadasLaborales = null;
        jornadaLaboralSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;

        context.reset("form:lovJornadaLaboral:globalFilter");
        context.execute("lovJornadaLaboral.clearFilters()");
    }

    /**
     * Metodo que cancela los cambios sobre estructura (vigencia localizacion)
     */
    public void cancelarCambioJornadaLaboral() {
        filtrarJornadasLaborales = null;
        jornadaLaboralSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovJornadaLaboral:globalFilter");
        context.execute("lovJornadaLaboral.clearFilters()");
        context.execute("JornadaLaboralDialogo.hide()");
    }

    //Motivo Localizacion
    /**
     * Metodo que actualiza el motivo localizacion seleccionado (vigencia
     * localizacion)
     */
    public void actualizarTipoDescanso() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) { //Si Se trabaja sobre la tabla y no en un dialogo
            //cambiosJornada = true;
            vigenciaJornadaSeleccionada.setTipodescanso(tipoDescansoSeleccionado);
            if (!listVJCrear.contains(vigenciaJornadaSeleccionada)) {
                if (listVJModificar.isEmpty()) {
                    listVJModificar.add(vigenciaJornadaSeleccionada);
                } else if (!listVJModificar.contains(vigenciaJornadaSeleccionada)) {
                    listVJModificar.add(vigenciaJornadaSeleccionada);
                }
            }

            if (guardado) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
            context.update("form:datosVJEmpleado");
            permitirIndex = true;
            //cambiosJornada = true;
        } else if (tipoActualizacion == 1) {//Para crear un registro
            nuevaVigencia.setTipodescanso(tipoDescansoSeleccionado);
            context.update("formularioDialogos:nuevaVJ");
        } else if (tipoActualizacion == 2) {//Para duplicar un registro
            duplicarVJ.setTipodescanso(tipoDescansoSeleccionado);
            context.update("formularioDialogos:duplicarVJ");
        }
        filtrarTiposDescansos = null;
        tipoDescansoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;

        context.reset("form:lovTipoDescanso:globalFilter");
        context.execute("lovTipoDescanso.clearFilters()");
    }

    /**
     * Metodo que cancela la seleccion del motivo localizacion (vigencia
     * localizacion)
     */
    public void cancelarCambioTipoDescanso() {
        filtrarTiposDescansos = null;
        tipoDescansoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoDescanso:globalFilter");
        context.execute("lovTipoDescanso.clearFilters()");
        context.execute("TiposDescansosDialogo.hide()");
    }
    //LISTA DE VALORES DINAMICA

    /**
     * Metodo que activa la lista de valores de todas las tablas con respecto al
     * index activo y la columna activa
     */
    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        //Si no hay registro selecciionado
        if (vigenciaJornadaSeleccionada == null && vigenciaDineroSeleccionada == null && vigenciaTiempoSeleccionada == null) {
            context.execute("seleccionarRegistro.show()");
        } else {
            if (vigenciaJornadaSeleccionada != null) {
                if (cualCelda == 1) {
                    contarRegistrosJornadaL();
                    context.update("form:JornadaLaboralDialogo");
                    context.execute("JornadaLaboralDialogo.show()");
                    tipoActualizacion = 0;
                }
                if (cualCelda == 2) {
                    contarRegistrosTipoD();
                    context.update("form:TiposDescansosDialogo");
                    context.execute("TiposDescansosDialogo.show()");
                    tipoActualizacion = 0;
                }
            }
        }
    }

    /**
     * Valida un proceso de nuevo registro dentro de la pagina con respecto a la
     * posicion en la pagina
     */
    public void validarNuevoRegistro() {

        RequestContext context = RequestContext.getCurrentInstance();
        if (cambiosJornada == false && cambiosTiempo == false && cambiosDinero == false) {
            if (vigenciaJornadaSeleccionada == null && vigenciaTiempoSeleccionada == null && vigenciaDineroSeleccionada == null) {
                context.execute("seleccionarRegistro.show()");
            } else {
                int tam = 0;
                if (listVigenciasJornadas != null) {
                    tam = listVigenciasJornadas.size();
                }
                int tam1 = 0;
                if (listVigenciasCompensacionesDinero != null) {
                    tam1 = listVigenciasCompensacionesDinero.size();
                }
                int tam2 = 0;
                if (listVigenciasCompensacionesTiempo != null) {
                    tam2 = listVigenciasCompensacionesTiempo.size();
                }
                if ((tam == 0) || (tam1 == 0) || (tam2 == 0)) {
                    context.update("form:NuevoRegistroPagina");
                    context.execute("NuevoRegistroPagina.show()");
                } else {
                    if (vigenciaJornadaSeleccionada != null) {
                        context.update("form:NuevoRegistroVJ");
                        context.execute("NuevoRegistroVJ.show()");
                    }
                    if (vigenciaTiempoSeleccionada != null) {
                        nuevaVigenciaCT = new VigenciasCompensaciones();
                        context.update("form:NuevoRegistroVCT");
                        context.execute("NuevoRegistroVCT.show()");
                    }
                    if (vigenciaDineroSeleccionada != null) {
                        nuevaVigenciaCD = new VigenciasCompensaciones();
                        context.update("form:NuevoRegistroVCD");
                        context.execute("NuevoRegistroVCD.show()");
                    }
                }
            }
        } else {
            context.execute("confirmarGuardarSinSalida.show()");
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
        if (vigenciaTiempoSeleccionada != null) {
            nombreTabla = ":formExportarVCT:datosVCTVigenciaExportar";
            nombreXML = "VigenciasCompensacionesTiempoXML";
        } else if (vigenciaDineroSeleccionada != null) {
            nombreTabla = ":formExportarVCD:datosVCDVigenciaExportar";
            nombreXML = "VigenciasCompensacionesDineroXML";
        } else if (vigenciaJornadaSeleccionada != null) {
            nombreTabla = ":formExportarVJ:datosVJEmpleadoExportar";
            nombreXML = "VigenciasJornadasXML";
        }
        return nombreTabla;
    }

    /**
     * Valida la tabla a exportar en PDF con respecto al index activo
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void validarExportPDF() throws IOException {
        if (vigenciaTiempoSeleccionada != null) {
            exportPDVCT();
        } else if (vigenciaDineroSeleccionada != null) {
            exportPDFVCD();
        } else if (vigenciaJornadaSeleccionada != null) {
            exportPDVJ();
        }
    }

    /**
     * Metodo que exporta datos a PDF Vigencia Localizacion
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportPDVJ() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVJ:datosVJEmpleadoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "VigenciasJornadasPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    /**
     * Metodo que exporta datos a PDF Vigencia Prorrateo
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportPDVCT() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVCT:datosVCTVigenciaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "VigenciasCompensacionesTiempoPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    /**
     * Metodo que exporta datos a PDF Vigencia Prorrateo Proyecto
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportPDFVCD() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVCD:datosVCDVigenciaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "VigenciasCompensacionesDineroPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    /**
     * Verifica que tabla exportar XLS con respecto al index activo
     *
     * @throws IOException
     */
    public void verificarExportXLS() throws IOException {
        if (vigenciaTiempoSeleccionada != null) {
            exportXLSVCT();
        } else if (vigenciaDineroSeleccionada != null) {
            exportXLSVCD();
        } else if (vigenciaJornadaSeleccionada != null) {
            exportXLSVJ();
        }
    }

    /**
     * Metodo que exporta datos a XLS Vigencia Localizacion
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportXLSVJ() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVJ:datosVJEmpleadoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "VigenciasJornadasXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    /**
     * Metodo que exporta datos a XLS Vigencia Prorrateo
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportXLSVCT() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVCT:datosVCTVigenciaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "VigenciasCompensacionesTiempoXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    /**
     * Metodo que exporta datos a XLS Vigencia Prorrateo Proyecto
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportXLSVCD() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVCD:datosVCDVigenciaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "VigenciasCompensacionesDineroXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastroTabla() {
        //Cuando no se ha seleccionado ningun registro:
        if (vigenciaJornadaSeleccionada == null && vigenciaDineroSeleccionada == null && vigenciaTiempoSeleccionada == null) {
            //Dialogo para seleccionar el rastro Historico de la tabla deseada
            RequestContext.getCurrentInstance().execute("verificarRastrosTablas.show()");
        } else {
            //Cuando se selecciono registro:            
            if (vigenciaTiempoSeleccionada != null) {
                verificarRastroVigenciaCompensacionTiempo();
            } else if (vigenciaDineroSeleccionada != null) {
                verificarRastroVigenciaCompensacionDinero();
            } else if (vigenciaJornadaSeleccionada != null) {
                verificarRastroVigenciaJornada();
            }
        }
    }

    public void verificarRastroVigenciaJornada() {
        RequestContext context = RequestContext.getCurrentInstance();
        int resultado = administrarRastros.obtenerTabla(vigenciaJornadaSeleccionada.getSecuencia(), "VIGENCIASJORNADAS");
        backUp = vigenciaJornadaSeleccionada.getSecuencia();
        if (resultado == 1) {
            context.execute("errorObjetosDB.show()");
        } else if (resultado == 2) {
            nombreTablaRastro = "VigenciasJornadas";
            msnConfirmarRastro = "La tabla VIGENCIASJORNADAS tiene rastros para el registro seleccionado, ¿desea continuar?";
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

    public void verificarRastroVigenciaJornadaHist() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (administrarRastros.verificarHistoricosTabla("VIGENCIASJORNADAS")) {
            nombreTablaRastro = "VigenciasJornadas";
            msnConfirmarRastroHistorico = "La tabla VIGENCIASJORNADAS tiene rastros historicos, ¿Desea continuar?";
            context.update("form:confirmarRastroHistorico");
            context.execute("confirmarRastroHistorico.show()");
        } else {
            context.execute("errorRastroHistorico.show()");
        }
    }

    public void verificarRastroVigenciaCompensacionTiempo() {
        RequestContext context = RequestContext.getCurrentInstance();
        int resultado = administrarRastros.obtenerTabla(vigenciaTiempoSeleccionada.getSecuencia(), "VIGENCIASCOMPENSACIONES");
        backUp = vigenciaTiempoSeleccionada.getSecuencia();
        if (resultado == 1) {
            context.execute("errorObjetosDB.show()");
        } else if (resultado == 2) {
            nombreTablaRastro = "VigenciasCompensaciones";
            msnConfirmarRastro = "La tabla VIGENCIASCOMPENSACIONES tiene rastros para el registro seleccionado, ¿desea continuar?";
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

    public void verificarRastroVigenciaCompensacionTiempoHist() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (administrarRastros.verificarHistoricosTabla("VIGENCIASCOMPENSACIONES")) {
            nombreTablaRastro = "VigenciasCompensaciones";
            msnConfirmarRastroHistorico = "La tabla VIGENCIASCOMPENSACIONES tiene rastros historicos, ¿Desea continuar?";
            context.update("form:confirmarRastroHistorico");
            context.execute("confirmarRastroHistorico.show()");
        } else {
            context.execute("errorRastroHistorico.show()");
        }
    }

    public void verificarRastroVigenciaCompensacionDinero() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaDineroSeleccionada.getSecuencia() != null) {
            int resultado = administrarRastros.obtenerTabla(vigenciaDineroSeleccionada.getSecuencia(), "VIGENCIASCOMPENSACIONES");
            backUp = vigenciaDineroSeleccionada.getSecuencia();
            if (resultado == 1) {
                context.execute("errorObjetosDB.show()");
            } else if (resultado == 2) {
                nombreTablaRastro = "VigenciasCompensaciones";
                msnConfirmarRastro = "La tabla VIGENCIASCOMPENSACIONES tiene rastros para el registro seleccionado, ¿desea continuar?";
                context.update("form:msnConfirmarRastro");
                context.execute("confirmarRastro.show()");
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
    }

    public void verificarRastroVigenciaCompensacionDineroHist() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (administrarRastros.verificarHistoricosTabla("VIGENCIASCOMPENSACIONES")) {
            nombreTablaRastro = "VigenciasCompensaciones";
            msnConfirmarRastroHistorico = "La tabla VIGENCIASCOMPENSACIONES tiene rastros historicos, ¿Desea continuar?";
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
    //EVENTO FILTRAR

    /**
     * Evento que cambia la lista real a la filtrada
     */
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        vigenciaJornadaSeleccionada = null;
        modificarInfoRegistroVJ(filtrarVJ.size());
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        RequestContext.getCurrentInstance().update("form:informacionRegistroVJ");
    }

    public void anularLOV() {
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void eventoFiltrarT() {
        if (tipoListaVCT == 0) {
            tipoListaVCT = 1;
        }
        vigenciaTiempoSeleccionada = null;
        modificarInfoRegistroVT(filtrarVigenciasCompensacionesTiempo.size());
        RequestContext.getCurrentInstance().update("form:informacionRegistroVT");
    }

    public void eventoFiltrarD() {
        if (tipoListaVCD == 0) {
            tipoListaVCD = 1;
        }
        vigenciaDineroSeleccionada = null;
        modificarInfoRegistroVD(filtrarVigenciasCompensacionesDinero.size());
        RequestContext.getCurrentInstance().update("form:informacionRegistroVD");
        RequestContext.getCurrentInstance().update("form:informacionRegistroVD");
    }

    public void eventoFiltrarTipoD() {
        modificarInfoRegistroTipoD(filtrarTiposDescansos.size());
        RequestContext.getCurrentInstance().update("form:infoRegistroTipoDescanso");
    }

    public void eventoFiltrarJornadaL() {
        modificarInfoRegistroJornadaL(filtrarJornadasLaborales.size());
        RequestContext.getCurrentInstance().update("form:infoRegistroJornadaLaboral");
    }

    private void modificarInfoRegistroVJ(int valor) {
        infoRegistroVJ = String.valueOf(valor);
    }

    private void modificarInfoRegistroVD(int valor) {
        infoRegistroVD = String.valueOf(valor);
    }

    private void modificarInfoRegistroVT(int valor) {
        infoRegistroVT = String.valueOf(valor);
    }

    private void modificarInfoRegistroJornadaL(int valor) {
        infoRegistroJornadaLaboral = String.valueOf(valor);
    }

    private void modificarInfoRegistroTipoD(int valor) {
        infoRegistroTipoDescanso = String.valueOf(valor);
    }

    public void contarRegistrosVJ() {
        if (listVigenciasJornadas != null) {
            modificarInfoRegistroVJ(listVigenciasJornadas.size());
            RequestContext.getCurrentInstance().update("form:informacionRegistroVJ");
        } else {
            modificarInfoRegistroVJ(0);
            RequestContext.getCurrentInstance().update("form:informacionRegistroVJ");
        }
    }

    public void contarRegistrosVT() {
        if (listVigenciasCompensacionesTiempo != null) {
            if (listVigenciasCompensacionesTiempo.size() > 0) {
                modificarInfoRegistroVT(listVigenciasCompensacionesTiempo.size());
                RequestContext.getCurrentInstance().update("form:informacionRegistroVT");
            } else {
                modificarInfoRegistroVT(0);
                RequestContext.getCurrentInstance().update("form:informacionRegistroVT");
            }
        } else {
            modificarInfoRegistroVT(0);
            RequestContext.getCurrentInstance().update("form:informacionRegistroVT");
        }
    }

    public void contarRegistrosVD() {
        if (listVigenciasCompensacionesDinero != null) {
            if (listVigenciasCompensacionesDinero.size() > 0) {
                modificarInfoRegistroVD(listVigenciasCompensacionesDinero.size());
                RequestContext.getCurrentInstance().update("form:informacionRegistroVD");
            } else {
                modificarInfoRegistroVD(0);
                RequestContext.getCurrentInstance().update("form:informacionRegistroVD");
            }
        } else {
            modificarInfoRegistroVD(0);
            RequestContext.getCurrentInstance().update("form:informacionRegistroVD");
        }
    }

    public void contarRegistrosJornadaL() {
        if (listJornadasLaborales != null) {
            if (listJornadasLaborales.size() > 0) {
                modificarInfoRegistroJornadaL(listJornadasLaborales.size());
            } else {
                modificarInfoRegistroJornadaL(0);
            }
        } else {
            modificarInfoRegistroJornadaL(0);
        }
    }

    public void contarRegistrosTipoD() {
        if (listTiposDescansos != null) {
            if (listTiposDescansos.size() > 0) {
                modificarInfoRegistroTipoD(listTiposDescansos.size());
            } else {
                modificarInfoRegistroTipoD(0);
            }
        } else {
            modificarInfoRegistroTipoD(0);
        }
    }

    public void recordarSeleccionVJ() {
        if (vigenciaJornadaSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosVJEmpleado");
            tablaC.setSelection(vigenciaJornadaSeleccionada);
        }
    }

    public void recordarSeleccionVT() {
        if (vigenciaTiempoSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosVigenciaCT");
            tablaC.setSelection(vigenciaTiempoSeleccionada);
        } else {
            vigenciaTiempoSeleccionada = null;
        }
    }

    public void recordarSeleccionVD() {
        if (vigenciaDineroSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosVigenciaCD");
            tablaC.setSelection(vigenciaDineroSeleccionada);
        } else {
            vigenciaDineroSeleccionada = null;
        }
    }

    public List<VigenciasJornadas> getListVigenciasJornadas() {
        try {
            if (listVigenciasJornadas == null) {
                if (empleado != null) {
                    listVigenciasJornadas = new ArrayList<VigenciasJornadas>();
                    listVigenciasJornadas = administrarVigenciasJornadas.VigenciasJornadasEmpleado(empleado.getSecuencia());
                }
                return listVigenciasJornadas;
            } else {
                return listVigenciasJornadas;
            }
        } catch (Exception e) {
            System.out.println("Error getVigenciaLocalizaciones " + e.toString());
            return null;
        }
    }

    public void setListVigenciasJornadas(List<VigenciasJornadas> vigenciasJornadas) {
        this.listVigenciasJornadas = vigenciasJornadas;
    }

    public List<VigenciasJornadas> getFiltrarVJ() {
        return filtrarVJ;
    }

    public void setFiltrarVJ(List<VigenciasJornadas> filtrarVJ) {
        this.filtrarVJ = filtrarVJ;
    }

    public List<TiposDescansos> getListTiposDescansos() {
        if (listTiposDescansos == null) {
            listTiposDescansos = administrarVigenciasJornadas.tiposDescansos();
        }
        return listTiposDescansos;
    }

    public void setListTiposDescansos(List<TiposDescansos> tiposDescansos) {
        this.listTiposDescansos = tiposDescansos;
    }

    public List<JornadasLaborales> getListJornadasLaborales() {
        if (listJornadasLaborales == null) {
            listJornadasLaborales = administrarVigenciasJornadas.jornadasLaborales();
        }
        return listJornadasLaborales;
    }

    public void setListJornadasLaborales(List<JornadasLaborales> jornadasLaborales) {
        this.listJornadasLaborales = jornadasLaborales;
    }

    public JornadasLaborales getJornadaLaboralSeleccionada() {
        return jornadaLaboralSeleccionada;
    }

    public void setJornadaLaboralSeleccionada(JornadasLaborales jornadasLaborales) {
        this.jornadaLaboralSeleccionada = jornadasLaborales;
    }

    public List<JornadasLaborales> getFiltrarJornadasLaborales() {
        return filtrarJornadasLaborales;
    }

    public void setFiltrarJornadasLaborales(List<JornadasLaborales> jornadasLaborales) {
        this.filtrarJornadasLaborales = jornadasLaborales;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<VigenciasJornadas> getListVJModificar() {
        return listVJModificar;
    }

    public void setListVJModificar(List<VigenciasJornadas> listVJModificar) {
        this.listVJModificar = listVJModificar;
    }

    public VigenciasJornadas getNuevaVigencia() {
        return nuevaVigencia;
    }

    public void setNuevaVigencia(VigenciasJornadas nuevaVigencia) {
        this.nuevaVigencia = nuevaVigencia;
    }

    public List<VigenciasJornadas> getListVJCrear() {
        return listVJCrear;
    }

    public void setListVJCrear(List<VigenciasJornadas> listVJCrear) {
        this.listVJCrear = listVJCrear;
    }

    public List<VigenciasJornadas> getListVJBorrar() {
        return listVJBorrar;
    }

    public void setListVJBorrar(List<VigenciasJornadas> listVJBorrar) {
        this.listVJBorrar = listVJBorrar;
    }

    public VigenciasJornadas getEditarVJ() {
        return editarVJ;
    }

    public void setEditarVJ(VigenciasJornadas editarVJ) {
        this.editarVJ = editarVJ;
    }

    public VigenciasJornadas getDuplicarVJ() {
        return duplicarVJ;
    }

    public void setDuplicarVJ(VigenciasJornadas duplicarVJ) {
        this.duplicarVJ = duplicarVJ;
    }

    public List<VigenciasCompensaciones> getListVigenciasCompensacionesTiempo() {
        if (vigenciaJornadaSeleccionada != null) {
            if (listVigenciasCompensacionesTiempo == null) {
                listVigenciasCompensacionesTiempo = new ArrayList<VigenciasCompensaciones>();
                String variable = "DESCANSO";
                List<VigenciasCompensaciones> listVigencia;
                listVigencia = administrarVigenciasJornadas.VigenciasCompensacionesSecVigencia(vigenciaJornadaSeleccionada.getSecuencia());
                if (listVigencia != null) {
                    for (int i = 0; i < listVigencia.size(); i++) {
                        if (listVigencia.get(i).getTipocompensacion().equalsIgnoreCase(variable)) {
                            if (listVigencia.get(i).getComentario() != null) {
                                String aux = listVigencia.get(i).getComentario().toUpperCase();
                                listVigencia.get(i).setComentario(aux);
                            }
                            listVigenciasCompensacionesTiempo.add(listVigencia.get(i));
                        }
                    }
                }
            }
        }
        return listVigenciasCompensacionesTiempo;
    }

    public void setListVigenciasCompensacionesTiempo(List<VigenciasCompensaciones> vigenciasCompensaciones) {
        this.listVigenciasCompensacionesTiempo = vigenciasCompensaciones;
    }

    public List<VigenciasCompensaciones> getFiltrarVigenciasCompensacionesTiempo() {
        return filtrarVigenciasCompensacionesTiempo;
    }

    public void setFiltrarVigenciasCompensacionesTiempo(List<VigenciasCompensaciones> vigenciasCompensaciones) {
        this.filtrarVigenciasCompensacionesTiempo = vigenciasCompensaciones;
    }

    public List<VigenciasCompensaciones> getListVigenciasCompensacionesDinero() {
        if (vigenciaJornadaSeleccionada != null) {
            if (listVigenciasCompensacionesDinero == null) {
                listVigenciasCompensacionesDinero = new ArrayList<VigenciasCompensaciones>();
                String variable = "DINERO";
                List<VigenciasCompensaciones> listVigencia;
                listVigencia = administrarVigenciasJornadas.VigenciasCompensacionesSecVigencia(vigenciaJornadaSeleccionada.getSecuencia());
                if (listVigencia != null) {
                    for (int i = 0; i < listVigencia.size(); i++) {
                        if (listVigencia.get(i).getTipocompensacion().equalsIgnoreCase(variable)) {
                            if (listVigencia.get(i).getComentario() != null) {
                                String aux = listVigencia.get(i).getComentario().toUpperCase();
                                listVigencia.get(i).setComentario(aux);
                            }
                            listVigenciasCompensacionesDinero.add(listVigencia.get(i));

                        }
                    }
                }
            }
        }
        return listVigenciasCompensacionesDinero;
    }

    public void setListVigenciasCompensacionesDinero(List<VigenciasCompensaciones> vigenciasCompensacioneses) {
        this.listVigenciasCompensacionesDinero = vigenciasCompensacioneses;
    }

    public List<VigenciasCompensaciones> getFiltrarVigenciasCompensacionesDinero() {
        return filtrarVigenciasCompensacionesDinero;
    }

    public void setFiltrarVigenciasCompensacionesDinero(List<VigenciasCompensaciones> vigenciasCompensaciones) {
        this.filtrarVigenciasCompensacionesDinero = vigenciasCompensaciones;
    }

    public VigenciasCompensaciones getEditarVCT() {
        return editarVCT;
    }

    public void setEditarVCT(VigenciasCompensaciones editarVCT) {
        this.editarVCT = editarVCT;
    }

    public VigenciasCompensaciones getEditarVCD() {
        return editarVCD;
    }

    public void setEditarVCD(VigenciasCompensaciones editarVCD) {
        this.editarVCD = editarVCD;
    }

    public TiposDescansos getTipoDescansoSeleccionado() {
        return tipoDescansoSeleccionado;
    }

    public void setTipoDescansoSeleccionado(TiposDescansos tiposDescansos) {
        this.tipoDescansoSeleccionado = tiposDescansos;
    }

    public List<TiposDescansos> getFiltradoTiposDescansos() {
        return filtrarTiposDescansos;
    }

    public void setFiltradoTiposDescansos(List<TiposDescansos> tiposDescansos) {
        this.filtrarTiposDescansos = tiposDescansos;
    }

    public VigenciasCompensaciones getNuevaVigenciaCT() {
        return nuevaVigenciaCT;
    }

    public void setNuevaVigenciaCT(VigenciasCompensaciones nuevaVigenciaCT) {
        this.nuevaVigenciaCT = nuevaVigenciaCT;
    }

    public VigenciasCompensaciones getNuevaVigenciaCD() {
        return nuevaVigenciaCD;
    }

    public void setNuevaVigenciaCD(VigenciasCompensaciones nuevaVigenciaCD) {
        this.nuevaVigenciaCD = nuevaVigenciaCD;
    }

    public VigenciasCompensaciones getDuplicarVCT() {
        return duplicarVCT;
    }

    public void setDuplicarVCT(VigenciasCompensaciones duplicarVCT) {
        this.duplicarVCT = duplicarVCT;
    }

    public VigenciasCompensaciones getDuplicarVCD() {
        return duplicarVCD;
    }

    public void setDuplicarVCD(VigenciasCompensaciones duplicarVCD) {
        this.duplicarVCD = duplicarVCD;
    }

    public List<VigenciasCompensaciones> getListVCTCrear() {
        return listVCTCrear;
    }

    public void setListVCTCrear(List<VigenciasCompensaciones> listVCTCrear) {
        this.listVCTCrear = listVCTCrear;
    }

    public List<VigenciasCompensaciones> getListVCDCrear() {
        return listVCDCrear;
    }

    public void setListVCDCrear(List<VigenciasCompensaciones> listVCDCrear) {
        this.listVCDCrear = listVCDCrear;
    }

    public List<VigenciasCompensaciones> getListVCTModificar() {
        return listVCTModificar;
    }

    public void setListVCTModificar(List<VigenciasCompensaciones> listCTModificar) {
        this.listVCTModificar = listCTModificar;
    }

    public List<VigenciasCompensaciones> getListVCDModificar() {
        return listVCDModificar;
    }

    public void setListVCDModificar(List<VigenciasCompensaciones> listVCDModificar) {
        this.listVCDModificar = listVCDModificar;
    }

    public List<VigenciasCompensaciones> getListVCTBorrar() {
        return listVCTBorrar;
    }

    public void setListVCTBorrar(List<VigenciasCompensaciones> listVCTBorrar) {
        this.listVCTBorrar = listVCTBorrar;
    }

    public List<VigenciasCompensaciones> getListVCDBorrar() {
        return listVCDBorrar;
    }

    public void setListVCDBorrar(List<VigenciasCompensaciones> listVCDBorrar) {
        this.listVCDBorrar = listVCDBorrar;
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

    public String getNombreTablaRastro() {
        return nombreTablaRastro;
    }

    public void setNombreTablaRastro(String nombreTablaRastro) {
        this.nombreTablaRastro = nombreTablaRastro;
    }

    public BigInteger getBackUp() {
        return backUp;
    }

    public void setBackUp(BigInteger backUp) {
        this.backUp = backUp;
    }

    public VigenciasJornadas getVigenciaJornadaSeleccionada() {
        return vigenciaJornadaSeleccionada;
    }

    public void setVigenciaJornadaSeleccionada(VigenciasJornadas vigenciaJornadaSeleccionada) {
        this.vigenciaJornadaSeleccionada = vigenciaJornadaSeleccionada;
    }

    public VigenciasCompensaciones getVigenciaTiempoSeleccionada() {
        return vigenciaTiempoSeleccionada;
    }

    public void setVigenciaTiempoSeleccionada(VigenciasCompensaciones vigenciaTiempoSeleccionada) {
        this.vigenciaTiempoSeleccionada = vigenciaTiempoSeleccionada;
    }

    public VigenciasCompensaciones getVigenciaDineroSeleccionada() {
        return vigenciaDineroSeleccionada;
    }

    public void setVigenciaDineroSeleccionada(VigenciasCompensaciones vigenciaDineroSeleccionada) {
        this.vigenciaDineroSeleccionada = vigenciaDineroSeleccionada;
    }

    public String getAltoTabla1() {
        return altoTabla1;
    }

    public String getAltoTabla2() {
        return altoTabla2;
    }

    public String getAltoTabla3() {
        return altoTabla3;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public String getInfoRegistroJornadaLaboral() {
        return infoRegistroJornadaLaboral;
    }

    public String getInfoRegistroTipoDescanso() {
        return infoRegistroTipoDescanso;
    }

    public String getInfoRegistroVD() {
        return infoRegistroVD;
    }

    public String getInfoRegistroVJ() {
        return infoRegistroVJ;
    }

    public String getInfoRegistroVT() {
        return infoRegistroVT;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }
}
