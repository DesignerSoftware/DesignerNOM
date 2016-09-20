/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;


import Entidades.Cargos;
import Entidades.Empleados;
import Entidades.Encargaturas;
import Entidades.Estructuras;
import Entidades.MotivosReemplazos;
import Entidades.TiposReemplazos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarNovedadesReemplazosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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

@ManagedBean
@SessionScoped
public class ControlNovedadesReemplazos implements Serializable {

    @EJB
    AdministrarNovedadesReemplazosInterface administrarNovedadesReemplazos; // Encargaturas
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //SECUENCIA DE LA PERSONA
    private BigInteger secuenciaEmpleado;
    private Empleados empleado;
    //LISTA ENCARGATURAS
    private List<Encargaturas> listaEncargaturas;
    private List<Encargaturas> filtradosListaEncargaturas;
    private Encargaturas encargaturaSeleccionada;
    //OTROS
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //Consultas sobre detalles
    private Empleados empleadoParametro;
    //editar celda
    private Encargaturas editarEncargaturas;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista;
    //L.O.V TIPOSREEMPLAZOS
    private List<TiposReemplazos> listaTiposReemplazos;
    private List<TiposReemplazos> filtradoslistaTiposReemplazos;
    private TiposReemplazos seleccionTiposReemplazos;
    //L.O.V MOTIVOSREEMPLAZOS
    private List<MotivosReemplazos> listaMotivosReemplazos;
    private List<MotivosReemplazos> filtradoslistaMotivosReemplazos;
    private MotivosReemplazos seleccionMotivosReemplazos;
    //L.O.V ESTRUCTURAS
    private List<Estructuras> listaEstructuras;
    private List<Estructuras> filtradoslistaEstructuras;
    private Estructuras seleccionEstructuras;
    //L.O.V CARGOS
    private List<Cargos> listaCargos;
    private List<Cargos> filtradoslistaCargos;
    private Cargos seleccionCargos;
    //L.O.V EMPLEADOS
    private List<Empleados> listaEmpleados;
    private List<Empleados> filtradoslistaEmpleados;
    private Empleados seleccionEmpleados;
    private Empleados seleccionEmpleadosReemplazados;
    //Seleccion Mostrar Todos
    private Empleados seleccionMostrar;
    //RASTROS
    //LISTA QUE NO ES LISTA - 1 SOLO ELEMENTO
    private List<Empleados> listaFalsaEmpleados;
    private List<Empleados> filtradosListaFalsaEmpleados;
    //Crear Encargaturas
    private List<Encargaturas> listaEncargaturasCrear;
    public Encargaturas nuevaEncargatura;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    //Modificar Encargaturas
    private List<Encargaturas> listaEncargaturasModificar;
    private boolean guardado, guardarOk;
    //Borrar Encargaturas
    private List<Encargaturas> listaEncargaturasBorrar;
    //Cual Tabla
    private int CualTabla;
    //Boolean deshabilitar botones en caso de que no haya un parametro
    private boolean botones;
    private String tipoPantalla;
    //Duplicar
    private Encargaturas duplicarEncargatura;
    //AUTOCOMPLETAR
    private String Reemplazado, TipoReemplazo, MotivoReemplazo, Cargo, Estructura;
    //Columnas Tabla Encargaturas
    private Column nREmpleadoReemplazado, nRTiposReemplazos, nRFechasPagos, nRFechasIniciales, nRFechasFinales, nRCargos, nRMotivosReemplazos, nREstructuras;
    private String altoTabla;

    public ControlNovedadesReemplazos() {
        permitirIndex = true;
        //secuenciaEmpleado = BigInteger.valueOf(10661474);
        aceptar = true;
        empleadoParametro = new Empleados();
        listaEncargaturas = null;
        listaTiposReemplazos = new ArrayList<TiposReemplazos>();
        listaMotivosReemplazos = new ArrayList<MotivosReemplazos>();
        listaEstructuras = new ArrayList<Estructuras>();
        listaEmpleados = new ArrayList<Empleados>();
        listaEncargaturasBorrar = new ArrayList<Encargaturas>();
        listaEncargaturasCrear = new ArrayList<Encargaturas>();
        listaEncargaturasModificar = new ArrayList<Encargaturas>();
        listaFalsaEmpleados = null;
        encargaturaSeleccionada = null;
        guardado = true;
        tipoLista = 0;
        //Crear VC
        nuevaEncargatura = new Encargaturas();
        nuevaEncargatura.setReemplazado(new Empleados());
        nuevaEncargatura.setTiporeemplazo(new TiposReemplazos());
        nuevaEncargatura.setCargo(new Cargos());
        nuevaEncargatura.setMotivoreemplazo(new MotivosReemplazos());
        nuevaEncargatura.setEstructura(new Estructuras());
        altoTabla = "155";
    }
    
    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarNovedadesReemplazos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirEmpleado(BigInteger secEmpleado) {
        if (secEmpleado.compareTo(new BigInteger("-1")) == 0) {
            botones = false;
            tipoPantalla = "TODOS";
        } else {
            botones = true;
            tipoPantalla = "UNO";
        }

        secuenciaEmpleado = secEmpleado;
        empleado = null;
        listaFalsaEmpleados = new ArrayList<Empleados>();
        getEmpleado();
        listaEmpleados = null;
        getListaEmpleados();
        if (tipoPantalla.equals("UNO")) {
            listaFalsaEmpleados.add(empleado);
        } else {
            listaFalsaEmpleados = listaEmpleados;
        }
        if (!listaFalsaEmpleados.isEmpty()) {
            seleccionMostrar = listaFalsaEmpleados.get(0);
            listaEncargaturas = null;
            getListaEncargaturas();
            listaEmpleados = new ArrayList<Empleados>();
        }

        listaTiposReemplazos = null;
        getListaTiposReemplazos();
        listaMotivosReemplazos = null;
        getListaMotivosReemplazos();
        listaEstructuras = null;
        getListaEstructuras();
        listaEmpleados = null;
        getListaEmpleados();
        aceptar = true;
    }

    //AUTOCOMPLETAR
    public void modificarEncargaturas(Encargaturas encargatura, String confirmarCambio, String valorConfirmar) {
        encargaturaSeleccionada = encargatura;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaEncargaturasCrear.contains(encargaturaSeleccionada)) {

                    if (listaEncargaturasModificar.isEmpty()) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    } else if (!listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                encargaturaSeleccionada = null;
                encargaturaSeleccionada = null;

            } else {
                if (!listaEncargaturasCrear.contains(encargaturaSeleccionada)) {

                    if (listaEncargaturasModificar.isEmpty()) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    } else if (!listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                encargaturaSeleccionada = null;
            }
            RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
        } else if (confirmarCambio.equalsIgnoreCase("REEMPLAZADO")) {
            if (tipoLista == 0) {
                encargaturaSeleccionada.getReemplazado().getPersona().setNombreCompleto(Reemplazado);
            } else {
                encargaturaSeleccionada.getReemplazado().getPersona().setNombreCompleto(Reemplazado);
            }

            for (int i = 0; i < listaEmpleados.size(); i++) {
                if (listaEmpleados.get(i).getPersona().getNombreCompleto().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    encargaturaSeleccionada.setReemplazado(listaEmpleados.get(indiceUnicoElemento));
                } else {
                    encargaturaSeleccionada.setReemplazado(listaEmpleados.get(indiceUnicoElemento));
                }
                listaEmpleados.clear();
                getListaEmpleados();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:empleadosAbajoDialogo");
                RequestContext.getCurrentInstance().execute("PF('empleadosAbajoDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("TIPOREEMPLAZO")) {
            if (tipoLista == 0) {
                encargaturaSeleccionada.getTiporeemplazo().setNombre(TipoReemplazo);
            } else {
                encargaturaSeleccionada.getTiporeemplazo().setNombre(TipoReemplazo);
            }
            for (int i = 0; i < listaTiposReemplazos.size(); i++) {
                if (listaTiposReemplazos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    encargaturaSeleccionada.setTiporeemplazo(listaTiposReemplazos.get(indiceUnicoElemento));
                } else {
                    encargaturaSeleccionada.setTiporeemplazo(listaTiposReemplazos.get(indiceUnicoElemento));
                }
                listaTiposReemplazos.clear();
                getListaTiposReemplazos();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposReemplazosDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposReemplazosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("MOTIVOREEMPLAZO")) {
            if (tipoLista == 0) {
                encargaturaSeleccionada.getMotivoreemplazo().setNombre(MotivoReemplazo);
            } else {
                encargaturaSeleccionada.getMotivoreemplazo().setNombre(MotivoReemplazo);
            }
            for (int i = 0; i < listaMotivosReemplazos.size(); i++) {
                if (listaMotivosReemplazos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    encargaturaSeleccionada.setMotivoreemplazo(listaMotivosReemplazos.get(indiceUnicoElemento));
                } else {
                    encargaturaSeleccionada.setMotivoreemplazo(listaMotivosReemplazos.get(indiceUnicoElemento));
                }
                listaMotivosReemplazos.clear();
                getListaMotivosReemplazos();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:motivosReemplazosDialogo");
                RequestContext.getCurrentInstance().execute("PF('motivosReemplazosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("ESTRUCTURAS")) {
            if (tipoLista == 0) {
                encargaturaSeleccionada.getEstructura().setNombre(Estructura);
            } else {
                encargaturaSeleccionada.getEstructura().setNombre(Estructura);
            }
            for (int i = 0; i < listaEstructuras.size(); i++) {
                if (listaEstructuras.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    encargaturaSeleccionada.setEstructura(listaEstructuras.get(indiceUnicoElemento));
                } else {
                    encargaturaSeleccionada.setEstructura(listaEstructuras.get(indiceUnicoElemento));
                }
                listaEstructuras.clear();
                getListaEstructuras();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:estructurasDialogo");
                RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("CARGOS")) {
            if (tipoLista == 0) {
                encargaturaSeleccionada.getCargo().setNombre(Cargo);
            } else {
                encargaturaSeleccionada.getCargo().setNombre(Cargo);
            }
            for (int i = 0; i < listaCargos.size(); i++) {
                if (listaCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    encargaturaSeleccionada.setCargo(listaCargos.get(indiceUnicoElemento));
                } else {
                    encargaturaSeleccionada.setCargo(listaCargos.get(indiceUnicoElemento));
                }
                listaCargos.clear();
                getListaCargos();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:cargosDialogo");
                RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listaEncargaturasCrear.contains(encargaturaSeleccionada)) {
                    if (listaEncargaturasModificar.isEmpty()) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    } else if (!listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                encargaturaSeleccionada = null;
                encargaturaSeleccionada = null;
            } else {
                if (!listaEncargaturasCrear.contains(encargaturaSeleccionada)) {

                    if (listaEncargaturasModificar.isEmpty()) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    } else if (!listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                encargaturaSeleccionada = null;
                encargaturaSeleccionada = null;
            }
        }
        RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
    }

    //Ubicacion Celda Arriba 
    public void cambiarEmpleado() {
        secuenciaEmpleado = seleccionMostrar.getSecuencia();
        listaEncargaturas = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
    }

    //Ubicacion Celda Indice Abajo.
    public void cambiarIndice(Encargaturas encargatura, int celda) {
        if (permitirIndex == true) {

            encargaturaSeleccionada = encargatura;
            cualCelda = celda;
            //tablaImprimir = ":formExportar:datosVigenciasFormalesExportar";
            //nombreArchivo = "VigenciasFormalesXML";
            //RequestContext context = RequestContext.getCurrentInstance();
            //RequestContext.getCurrentInstance().update("form:exportarXML");
            if (tipoLista == 0) {
                encargaturaSeleccionada.getSecuencia();
                if (cualCelda == 0) {
                    Reemplazado = encargaturaSeleccionada.getReemplazado().getPersona().getNombreCompleto();
                } else if (cualCelda == 1) {
                    TipoReemplazo = encargaturaSeleccionada.getTiporeemplazo().getNombre();
                } else if (cualCelda == 5) {
                    Cargo = encargaturaSeleccionada.getCargo().getNombre();
                } else if (cualCelda == 6) {
                    MotivoReemplazo = encargaturaSeleccionada.getMotivoreemplazo().getNombre();
                } else if (cualCelda == 7) {
                    Estructura = encargaturaSeleccionada.getEstructura().getNombre();
                }
            } else {
                 encargaturaSeleccionada.getSecuencia();
                if (cualCelda == 0) {
                    Reemplazado = encargaturaSeleccionada.getReemplazado().getPersona().getNombreCompleto();
                } else if (cualCelda == 1) {
                    TipoReemplazo = encargaturaSeleccionada.getTiporeemplazo().getNombre();
                } else if (cualCelda == 5) {
                    Cargo = encargaturaSeleccionada.getCargo().getNombre();

                } else if (cualCelda == 6) {
                    MotivoReemplazo = encargaturaSeleccionada.getMotivoreemplazo().getNombre();
                } else if (cualCelda == 7) {
                    Estructura = encargaturaSeleccionada.getEstructura().getNombre();
                }
            }
        }
    }

    public void asignarIndex(Encargaturas encargatura, int dlg, int LND) {
        encargaturaSeleccionada= encargatura;
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
            encargaturaSeleccionada = null;
            encargaturaSeleccionada = null;
            System.out.println("Tipo Actualizacion: " + tipoActualizacion);
        } else if (LND == 2) {
            encargaturaSeleccionada = null;
            encargaturaSeleccionada = null;
            tipoActualizacion = 2;
        }
        if (dlg == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
        } else if (dlg == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposReemplazosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposReemplazosDialogo').show()");
        } else if (dlg == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:motivosReemplazosDialogo");
            RequestContext.getCurrentInstance().execute("PF('motivosReemplazosDialogo').show()");
        } else if (dlg == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:cargosDialogo");
            RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
        } else if (dlg == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:estructurasDialogo");
            RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').show()");
        } else if (dlg == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:empleadosAbajoDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadosAbajoDialogo').show()");
        }
    }

    public void mostrarTodos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaFalsaEmpleados.isEmpty()) {
            listaFalsaEmpleados.clear();
            listaFalsaEmpleados = listaEmpleados;
        } else {
            listaFalsaEmpleados = listaEmpleados;
        }
        if (!listaFalsaEmpleados.isEmpty()) {
            seleccionMostrar = listaFalsaEmpleados.get(0);
            listaEncargaturas = null;
            getListaEncargaturas();
        }
        listaEncargaturas = null;
        RequestContext.getCurrentInstance().update("form:datosEmpleados");
        RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
        filtradosListaFalsaEmpleados = null;
        aceptar = true;
        encargaturaSeleccionada = null;
        encargaturaSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;

    }

    public void actualizarEmpleadosFalso() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaFalsaEmpleados.isEmpty()) {
            listaFalsaEmpleados.clear();
            listaFalsaEmpleados.add(seleccionEmpleados);
        } else {
            listaFalsaEmpleados.add(seleccionEmpleados);
        }
        secuenciaEmpleado = seleccionEmpleados.getSecuencia();
        for (int i = 0; i < listaFalsaEmpleados.size(); i++) {
            System.out.println("En la lista está:" + listaFalsaEmpleados.get(i).getPersona().getNombre());
        }
        // listaEncargaturas = null;
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVEmpleados");
        RequestContext.getCurrentInstance().update("form:datosEmpleados");
        RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
        filtradosListaFalsaEmpleados = null;
        seleccionEmpleados = null;
        aceptar = true;
        encargaturaSeleccionada = null;
        encargaturaSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
    }

    public void actualizarEmpleados() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            System.out.println("Tipo Lista: " + tipoLista);
            if (tipoLista == 0) {
                encargaturaSeleccionada.setReemplazado(seleccionEmpleadosReemplazados);
                if (!listaEncargaturasCrear.contains(encargaturaSeleccionada)) {
                    if (listaEncargaturasModificar.isEmpty()) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    } else if (!listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    }
                }
            } else {
                encargaturaSeleccionada.setReemplazado(seleccionEmpleadosReemplazados);
                if (!listaEncargaturasCrear.contains(encargaturaSeleccionada)) {
                    if (listaEncargaturasModificar.isEmpty()) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    } else if (!listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            for (int i = 0; i < listaEncargaturas.size(); i++) {
                System.out.println("En la lista encargaturas está:" + listaEncargaturas.get(i).getReemplazado().getPersona().getNombreCompleto());
                System.out.println("Seleccionado: " + seleccionEmpleadosReemplazados.getPersona().getNombreCompleto());
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevaEncargatura.setReemplazado(seleccionEmpleadosReemplazados);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEncargatura");
        } else if (tipoActualizacion == 2) {
            duplicarEncargatura.setReemplazado(seleccionEmpleadosReemplazados);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEncargatura");
        }
        filtradoslistaEmpleados = null;
        seleccionEmpleadosReemplazados = null;
        aceptar = true;
        encargaturaSeleccionada = null;
        encargaturaSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVEmpleadosAbajo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleadosAbajo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empleadosAbajoDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVEmpleadosAbajo");
    }

    public void actualizarTiposReemplazos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                encargaturaSeleccionada.setTiporeemplazo(seleccionTiposReemplazos);
                if (!listaEncargaturasCrear.contains(encargaturaSeleccionada)) {
                    if (listaEncargaturasModificar.isEmpty()) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    } else if (!listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    }
                }
            } else {
                encargaturaSeleccionada.setTiporeemplazo(seleccionTiposReemplazos);
                if (!listaEncargaturasCrear.contains(encargaturaSeleccionada)) {
                    if (listaEncargaturasModificar.isEmpty()) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    } else if (!listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevaEncargatura.setTiporeemplazo(seleccionTiposReemplazos);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEncargatura");
        } else if (tipoActualizacion == 2) {
            duplicarEncargatura.setTiporeemplazo(seleccionTiposReemplazos);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEncargatura");

        }
        filtradoslistaTiposReemplazos = null;
        seleccionTiposReemplazos = null;
        aceptar = true;
        encargaturaSeleccionada = null;
        encargaturaSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVTiposReemplazos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTiposReemplazos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposReemplazosDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposReemplazos");
    }

    public void actualizarCargos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            System.out.println("Tipo Lista: " + tipoLista);
            if (tipoLista == 0) {
                encargaturaSeleccionada.setCargo(seleccionCargos);
                if (!listaEncargaturasCrear.contains(encargaturaSeleccionada)) {
                    if (listaEncargaturasModificar.isEmpty()) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    } else if (!listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    }
                }
            } else {
                encargaturaSeleccionada.setCargo(seleccionCargos);
                if (!listaEncargaturasCrear.contains(encargaturaSeleccionada)) {
                    if (listaEncargaturasModificar.isEmpty()) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    } else if (!listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            /*for (int i = 0; i < listaEncargaturas.size(); i++) {
             System.out.println("En la lista encargaturas está:" + listaEncargaturas.get(i).getReemplazado().getPersona().getNombreCompleto());
             System.out.println("Seleccionado: " + seleccionEmpleadosReemplazados.getPersona().getNombreCompleto());
             }*/
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevaEncargatura.setCargo(seleccionCargos);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEncargatura");
        } else if (tipoActualizacion == 2) {
            duplicarEncargatura.setCargo(seleccionCargos);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEncargatura");
        }
        filtradoslistaCargos = null;
        seleccionCargos = null;
        aceptar = true;
        encargaturaSeleccionada = null;
        encargaturaSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVCargos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVCargos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('cargosDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVCargos");
    }

    public void actualizarMotivosReemplazos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                encargaturaSeleccionada.setMotivoreemplazo(seleccionMotivosReemplazos);
                if (!listaEncargaturasCrear.contains(encargaturaSeleccionada)) {
                    if (listaEncargaturasModificar.isEmpty()) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    } else if (!listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    }
                }
            } else {
                encargaturaSeleccionada.setMotivoreemplazo(seleccionMotivosReemplazos);
                if (!listaEncargaturasCrear.contains(encargaturaSeleccionada)) {
                    if (listaEncargaturasModificar.isEmpty()) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    } else if (!listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevaEncargatura.setMotivoreemplazo(seleccionMotivosReemplazos);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEncargatura");
        } else if (tipoActualizacion == 2) {
            duplicarEncargatura.setMotivoreemplazo(seleccionMotivosReemplazos);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEncargatura");

        }
        filtradoslistaTiposReemplazos = null;
        seleccionTiposReemplazos = null;
        aceptar = true;
        encargaturaSeleccionada = null;
        encargaturaSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVMotivosReemplazos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVMotivosReemplazos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('motivosReemplazosDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVMotivosReemplazos");
    }

    public void actualizarEstructuras() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                encargaturaSeleccionada.setEstructura(seleccionEstructuras);
                if (!listaEncargaturasCrear.contains(encargaturaSeleccionada)) {
                    if (listaEncargaturasModificar.isEmpty()) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    } else if (!listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    }
                }
            } else {
                encargaturaSeleccionada.setEstructura(seleccionEstructuras);
                if (!listaEncargaturasCrear.contains(encargaturaSeleccionada)) {
                    if (listaEncargaturasModificar.isEmpty()) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    } else if (!listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
                        listaEncargaturasModificar.add(encargaturaSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
        } else if (tipoActualizacion == 1) {
            nuevaEncargatura.setEstructura(seleccionEstructuras);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEncargatura");
        } else if (tipoActualizacion == 2) {
            duplicarEncargatura.setEstructura(seleccionEstructuras);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEncargatura");

        }
        filtradoslistaEstructuras = null;
        seleccionEstructuras = null;
        aceptar = true;
        encargaturaSeleccionada = null;
        encargaturaSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVEstructuras:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEstructuras').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVEstructuras");
    }

    public void cancelarCambioEstructuras() {
        filtradoslistaEstructuras = null;
        seleccionEstructuras = null;
        aceptar = true;
        encargaturaSeleccionada = null;
        encargaturaSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVEstructuras:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEstructuras').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').hide()");
    }

    public void cancelarCambioEmpleados() {
        filtradoslistaEmpleados = null;
        seleccionEmpleados = null;
        seleccionEmpleadosReemplazados = null;
        aceptar = true;
        encargaturaSeleccionada = null;
        encargaturaSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
    }
    
    public void cancelarCambioEmpleadosAbajo() {
        filtradoslistaEmpleados = null;
        seleccionEmpleados = null;
        seleccionEmpleadosReemplazados = null;
        aceptar = true;
        encargaturaSeleccionada = null;
        encargaturaSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVEmpleadosAbajo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleadosAbajo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empleadosAbajoDialogo').hide()");
    }

    public void cancelarCambioCargos() {
        filtradoslistaCargos = null;
        seleccionCargos = null;
        aceptar = true;
        encargaturaSeleccionada = null;
        encargaturaSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVCargos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVCargos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('cargosDialogo').hide()");
    }

    public void cancelarCambioMotivosReemplazos() {
        filtradoslistaMotivosReemplazos = null;
        seleccionMotivosReemplazos = null;
        aceptar = true;
        encargaturaSeleccionada = null;
        encargaturaSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVMotivosReemplazos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVMotivosReemplazos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('motivosReemplazosDialogo').hide()");
    }

    public void cancelarCambioTiposReemplazos() {
        filtradoslistaTiposReemplazos = null;
        seleccionTiposReemplazos = null;
        aceptar = true;
        encargaturaSeleccionada = null;
        encargaturaSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVTiposReemplazos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTiposReemplazos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposReemplazosDialogo').hide()");
    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void activarCtrlF11() {
        System.out.println("TipoLista= " + tipoLista);
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            nREmpleadoReemplazado = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREmpleadoReemplazado");
            nREmpleadoReemplazado.setFilterStyle("width: 85% !important;");
            nRTiposReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRTiposReemplazos");
            nRTiposReemplazos.setFilterStyle("85%");
            nRFechasPagos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasPagos");
            nRFechasPagos.setFilterStyle("width: 85% !important;");
            nRFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasIniciales");
            nRFechasIniciales.setFilterStyle("width: 85% !important;");
            nRFechasFinales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasFinales");
            nRFechasFinales.setFilterStyle("width: 85% !important;");
            nRCargos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRCargos");
            nRCargos.setFilterStyle("width: 85% !important;");
            nRMotivosReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRMotivosReemplazos");
            nRMotivosReemplazos.setFilterStyle("width: 85% !important;");
            nREstructuras = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREstructuras");
            nREstructuras.setFilterStyle("width: 85% !important;");
            altoTabla = "135";
            RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            nREmpleadoReemplazado = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREmpleadoReemplazado");
            nREmpleadoReemplazado.setFilterStyle("display: none; visibility: hidden;");
            nRTiposReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRTiposReemplazos");
            nRTiposReemplazos.setFilterStyle("display: none; visibility: hidden;");
            nRFechasPagos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasPagos");
            nRFechasPagos.setFilterStyle("display: none; visibility: hidden;");
            nRFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasIniciales");
            nRFechasIniciales.setFilterStyle("display: none; visibility: hidden;");
            nRFechasFinales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasFinales");
            nRFechasFinales.setFilterStyle("display: none; visibility: hidden;");
            nRCargos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRCargos");
            nRCargos.setFilterStyle("display: none; visibility: hidden;");
            nRMotivosReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRMotivosReemplazos");
            nRMotivosReemplazos.setFilterStyle("display: none; visibility: hidden;");
            nREstructuras = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREstructuras");
            nREstructuras.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "155";
            RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
            bandera = 0;
            filtradosListaEncargaturas = null;
            tipoLista = 0;
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEncargaturasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "EncargaturasPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        encargaturaSeleccionada = null;
        encargaturaSeleccionada = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEncargaturasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "EncargaturasXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        encargaturaSeleccionada = null;
        encargaturaSeleccionada = null;
    }

    //CREAR ENCARGATURAS
    public void agregarNuevaNovedadReemplazo() {
        int pasa = 0;
        int pasa2 = 0;
        mensajeValidacion = new String();
        RequestContext context = RequestContext.getCurrentInstance();

        if ((!nuevaEncargatura.getReemplazado().getPersona().getNombreCompleto().equals("  ") && !nuevaEncargatura.getReemplazado().getPersona().getNombreCompleto().equals(" ")) && (!nuevaEncargatura.getCargo().getNombre().equals(" ") && !nuevaEncargatura.getCargo().getNombre().equals(""))) {
            System.out.println("Entro a Inconsistencia");
            RequestContext.getCurrentInstance().update("formularioDialogos:inconsistencia");
            RequestContext.getCurrentInstance().execute("PF('inconsistencia').show()");
            pasa2++;
        }
        if (nuevaEncargatura.getTiporeemplazo().getNombre().equals(" ")) {
            System.out.println("Entro a Tipo Reemplazo");
            mensajeValidacion = mensajeValidacion + " * TipoReemplazo \n";
            pasa++;
        }
        if (nuevaEncargatura.getFechainicial() == null) {
            System.out.println("Entro a Fecha Inicial");
            mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
            pasa++;
        }
        if (nuevaEncargatura.getFechapago() == null) {
            System.out.println("Entro a FechaPago");
            mensajeValidacion = mensajeValidacion + " * Fecha Pago\n";
            pasa++;
        }
        if (nuevaEncargatura.getFechafinal() == null) {
            System.out.println("Entro a FechaFinal");
            mensajeValidacion = mensajeValidacion + " * Fecha Final\n";
            pasa++;
        }

        System.out.println("Valor Pasa: " + pasa);
        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedadReemplazo");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedadReemplazo').show()");
        }

        if (pasa == 0 && pasa2 == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();

                System.out.println("Desactivar");
                System.out.println("TipoLista= " + tipoLista);
                nREmpleadoReemplazado = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREmpleadoReemplazado");
                nREmpleadoReemplazado.setFilterStyle("display: none; visibility: hidden;");
                nRTiposReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRTiposReemplazos");
                nRTiposReemplazos.setFilterStyle("display: none; visibility: hidden;");
                nRFechasPagos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasPagos");
                nRFechasPagos.setFilterStyle("display: none; visibility: hidden;");
                nRFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasIniciales");
                nRFechasIniciales.setFilterStyle("display: none; visibility: hidden;");
                nRFechasFinales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasFinales");
                nRFechasFinales.setFilterStyle("display: none; visibility: hidden;");
                nRCargos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRCargos");
                nRCargos.setFilterStyle("display: none; visibility: hidden;");
                nRMotivosReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRMotivosReemplazos");
                nRMotivosReemplazos.setFilterStyle("display: none; visibility: hidden;");
                nREstructuras = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREstructuras");
                nREstructuras.setFilterStyle("display: none; visibility: hidden;");
                altoTabla = "155";
                RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
                bandera = 0;
                filtradosListaEncargaturas = null;
                tipoLista = 0;

            }
            //REGISTRO A LA LISTA ENCARGATURA .
            k++;
            l = BigInteger.valueOf(k);
            nuevaEncargatura.setSecuencia(l);
            nuevaEncargatura.setEmpleado(seleccionMostrar); //Envia empleado
            listaEncargaturasCrear.add(nuevaEncargatura);
            listaEncargaturas.add(nuevaEncargatura);
            nuevaEncargatura = new Encargaturas();
            nuevaEncargatura.setReemplazado(new Empleados());
            nuevaEncargatura.setTiporeemplazo(new TiposReemplazos());
            nuevaEncargatura.setCargo(new Cargos());
            nuevaEncargatura.setMotivoreemplazo(new MotivosReemplazos());
            nuevaEncargatura.setEstructura(new Estructuras());
            RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevaNovedadReemplazo').hide()");
            encargaturaSeleccionada = null;
            encargaturaSeleccionada = null;
        } else {

        }
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listaEncargaturas.isEmpty()) {
            if (encargaturaSeleccionada != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(encargaturaSeleccionada.getSecuencia(), "ENCARGATURAS");
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
            } else {
                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("ENCARGATURAS")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }

        }
        encargaturaSeleccionada = null;
    }

    //LIMPIAR NUEVO REGISTRO ENCARGATURA
    public void limpiarNuevaEncargatura() {
        nuevaEncargatura = new Encargaturas();
        nuevaEncargatura.setReemplazado(new Empleados());
        nuevaEncargatura.getReemplazado().getPersona().setNombreCompleto(" ");
        nuevaEncargatura.setTiporeemplazo(new TiposReemplazos());
        nuevaEncargatura.getTiporeemplazo().setNombre(" ");
        nuevaEncargatura.setCargo(new Cargos());
        nuevaEncargatura.getCargo().setNombre(" ");
        nuevaEncargatura.setMotivoreemplazo(new MotivosReemplazos());
        nuevaEncargatura.getMotivoreemplazo().setNombre(" ");
        nuevaEncargatura.setEstructura(new Estructuras());
        nuevaEncargatura.getEstructura().setNombre(" ");
        encargaturaSeleccionada = null;
        encargaturaSeleccionada = null;
    }

    //GUARDAR
    public void guardarCambiosEncargaturas() {
        if (guardado == false) {
            System.out.println("Realizando Operaciones Encargaturas");
            if (!listaEncargaturasBorrar.isEmpty()) {
                for (int i = 0; i < listaEncargaturasBorrar.size(); i++) {
                    System.out.println("Borrando...");
                    if (listaEncargaturasBorrar.get(i).getCargo().getSecuencia() == null) {
                        listaEncargaturasBorrar.get(i).setCargo(null);
                    }
                    if (listaEncargaturasBorrar.get(i).getEstructura().getSecuencia() == null) {
                        listaEncargaturasBorrar.get(i).setEstructura(null);
                    }
                    if (listaEncargaturasBorrar.get(i).getMotivoreemplazo().getSecuencia() == null) {
                        listaEncargaturasBorrar.get(i).setMotivoreemplazo(null);
                    }
                    if (listaEncargaturasBorrar.get(i).getReemplazado().getSecuencia() == null) {
                        listaEncargaturasBorrar.get(i).setReemplazado(null);
                    }
                    administrarNovedadesReemplazos.borrarEncargaturas(listaEncargaturasBorrar.get(i));
                }
                System.out.println("Entra");
                listaEncargaturasBorrar.clear();
            }

            if (!listaEncargaturasCrear.isEmpty()) {
                for (int i = 0; i < listaEncargaturasCrear.size(); i++) {
                    System.out.println("Creando...");

                    if (listaEncargaturasCrear.get(i).getMotivoreemplazo().getSecuencia() == null) {
                        listaEncargaturasCrear.get(i).setMotivoreemplazo(null);
                    }
                    if (listaEncargaturasCrear.get(i).getReemplazado().getSecuencia() == null) {
                        listaEncargaturasCrear.get(i).setReemplazado(null);
                    }
                    if (listaEncargaturasCrear.get(i).getCargo().getSecuencia() == null) {
                        listaEncargaturasCrear.get(i).setCargo(null);
                    }
                    if (listaEncargaturasCrear.get(i).getEstructura().getSecuencia() == null) {
                        listaEncargaturasCrear.get(i).setEstructura(null);
                    }
                    administrarNovedadesReemplazos.crearEncargaturas(listaEncargaturasCrear.get(i));

                }
                System.out.println("LimpiaLista");
                listaEncargaturasCrear.clear();
            }
            if (!listaEncargaturasModificar.isEmpty()) {
                administrarNovedadesReemplazos.modificarEncargatura(listaEncargaturasModificar);
                listaEncargaturasModificar.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listaEncargaturas = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");

            //  k = 0;
        }
        System.out.println("Tamaño lista: " + listaEncargaturasCrear.size());
        System.out.println("Valor k: " + k);
        encargaturaSeleccionada = null;
        encargaturaSeleccionada = null;
    }

    //BORRAR ENCARGATURAS
    public void borrarEncargaturas() {

        if (encargaturaSeleccionada != null) {
                if (!listaEncargaturasModificar.isEmpty() && listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
                    int modIndex = listaEncargaturasModificar.indexOf(encargaturaSeleccionada);
                    listaEncargaturasModificar.remove(modIndex);
                    listaEncargaturasBorrar.add(encargaturaSeleccionada);
                } else if (!listaEncargaturasCrear.isEmpty() && listaEncargaturasCrear.contains(encargaturaSeleccionada)) {
                    int crearIndex = listaEncargaturasCrear.indexOf(encargaturaSeleccionada);
                    listaEncargaturasCrear.remove(crearIndex);
                } else {
                    listaEncargaturasBorrar.add(encargaturaSeleccionada);
                }
                listaEncargaturas.remove(encargaturaSeleccionada);

            if (tipoLista == 1) {
                filtradosListaEncargaturas.remove(encargaturaSeleccionada);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
            encargaturaSeleccionada = null;
            encargaturaSeleccionada = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("REEMPLAZADO")) {
            if (tipoNuevo == 1) {
                Reemplazado = nuevaEncargatura.getReemplazado().getPersona().getNombreCompleto();
            } else if (tipoNuevo == 2) {
                Reemplazado = duplicarEncargatura.getReemplazado().getPersona().getNombreCompleto();
            }
        } else if (Campo.equals("TIPOREEMPLAZO")) {
            if (tipoNuevo == 1) {
                TipoReemplazo = nuevaEncargatura.getTiporeemplazo().getNombre();
            } else if (tipoNuevo == 2) {
                TipoReemplazo = duplicarEncargatura.getTiporeemplazo().getNombre();
            }
        } else if (Campo.equals("CARGO")) {
            if (tipoNuevo == 1) {
                Cargo = nuevaEncargatura.getCargo().getNombre();
            } else if (tipoNuevo == 2) {
                Cargo = duplicarEncargatura.getCargo().getNombre();
            }
        } else if (Campo.equals("MOTIVOREEMPLAZO")) {
            if (tipoNuevo == 1) {
                MotivoReemplazo = nuevaEncargatura.getMotivoreemplazo().getNombre();
            } else if (tipoNuevo == 2) {
                MotivoReemplazo = duplicarEncargatura.getMotivoreemplazo().getNombre();
            }
        } else if (Campo.equals("ESTRUCTURA")) {
            if (tipoNuevo == 1) {
                Estructura = nuevaEncargatura.getEstructura().getNombre();
            } else if (tipoNuevo == 2) {
                Estructura = duplicarEncargatura.getEstructura().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("REEMPLAZADO")) {
            if (tipoNuevo == 1) {
                nuevaEncargatura.getReemplazado().getPersona().setNombreCompleto(Reemplazado);
            } else if (tipoNuevo == 2) {
                duplicarEncargatura.getReemplazado().getPersona().setNombreCompleto(Reemplazado);
            }
            for (int i = 0; i < listaEmpleados.size(); i++) {
                if (listaEmpleados.get(i).getPersona().getNombreCompleto().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaEncargatura.setReemplazado(listaEmpleados.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoReemplazado");
                } else if (tipoNuevo == 2) {
                    duplicarEncargatura.setReemplazado(listaEmpleados.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarReemplazado");
                }
                listaEmpleados.clear();
                getListaEmpleados();
            } else {
                RequestContext.getCurrentInstance().update("form:empleadosAbajoDialogo");
                RequestContext.getCurrentInstance().execute("PF('empleadosAbajoDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoReemplazado");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarReemplazado");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("TIPOREEMPLAZO")) {
            if (tipoNuevo == 1) {
                nuevaEncargatura.getTiporeemplazo().setNombre(TipoReemplazo);
            } else if (tipoNuevo == 2) {
                duplicarEncargatura.getTiporeemplazo().setNombre(TipoReemplazo);
            }

            for (int i = 0; i < listaTiposReemplazos.size(); i++) {
                if (listaTiposReemplazos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaEncargatura.setTiporeemplazo(listaTiposReemplazos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoReemplazo");
                } else if (tipoNuevo == 2) {
                    duplicarEncargatura.setTiporeemplazo(listaTiposReemplazos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoReemplazo");
                }
                listaTiposReemplazos.clear();
                getListaTiposReemplazos();
            } else {
                RequestContext.getCurrentInstance().update("form:tiposReemplazosDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposReemplazosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoReemplazo");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicartipoReemplazo");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("CARGO")) {
            if (tipoNuevo == 1) {
                nuevaEncargatura.getCargo().setNombre(Cargo);
            } else if (tipoNuevo == 2) {
                duplicarEncargatura.getCargo().setNombre(Cargo);
            }

            for (int i = 0; i < listaCargos.size(); i++) {
                if (listaCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaEncargatura.setCargo(listaCargos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
                } else if (tipoNuevo == 2) {
                    duplicarEncargatura.setCargo(listaCargos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
                }
                listaCargos.clear();
                getListaCargos();
            } else {
                RequestContext.getCurrentInstance().update("form:cargosDialogo");
                RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
                }
            }

        } else if (confirmarCambio.equalsIgnoreCase("MOTIVOREEMPLAZO")) {
            if (tipoNuevo == 1) {
                nuevaEncargatura.getMotivoreemplazo().setNombre(MotivoReemplazo);
            } else if (tipoNuevo == 2) {
                duplicarEncargatura.getMotivoreemplazo().setNombre(MotivoReemplazo);
            }

            for (int i = 0; i < listaMotivosReemplazos.size(); i++) {
                if (listaMotivosReemplazos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaEncargatura.setMotivoreemplazo(listaMotivosReemplazos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoMotivoReemplazo");
                } else if (tipoNuevo == 2) {
                    duplicarEncargatura.setMotivoreemplazo(listaMotivosReemplazos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivoReemplazo");
                }
                listaMotivosReemplazos.clear();
                getListaMotivosReemplazos();
            } else {
                RequestContext.getCurrentInstance().update("form:motivosReemplazosDialogo");
                RequestContext.getCurrentInstance().execute("PF('motivosReemplazosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoMotivoReemplazo");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivoReemplazo");
                }
            }

        } else if (confirmarCambio.equalsIgnoreCase("ESTRUCTURA")) {
            if (tipoNuevo == 1) {
                nuevaEncargatura.getEstructura().setNombre(Estructura);
            } else if (tipoNuevo == 2) {
                duplicarEncargatura.getEstructura().setNombre(Estructura);
            }

            for (int i = 0; i < listaEstructuras.size(); i++) {
                if (listaEstructuras.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaEncargatura.setEstructura(listaEstructuras.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEstructura");
                } else if (tipoNuevo == 2) {
                    duplicarEncargatura.setEstructura(listaEstructuras.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstructura");
                }
                listaEstructuras.clear();
                getListaEstructuras();
            } else {
                RequestContext.getCurrentInstance().update("form:estructurasDialogo");
                RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEstructura");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstructura");
                }
            }

        }
    }

    //CANCELAR MODIFICACIONES
    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            nREmpleadoReemplazado = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREmpleadoReemplazado");
            nREmpleadoReemplazado.setFilterStyle("display: none; visibility: hidden;");
            nRTiposReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRTiposReemplazos");
            nRTiposReemplazos.setFilterStyle("display: none; visibility: hidden;");
            nRFechasPagos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasPagos");
            nRFechasPagos.setFilterStyle("display: none; visibility: hidden;");
            nRFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasIniciales");
            nRFechasIniciales.setFilterStyle("display: none; visibility: hidden;");
            nRFechasFinales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasFinales");
            nRFechasFinales.setFilterStyle("display: none; visibility: hidden;");
            nRCargos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRCargos");
            nRCargos.setFilterStyle("display: none; visibility: hidden;");
            nRMotivosReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRMotivosReemplazos");
            nRMotivosReemplazos.setFilterStyle("display: none; visibility: hidden;");
            nREstructuras = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREstructuras");
            nREstructuras.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "155";
            RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
            bandera = 0;
            filtradosListaEncargaturas = null;
            tipoLista = 0;
        }

        listaEncargaturasBorrar.clear();
        listaEncargaturasCrear.clear();
        listaEncargaturasModificar.clear();
        encargaturaSeleccionada = null;
        encargaturaSeleccionada = null;
//        k = 0;
        listaEncargaturas = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
    }

    public void salir() {

        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            nREmpleadoReemplazado = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREmpleadoReemplazado");
            nREmpleadoReemplazado.setFilterStyle("display: none; visibility: hidden;");
            nRTiposReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRTiposReemplazos");
            nRTiposReemplazos.setFilterStyle("display: none; visibility: hidden;");
            nRFechasPagos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasPagos");
            nRFechasPagos.setFilterStyle("display: none; visibility: hidden;");
            nRFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasIniciales");
            nRFechasIniciales.setFilterStyle("display: none; visibility: hidden;");
            nRFechasFinales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasFinales");
            nRFechasFinales.setFilterStyle("display: none; visibility: hidden;");
            nRCargos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRCargos");
            nRCargos.setFilterStyle("display: none; visibility: hidden;");
            nRMotivosReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRMotivosReemplazos");
            nRMotivosReemplazos.setFilterStyle("display: none; visibility: hidden;");
            nREstructuras = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREstructuras");
            nREstructuras.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "155";
            RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
            bandera = 0;
            filtradosListaEncargaturas = null;
            tipoLista = 0;
        }

        listaEncargaturasBorrar.clear();
        listaEncargaturasCrear.clear();
        listaEncargaturasModificar.clear();
        encargaturaSeleccionada = null;
        encargaturaSeleccionada = null;
//        k = 0;
        listaEncargaturas = null;
        guardado = true;
        permitirIndex = true;

    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (encargaturaSeleccionada != null) {
            if (tipoLista == 0) {
                editarEncargaturas = encargaturaSeleccionada;
            }
            if (tipoLista == 1) {
                editarEncargaturas = encargaturaSeleccionada;
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarReemplazados");
                RequestContext.getCurrentInstance().execute("PF('editarReemplazados').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTiposReemplazos");
                RequestContext.getCurrentInstance().execute("PF('editarTiposReemplazos').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasPagos");
                RequestContext.getCurrentInstance().execute("PF('editarFechasPagos').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasIniciales");
                RequestContext.getCurrentInstance().execute("PF('editarFechasIniciales').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasFinales");
                RequestContext.getCurrentInstance().execute("PF('editarFechasFinales').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarMotivosReemplazos");
                RequestContext.getCurrentInstance().execute("PF('editarMotivosReemplazos').show()");
            } else if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEstructuras");
                RequestContext.getCurrentInstance().execute("PF('editarEstructuras').show()");
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCargos");
                RequestContext.getCurrentInstance().execute("PF('editarCargos').show()");
                cualCelda = -1;
            }
        }
        encargaturaSeleccionada = null;
        encargaturaSeleccionada = null;
    }

    //DUPLICAR ENCARGATURA
    public void duplicarNR() {
        if (encargaturaSeleccionada != null) {
            duplicarEncargatura = new Encargaturas();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarEncargatura.setSecuencia(l);
                duplicarEncargatura.setEmpleado(encargaturaSeleccionada.getEmpleado());
                duplicarEncargatura.setReemplazado(encargaturaSeleccionada.getReemplazado());
                duplicarEncargatura.setTiporeemplazo(encargaturaSeleccionada.getTiporeemplazo());
                duplicarEncargatura.setFechapago(encargaturaSeleccionada.getFechapago());
                duplicarEncargatura.setFechainicial(encargaturaSeleccionada.getFechainicial());
                duplicarEncargatura.setFechafinal(encargaturaSeleccionada.getFechafinal());
                duplicarEncargatura.setCargo(encargaturaSeleccionada.getCargo());
                duplicarEncargatura.setMotivoreemplazo(encargaturaSeleccionada.getMotivoreemplazo());
                duplicarEncargatura.setEstructura(encargaturaSeleccionada.getEstructura());
            }
            if (tipoLista == 1) {
                duplicarEncargatura.setSecuencia(l);
                duplicarEncargatura.setEmpleado(encargaturaSeleccionada.getEmpleado());
                duplicarEncargatura.setReemplazado(encargaturaSeleccionada.getReemplazado());
                duplicarEncargatura.setTiporeemplazo(encargaturaSeleccionada.getTiporeemplazo());
                duplicarEncargatura.setFechapago(encargaturaSeleccionada.getFechapago());
                duplicarEncargatura.setFechainicial(encargaturaSeleccionada.getFechainicial());
                duplicarEncargatura.setFechafinal(encargaturaSeleccionada.getFechafinal());
                duplicarEncargatura.setCargo(encargaturaSeleccionada.getCargo());
                duplicarEncargatura.setMotivoreemplazo(encargaturaSeleccionada.getMotivoreemplazo());
                duplicarEncargatura.setEstructura(encargaturaSeleccionada.getEstructura());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEncargatura");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroEncargatura').show()");
            encargaturaSeleccionada = null;
            encargaturaSeleccionada = null;
        }
    }

    public void confirmarDuplicar() {

        int pasa2 = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        if ((!duplicarEncargatura.getReemplazado().getPersona().getNombreCompleto().equals("  ") && !duplicarEncargatura.getReemplazado().getPersona().getNombreCompleto().equals(" ")) && (!duplicarEncargatura.getCargo().getNombre().equals(" ") && !duplicarEncargatura.getCargo().getNombre().equals(""))) {
            System.out.println("Entro a Inconsistencia");
            RequestContext.getCurrentInstance().update("formularioDialogos:inconsistencia");
            RequestContext.getCurrentInstance().execute("PF('inconsistencia').show()");
            pasa2++;
        }

        if (pasa2 == 0) {
            listaEncargaturas.add(duplicarEncargatura);
            listaEncargaturasCrear.add(duplicarEncargatura);

            RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
            encargaturaSeleccionada = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();

                nREmpleadoReemplazado = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREmpleadoReemplazado");
                nREmpleadoReemplazado.setFilterStyle("display: none; visibility: hidden;");
                nRTiposReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRTiposReemplazos");
                nRTiposReemplazos.setFilterStyle("display: none; visibility: hidden;");
                nRFechasPagos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasPagos");
                nRFechasPagos.setFilterStyle("display: none; visibility: hidden;");
                nRFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasIniciales");
                nRFechasIniciales.setFilterStyle("display: none; visibility: hidden;");
                nRFechasFinales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasFinales");
                nRFechasFinales.setFilterStyle("display: none; visibility: hidden;");
                nRCargos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRCargos");
                nRCargos.setFilterStyle("display: none; visibility: hidden;");
                nRMotivosReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRMotivosReemplazos");
                nRMotivosReemplazos.setFilterStyle("display: none; visibility: hidden;");
                nREstructuras = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREstructuras");
                nREstructuras.setFilterStyle("display: none; visibility: hidden;");
                altoTabla = "155";
                RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
                bandera = 0;
                filtradosListaEncargaturas = null;
                tipoLista = 0;
            }
            duplicarEncargatura = new Encargaturas();
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroEncargatura");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroEncargatura').hide()");
        }
    }
    //LIMPIAR DUPLICAR

    /**
     * Metodo que limpia los datos de un duplicar Encargaturas
     */
    public void limpiarduplicarEncargaturas() {
        duplicarEncargatura = new Encargaturas();
        duplicarEncargatura.setReemplazado(new Empleados());
        duplicarEncargatura.setTiporeemplazo(new TiposReemplazos());
        duplicarEncargatura.setCargo(new Cargos());
        duplicarEncargatura.setMotivoreemplazo(new MotivosReemplazos());
        duplicarEncargatura.setEstructura(new Estructuras());
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (encargaturaSeleccionada != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:empleadosAbajoDialogo");
                RequestContext.getCurrentInstance().execute("PF('empleadosAbajoDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposReemplazosDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposReemplazosDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:motivosReemplazosDialogo");
                RequestContext.getCurrentInstance().execute("PF('motivosReemplazosDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:estructurasDialogo");
                RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').show()");
            }
        }
    }

    //Getter & Setter
    public Empleados getEmpleado() {
        if (tipoPantalla != null && tipoPantalla.equals("UNO") && empleado == null) {
            empleado = administrarNovedadesReemplazos.encontrarEmpleado(secuenciaEmpleado);
        } else {
            empleado = new Empleados();
        }

        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public List<Encargaturas> getListaEncargaturas() {
        if (listaEncargaturas == null && seleccionMostrar != null) {
            listaEncargaturas = administrarNovedadesReemplazos.encargaturasEmpleado(seleccionMostrar.getSecuencia());
        }
        return listaEncargaturas;
    }

    public void setListaEncargaturas(List<Encargaturas> listaEncargaturas) {
        this.listaEncargaturas = listaEncargaturas;
    }

    public List<Encargaturas> getFiltradosListaEncargaturas() {
        return filtradosListaEncargaturas;
    }

    public void setFiltradosListaEncargaturas(List<Encargaturas> filtradosListaEncargaturas) {
        this.filtradosListaEncargaturas = filtradosListaEncargaturas;
    }

    public List<TiposReemplazos> getListaTiposReemplazos() {
        if (listaTiposReemplazos == null) {
            listaTiposReemplazos = administrarNovedadesReemplazos.lovTiposReemplazos();
        }
        return listaTiposReemplazos;
    }

    public void setListaTiposReemplazos(List<TiposReemplazos> listaTiposReemplazos) {
        this.listaTiposReemplazos = listaTiposReemplazos;
    }

    public List<MotivosReemplazos> getListaMotivosReemplazos() {
        if (listaMotivosReemplazos == null) {
            listaMotivosReemplazos = administrarNovedadesReemplazos.lovMotivosReemplazos();
        }
        return listaMotivosReemplazos;
    }

    public void setListaMotivosReemplazos(List<MotivosReemplazos> listaMotivosReemplazos) {
        this.listaMotivosReemplazos = listaMotivosReemplazos;
    }

    public List<Estructuras> getListaEstructuras() {
        if (listaEstructuras == null) {
            listaEstructuras = administrarNovedadesReemplazos.lovEstructuras();
        }
        return listaEstructuras;
    }

    public void setListaEstructuras(List<Estructuras> listaEstructuras) {
        this.listaEstructuras = listaEstructuras;
    }

    public List<Empleados> getListaEmpleados() {
        if (listaEmpleados == null) {
            listaEmpleados = administrarNovedadesReemplazos.lovEmpleados();
        }
        return listaEmpleados;
    }

    public void setListaEmpleados(List<Empleados> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    public List<Empleados> getListaFalsaEmpleados() {
        return listaFalsaEmpleados;
    }

    public void setListaFalsaEmpleados(List<Empleados> listaFalsaEmpleados) {
        this.listaFalsaEmpleados = listaFalsaEmpleados;
    }

    public List<TiposReemplazos> getFiltradoslistaTiposReemplazos() {
        return filtradoslistaTiposReemplazos;
    }

    public void setFiltradoslistaTiposReemplazos(List<TiposReemplazos> filtradoslistaTiposReemplazos) {
        this.filtradoslistaTiposReemplazos = filtradoslistaTiposReemplazos;
    }

    public List<MotivosReemplazos> getFiltradoslistaMotivosReemplazos() {
        return filtradoslistaMotivosReemplazos;
    }

    public void setFiltradoslistaMotivosReemplazos(List<MotivosReemplazos> filtradoslistaMotivosReemplazos) {
        this.filtradoslistaMotivosReemplazos = filtradoslistaMotivosReemplazos;
    }

    public List<Estructuras> getFiltradoslistaEstructuras() {
        return filtradoslistaEstructuras;
    }

    public void setFiltradoslistaEstructuras(List<Estructuras> filtradoslistaEstructuras) {
        this.filtradoslistaEstructuras = filtradoslistaEstructuras;
    }

    public List<Empleados> getFiltradoslistaEmpleados() {
        return filtradoslistaEmpleados;
    }

    public void setFiltradoslistaEmpleados(List<Empleados> filtradoslistaEmpleados) {
        this.filtradoslistaEmpleados = filtradoslistaEmpleados;
    }

    public List<Empleados> getFiltradosListaFalsaEmpleados() {
        return filtradosListaFalsaEmpleados;
    }

    public void setFiltradosListaFalsaEmpleados(List<Empleados> filtradoListaFalsaEmpleados) {
        this.filtradosListaFalsaEmpleados = filtradoListaFalsaEmpleados;
    }

    public TiposReemplazos getSeleccionTiposReemplazos() {
        return seleccionTiposReemplazos;
    }

    public void setSeleccionTiposReemplazos(TiposReemplazos seleccionTiposReemplazos) {
        this.seleccionTiposReemplazos = seleccionTiposReemplazos;
    }

    public MotivosReemplazos getSeleccionMotivosReemplazos() {
        return seleccionMotivosReemplazos;
    }

    public void setSeleccionMotivosReemplazos(MotivosReemplazos seleccionMotivosReemplazos) {
        this.seleccionMotivosReemplazos = seleccionMotivosReemplazos;
    }

    public Estructuras getSeleccionEstructuras() {
        return seleccionEstructuras;
    }

    public void setSeleccionEstructuras(Estructuras seleccionEstructuras) {
        this.seleccionEstructuras = seleccionEstructuras;
    }

    public Empleados getSeleccionEmpleados() {
        return seleccionEmpleados;
    }

    public void setSeleccionEmpleados(Empleados seleccionEmpleados) {
        this.seleccionEmpleados = seleccionEmpleados;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public Empleados getSeleccionEmpleadosReemplazados() {
        return seleccionEmpleadosReemplazados;
    }

    public void setSeleccionEmpleadosReemplazados(Empleados seleccionEmpleadosReemplazados) {
        this.seleccionEmpleadosReemplazados = seleccionEmpleadosReemplazados;
    }

    public Encargaturas getEditarEncargaturas() {
        return editarEncargaturas;
    }

    public void setEditarEncargaturas(Encargaturas editarEncargaturas) {
        this.editarEncargaturas = editarEncargaturas;
    }

    public List<Cargos> getListaCargos() {
        if (listaCargos == null) {
            listaCargos = administrarNovedadesReemplazos.lovCargos();
        }
        return listaCargos;
    }

    public void setListaCargos(List<Cargos> listaCargos) {
        this.listaCargos = listaCargos;
    }

    public List<Cargos> getFiltradoslistaCargos() {
        return filtradoslistaCargos;
    }

    public void setFiltradoslistaCargos(List<Cargos> filtradoslistaCargos) {
        this.filtradoslistaCargos = filtradoslistaCargos;
    }

    public Cargos getSeleccionCargos() {
        return seleccionCargos;
    }

    public void setSeleccionCargos(Cargos seleccionCargos) {
        this.seleccionCargos = seleccionCargos;
    }

    public Encargaturas getNuevaEncargatura() {
        return nuevaEncargatura;
    }

    public void setNuevaEncargatura(Encargaturas nuevaEncargatura) {
        this.nuevaEncargatura = nuevaEncargatura;
    }

    public Encargaturas getDuplicarEncargatura() {
        return duplicarEncargatura;
    }

    public void setDuplicarEncargatura(Encargaturas duplicarEncargatura) {
        this.duplicarEncargatura = duplicarEncargatura;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public Empleados getSeleccionMostrar() {
        return seleccionMostrar;
    }

    public void setSeleccionMostrar(Empleados seleccionMostrar) {
        this.seleccionMostrar = seleccionMostrar;
    }

    public boolean isBotones() {
        return botones;
    }

    public Encargaturas getEncargaturaSeleccionada() {
        return encargaturaSeleccionada;
    }

    public void setEncargaturaSeleccionada(Encargaturas encargaturaSeleccionada) {
        this.encargaturaSeleccionada = encargaturaSeleccionada;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }
    
}
