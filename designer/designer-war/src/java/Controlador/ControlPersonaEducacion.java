package Controlador;

import Entidades.AdiestramientosF;
import Entidades.AdiestramientosNF;
import Entidades.Cursos;
import Entidades.Empleados;
import Entidades.Instituciones;
import Entidades.Personas;
import Entidades.Profesiones;
import Entidades.TiposEducaciones;
import Entidades.VigenciasFormales;
import Entidades.VigenciasNoFormales;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciasFormalesInterface;
import InterfaceAdministrar.AdministrarVigenciasNoFormalesInterface;
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
public class ControlPersonaEducacion implements Serializable {

    @EJB
    AdministrarVigenciasNoFormalesInterface administrarVigenciasNoFormales;
    @EJB
    AdministrarVigenciasFormalesInterface administrarVigenciasFormales;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //SECUENCIA DE LA PERSONA
    private BigInteger secuenciaPersona;
    private Personas persona;
    //LISTA VIGENCIAS FORMALES
    private List<VigenciasFormales> listaVigenciasFormales;
    private List<VigenciasFormales> filtradosListaVigenciasFormales;
    private VigenciasFormales vigenciaFormalSeleccionada;
    //LISTA VIGENCIAS NO FORMALES
    private List<VigenciasNoFormales> listaVigenciasNoFormales;
    private List<VigenciasNoFormales> filtradosListaVigenciasNoFormales;
    private VigenciasNoFormales vigenciaNoFormalSeleccionada;
    //L.O.V EDUCACION
    private List<TiposEducaciones> listaTiposEducaciones;
    private List<TiposEducaciones> filtradoslistaTiposEducaciones;
    private TiposEducaciones seleccionTiposEducaciones;
    //L.O.V CURSOS
    private List<Cursos> listaCursos;
    private List<Cursos> filtradoslistaCursos;
    private Cursos seleccionCursos;
    //L.O.V PROFESION
    private List<Profesiones> listaProfesiones;
    private List<Profesiones> filtradoslistaProfesiones;
    private Profesiones seleccionProfesiones;
    //L.O.V INSTITUCIONES
    private List<Instituciones> listaInstituciones;
    private List<Instituciones> filtradoslistaInstituciones;
    private Instituciones seleccionInstituciones;
    //L.O.V ADIESTRAMIENTOS FORMALES
    private List<AdiestramientosF> listaAdiestramientosFormales;
    private List<AdiestramientosF> filtradoslistaAdiestramientosFormales;
    private AdiestramientosF seleccionAdiestramientosFormales;
    //L.O.V ADIESTRAMIENTOS No FORMALES
    private List<AdiestramientosNF> listaAdiestramientosNoFormales;
    private List<AdiestramientosNF> filtradoslistaAdiestramientosNoFormales;
    private AdiestramientosNF seleccionAdiestramientosNoFormales;
    //OTROS
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private int banderaNF;
    private boolean permitirIndex;
    //Crear Vigencias 
    public VigenciasFormales nuevaVigenciaFormal;
    public VigenciasNoFormales nuevaVigenciaNoFormal;
    private List<VigenciasFormales> listaVigenciasFormalesCrear;
    private int k;
    private BigInteger l;
    private int m;
    private BigInteger n;
    private String mensajeValidacion;
    private String mensajeValidacionNF;
    //Crear Vigencias No Formales
    private List<VigenciasNoFormales> listaVigenciasNoFormalesCrear;
    //Modificar Vigencias Formales
    private List<VigenciasFormales> listaVigenciasFormalesModificar;
    private boolean guardado, guardarOk;
    //Modificar Vigencias No Formales
    private List<VigenciasNoFormales> listaVigenciasNoFormalesModificar;
    //Borrar VigenciasFormales
    private List<VigenciasFormales> listaVigenciasFormalesBorrar;
    //Borrar Vigencias No Formales
    private List<VigenciasNoFormales> listaVigenciasNoFormalesBorrar;
    //editar celda
    private VigenciasFormales editarVigenciasFormales;
    private VigenciasNoFormales editarVigenciasNoFormales;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista, tipoListaNF;
    //AUTOCOMPLETAR
    private String Fecha, TipoEducacion, Profesion, Institucion, AdiestramientoF;
    private String Curso, AdiestramientoNF;
    //Columnas Tabla Vigencias Formales
    private Column pEFechas, pETiposEducaciones, pEProfesiones, pEInstituciones, pEAdiestramientosF, pECalificaciones;
    private Column pENumerosTarjetas, pEFechasExpediciones, pEFechasVencimientos, pEObservaciones;
    //Columnas Tabla Vigencias No Formales
    private Column pEFechasNF, pECursosNF, pETitulosNF, pEInstitucionesNF, pEAdiestramientosNF, pECalificacionesNF, pEObservacionesNF;
    //Duplicar
    private VigenciasFormales duplicarVigenciaFormal;
    private VigenciasNoFormales duplicarVigenciaNoFormal;
    //Cual Tabla
    private int CualTabla;
    //Tabla a Imprimir
    private String tablaImprimir, nombreArchivo;
    //Cual Insertar
    private String cualInsertar;
    //Cual Nuevo Update
    private String cualNuevo;
    public String altoTabla1;
    public String altoTabla2;
    private Empleados empleado;
    private String infoRegistroF, infoRegistroNF, infoRegistroEducacion, infoRegistroCursos, infoRegistrosProfesion, infoRegistroInstituciones, infoRegistroInstitucionesF, infoRegistroAdiestramientosF, infoRegistroAdiestramientosNF;
    private boolean activarLov;
    private DataTable tablaC, tablaC2;

    public ControlPersonaEducacion() {
        permitirIndex = true;
        //secuenciaPersona = BigInteger.valueOf(10668967);
        aceptar = true;
        listaVigenciasNoFormalesBorrar = new ArrayList<VigenciasNoFormales>();
        listaVigenciasNoFormalesCrear = new ArrayList<VigenciasNoFormales>();
        listaVigenciasNoFormalesModificar = new ArrayList<VigenciasNoFormales>();
        listaVigenciasFormalesBorrar = new ArrayList<VigenciasFormales>();
        listaVigenciasFormalesCrear = new ArrayList<VigenciasFormales>();
        listaVigenciasFormalesModificar = new ArrayList<VigenciasFormales>();
        //INICIALIZAR LOVS
        listaCursos = new ArrayList<Cursos>();
        listaAdiestramientosNoFormales = new ArrayList<AdiestramientosNF>();
        listaTiposEducaciones = new ArrayList<TiposEducaciones>();
        listaProfesiones = new ArrayList<Profesiones>();
        listaInstituciones = new ArrayList<Instituciones>();
        listaAdiestramientosFormales = new ArrayList<AdiestramientosF>();
        vigenciaFormalSeleccionada = null;
        //editar
        editarVigenciasFormales = new VigenciasFormales();
        editarVigenciasNoFormales = new VigenciasNoFormales();
        cambioEditor = false;
        aceptarEditar = true;
        cualCelda = -1;
        tipoLista = 0;
        tipoListaNF = 0;
        //Crear Vigencia Formal
        nuevaVigenciaFormal = new VigenciasFormales();
        nuevaVigenciaFormal.setTipoeducacion(new TiposEducaciones());
        nuevaVigenciaFormal.setProfesion(new Profesiones());
        nuevaVigenciaFormal.setInstitucion(new Instituciones());
        nuevaVigenciaFormal.setAdiestramientof(new AdiestramientosF());
        nuevaVigenciaNoFormal = new VigenciasNoFormales();
        nuevaVigenciaNoFormal.setCurso(new Cursos());
        nuevaVigenciaNoFormal.setInstitucion(new Instituciones());
        nuevaVigenciaNoFormal.setAdiestramientonf(new AdiestramientosNF());
        guardado = true;
        tablaImprimir = ":formExportar:datosVigenciasFormalesExportar";
        nombreArchivo = "VigenciasFormalesXML";
        k = 0;
        cualInsertar = ":formularioDialogos:NuevoRegistroVigenciaFormal";
        cualNuevo = ":formularioDialogos:nuevaVigenciaFormal";
        m = 0;
        altoTabla1 = "115";
        altoTabla2 = "115";
        empleado = new Empleados();
        activarLov = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarVigenciasNoFormales.obtenerConexion(ses.getId());
            administrarVigenciasFormales.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPersona(BigInteger secEmpl) {
        secuenciaPersona = secEmpl;
        empleado = administrarVigenciasFormales.empleadoActual(secEmpl);
        //persona = null;
        getPersona();
        listaVigenciasFormales = null;
        getListaVigenciasFormales();
        listaVigenciasNoFormales = null;
        getListaVigenciasNoFormales();
        listaTiposEducaciones = null;
        //getListaTiposEducaciones();
        listaProfesiones = null;
       // getListaProfesiones();
        listaInstituciones = null;
        //getListaInstituciones();
        listaAdiestramientosFormales = null;
        listaCursos = null;
        listaAdiestramientosNoFormales = null;
        //getListaAdiestramientosFormales();
        //getListaAdiestramientosNoFormales();
        aceptar = true;
        contarRegistrosF();
        contarRegistrosNF();
        if (!listaVigenciasFormales.isEmpty()) {
            vigenciaFormalSeleccionada = listaVigenciasFormales.get(0);
        }

    }

    //Ubicacion Celda.
    public void cambiarIndice(VigenciasFormales vigenciaformal, int celda) {
        if (permitirIndex == true) {
            vigenciaFormalSeleccionada = vigenciaformal;
            cualCelda = celda;
            CualTabla = 0;
            deshabilitarBotonLov();
            tablaImprimir = ":formExportar:datosVigenciasFormalesExportar";
            nombreArchivo = "VigenciasFormalesXML";
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:exportarXML");
            if (tipoLista == 0) {
                vigenciaFormalSeleccionada.getSecuencia();
                if (cualCelda == 1) {
                    habilitarBotonLov();
                    TipoEducacion = vigenciaFormalSeleccionada.getTipoeducacion().getNombre();
                } else if (cualCelda == 2) {
                    habilitarBotonLov();
                    Profesion = vigenciaFormalSeleccionada.getProfesion().getDescripcion();
                } else if (cualCelda == 3) {
                    habilitarBotonLov();
                    Institucion = vigenciaFormalSeleccionada.getInstitucion().getDescripcion();
                } else if (cualCelda == 4) {
                    habilitarBotonLov();
                    AdiestramientoF = vigenciaFormalSeleccionada.getAdiestramientof().getDescripcion();
                }
            } else {
                vigenciaFormalSeleccionada.getSecuencia();
                if (cualCelda == 1) {
                    habilitarBotonLov();
                    TipoEducacion = vigenciaFormalSeleccionada.getTipoeducacion().getNombre();
                } else if (cualCelda == 2) {
                    habilitarBotonLov();
                    Profesion = vigenciaFormalSeleccionada.getProfesion().getDescripcion();
                } else if (cualCelda == 3) {
                    habilitarBotonLov();
                    Institucion = vigenciaFormalSeleccionada.getInstitucion().getDescripcion();
                } else if (cualCelda == 4) {
                    habilitarBotonLov();
                    AdiestramientoF = vigenciaFormalSeleccionada.getAdiestramientof().getDescripcion();
                }
            }
        }
    }

//AUTOCOMPLETAR
    public void modificarVigenciasFormales(VigenciasFormales vigenciaformal, String confirmarCambio, String valorConfirmar) {
        vigenciaFormalSeleccionada = vigenciaformal;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {

                    if (listaVigenciasFormalesModificar.isEmpty()) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listaVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
                vigenciaFormalSeleccionada = null;
                vigenciaFormalSeleccionada = null;

            } else {
                if (!listaVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {

                    if (listaVigenciasFormalesModificar.isEmpty()) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listaVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
                vigenciaFormalSeleccionada = null;
                vigenciaFormalSeleccionada = null;
            }
            context.update("form:datosVigenciasFormalesPersona");
        } else if (confirmarCambio.equalsIgnoreCase("TIPOEDUCACION")) {
            if (tipoLista == 0) {
                vigenciaFormalSeleccionada.getTipoeducacion().setNombre(TipoEducacion);
            } else {
                vigenciaFormalSeleccionada.getTipoeducacion().setNombre(TipoEducacion);
            }

            for (int i = 0; i < listaTiposEducaciones.size(); i++) {
                if (listaTiposEducaciones.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    vigenciaFormalSeleccionada.setTipoeducacion(listaTiposEducaciones.get(indiceUnicoElemento));
                } else {
                    vigenciaFormalSeleccionada.setTipoeducacion(listaTiposEducaciones.get(indiceUnicoElemento));
                }
                listaTiposEducaciones.clear();
                getListaTiposEducaciones();
            } else {
                permitirIndex = false;
                context.update("formularioDialogos:tiposEducacionesDialogo");
                context.execute("tiposEducacionesDialogo.show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("PROFESION")) {
            if (tipoLista == 0) {
                vigenciaFormalSeleccionada.getProfesion().setDescripcion(Profesion);
            } else {
                vigenciaFormalSeleccionada.getProfesion().setDescripcion(Profesion);
            }
            for (int i = 0; i < listaProfesiones.size(); i++) {
                if (listaProfesiones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    vigenciaFormalSeleccionada.setProfesion(listaProfesiones.get(indiceUnicoElemento));
                } else {
                    vigenciaFormalSeleccionada.setProfesion(listaProfesiones.get(indiceUnicoElemento));
                }
                listaProfesiones.clear();
                getListaProfesiones();
            } else {
                permitirIndex = false;
                context.update("formularioDialogos:profesionesDialogo");
                context.execute("profesionesDialogo.show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("INSTITUCION")) {
            if (tipoLista == 0) {
                vigenciaFormalSeleccionada.getInstitucion().setDescripcion(Institucion);
            } else {
                vigenciaFormalSeleccionada.getInstitucion().setDescripcion(Institucion);
            }
            for (int i = 0; i < listaInstituciones.size(); i++) {
                if (listaInstituciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    vigenciaFormalSeleccionada.setInstitucion(listaInstituciones.get(indiceUnicoElemento));
                } else {
                    vigenciaFormalSeleccionada.setInstitucion(listaInstituciones.get(indiceUnicoElemento));
                }
                listaInstituciones.clear();
                getListaInstituciones();
            } else {
                permitirIndex = false;
                context.update("formularioDialogos:institucionesDialogo");
                context.execute("institucionesDialogo.show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("ADIESTRAMENTOF")) {
            if (tipoLista == 0) {
                vigenciaFormalSeleccionada.getAdiestramientof().setDescripcion(AdiestramientoF);
            } else {
                vigenciaFormalSeleccionada.getAdiestramientof().setDescripcion(AdiestramientoF);
            }
            for (int i = 0; i < listaAdiestramientosFormales.size(); i++) {
                if (listaAdiestramientosFormales.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    vigenciaFormalSeleccionada.setAdiestramientof(listaAdiestramientosFormales.get(indiceUnicoElemento));
                } else {
                    vigenciaFormalSeleccionada.setAdiestramientof(listaAdiestramientosFormales.get(indiceUnicoElemento));
                }
                listaProfesiones.clear();
                getListaProfesiones();
            } else {
                permitirIndex = false;
                context.update("formularioDialogos:profesionesDialogo");
                context.execute("profesionesDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listaVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                    if (listaVigenciasFormalesModificar.isEmpty()) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listaVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
            } else {
                if (!listaVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {

                    if (listaVigenciasFormalesModificar.isEmpty()) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listaVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
            }
        }
        context.update("form:datosVigenciasFormalesPersona");
    }

    //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO)
    public void asignarIndex(VigenciasFormales vigenciaFormal, int dlg, int LND) {
        vigenciaFormalSeleccionada = vigenciaFormal;
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            deshabilitarBotonLov();
            tipoActualizacion = 0;
        } else if (LND == 1) {
            deshabilitarBotonLov();
            tipoActualizacion = 1;
            System.out.println("Tipo Actualizacion: " + tipoActualizacion);
        } else if (LND == 2) {
            deshabilitarBotonLov();
            tipoActualizacion = 2;
        }
        if (dlg == 0) {
            habilitarBotonLov();
            modificarInfoRegistroEducacion(listaTiposEducaciones.size());
            RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEducacion");
            context.update("form:tiposEducacionesDialogo");
            context.execute("tiposEducacionesDialogo.show()");
        } else if (dlg == 1) {
            habilitarBotonLov();
            modificarInfoRegistroProfesion(listaProfesiones.size());
            RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistrosProfesion");
            context.update("form:profesionesDialogo");
            context.execute("profesionesDialogo.show()");
        } else if (dlg == 2) {
            habilitarBotonLov();
            modificarInfoRegistroInstitucionesF(listaInstituciones.size());
            RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroInstitucionesF");
            context.update("form:institucionesDialogo");
            context.execute("institucionesDialogo.show()");
        } else if (dlg == 3) {
            habilitarBotonLov();
            modificarInfoRegistroAdiestramientoF(listaAdiestramientosFormales.size());
            RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroAdiestramientosF");
            context.update("form:adiestramientosFDialogo");
            context.execute("adiestramientosFDialogo.show()");
        }

    }

    public void actualizarTiposEducaciones() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                vigenciaFormalSeleccionada.setTipoeducacion(seleccionTiposEducaciones);
                if (!listaVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                    if (listaVigenciasFormalesModificar.isEmpty()) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listaVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                }
            } else {
                vigenciaFormalSeleccionada.setTipoeducacion(seleccionTiposEducaciones);
                if (!listaVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                    if (listaVigenciasFormalesModificar.isEmpty()) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listaVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            permitirIndex = true;
            context.update("form:datosVigenciasFormalesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaFormal.setTipoeducacion(seleccionTiposEducaciones);
            context.update("formularioDialogos:nuevaVigenciaFormal");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaFormal.setTipoeducacion(seleccionTiposEducaciones);
            context.update("formularioDialogos:duplicarVigenciaFormal");
        }
        filtradoslistaTiposEducaciones = null;
        seleccionTiposEducaciones = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVTiposEducaciones:globalFilter");
        context.execute("LOVTiposEducaciones.clearFilters()");
        context.execute("tiposEducacionesDialogo.hide()");
        //context.update("formularioDialogos:LOVTiposEducaciones");
    }

    public void actualizarProfesiones() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                vigenciaFormalSeleccionada.setProfesion(seleccionProfesiones);
                if (!listaVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                    if (listaVigenciasFormalesModificar.isEmpty()) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listaVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                }
            } else {
                vigenciaFormalSeleccionada.setProfesion(seleccionProfesiones);
                if (!listaVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                    if (listaVigenciasFormalesModificar.isEmpty()) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listaVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            permitirIndex = true;
            context.update("form:datosVigenciasFormalesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaFormal.setProfesion(seleccionProfesiones);
            context.update("formularioDialogos:nuevaVigenciaFormal");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaFormal.setProfesion(seleccionProfesiones);
            context.update("formularioDialogos:duplicarVigenciaFormal");
        }
        filtradoslistaProfesiones = null;
        seleccionProfesiones = null;
        aceptar = true;
        vigenciaFormalSeleccionada = null;
        vigenciaFormalSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVProfesiones:globalFilter");
        context.execute("LOVProfesiones.clearFilters()");
        context.execute("profesionesDialogo.hide()");
        //context.update("formularioDialogos:LOVProfesiones");
    }

    public void actualizarInstituciones() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                vigenciaFormalSeleccionada.setInstitucion(seleccionInstituciones);
                if (!listaVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                    if (listaVigenciasFormalesModificar.isEmpty()) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listaVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                }
            } else {
                vigenciaFormalSeleccionada.setInstitucion(seleccionInstituciones);
                if (!listaVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                    if (listaVigenciasFormalesModificar.isEmpty()) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listaVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            permitirIndex = true;
            context.update("form:datosVigenciasFormalesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaFormal.setInstitucion(seleccionInstituciones);
            context.update("formularioDialogos:nuevaVigenciaFormal");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaFormal.setInstitucion(seleccionInstituciones);
            context.update("formularioDialogos:duplicarVigenciaFormal");
        }
        filtradoslistaInstituciones = null;
        seleccionInstituciones = null;
        aceptar = true;
        vigenciaFormalSeleccionada = null;
        vigenciaFormalSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVInstituciones:globalFilter");
        context.execute("LOVInstituciones.clearFilters()");
        context.execute("institucionesDialogo.hide()");
        //context.update("formularioDialogos:LOVInstituciones");
    }

    public void actualizarAdiestramientoF() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                vigenciaFormalSeleccionada.setAdiestramientof(seleccionAdiestramientosFormales);
                if (!listaVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                    if (listaVigenciasFormalesModificar.isEmpty()) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listaVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                }
            } else {
                vigenciaFormalSeleccionada.setAdiestramientof(seleccionAdiestramientosFormales);
                if (!listaVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                    if (listaVigenciasFormalesModificar.isEmpty()) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listaVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            permitirIndex = true;
            context.update("form:datosVigenciasFormalesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaFormal.setAdiestramientof(seleccionAdiestramientosFormales);
            context.update("formularioDialogos:nuevaVigenciaFormal");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaFormal.setAdiestramientof(seleccionAdiestramientosFormales);
            context.update("formularioDialogos:duplicarVigenciaFormal");
        }
        filtradoslistaAdiestramientosFormales = null;
        seleccionAdiestramientosFormales = null;
        aceptar = true;
        vigenciaFormalSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVAdiestramientosF:globalFilter");
        context.execute("LOVAdiestramientosF.clearFilters()");
        context.execute("adiestramientosFDialogo.hide()");
        //context.update("formularioDialogos:LOVAdiestramientosF");
    }

    public void cancelarCambioTiposEducaciones() {
        filtradoslistaTiposEducaciones = null;
        seleccionTiposEducaciones = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVTiposEducaciones:globalFilter");
        context.execute("LOVTiposEducaciones.clearFilters()");
        context.execute("tiposEducacionesDialogo.hide()");
    }

    public void cancelarCambioProfesiones() {
        filtradoslistaProfesiones = null;
        seleccionProfesiones = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVProfesiones:globalFilter");
        context.execute("LOVProfesiones.clearFilters()");
        context.execute("profesionesDialogo.hide()");
    }

    public void cancelarCambioInstituciones() {
        filtradoslistaInstituciones = null;
        seleccionInstituciones = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVInstituciones:globalFilter");
        context.execute("LOVInstituciones.clearFilters()");
        context.execute("institucionesDialogo.hide()");
    }

    public void cancelarCambioAdiestramientoF() {
        filtradoslistaAdiestramientosFormales = null;
        seleccionAdiestramientosFormales = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVAdiestramientosF:globalFilter");
        context.execute("LOVAdiestramientosF.clearFilters()");
        context.execute("adiestramientosFDialogo.hide()");
    }

    public void activarAceptar() {
        aceptar = false;
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (vigenciaFormalSeleccionada != null) {
            if (CualTabla == 0) {

                if (tipoLista == 0) {
                    editarVigenciasFormales = vigenciaFormalSeleccionada;
                }
                if (tipoLista == 1) {
                    editarVigenciasFormales = vigenciaFormalSeleccionada;
                }

                RequestContext context = RequestContext.getCurrentInstance();
                System.out.println("Entro a editar... valor celda: " + cualCelda);
                if (cualCelda == 0) {
                    context.update("formularioDialogos:editarFecha");
                    context.execute("editarFecha.show()");
                    cualCelda = -1;
                } else if (cualCelda == 1) {
                    context.update("formularioDialogos:editarTipoEducacion");
                    context.execute("editarTipoEducacion.show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    context.update("formularioDialogos:editarProfesion");
                    context.execute("editarProfesion.show()");
                    cualCelda = -1;
                } else if (cualCelda == 3) {
                    context.update("formularioDialogos:editarInstitucion");
                    context.execute("editarInstitucion.show()");
                    cualCelda = -1;
                } else if (cualCelda == 4) {
                    context.update("formularioDialogos:editarAdiestramientoF");
                    context.execute("editarAdiestramientoF.show()");
                    cualCelda = -1;
                } else if (cualCelda == 5) {
                    context.update("formularioDialogos:editarValoracion");
                    context.execute("editarValoracion.show()");
                    cualCelda = -1;
                } else if (cualCelda == 6) {
                    context.update("formularioDialogos:editarNumeroTarjeta");
                    context.execute("editarNumeroTarjeta.show()");
                    cualCelda = -1;
                } else if (cualCelda == 7) {
                    context.update("formularioDialogos:editarFechaExpedicion");
                    context.execute("editarFechaExpedicion.show()");
                    cualCelda = -1;
                } else if (cualCelda == 8) {
                    context.update("formularioDialogos:editarFechaVencimiento");
                    context.execute("editarFechaVencimiento.show()");
                    cualCelda = -1;
                } else if (cualCelda == 9) {
                    context.update("formularioDialogos:editarObservacion");
                    context.execute("editarObservacion.show()");
                    cualCelda = -1;
                }
            } else if (CualTabla == 1) {
                if (tipoListaNF == 0) {
                    editarVigenciasNoFormales = vigenciaNoFormalSeleccionada;
                }
                if (tipoListaNF == 1) {
                    editarVigenciasNoFormales = vigenciaNoFormalSeleccionada;
                }

                RequestContext context = RequestContext.getCurrentInstance();
                System.out.println("Entro a editar... valor celda: " + cualCelda);
                System.out.println("Cual Tabla: " + CualTabla);
                if (cualCelda == 0) {
                    context.update("formularioDialogos:editarFechaNF");
                    context.execute("editarFechaNF.show()");
                    cualCelda = -1;
                } else if (cualCelda == 1) {
                    context.update("formularioDialogos:editarCursoNF");
                    context.execute("editarCursoNF.show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    context.update("formularioDialogos:editarTitulo");
                    context.execute("editarTitulo.show()");
                    cualCelda = -1;
                } else if (cualCelda == 3) {
                    context.update("formularioDialogos:editarInstitucionNF");
                    context.execute("editarInstitucionNF.show()");
                    cualCelda = -1;
                } else if (cualCelda == 4) {
                    context.update("formularioDialogos:editarAdiestramientoNF");
                    context.execute("editarAdiestramientoNF.show()");
                    cualCelda = -1;
                } else if (cualCelda == 5) {
                    context.update("formularioDialogos:editarCalificacionNF");
                    context.execute("editarCalificacionNF.show()");
                    cualCelda = -1;
                } else if (cualCelda == 6) {
                    context.update("formularioDialogos:editarObservacion");
                    context.execute("editarObservacion.show()");
                    cualCelda = -1;
                }
                vigenciaNoFormalSeleccionada = null;
            }

        } else {
            RequestContext.getCurrentInstance().execute("formularioDialogos:seleccionarRegistro.show()");
        }

    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (vigenciaFormalSeleccionada != null ) {
            if (CualTabla == 0){
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 1) {
                habilitarBotonLov();
                modificarInfoRegistroEducacion(listaTiposEducaciones.size());
                context.update("formularioDialogos:tiposEducacionesDialogo");
                context.execute("tiposEducacionesDialogo.show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 2) {
                habilitarBotonLov();
                modificarInfoRegistroProfesion(listaProfesiones.size());
                context.update("formularioDialogos:profesionesDialogo");
                context.execute("profesionesDialogo.show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 3) {
                habilitarBotonLov();
                modificarInfoRegistroInstitucionesF(listaInstituciones.size());
                context.update("formularioDialogos:institucionesDialogo");
                context.execute("institucionesDialogo.show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 4) {
                habilitarBotonLov();
                modificarInfoRegistroAdiestramientoF(listaAdiestramientosFormales.size());
                context.update("formularioDialogos:adiestramientosFDialogo");
                context.execute("adiestramientosFDialogo.show()");
                tipoActualizacion = 0;
            }
                
            }
        } 
        else if(vigenciaNoFormalSeleccionada != null){
            if (CualTabla == 1) {
                RequestContext context = RequestContext.getCurrentInstance();
                if (cualCelda == 1) {
                    habilitarBotonLov();
                    modificarInfoRegistroEducacion(listaTiposEducaciones.size());
                    context.update("formularioDialogos:cursosDialogo");
                    context.execute("cursosDialogo.show()");
                    tipoActualizacion = 0;
                } else if (cualCelda == 3) {
                    habilitarBotonLov();
                    modificarInfoRegistroInstituciones(listaInstituciones.size());
                    context.update("formularioDialogos:institucionesNFDialogo");
                    context.execute("institucionesNFDialogo.show()");
                    tipoActualizacion = 0;
                } else if (cualCelda == 4) {
                    habilitarBotonLov();
                    modificarInfoRegistroAdiestramientoNF(listaAdiestramientosNoFormales.size());
                    context.update("formularioDialogos:adiestramientosNFDialogo");
                    context.execute("adiestramientosNFDialogo.show()");
                    tipoActualizacion = 0;
                }

            }
        }
    }

    //FILTRADO
    public void activarCtrlF11() {
        System.out.println("TipoLista= " + tipoLista);
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0 || banderaNF == 0) {
            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            pEFechas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechas");
            pEFechas.setFilterStyle("width: 85%");
            pETiposEducaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pETiposEducaciones");
            pETiposEducaciones.setFilterStyle("");
            pEProfesiones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEProfesiones");
            pEProfesiones.setFilterStyle("");
            pEInstituciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEInstituciones");
            pEInstituciones.setFilterStyle("width: 85%");
            pEAdiestramientosF = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEAdiestramientosF");
            pEAdiestramientosF.setFilterStyle("width: 85%");
            pECalificaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pECalificaciones");
            pECalificaciones.setFilterStyle("width: 85%");
            pENumerosTarjetas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pENumerosTarjetas");
            pENumerosTarjetas.setFilterStyle("width: 85%");
            pEFechasExpediciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasExpediciones");
            pEFechasExpediciones.setFilterStyle("width: 85%");
            pEFechasVencimientos = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasVencimientos");
            pEFechasVencimientos.setFilterStyle("width: 85%");
            pEObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEObservaciones");
            pEObservaciones.setFilterStyle("width: 85%");
            altoTabla1 = "91";
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
            bandera = 1;

            ////////////
            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            pEFechasNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEFechasNF");
            pEFechasNF.setFilterStyle("width: 85%");
            pECursosNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pECursosNF");
            pECursosNF.setFilterStyle("");
            pETitulosNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pETitulosNF");
            pETitulosNF.setFilterStyle("");
            pEInstitucionesNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEInstitucionesNF");
            pEInstitucionesNF.setFilterStyle("width: 85%");
            pEAdiestramientosNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEAdiestramientosNF");
            pEAdiestramientosNF.setFilterStyle("width: 85%");
            pECalificacionesNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pECalificacionesNF");
            pECalificacionesNF.setFilterStyle("width: 85%");
            pEObservacionesNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEObservacionesNF");
            pEObservacionesNF.setFilterStyle("width: 85%");
            altoTabla2 = "91";
            RequestContext.getCurrentInstance().update("form:datosVigenciasNoFormalesPersona");
            banderaNF = 1;

        } else if (bandera == 1 || banderaNF == 1) {
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            pEFechas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechas");
            pEFechas.setFilterStyle("display: none; visibility: hidden;");
            pETiposEducaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pETiposEducaciones");
            pETiposEducaciones.setFilterStyle("display: none; visibility: hidden;");
            pEProfesiones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEProfesiones");
            pEProfesiones.setFilterStyle("display: none; visibility: hidden;");
            pEInstituciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEInstituciones");
            pEInstituciones.setFilterStyle("display: none; visibility: hidden;");
            pEAdiestramientosF = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEAdiestramientosF");
            pEAdiestramientosF.setFilterStyle("display: none; visibility: hidden;");
            pECalificaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pECalificaciones");
            pECalificaciones.setFilterStyle("display: none; visibility: hidden;");
            pENumerosTarjetas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pENumerosTarjetas");
            pENumerosTarjetas.setFilterStyle("display: none; visibility: hidden;");
            pEFechasExpediciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasExpediciones");
            pEFechasExpediciones.setFilterStyle("display: none; visibility: hidden;");
            pEFechasVencimientos = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasVencimientos");
            pEFechasVencimientos.setFilterStyle("display: none; visibility: hidden;");
            pEObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEObservaciones");
            pEObservaciones.setFilterStyle("display: none; visibility: hidden;");
            altoTabla1 = "115";
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
            bandera = 0;
            filtradosListaVigenciasFormales = null;
            tipoLista = 0;

            //////
            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            pEFechasNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEFechasNF");
            pEFechasNF.setFilterStyle("display: none; visibility: hidden;");
            pECursosNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pECursosNF");
            pECursosNF.setFilterStyle("display: none; visibility: hidden;");
            pETitulosNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pETitulosNF");
            pETitulosNF.setFilterStyle("display: none; visibility: hidden;");
            pEInstitucionesNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEInstitucionesNF");
            pEInstitucionesNF.setFilterStyle("display: none; visibility: hidden;");
            pEAdiestramientosNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEAdiestramientosNF");
            pEAdiestramientosNF.setFilterStyle("display: none; visibility: hidden;");
            pECalificacionesNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pECalificacionesNF");
            pECalificacionesNF.setFilterStyle("display: none; visibility: hidden;");
            pEObservacionesNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEObservacionesNF");
            pEObservacionesNF.setFilterStyle("display: none; visibility: hidden;");
            altoTabla2 = "115";
            RequestContext.getCurrentInstance().update("form:datosVigenciasNoFormalesPersona");
            banderaNF = 0;
            filtradosListaVigenciasNoFormales = null;
            tipoListaNF = 0;
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        if (CualTabla == 0) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasFormalesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "VigenciasFormalesPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
            vigenciaFormalSeleccionada = null;
        } else {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasNoFormalesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "VigenciasNoFormalesPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
            vigenciaNoFormalSeleccionada = null;
        }
    }

    public void exportXLS() throws IOException {
        if (CualTabla == 0) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasFormalesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "VigenciasFormalesXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
            vigenciaFormalSeleccionada = null;
        } else {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasNoFormalesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "VigenciasNoFormalesXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
            vigenciaNoFormalSeleccionada = null;
        }
    }

    //LIMPIAR NUEVO REGISTRO
    public void limpiarNuevaVigenciaFormal() {
        nuevaVigenciaFormal = new VigenciasFormales();
        nuevaVigenciaFormal.setTipoeducacion(new TiposEducaciones());
        nuevaVigenciaFormal.setProfesion(new Profesiones());
        nuevaVigenciaFormal.setInstitucion(new Instituciones());
        nuevaVigenciaFormal.setAdiestramientof(new AdiestramientosF());
    }

    public void limpiarNuevaVigenciaNoFormal() {

        nuevaVigenciaNoFormal = new VigenciasNoFormales();
        nuevaVigenciaNoFormal.setCurso(new Cursos());
        nuevaVigenciaNoFormal.setInstitucion(new Instituciones());
        nuevaVigenciaNoFormal.setAdiestramientonf(new AdiestramientosNF());
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("TIPOEDUCACION")) {
            if (tipoNuevo == 1) {
                TipoEducacion = nuevaVigenciaFormal.getTipoeducacion().getNombre();
            } else if (tipoNuevo == 2) {
                TipoEducacion = duplicarVigenciaFormal.getTipoeducacion().getNombre();
            } else if (Campo.equals("PROFESION")) {
                if (tipoNuevo == 1) {
                    Profesion = nuevaVigenciaFormal.getProfesion().getDescripcion();
                } else if (tipoNuevo == 2) {
                    Profesion = duplicarVigenciaFormal.getProfesion().getDescripcion();
                }
            } else if (Campo.equals("INSTITUCION")) {
                if (tipoNuevo == 1) {
                    Institucion = nuevaVigenciaFormal.getInstitucion().getDescripcion();
                } else if (tipoNuevo == 2) {
                    Institucion = duplicarVigenciaFormal.getInstitucion().getDescripcion();
                }
            } else if (Campo.equals("ADIESTRAMIENTOF")) {
                if (tipoNuevo == 1) {
                    AdiestramientoF = nuevaVigenciaFormal.getAdiestramientof().getDescripcion();
                } else if (tipoNuevo == 2) {
                    AdiestramientoF = duplicarVigenciaFormal.getAdiestramientof().getDescripcion();
                }
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOEDUCACION")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaFormal.getTipoeducacion().setNombre(TipoEducacion);
            } else if (tipoNuevo == 2) {
                duplicarVigenciaFormal.getTipoeducacion().setNombre(TipoEducacion);
            }
            for (int i = 0; i < listaTiposEducaciones.size(); i++) {
                if (listaTiposEducaciones.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaFormal.setTipoeducacion(listaTiposEducaciones.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevoTipoEducacion");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaFormal.setTipoeducacion(listaTiposEducaciones.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarTipoEducacion");
                }
                listaTiposEducaciones.clear();
                getListaTiposEducaciones();
            } else {
                context.update("form:tiposEducacionesDialogo");
                context.execute("tiposEducacionesDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevoTipoEducacion");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarTipoEducacion");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("PROFESION")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaFormal.getProfesion().setDescripcion(Profesion);
            } else if (tipoNuevo == 2) {
                duplicarVigenciaFormal.getProfesion().setDescripcion(Profesion);
            }
            for (int i = 0; i < listaProfesiones.size(); i++) {
                if (listaProfesiones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaFormal.setProfesion(listaProfesiones.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaProfesion");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaFormal.setProfesion(listaProfesiones.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarProfesion");
                }
                listaProfesiones.clear();
                getListaProfesiones();
            } else {
                context.update("form:profesionesDialogo");
                context.execute("profesionesDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaProfesion");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarProfesion");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("INSTITUCION")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaFormal.getInstitucion().setDescripcion(Institucion);
            } else if (tipoNuevo == 2) {
                duplicarVigenciaFormal.getInstitucion().setDescripcion(Institucion);
            }
            for (int i = 0; i < listaInstituciones.size(); i++) {
                if (listaInstituciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaFormal.setInstitucion(listaInstituciones.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaInstitucion");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaFormal.setInstitucion(listaInstituciones.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarInstitucion");
                }
                listaInstituciones.clear();
                getListaInstituciones();
            } else {
                context.update("form:institucionesDialogo");
                context.execute("institucionesDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaInstitucion");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarInstitucion");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("ADIESTRAMIENTOF")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaFormal.getAdiestramientof().setDescripcion(AdiestramientoF);
            } else if (tipoNuevo == 2) {
                duplicarVigenciaFormal.getAdiestramientof().setDescripcion(AdiestramientoF);
            }
            for (int i = 0; i < listaAdiestramientosFormales.size(); i++) {
                if (listaAdiestramientosFormales.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaFormal.setAdiestramientof(listaAdiestramientosFormales.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevoAdiestramientoF");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaFormal.setAdiestramientof(listaAdiestramientosFormales.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarAdiestramientoF");
                }
                listaAdiestramientosFormales.clear();
                getListaAdiestramientosFormales();
            } else {
                context.update("form:adiestramientosFDialogo");
                context.execute("adiestramientosFDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevoAdiestramientoF");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarAdiestramientoF");
                }
            }
        }
    }
// Agregar Nueva Vigencia Formal

    public void agregarNuevaVigenciaFormal() {
        int pasa = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevaVigenciaFormal.getFechavigencia() == null) {
            System.out.println("Entro a FechaNF");
            mensajeValidacion = " * Fecha \n";
            pasa++;
        }
        if (nuevaVigenciaFormal.getTipoeducacion().getSecuencia() == null) {
            System.out.println("Entro a TipoEducacion");
            mensajeValidacion = mensajeValidacion + " * Tipo de Educacion\n";
            pasa++;
        }
        if (nuevaVigenciaFormal.getProfesion().getSecuencia() == null) {
            System.out.println("Entro a Profesion");
            mensajeValidacion = mensajeValidacion + " * Profesion\n";
            pasa++;
        }
        if (nuevaVigenciaFormal.getInstitucion().getSecuencia() == null) {
            System.out.println("Entro a Institucion");
            mensajeValidacion = mensajeValidacion + " * Institucion \n";
            pasa++;
        }
        if (nuevaVigenciaFormal.getAdiestramientof().getSecuencia() == null) {
            System.out.println("Entro a AdiestramientoF");
            mensajeValidacion = mensajeValidacion + " * Adiestramiento\n";
            pasa++;
        }

        if (pasa == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();

                System.out.println("Desactivar");
                System.out.println("TipoLista= " + tipoLista);
                pEFechas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechas");
                pEFechas.setFilterStyle("display: none; visibility: hidden;");
                pETiposEducaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pETiposEducaciones");
                pETiposEducaciones.setFilterStyle("display: none; visibility: hidden;");
                pEProfesiones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEProfesiones");
                pEProfesiones.setFilterStyle("display: none; visibility: hidden;");
                pEInstituciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEInstituciones");
                pEInstituciones.setFilterStyle("display: none; visibility: hidden;");
                pEAdiestramientosF = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEAdiestramientosF");
                pEAdiestramientosF.setFilterStyle("display: none; visibility: hidden;");
                pECalificaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pECalificaciones");
                pECalificaciones.setFilterStyle("display: none; visibility: hidden;");
                pENumerosTarjetas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pENumerosTarjetas");
                pENumerosTarjetas.setFilterStyle("display: none; visibility: hidden;");
                pEFechasExpediciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasExpediciones");
                pEFechasExpediciones.setFilterStyle("display: none; visibility: hidden;");
                pEFechasVencimientos = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasVencimientos");
                pEFechasVencimientos.setFilterStyle("display: none; visibility: hidden;");
                pEObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEObservaciones");
                pEObservaciones.setFilterStyle("display: none; visibility: hidden;");
                altoTabla1 = "115";
                RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
                bandera = 0;
                filtradosListaVigenciasFormales = null;
                tipoLista = 0;

            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS FORMALES.
            System.out.println("entra a agregar");
            k++;
            l = BigInteger.valueOf(k);
            nuevaVigenciaFormal.setSecuencia(l);
            nuevaVigenciaFormal.setPersona(empleado.getPersona());
//            if (nuevaVigenciaFormal.getTipoeducacion().getSecuencia() == null) {
//                nuevaVigenciaFormal.setTipoeducacion(null);
//            }
//            if (nuevaVigenciaFormal.getProfesion().getSecuencia() == null) {
//                nuevaVigenciaFormal.setProfesion(null);
//            }
//            if (nuevaVigenciaFormal.getInstitucion().getSecuencia() == null) {
//                nuevaVigenciaFormal.setInstitucion(null);
//            }
//            if (nuevaVigenciaFormal.getAdiestramientof().getSecuencia() == null) {
//                nuevaVigenciaFormal.setAdiestramientof(null);
//            }

            listaVigenciasFormalesCrear.add(nuevaVigenciaFormal);
            listaVigenciasFormales.add(nuevaVigenciaFormal);
            modificarInfoRegistroF(listaVigenciasFormales.size());
            context.update("form:infoRegistroF");
            vigenciaFormalSeleccionada = nuevaVigenciaFormal;
            nuevaVigenciaFormal = new VigenciasFormales();
            nuevaVigenciaFormal.setTipoeducacion(new TiposEducaciones());
            nuevaVigenciaFormal.setProfesion(new Profesiones());
            nuevaVigenciaFormal.setInstitucion(new Instituciones());
            nuevaVigenciaFormal.setAdiestramientof(new AdiestramientosF());
            context.update("form:datosVigenciasFormalesPersona");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            context.execute("NuevoRegistroVigenciaFormal.hide()");
        } else {
            context.update("formularioDialogos:validacionNuevaVigenciaFormal");
            context.execute("validacionNuevaVigenciaFormal.show()");
        }
    }

    //BORRAR VIGENCIA FORMAL
    public void borrarVigenciaFormal() {

        if (vigenciaFormalSeleccionada != null) {
            if (CualTabla == 0) {
                if (!listaVigenciasFormalesModificar.isEmpty() && listaVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                    int modIndex = listaVigenciasFormalesModificar.indexOf(vigenciaFormalSeleccionada);
                    listaVigenciasFormalesModificar.remove(modIndex);
                    listaVigenciasFormalesBorrar.add(vigenciaFormalSeleccionada);
                } else if (!listaVigenciasFormalesCrear.isEmpty() && listaVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                    int crearIndex = listaVigenciasFormalesCrear.indexOf(vigenciaFormalSeleccionada);
                    listaVigenciasFormalesCrear.remove(crearIndex);
                } else {
                    listaVigenciasFormalesBorrar.add(vigenciaFormalSeleccionada);
                }
                listaVigenciasFormales.remove(vigenciaFormalSeleccionada);
                if (tipoLista == 1) {
                    filtradosListaVigenciasFormales.remove(vigenciaFormalSeleccionada);
                    System.out.println("Realizado");
                }

                RequestContext context = RequestContext.getCurrentInstance();
                modificarInfoRegistroF(listaVigenciasFormales.size());
                context.update("form:infoRegistroF");
                context.update("form:datosVigenciasFormalesPersona");
                vigenciaFormalSeleccionada = null;

                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            } else if (CualTabla == 1) {

                if (!listaVigenciasNoFormalesModificar.isEmpty() && listaVigenciasNoFormalesModificar.contains(vigenciaNoFormalSeleccionada)) {
                    int modIndex = listaVigenciasNoFormalesModificar.indexOf(vigenciaNoFormalSeleccionada);
                    listaVigenciasNoFormalesModificar.remove(modIndex);
                    listaVigenciasNoFormalesBorrar.add(vigenciaNoFormalSeleccionada);
                } else if (!listaVigenciasNoFormalesCrear.isEmpty() && listaVigenciasNoFormalesCrear.contains(vigenciaNoFormalSeleccionada)) {
                    int crearIndex = listaVigenciasNoFormalesCrear.indexOf(vigenciaNoFormalSeleccionada);
                    listaVigenciasNoFormalesCrear.remove(crearIndex);
                } else {
                    listaVigenciasNoFormalesBorrar.add(vigenciaNoFormalSeleccionada);
                }
                listaVigenciasNoFormales.remove(vigenciaNoFormalSeleccionada);

                if (tipoListaNF == 1) {
                    filtradosListaVigenciasNoFormales.remove(vigenciaNoFormalSeleccionada);
                    System.out.println("Realizado");
                }

                RequestContext context = RequestContext.getCurrentInstance();
                modificarInfoRegistroNF(listaVigenciasNoFormales.size());
                context.update("form:infoRegistroNF");
                context.update("form:datosVigenciasNoFormalesPersona");
                vigenciaNoFormalSeleccionada = null;

                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }

        } else {
            RequestContext.getCurrentInstance().execute("formularioDialogos:seleccionarRegistro.show()");
        }

    }

    //DUPLICAR VIGENCIAFORMAL
    public void duplicarVF() {
        if (vigenciaFormalSeleccionada != null) {
            if (CualTabla == 0) {
                duplicarVigenciaFormal = new VigenciasFormales();
                k++;
                l = BigInteger.valueOf(k);

                if (tipoLista == 0) {
                    duplicarVigenciaFormal.setSecuencia(l);
                    duplicarVigenciaFormal.setFechavigencia(vigenciaFormalSeleccionada.getFechavigencia());
                    duplicarVigenciaFormal.setTipoeducacion(vigenciaFormalSeleccionada.getTipoeducacion());
                    duplicarVigenciaFormal.setProfesion(vigenciaFormalSeleccionada.getProfesion());
                    duplicarVigenciaFormal.setInstitucion(vigenciaFormalSeleccionada.getInstitucion());
                    duplicarVigenciaFormal.setAdiestramientof(vigenciaFormalSeleccionada.getAdiestramientof());
                    duplicarVigenciaFormal.setCalificacionobtenida(vigenciaFormalSeleccionada.getCalificacionobtenida());
                    duplicarVigenciaFormal.setNumerotarjeta(vigenciaFormalSeleccionada.getNumerotarjeta());
                    duplicarVigenciaFormal.setFechaexpediciontarjeta(vigenciaFormalSeleccionada.getFechaexpediciontarjeta());
                    duplicarVigenciaFormal.setFechavencimientotarjeta(vigenciaFormalSeleccionada.getFechavencimientotarjeta());
                    duplicarVigenciaFormal.setObservacion(vigenciaFormalSeleccionada.getObservacion());
                }
                if (tipoLista == 1) {
                    duplicarVigenciaFormal.setSecuencia(l);
                    duplicarVigenciaFormal.setFechavigencia(vigenciaFormalSeleccionada.getFechavigencia());
                    duplicarVigenciaFormal.setTipoeducacion(vigenciaFormalSeleccionada.getTipoeducacion());
                    duplicarVigenciaFormal.setProfesion(vigenciaFormalSeleccionada.getProfesion());
                    duplicarVigenciaFormal.setInstitucion(vigenciaFormalSeleccionada.getInstitucion());
                    duplicarVigenciaFormal.setAdiestramientof(vigenciaFormalSeleccionada.getAdiestramientof());
                    duplicarVigenciaFormal.setCalificacionobtenida(vigenciaFormalSeleccionada.getCalificacionobtenida());
                    duplicarVigenciaFormal.setNumerotarjeta(vigenciaFormalSeleccionada.getNumerotarjeta());
                    duplicarVigenciaFormal.setFechaexpediciontarjeta(vigenciaFormalSeleccionada.getFechaexpediciontarjeta());
                    duplicarVigenciaFormal.setFechavencimientotarjeta(vigenciaFormalSeleccionada.getFechavencimientotarjeta());
                    duplicarVigenciaFormal.setObservacion(vigenciaFormalSeleccionada.getObservacion());
                }
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("formularioDialogos:duplicarVigenciaFormal");
                context.execute("DuplicarRegistroVigenciaFormal.show()");
            } else if (CualTabla == 1) {
                System.out.println("Entra Duplicar NF");

                duplicarVigenciaNoFormal = new VigenciasNoFormales();
                m++;
                n = BigInteger.valueOf(m);

                if (tipoListaNF == 0) {
                    duplicarVigenciaNoFormal.setSecuencia(n);
                    duplicarVigenciaNoFormal.setFechavigencia(vigenciaNoFormalSeleccionada.getFechavigencia());
                    duplicarVigenciaNoFormal.setCurso(vigenciaNoFormalSeleccionada.getCurso());
                    duplicarVigenciaNoFormal.setTitulo(vigenciaNoFormalSeleccionada.getTitulo());
                    duplicarVigenciaNoFormal.setInstitucion(vigenciaNoFormalSeleccionada.getInstitucion());
                    duplicarVigenciaNoFormal.setAdiestramientonf(vigenciaNoFormalSeleccionada.getAdiestramientonf());
                    duplicarVigenciaNoFormal.setCalificacionobtenida(vigenciaNoFormalSeleccionada.getCalificacionobtenida());
                    duplicarVigenciaNoFormal.setObservacion(vigenciaNoFormalSeleccionada.getObservacion());
                }
                if (tipoListaNF == 1) {
                    duplicarVigenciaNoFormal.setSecuencia(n);
                    duplicarVigenciaNoFormal.setFechavigencia(vigenciaNoFormalSeleccionada.getFechavigencia());
                    duplicarVigenciaNoFormal.setCurso(vigenciaNoFormalSeleccionada.getCurso());
                    duplicarVigenciaNoFormal.setTitulo(vigenciaNoFormalSeleccionada.getTitulo());
                    duplicarVigenciaNoFormal.setInstitucion(vigenciaNoFormalSeleccionada.getInstitucion());
                    duplicarVigenciaNoFormal.setAdiestramientonf(vigenciaNoFormalSeleccionada.getAdiestramientonf());
                    duplicarVigenciaNoFormal.setCalificacionobtenida(vigenciaNoFormalSeleccionada.getCalificacionobtenida());
                    duplicarVigenciaNoFormal.setObservacion(vigenciaNoFormalSeleccionada.getObservacion());
                }
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("formularioDialogos:duplicarVigenciaNoFormal");
                context.execute("DuplicarRegistroVigenciaNoFormal.show()");
            }
        } else {
            RequestContext.getCurrentInstance().execute("formularioDialogos:seleccionarRegistro.show()");
        }
    }

    public void confirmarDuplicar() {

        listaVigenciasFormales.add(duplicarVigenciaFormal);
        listaVigenciasFormalesCrear.add(duplicarVigenciaFormal);
        RequestContext context = RequestContext.getCurrentInstance();
        modificarInfoRegistroF(listaVigenciasFormales.size());
        context.update("form:infoRegistroF");
        context.update("form:datosVigenciasFormalesPersona");
        vigenciaFormalSeleccionada = null;
        vigenciaFormalSeleccionada = null;
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            System.out.println("Desactivar");
            System.out.println("TipoLista= " + tipoLista);
            pEFechas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechas");
            pEFechas.setFilterStyle("display: none; visibility: hidden;");
            pETiposEducaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pETiposEducaciones");
            pETiposEducaciones.setFilterStyle("display: none; visibility: hidden;");
            pEProfesiones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEProfesiones");
            pEProfesiones.setFilterStyle("display: none; visibility: hidden;");
            pEInstituciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEInstituciones");
            pEInstituciones.setFilterStyle("display: none; visibility: hidden;");
            pEAdiestramientosF = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEAdiestramientosF");
            pEAdiestramientosF.setFilterStyle("display: none; visibility: hidden;");
            pECalificaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pECalificaciones");
            pECalificaciones.setFilterStyle("display: none; visibility: hidden;");
            pENumerosTarjetas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pENumerosTarjetas");
            pENumerosTarjetas.setFilterStyle("display: none; visibility: hidden;");
            pEFechasExpediciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasExpediciones");
            pEFechasExpediciones.setFilterStyle("display: none; visibility: hidden;");
            pEFechasVencimientos = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasVencimientos");
            pEFechasVencimientos.setFilterStyle("display: none; visibility: hidden;");
            pEObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEObservaciones");
            pEObservaciones.setFilterStyle("display: none; visibility: hidden;");
            altoTabla1 = "115";
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
            bandera = 0;
            filtradosListaVigenciasFormales = null;
            tipoLista = 0;

        }
        duplicarVigenciaFormal = new VigenciasFormales();
    }
    //LIMPIAR DUPLICAR

    public void limpiarduplicarVigenciaFormal() {
        duplicarVigenciaFormal = new VigenciasFormales();
    }
    //LIMPIAR DUPLICAR NO FORMAL

    public void limpiarduplicarVigenciaNoFormal() {
        duplicarVigenciaNoFormal = new VigenciasNoFormales();
    }

    public void verificarRastro() {
        if (CualTabla == 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("lol");

            if (vigenciaFormalSeleccionada != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(vigenciaFormalSeleccionada.getSecuencia(), "VIGENCIASFORMALES");
                System.out.println("resultado: " + resultado);
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
                if (administrarRastros.verificarHistoricosTabla("VIGENCIASFORMALES")) {
                    context.execute("confirmarRastroHistorico.show()");
                } else {
                    context.execute("errorRastroHistorico.show()");
                }

            }
            vigenciaFormalSeleccionada = null;
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("NF");
            if (vigenciaNoFormalSeleccionada != null) {
                System.out.println("NF2");
                int resultadoNF = administrarRastros.obtenerTabla(vigenciaNoFormalSeleccionada.getSecuencia(), "VIGENCIASNOFORMALES");
                System.out.println("resultado: " + resultadoNF);
                if (resultadoNF == 1) {
                    context.execute("errorObjetosDBNF.show()");
                } else if (resultadoNF == 2) {
                    context.execute("confirmarRastroNF.show()");
                } else if (resultadoNF == 3) {
                    context.execute("errorRegistroRastroNF.show()");
                } else if (resultadoNF == 4) {
                    context.execute("errorTablaConRastroNF.show()");
                } else if (resultadoNF == 5) {
                    context.execute("errorTablaSinRastroNF.show()");
                }
            } else {
                if (administrarRastros.verificarHistoricosTabla("VIGENCIASNOFORMALES")) {
                    context.execute("confirmarRastroHistoricoNF.show()");
                } else {
                    context.execute("errorRastroHistoricoNF.show()");
                }

            }
            vigenciaNoFormalSeleccionada = null;
        }

    }

    public void salir() {

        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            pEFechas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechas");
            pEFechas.setFilterStyle("display: none; visibility: hidden;");
            pETiposEducaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pETiposEducaciones");
            pETiposEducaciones.setFilterStyle("display: none; visibility: hidden;");
            pEProfesiones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEProfesiones");
            pEProfesiones.setFilterStyle("display: none; visibility: hidden;");
            pEInstituciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEInstituciones");
            pEInstituciones.setFilterStyle("display: none; visibility: hidden;");
            pEAdiestramientosF = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEAdiestramientosF");
            pEAdiestramientosF.setFilterStyle("display: none; visibility: hidden;");
            pECalificaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pECalificaciones");
            pECalificaciones.setFilterStyle("display: none; visibility: hidden;");
            pENumerosTarjetas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pENumerosTarjetas");
            pENumerosTarjetas.setFilterStyle("display: none; visibility: hidden;");
            pEFechasExpediciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasExpediciones");
            pEFechasExpediciones.setFilterStyle("display: none; visibility: hidden;");
            pEFechasVencimientos = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasVencimientos");
            pEFechasVencimientos.setFilterStyle("display: none; visibility: hidden;");
            pEObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEObservaciones");
            pEObservaciones.setFilterStyle("display: none; visibility: hidden;");
            altoTabla1 = "115";
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
            bandera = 0;
            filtradosListaVigenciasFormales = null;
            tipoLista = 0;
        }

        listaVigenciasFormalesBorrar.clear();
        listaVigenciasFormalesCrear.clear();
        listaVigenciasFormalesModificar.clear();
        vigenciaFormalSeleccionada = null;
        listaVigenciasFormales = null;
        getListaVigenciasFormales();
        contarRegistrosF();
        //  k = 0;
        guardado = true;
        permitirIndex = true;

    }

    //CANCELAR MODIFICACIONES
    public void cancelarModificacion() {

        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            pEFechas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechas");
            pEFechas.setFilterStyle("display: none; visibility: hidden;");
            pETiposEducaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pETiposEducaciones");
            pETiposEducaciones.setFilterStyle("display: none; visibility: hidden;");
            pEProfesiones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEProfesiones");
            pEProfesiones.setFilterStyle("display: none; visibility: hidden;");
            pEInstituciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEInstituciones");
            pEInstituciones.setFilterStyle("display: none; visibility: hidden;");
            pEAdiestramientosF = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEAdiestramientosF");
            pEAdiestramientosF.setFilterStyle("display: none; visibility: hidden;");
            pECalificaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pECalificaciones");
            pECalificaciones.setFilterStyle("display: none; visibility: hidden;");
            pENumerosTarjetas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pENumerosTarjetas");
            pENumerosTarjetas.setFilterStyle("display: none; visibility: hidden;");
            pEFechasExpediciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasExpediciones");
            pEFechasExpediciones.setFilterStyle("display: none; visibility: hidden;");
            pEFechasVencimientos = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasVencimientos");
            pEFechasVencimientos.setFilterStyle("display: none; visibility: hidden;");
            pEObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEObservaciones");
            pEObservaciones.setFilterStyle("display: none; visibility: hidden;");
            altoTabla1 = "115";
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
            bandera = 0;
            filtradosListaVigenciasFormales = null;
            tipoLista = 0;
        }

        if (banderaNF == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            pEFechasNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEFechasNF");

            pEFechasNF.setFilterStyle("display: none; visibility: hidden;");
            pECursosNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pECursosNF");
            pECursosNF.setFilterStyle("display: none; visibility: hidden;");
            pETitulosNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pETitulosNF");
            pETitulosNF.setFilterStyle("display: none; visibility: hidden;");
            pEInstitucionesNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEInstitucionesNF");
            pEInstitucionesNF.setFilterStyle("display: none; visibility: hidden;");
            pEAdiestramientosNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEAdiestramientosNF");
            pEAdiestramientosNF.setFilterStyle("display: none; visibility: hidden;");
            pECalificacionesNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pECalificacionesNF");
            pECalificacionesNF.setFilterStyle("display: none; visibility: hidden;");
            pEObservacionesNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEObservacionesNF");
            pEObservacionesNF.setFilterStyle("display: none; visibility: hidden;");
            altoTabla2 = "115";
            RequestContext.getCurrentInstance().update("form:datosVigenciasNoFormalesPersona");
            banderaNF = 0;
            filtradosListaVigenciasNoFormales = null;
            tipoListaNF = 0;
        }
        listaVigenciasFormalesBorrar.clear();
        listaVigenciasFormalesCrear.clear();
        listaVigenciasFormalesModificar.clear();
        vigenciaFormalSeleccionada = null;
        listaVigenciasFormales = null;
        getListaVigenciasFormales();
        contarRegistrosF();

        listaVigenciasNoFormalesBorrar.clear();
        listaVigenciasNoFormalesCrear.clear();
        listaVigenciasNoFormalesModificar.clear();
        vigenciaNoFormalSeleccionada = null;
        listaVigenciasNoFormales = null;
        getListaVigenciasNoFormales();
        contarRegistrosNF();
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosVigenciasFormalesPersona");
        context.update("form:datosVigenciasNoFormalesPersona");
    }

    //GUARDAR TODO
    public void guardarTodo() {

        System.out.println("Guardado: " + guardado);
        System.out.println("Guardadondo Primera Tabla: ");
        if (guardado == false) {
            System.out.println("Realizando Operaciones VigenciasFormales");
            if (!listaVigenciasFormalesBorrar.isEmpty()) {
                for (int i = 0; i < listaVigenciasFormalesBorrar.size(); i++) {
                    System.out.println("Borrando...");
                    if (listaVigenciasFormalesBorrar.get(i).getAdiestramientof().getSecuencia() == null) {
                        listaVigenciasFormalesBorrar.get(i).setAdiestramientof(null);
                        administrarVigenciasFormales.borrarVigenciaFormal(listaVigenciasFormalesBorrar.get(i));
                    } else {

                        administrarVigenciasFormales.borrarVigenciaFormal(listaVigenciasFormalesBorrar.get(i));
                    }

                    listaVigenciasFormalesBorrar.clear();
                }
            }
            if (!listaVigenciasNoFormalesBorrar.isEmpty()) {
                for (int i = 0; i < listaVigenciasNoFormalesBorrar.size(); i++) {
                    System.out.println("Borrando...");
                    if (listaVigenciasNoFormalesBorrar.get(i).getAdiestramientonf().getSecuencia() == null) {
                        listaVigenciasNoFormalesBorrar.get(i).setAdiestramientonf(null);
                        administrarVigenciasNoFormales.borrarVigenciaNoFormal(listaVigenciasNoFormalesBorrar.get(i));
                    } else {

                        administrarVigenciasNoFormales.borrarVigenciaNoFormal(listaVigenciasNoFormalesBorrar.get(i));
                    }

                    System.out.println("Entra");
                    listaVigenciasNoFormalesBorrar.clear();
                }
            }
            if (!listaVigenciasFormalesCrear.isEmpty()) {
                for (int i = 0; i < listaVigenciasFormalesCrear.size(); i++) {
                    System.out.println("Creando...");
                    System.out.println(listaVigenciasFormalesCrear.size());
                    if (listaVigenciasFormalesCrear.get(i).getNumerotarjeta() != null) {
                        listaVigenciasFormalesCrear.get(i).setTarjetaprofesional("S");
                    } else {
                        listaVigenciasFormalesCrear.get(i).setTarjetaprofesional("N");
                    }

                    if (listaVigenciasFormalesCrear.get(i).getAdiestramientof().getDescripcion() != null) {
                        listaVigenciasFormalesCrear.get(i).setAcargo("S");
                    } else {
                        listaVigenciasFormalesCrear.get(i).setAcargo("N");
                    }

                    if (listaVigenciasFormalesCrear.get(i).getAdiestramientof().getSecuencia() == null) {
                        listaVigenciasFormalesCrear.get(i).setAdiestramientof(null);
                        administrarVigenciasFormales.crearVigenciaFormal(listaVigenciasFormalesCrear.get(i));
                    } else {
                        administrarVigenciasFormales.crearVigenciaFormal(listaVigenciasFormalesCrear.get(i));
                    }

                }
                System.out.println("LimpiaLista");
                listaVigenciasFormalesCrear.clear();
            }

            if (!listaVigenciasNoFormalesCrear.isEmpty()) {
                for (int i = 0; i < listaVigenciasNoFormalesCrear.size(); i++) {
                    System.out.println("Creando...");
                    System.out.println(listaVigenciasNoFormalesCrear.size());
                    if (listaVigenciasNoFormalesCrear.get(i).getAdiestramientonf().getDesccripcion() != null) {
                        listaVigenciasNoFormalesCrear.get(i).setAcargo("S");
                    } else {
                        listaVigenciasNoFormalesCrear.get(i).setAcargo("N");
                    }

                    if (listaVigenciasNoFormalesCrear.get(i).getAdiestramientonf().getSecuencia() == null) {
                        listaVigenciasNoFormalesCrear.get(i).setAdiestramientonf(null);
                        administrarVigenciasNoFormales.crearVigenciaNoFormal(listaVigenciasNoFormalesCrear.get(i));
                    } else {

                        administrarVigenciasNoFormales.crearVigenciaNoFormal(listaVigenciasNoFormalesCrear.get(i));
                    }
                }

                System.out.println("LimpiaLista");
                listaVigenciasNoFormalesCrear.clear();
            }

            if (!listaVigenciasFormalesModificar.isEmpty()) {
                administrarVigenciasFormales.modificarVigenciaFormal(listaVigenciasFormalesModificar);
                listaVigenciasFormalesModificar.clear();
            }
            if (!listaVigenciasNoFormalesModificar.isEmpty()) {
                administrarVigenciasNoFormales.modificarVigenciaNoFormal(listaVigenciasNoFormalesModificar);
                listaVigenciasNoFormalesModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaVigenciasFormales = null;
            listaVigenciasNoFormales = null;
            getListaVigenciasNoFormales();
            getListaVigenciasFormales();
            contarRegistrosF();
            contarRegistrosNF();
            vigenciaNoFormalSeleccionada = null;
            vigenciaFormalSeleccionada = null;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosVigenciasFormalesPersona");
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

        }
    }

    //<--------------------------------------------METODOS VIGENCIAS NO FORMALES--------------------------------------------->
    //AUTOCOMPLETAR
    public void modificarVigenciasNoFormales(VigenciasNoFormales vigenciaNoFormal, String confirmarCambio, String valorConfirmar) {
        vigenciaNoFormalSeleccionada = vigenciaNoFormal;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoListaNF == 0) {
                if (!listaVigenciasNoFormalesCrear.contains(vigenciaNoFormalSeleccionada)) {

                    if (listaVigenciasNoFormalesModificar.isEmpty()) {
                        listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
                    } else if (!listaVigenciasNoFormalesModificar.contains(vigenciaNoFormalSeleccionada)) {
                        listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
                vigenciaNoFormalSeleccionada = null;
                vigenciaNoFormalSeleccionada = null;

            } else {
                if (!listaVigenciasNoFormalesCrear.contains(vigenciaNoFormalSeleccionada)) {

                    if (listaVigenciasNoFormalesModificar.isEmpty()) {
                        listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
                    } else if (!listaVigenciasNoFormalesModificar.contains(vigenciaNoFormalSeleccionada)) {
                        listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
                vigenciaNoFormalSeleccionada = null;
                vigenciaNoFormalSeleccionada = null;
            }
            context.update("form:datosVigenciasNoFormalesPersona");
        } else if (confirmarCambio.equalsIgnoreCase("CURSO")) {
            if (tipoListaNF == 0) {
                vigenciaNoFormalSeleccionada.getCurso().setNombre(Curso);
            } else {
                vigenciaNoFormalSeleccionada.getCurso().setNombre(Curso);
            }

            for (int i = 0; i < listaCursos.size(); i++) {
                if (listaCursos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaNF == 0) {
                    vigenciaNoFormalSeleccionada.setCurso(listaCursos.get(indiceUnicoElemento));
                } else {
                    vigenciaNoFormalSeleccionada.setCurso(listaCursos.get(indiceUnicoElemento));
                }
                listaCursos.clear();
                getListaCursos();
            } else {
                permitirIndex = false;
                context.update("formularioDialogos:cursosDialogo");
                context.execute("cursosDialogo.show()");
                tipoActualizacion = 0;
            }

        } else if (confirmarCambio.equalsIgnoreCase("INSTITUCION")) {
            if (tipoListaNF == 0) {
                vigenciaNoFormalSeleccionada.getInstitucion().setDescripcion(Institucion);
            } else {
                vigenciaNoFormalSeleccionada.getInstitucion().setDescripcion(Institucion);
            }
            for (int i = 0; i < listaInstituciones.size(); i++) {
                if (listaInstituciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaNF == 0) {
                    vigenciaNoFormalSeleccionada.setInstitucion(listaInstituciones.get(indiceUnicoElemento));
                } else {
                    vigenciaNoFormalSeleccionada.setInstitucion(listaInstituciones.get(indiceUnicoElemento));
                }
                listaInstituciones.clear();
                getListaInstituciones();
            } else {
                permitirIndex = false;
                context.update("formularioDialogos:institucionesDialogo");
                context.execute("institucionesDialogo.show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("ADIESTRAMENTOSNF")) {
            if (tipoListaNF == 0) {
                vigenciaNoFormalSeleccionada.getAdiestramientonf().setDesccripcion(AdiestramientoNF);
            } else {
                vigenciaNoFormalSeleccionada.getAdiestramientonf().setDesccripcion(AdiestramientoNF);
            }
            for (int i = 0; i < listaAdiestramientosNoFormales.size(); i++) {
                if (listaAdiestramientosNoFormales.get(i).getDesccripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaNF == 0) {
                    vigenciaNoFormalSeleccionada.setAdiestramientonf(listaAdiestramientosNoFormales.get(indiceUnicoElemento));
                } else {
                    vigenciaNoFormalSeleccionada.setAdiestramientonf(listaAdiestramientosNoFormales.get(indiceUnicoElemento));
                }
                listaAdiestramientosNoFormales.clear();
                getListaAdiestramientosNoFormales();
            } else {
                permitirIndex = false;
                context.update("formularioDialogos:adiestramientosNFDialogo");
                context.execute("adiestramientosNFDialogo.show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoListaNF == 0) {
                if (!listaVigenciasNoFormalesCrear.contains(vigenciaNoFormalSeleccionada)) {
                    if (listaVigenciasNoFormalesModificar.isEmpty()) {
                        listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
                    } else if (!listaVigenciasNoFormalesModificar.contains(vigenciaNoFormalSeleccionada)) {
                        listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
                vigenciaNoFormalSeleccionada = null;
                vigenciaNoFormalSeleccionada = null;
            } else {
                if (!listaVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {

                    if (listaVigenciasFormalesModificar.isEmpty()) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    } else if (!listaVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                        listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
                vigenciaNoFormalSeleccionada = null;
                vigenciaNoFormalSeleccionada = null;
            }
        }
        context.update("form:datosVigenciasNoFormalesPersona");
    }

    //Ubicacion Celda.
    public void cambiarIndiceNF(VigenciasNoFormales vigenciaNoFormal, int celdaNF) {

        if (permitirIndex == true) {
            vigenciaNoFormalSeleccionada = vigenciaNoFormal;
            cualCelda = celdaNF;
            CualTabla = 1;
            tablaImprimir = ":formExportar:datosVigenciasNoFormalesExportar";
            cualNuevo = ":formularioDialogos:nuevaVigenciaFormal";
            cualInsertar = "formularioDialogos:NuevoRegistroVigenciaNoFormal";
            nombreArchivo = "VigenciasNoFormalesXML";
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:exportarXML");
            deshabilitarBotonLov();
            if (tipoListaNF == 0) {
                vigenciaNoFormalSeleccionada.getSecuencia();
                if (cualCelda == 1) {
                    habilitarBotonLov();
                    modificarInfoRegistroCursos(listaCursos.size());
                    TipoEducacion = vigenciaNoFormalSeleccionada.getCurso().getNombre();
                } else if (cualCelda == 3) {
                    habilitarBotonLov();
                    modificarInfoRegistroInstituciones(listaInstituciones.size());
                    Institucion = vigenciaNoFormalSeleccionada.getInstitucion().getDescripcion();
                } else if (cualCelda == 4) {
                    habilitarBotonLov();
                    modificarInfoRegistroAdiestramientoNF(listaAdiestramientosNoFormales.size());
                    AdiestramientoNF = vigenciaNoFormalSeleccionada.getAdiestramientonf().getDesccripcion();
                }
            } else {
                vigenciaNoFormalSeleccionada.getSecuencia();
                modificarInfoRegistroAdiestramientoNF(listaAdiestramientosNoFormales.size());
                modificarInfoRegistroCursos(listaCursos.size());
                modificarInfoRegistroInstituciones(listaInstituciones.size());
                if (cualCelda == 1) {
                    habilitarBotonLov();
                    Curso = vigenciaNoFormalSeleccionada.getCurso().getNombre();
                } else if (cualCelda == 3) {
                    habilitarBotonLov();
                    Institucion = vigenciaNoFormalSeleccionada.getInstitucion().getDescripcion();
                } else if (cualCelda == 4) {
                    habilitarBotonLov();
                    AdiestramientoNF = vigenciaNoFormalSeleccionada.getAdiestramientonf().getDesccripcion();
                }
            }
        }
    }

    //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO)
    public void asignarIndexNF(VigenciasNoFormales vigenciaNoFormal, int dlg, int LND) {
        vigenciaNoFormalSeleccionada = vigenciaNoFormal;
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
            System.out.println("Tipo Actualizacion: " + tipoActualizacion);
        } else if (LND == 2) {
            tipoActualizacion = 2;
        }
        if (dlg == 0) {
            modificarInfoRegistroCursos(listaCursos.size());
            RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCursos");
            habilitarBotonLov();
            context.update("form:cursosDialogo");
            context.execute("cursosDialogo.show()");
        } else if (dlg == 2) {
            habilitarBotonLov();
            modificarInfoRegistroInstituciones(listaInstituciones.size());
            RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroInstituciones");
            context.update("form:institucionesNFDialogo");
            context.execute("institucionesNFDialogo.show()");
        } else if (dlg == 3) {
            habilitarBotonLov();
            modificarInfoRegistroAdiestramientoNF(listaAdiestramientosNoFormales.size());
            RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroAdiestramientosNF");
            context.update("form:adiestramientosNFDialogo");
            context.execute("adiestramientosNFDialogo.show()");
        }

    }

    public void actualizarCursos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaNF == 0) {
                vigenciaNoFormalSeleccionada.setCurso(seleccionCursos);
                if (!listaVigenciasNoFormalesCrear.contains(vigenciaNoFormalSeleccionada)) {
                    if (listaVigenciasNoFormalesModificar.isEmpty()) {
                        listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
                    } else if (!listaVigenciasNoFormalesModificar.contains(vigenciaNoFormalSeleccionada)) {
                        listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
                    }
                }
            } else {
                vigenciaNoFormalSeleccionada.setCurso(seleccionCursos);
                if (!listaVigenciasNoFormalesCrear.contains(vigenciaNoFormalSeleccionada)) {
                    if (listaVigenciasNoFormalesModificar.isEmpty()) {
                        listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
                    } else if (!listaVigenciasNoFormalesModificar.contains(vigenciaNoFormalSeleccionada)) {
                        listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            permitirIndex = true;
            context.update("form:datosVigenciasNoFormalesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaNoFormal.setCurso(seleccionCursos);
            context.update("formularioDialogos:nuevaVigenciaNoFormal");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaNoFormal.setCurso(seleccionCursos);
            context.update("formularioDialogos:duplicarVigenciaNoFormal");
        }
        filtradoslistaCursos = null;
        seleccionCursos = null;
        aceptar = true;
        vigenciaNoFormalSeleccionada = null;
        vigenciaNoFormalSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVCursos:globalFilter");
        context.execute("LOVCursos.clearFilters()");
        context.execute("cursosDialogo.hide()");
        //context.update("formularioDialogos:LOVCursos");
    }

    public void actualizarInstitucionesNF() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaNF == 0) {
                vigenciaNoFormalSeleccionada.setInstitucion(seleccionInstituciones);
                if (!listaVigenciasNoFormalesCrear.contains(vigenciaNoFormalSeleccionada)) {
                    if (listaVigenciasNoFormalesModificar.isEmpty()) {
                        listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
                    } else if (!listaVigenciasNoFormalesModificar.contains(vigenciaNoFormalSeleccionada)) {
                        listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
                    }
                }
            } else {
                vigenciaNoFormalSeleccionada.setInstitucion(seleccionInstituciones);
                if (!listaVigenciasNoFormalesCrear.contains(vigenciaNoFormalSeleccionada)) {
                    if (listaVigenciasNoFormalesModificar.isEmpty()) {
                        listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
                    } else if (!listaVigenciasNoFormalesModificar.contains(vigenciaNoFormalSeleccionada)) {
                        listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            permitirIndex = true;
            context.update("form:datosVigenciasNoFormalesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaNoFormal.setInstitucion(seleccionInstituciones);
            context.update("formularioDialogos:nuevaVigenciaNoFormal");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaNoFormal.setInstitucion(seleccionInstituciones);
            context.update("formularioDialogos:duplicarVigenciaNoFormal");
        }
        filtradoslistaInstituciones = null;
        seleccionInstituciones = null;
        aceptar = true;
        vigenciaNoFormalSeleccionada = null;
        vigenciaNoFormalSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVInstitucionesNF:globalFilter");
        context.execute("LOVInstitucionesNF.clearFilters()");
        context.execute("institucionesNFDialogo.hide()");
        //context.update("formularioDialogos:LOVInstitucionesNF");
    }

    public void actualizarAdiestramientoNF() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaNF == 0) {
                vigenciaNoFormalSeleccionada.setAdiestramientonf(seleccionAdiestramientosNoFormales);
                if (!listaVigenciasNoFormalesCrear.contains(vigenciaNoFormalSeleccionada)) {
                    if (listaVigenciasNoFormalesModificar.isEmpty()) {
                        listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
                    } else if (!listaVigenciasNoFormalesModificar.contains(vigenciaNoFormalSeleccionada)) {
                        listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
                    }
                }
            } else {
                vigenciaNoFormalSeleccionada.setAdiestramientonf(seleccionAdiestramientosNoFormales);
                if (!listaVigenciasNoFormalesCrear.contains(vigenciaNoFormalSeleccionada)) {
                    if (listaVigenciasNoFormalesModificar.isEmpty()) {
                        listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
                    } else if (!listaVigenciasNoFormalesModificar.contains(vigenciaNoFormalSeleccionada)) {
                        listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
            permitirIndex = true;
            context.update("form:datosVigenciasNoFormalesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaNoFormal.setAdiestramientonf(seleccionAdiestramientosNoFormales);
            context.update("formularioDialogos:nuevaVigenciaNoFormal");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaNoFormal.setAdiestramientonf(seleccionAdiestramientosNoFormales);
            context.update("formularioDialogos:duplicarVigenciaNoFormal");
        }
        filtradoslistaAdiestramientosNoFormales = null;
        seleccionAdiestramientosNoFormales = null;
        aceptar = true;
        vigenciaNoFormalSeleccionada = null;
        vigenciaNoFormalSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVAdiestramientosNF:globalFilter");
        context.execute("LOVAdiestramientosNF.clearFilters()");
        context.execute("adiestramientosNFDialogo.hide()");
        //context.update("formularioDialogos:LOVAdiestramientosNF");
    }

    public void cancelarCambioInstitucionesNF() {
        filtradoslistaInstituciones = null;
        seleccionInstituciones = null;
        aceptar = true;
        vigenciaNoFormalSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVInstitucionesNF:globalFilter");
        context.execute("LOVInstitucionesNF.clearFilters()");
        context.execute("institucionesNFDialogo.hide()");
    }

    public void cancelarCambioCursoNF() {
        filtradoslistaCursos = null;
        seleccionCursos = null;
        aceptar = true;
        vigenciaNoFormalSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVCursos:globalFilter");
        context.execute("LOVCursos.clearFilters()");
        context.execute("cursosDialogo.hide()");
    }

    public void cancelarCambioAdiestramientoNF() {
        filtradoslistaAdiestramientosNoFormales = null;
        seleccionAdiestramientosNoFormales = null;
        aceptar = true;
        vigenciaNoFormalSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVAdiestramientosNF:globalFilter");
        context.execute("LOVAdiestramientosNF.clearFilters()");
        context.execute("adiestramientosNFDialogo.hide()");
    }

    public void autocompletarNuevoyDuplicadoNF(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("CURSO")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaNoFormal.getCurso().setNombre(Curso);
            } else if (tipoNuevo == 2) {
                duplicarVigenciaNoFormal.getCurso().setNombre(Curso);
            }
            for (int i = 0; i < listaCursos.size(); i++) {
                if (listaCursos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaNoFormal.setCurso(listaCursos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevoCursoNF");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaNoFormal.setCurso(listaCursos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarCursoNF");
                }
                listaCursos.clear();
                getListaCursos();
            } else {
                context.update("form:cursosDialogo");
                context.execute("cursosDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevoCursoNF");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarCursoNF");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("INSTITUCIONNF")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaNoFormal.getInstitucion().setDescripcion(Institucion);
            } else if (tipoNuevo == 2) {
                duplicarVigenciaNoFormal.getInstitucion().setDescripcion(Institucion);
            }
            for (int i = 0; i < listaInstituciones.size(); i++) {
                if (listaInstituciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaNoFormal.setInstitucion(listaInstituciones.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevaInstitucionNF");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaNoFormal.setInstitucion(listaInstituciones.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarInstitucionNF");
                }
                listaInstituciones.clear();
                getListaInstituciones();
            } else {
                context.update("form:institucionesNFDialogo");
                context.execute("institucionesNFDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaInstitucionNF");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarInstitucionNF");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("ADIESTRAMIENTONF")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaNoFormal.getAdiestramientonf().setDesccripcion(AdiestramientoNF);
            } else if (tipoNuevo == 2) {
                duplicarVigenciaNoFormal.getAdiestramientonf().setDesccripcion(AdiestramientoNF);
            }
            for (int i = 0; i < listaAdiestramientosNoFormales.size(); i++) {
                if (listaAdiestramientosNoFormales.get(i).getDesccripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaNoFormal.setAdiestramientonf(listaAdiestramientosNoFormales.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevoAdiestramientoNF");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaNoFormal.setAdiestramientonf(listaAdiestramientosNoFormales.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarAdiestramientoNF");
                }
                listaAdiestramientosNoFormales.clear();
                getListaAdiestramientosNoFormales();
            } else {
                context.update("form:adiestramientosNFDialogo");
                context.execute("adiestramientosNFDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevoAdiestramientoNF");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarAdiestramientoNF");
                }
            }
        }
    }

    public void valoresBackupAutocompletarNF(int tipoNuevo, String Campo) {
        if (Campo.equals("CURSO")) {
            if (tipoNuevo == 1) {
                Curso = nuevaVigenciaNoFormal.getCurso().getNombre();
            } else if (tipoNuevo == 2) {
                Curso = duplicarVigenciaNoFormal.getCurso().getNombre();
            } else if (Campo.equals("iNSTITUCIONNF")) {
                if (tipoNuevo == 1) {
                    Institucion = nuevaVigenciaNoFormal.getInstitucion().getDescripcion();
                } else if (tipoNuevo == 2) {
                    Institucion = duplicarVigenciaNoFormal.getInstitucion().getDescripcion();
                }
            } else if (Campo.equals("ADIESTRAMIENTONF")) {
                if (tipoNuevo == 1) {
                    AdiestramientoNF = nuevaVigenciaNoFormal.getAdiestramientonf().getDesccripcion();
                } else if (tipoNuevo == 2) {
                    AdiestramientoNF = duplicarVigenciaNoFormal.getAdiestramientonf().getDesccripcion();
                }
            }
        }
    }

    //CREAR NUEVA VIGENCIA NO FORMAL
    public void agregarNuevaVigenciaNoFormal() {
        int pasa = 0;
        CualTabla = 1;
        mensajeValidacionNF = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("Tamaño Lista Vigencias NF Modificar" + listaVigenciasNoFormalesModificar.size());

        if (nuevaVigenciaNoFormal.getFechavigencia() == null) {
            System.out.println("Entro a Fecha");
            mensajeValidacionNF = " * Fecha \n";
            pasa++;
        }
        if (nuevaVigenciaNoFormal.getCurso().getSecuencia() == null) {
            System.out.println("Entro a Curso");
            mensajeValidacionNF = mensajeValidacionNF + " * Curso \n";
            pasa++;
        }

        if (nuevaVigenciaNoFormal.getInstitucion().getSecuencia() == null) {
            System.out.println("Entro a Institucion");
            mensajeValidacionNF = mensajeValidacionNF + " * Institucion \n";
            pasa++;
        }

        if (nuevaVigenciaNoFormal.getAdiestramientonf().getSecuencia() == null) {
            System.out.println("Entro a AdiestramientoNF");
            mensajeValidacionNF = mensajeValidacionNF + " * Adiestramiento\n";
            pasa++;
        }

        if (pasa == 0) {
            if (bandera == 1) {

                if (CualTabla == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();

                    pEFechasNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEFechasNF");
                    pEFechasNF.setFilterStyle("display: none; visibility: hidden;");
                    pECursosNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pECursosNF");
                    pECursosNF.setFilterStyle("display: none; visibility: hidden;");
                    pETitulosNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pETitulosNF");
                    pETitulosNF.setFilterStyle("display: none; visibility: hidden;");
                    pEInstitucionesNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEInstitucionesNF");
                    pEInstitucionesNF.setFilterStyle("display: none; visibility: hidden;");
                    pEAdiestramientosNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEAdiestramientosNF");
                    pEAdiestramientosNF.setFilterStyle("display: none; visibility: hidden;");
                    pECalificacionesNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pECalificacionesNF");
                    pECalificacionesNF.setFilterStyle("display: none; visibility: hidden;");
                    pEObservacionesNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEObservacionesNF");
                    pEObservacionesNF.setFilterStyle("display: none; visibility: hidden;");
                    altoTabla2 = "115";
                    RequestContext.getCurrentInstance().update("form:datosVigenciasNoFormalesPersona");
                    bandera = 0;
                    filtradosListaVigenciasNoFormales = null;
                    tipoListaNF = 0;
                }
            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS NO FORMALES.
            System.out.println("entro a AGREGAR");
            k++;
            l = BigInteger.valueOf(k);
            nuevaVigenciaNoFormal.setSecuencia(l);
            nuevaVigenciaNoFormal.setPersona(empleado.getPersona());
//            if (nuevaVigenciaNoFormal.getCurso().getSecuencia() == null) {
//                nuevaVigenciaNoFormal.setCurso(null);
//            }
//            if (nuevaVigenciaNoFormal.getInstitucion().getSecuencia() == null) {
//                nuevaVigenciaNoFormal.setInstitucion(null);
//            }
//            if (nuevaVigenciaNoFormal.getAdiestramientonf().getSecuencia() == null) {
//                nuevaVigenciaNoFormal.setAdiestramientonf(null);
//            }
            listaVigenciasNoFormalesCrear.add(nuevaVigenciaNoFormal);
            if (listaVigenciasNoFormales == null) {
                listaVigenciasNoFormales = new ArrayList<VigenciasNoFormales>();
            }

            listaVigenciasNoFormales.add(nuevaVigenciaNoFormal);
            vigenciaNoFormalSeleccionada = nuevaVigenciaNoFormal;
            getListaVigenciasFormales();
            modificarInfoRegistroNF(listaVigenciasNoFormales.size());
            context.update("form:infoRegistroNF");
            context.update("form:datosVigenciasNoFormalesPersona");

            nuevaVigenciaNoFormal = new VigenciasNoFormales();
            nuevaVigenciaNoFormal.setCurso(new Cursos());
            nuevaVigenciaNoFormal.setInstitucion(new Instituciones());
            nuevaVigenciaNoFormal.setAdiestramientonf(new AdiestramientosNF());

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            context.execute("NuevoRegistroVigenciaNoFormal.hide()");
        } else {
            context.update("formularioDialogos:validacionNuevaVigenciaNoFormal");
            context.execute("validacionNuevaVigenciaNoFormal.show()");
        }
    }

    public void chiste() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("Cual Tabla= " + CualTabla);

        if ((listaVigenciasFormales.isEmpty() || listaVigenciasNoFormales.isEmpty())) {
            context.update("formularioDialogos:elegirTabla");
            context.execute("elegirTabla.show()");
        } else if (CualTabla == 0) {
            context.update("formularioDialogos:NuevoRegistroVigenciaFormal");
            context.execute("NuevoRegistroVigenciaFormal.show()");
        } else if (CualTabla == 1) {
            context.update("formularioDialogos:NuevoRegistroVigenciaNoFormal");
            context.execute("NuevoRegistroVigenciaNoFormal.show()");
        }
    }

    public void confirmarDuplicarNF() {

        listaVigenciasNoFormales.add(duplicarVigenciaNoFormal);
        listaVigenciasNoFormalesCrear.add(duplicarVigenciaNoFormal);
        RequestContext context = RequestContext.getCurrentInstance();
        modificarInfoRegistroNF(listaVigenciasNoFormales.size());
        context.update("form:infoRegistroNF");
        context.update("form:datosVigenciasNoFormalesPersona");
        vigenciaNoFormalSeleccionada = null;
        vigenciaNoFormalSeleccionada = null;
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            pEFechasNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEFechasNF");
            pEFechasNF.setFilterStyle("display: none; visibility: hidden;");
            pECursosNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pECursosNF");
            pECursosNF.setFilterStyle("display: none; visibility: hidden;");
            pETitulosNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pETitulosNF");
            pETitulosNF.setFilterStyle("display: none; visibility: hidden;");
            pEInstitucionesNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEInstitucionesNF");
            pEInstitucionesNF.setFilterStyle("display: none; visibility: hidden;");
            pEAdiestramientosNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEAdiestramientosNF");
            pEAdiestramientosNF.setFilterStyle("display: none; visibility: hidden;");
            pECalificacionesNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pECalificacionesNF");
            pECalificacionesNF.setFilterStyle("display: none; visibility: hidden;");
            pEObservacionesNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEObservacionesNF");
            pEObservacionesNF.setFilterStyle("display: none; visibility: hidden;");
            altoTabla2 = "115";
            RequestContext.getCurrentInstance().update("form:datosVigenciasNoFormalesPersona");
            bandera = 0;
            filtradosListaVigenciasNoFormales = null;
            tipoListaNF = 0;

        }
        duplicarVigenciaNoFormal = new VigenciasNoFormales();
    }

    public void dialogoVigenciasFormales() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:NuevoRegistroVigenciaFormal");
        context.execute("NuevoRegistroVigenciaFormal.show()");
    }

    public void dialogoVigenciasNoFormales() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:NuevoRegistroVigenciaNoFormal");
        context.execute("NuevoRegistroVigenciaNoFormal.show()");

    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        deshabilitarBotonLov();
        vigenciaFormalSeleccionada = null;
        modificarInfoRegistroF(filtradosListaVigenciasFormales.size());
        RequestContext.getCurrentInstance().update("form:infoRegistroF");
    }

    public void modificarInfoRegistroF(int valor) {
        infoRegistroF = String.valueOf(valor);
    }

    public void modificarInfoRegistroEducacion(int valor) {
        infoRegistroEducacion = String.valueOf(valor);
    }

    public void modificarInfoRegistroCursos(int valor) {
        infoRegistroCursos = String.valueOf(valor);
    }

    public void modificarInfoRegistroProfesion(int valor) {
        infoRegistrosProfesion = String.valueOf(valor);
    }

    public void modificarInfoRegistroInstituciones(int valor) {
        infoRegistroInstituciones = String.valueOf(valor);
    }

    public void modificarInfoRegistroInstitucionesF(int valor) {
        infoRegistroInstitucionesF = String.valueOf(valor);
    }

    public void modificarInfoRegistroAdiestramientoF(int valor) {
        infoRegistroAdiestramientosF = String.valueOf(valor);
    }

    public void modificarInfoRegistroAdiestramientoNF(int valor) {
        infoRegistroAdiestramientosNF = String.valueOf(valor);
    }

    public void contarRegistrosF() {
        if (listaVigenciasFormales != null) {
            modificarInfoRegistroF(listaVigenciasFormales.size());
        } else {
            modificarInfoRegistroF(0);
        }

    }

    public void eventoFiltrarEducacion() {
        modificarInfoRegistroEducacion(filtradoslistaTiposEducaciones.size());
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEducacion");
    }

    public void eventoFiltrarCursos() {
        modificarInfoRegistroCursos(filtradoslistaCursos.size());
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCursos");
    }

    public void eventoFiltrarProfesion() {
        modificarInfoRegistroProfesion(filtradoslistaProfesiones.size());
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistrosProfesion");
    }

    public void eventoFiltrarInstitucion() {
        modificarInfoRegistroInstituciones(filtradoslistaInstituciones.size());
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroInstituciones");
    }

    public void eventoFiltrarInstitucionF() {
        modificarInfoRegistroInstituciones(filtradoslistaInstituciones.size());
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroInstitucionesF");
    }

    public void eventoFiltrarAdiestramientoF() {
        modificarInfoRegistroAdiestramientoF(filtradoslistaAdiestramientosFormales.size());
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroAdiestramientosF");
    }

    public void eventoFiltrarAdiestramientoNF() {
        modificarInfoRegistroAdiestramientoNF(filtradoslistaAdiestramientosNoFormales.size());
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroAdiestramientosNF");
    }

    //EVENTO FILTRARNF
    public void eventoFiltrarNF() {
        if (tipoListaNF == 0) {
            tipoListaNF = 1;
        }
        deshabilitarBotonLov();
        vigenciaNoFormalSeleccionada = null;
        modificarInfoRegistroNF(filtradosListaVigenciasNoFormales.size());
        RequestContext.getCurrentInstance().update("form:infoRegistroNF");
    }

    public void modificarInfoRegistroNF(int valor) {
        infoRegistroNF = String.valueOf(valor);

    }

    public void contarRegistrosNF() {
        if (listaVigenciasNoFormales != null) {
            modificarInfoRegistroNF(listaVigenciasNoFormales.size());
        } else {
            modificarInfoRegistroNF(0);
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

    public void recordarSeleccionNF() {
        if (vigenciaNoFormalSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC2 = (DataTable) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona");
            tablaC2.setSelection(vigenciaNoFormalSeleccionada);
        }
    }

    public void recordarSeleccionF() {
        if (vigenciaFormalSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona");
            tablaC.setSelection(vigenciaFormalSeleccionada);
        }
    }

    //<--------------------------------------------FIN METODOS VIGENCIAS NO FORMALES ----------------------------------------->
//GETTER & SETTER
    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public BigInteger getSecuenciaPersona() {
        return secuenciaPersona;
    }

    public void setSecuenciaPersona(BigInteger secuenciaPersona) {
        this.secuenciaPersona = secuenciaPersona;
    }

    public List<VigenciasFormales> getListaVigenciasFormales() {
        if (listaVigenciasFormales == null && empleado.getPersona() != null) {
            listaVigenciasFormales = administrarVigenciasFormales.vigenciasFormalesPersona(empleado.getPersona().getSecuencia());
        }
        return listaVigenciasFormales;
    }

    public void setListaVigenciasFormales(List<VigenciasFormales> listaVigenciasFormales) {
        this.listaVigenciasFormales = listaVigenciasFormales;
    }

    public List<VigenciasFormales> getFiltradosListaVigenciasFormales() {
        return filtradosListaVigenciasFormales;
    }

    public void setFiltradosListaVigenciasFormales(List<VigenciasFormales> filtradosListaVigenciasFormales) {
        this.filtradosListaVigenciasFormales = filtradosListaVigenciasFormales;
    }

    public List<TiposEducaciones> getListaTiposEducaciones() {
        if (listaTiposEducaciones == null) {
            listaTiposEducaciones = administrarVigenciasFormales.lovTiposEducaciones();
        }
        return listaTiposEducaciones;
    }

    public void setListaEducaciones(List<TiposEducaciones> listaEducaciones) {
        this.listaTiposEducaciones = listaEducaciones;
    }

    public List<TiposEducaciones> getFiltradoslistaTiposEducaciones() {
        return filtradoslistaTiposEducaciones;
    }

    public void setFiltradoslistaTiposEducaciones(List<TiposEducaciones> filtradoslistaTiposEducaciones) {
        this.filtradoslistaTiposEducaciones = filtradoslistaTiposEducaciones;
    }

    public List<Profesiones> getListaProfesiones() {
        if (listaProfesiones == null) {
            listaProfesiones = administrarVigenciasFormales.lovProfesiones();
        }
        return listaProfesiones;
    }

    public void setListaProfesiones(List<Profesiones> listaProfesiones) {
        this.listaProfesiones = listaProfesiones;
    }

    public List<Profesiones> getFiltradoslistaProfesiones() {
        return filtradoslistaProfesiones;
    }

    public void setFiltradoslistaProfesiones(List<Profesiones> filtradoslistaProfesiones) {
        this.filtradoslistaProfesiones = filtradoslistaProfesiones;
    }

    public List<Instituciones> getListaInstituciones() {
        if (listaInstituciones == null) {
            listaInstituciones = administrarVigenciasFormales.lovInstituciones();
        }
        return listaInstituciones;
    }

    public void setListaInstituciones(List<Instituciones> listaInstituciones) {
        this.listaInstituciones = listaInstituciones;
    }

    public List<Instituciones> getFiltradoslistaInstituciones() {
        return filtradoslistaInstituciones;
    }

    public void setFiltradoslistaInstituciones(List<Instituciones> filtradoslistaInstituciones) {
        this.filtradoslistaInstituciones = filtradoslistaInstituciones;
    }

    public List<AdiestramientosF> getListaAdiestramientosFormales() {
        if (listaAdiestramientosFormales == null) {
            listaAdiestramientosFormales = administrarVigenciasFormales.lovAdiestramientosF();
        }
        return listaAdiestramientosFormales;
    }

    public void setListaAdiestramientosFormales(List<AdiestramientosF> listaAdiestramientosFormales) {
        this.listaAdiestramientosFormales = listaAdiestramientosFormales;
    }

    public List<AdiestramientosF> getFiltradoslistaAdiestramientosFormales() {
        return filtradoslistaAdiestramientosFormales;
    }

    public void setFiltradoslistaAdiestramientosFormales(List<AdiestramientosF> filtradoslistaAdiestramientosFormales) {
        this.filtradoslistaAdiestramientosFormales = filtradoslistaAdiestramientosFormales;
    }

    public TiposEducaciones getSeleccionTiposEducaciones() {
        return seleccionTiposEducaciones;
    }

    public void setSeleccionTiposEducaciones(TiposEducaciones seleccionTiposEducaciones) {
        this.seleccionTiposEducaciones = seleccionTiposEducaciones;
    }

    public Profesiones getSeleccionProfesiones() {
        return seleccionProfesiones;
    }

    public void setSeleccionProfesiones(Profesiones seleccionProfesiones) {
        this.seleccionProfesiones = seleccionProfesiones;
    }

    public Instituciones getSeleccionInstituciones() {
        return seleccionInstituciones;
    }

    public void setSeleccionInstituciones(Instituciones seleccionInstituciones) {
        this.seleccionInstituciones = seleccionInstituciones;
    }

    public AdiestramientosF getSeleccionAdiestramientosFormales() {
        return seleccionAdiestramientosFormales;
    }

    public void setSeleccionAdiestramientosFormales(AdiestramientosF seleccionAdiestramientosFormales) {
        this.seleccionAdiestramientosFormales = seleccionAdiestramientosFormales;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public VigenciasFormales getEditarVigenciasFormales() {
        return editarVigenciasFormales;
    }

    public void setEditarVigenciasFormales(VigenciasFormales editarVigenciasFormales) {
        this.editarVigenciasFormales = editarVigenciasFormales;
    }

    public VigenciasFormales getNuevaVigenciaFormal() {
        return nuevaVigenciaFormal;
    }

    public void setNuevaVigenciaFormal(VigenciasFormales nuevaVigenciaFormal) {
        this.nuevaVigenciaFormal = nuevaVigenciaFormal;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public VigenciasFormales getDuplicarVigenciaFormal() {
        return duplicarVigenciaFormal;
    }

    public void setDuplicarVigenciaFormal(VigenciasFormales duplicarVigenciaFormal) {
        this.duplicarVigenciaFormal = duplicarVigenciaFormal;
    }

// SETS Y GETS  de Vigencias No Formales
    public List<VigenciasNoFormales> getListaVigenciasNoFormales() {
        if (listaVigenciasNoFormales == null) {
            listaVigenciasNoFormales = administrarVigenciasNoFormales.vigenciasNoFormalesPersona(empleado.getPersona().getSecuencia());
        }
        return listaVigenciasNoFormales;
    }

    public void setListaVigenciasNoFormales(List<VigenciasNoFormales> listaVigenciasNoFormales) {
        this.listaVigenciasNoFormales = listaVigenciasNoFormales;
    }

    public List<VigenciasNoFormales> getFiltradosListaVigenciasNoFormales() {
        return filtradosListaVigenciasNoFormales;
    }

    public void setFiltradosListaVigenciasNoFormales(List<VigenciasNoFormales> filtradosListaVigenciasNoFormales) {
        this.filtradosListaVigenciasNoFormales = filtradosListaVigenciasNoFormales;
    }

    public List<Cursos> getListaCursos() {
        if (listaCursos==null) {
            listaCursos = administrarVigenciasNoFormales.lovCursos();
        }
        return listaCursos;
    }

    public void setListaCursos(List<Cursos> listaCursos) {
        this.listaCursos = listaCursos;
    }

    public List<Cursos> getFiltradoslistaCursos() {
        return filtradoslistaCursos;
    }

    public void setFiltradoslistaCursos(List<Cursos> filtradoslistaCursos) {
        this.filtradoslistaCursos = filtradoslistaCursos;
    }

    public Cursos getSeleccionCursos() {
        return seleccionCursos;
    }

    public void setSeleccionCursos(Cursos seleccionCursos) {
        this.seleccionCursos = seleccionCursos;
    }

    public List<AdiestramientosNF> getListaAdiestramientosNoFormales() {
        if (listaAdiestramientosNoFormales == null) {
            listaAdiestramientosNoFormales = administrarVigenciasNoFormales.lovAdiestramientosNF();
        }
        return listaAdiestramientosNoFormales;
    }

    public void setListaAdiestramientosNoFormales(List<AdiestramientosNF> listaAdiestramientosNoFormales) {
        this.listaAdiestramientosNoFormales = listaAdiestramientosNoFormales;
    }

    public List<AdiestramientosNF> getFiltradoslistaAdiestramientosNoFormales() {
        return filtradoslistaAdiestramientosNoFormales;
    }

    public void setFiltradoslistaAdiestramientosNoFormales(List<AdiestramientosNF> filtradoslistaAdiestramientosNoFormales) {
        this.filtradoslistaAdiestramientosNoFormales = filtradoslistaAdiestramientosNoFormales;
    }

    public AdiestramientosNF getSeleccionAdiestramientosNoFormales() {
        return seleccionAdiestramientosNoFormales;
    }

    public void setSeleccionAdiestramientosNoFormales(AdiestramientosNF seleccionAdiestramientosNoFormales) {
        this.seleccionAdiestramientosNoFormales = seleccionAdiestramientosNoFormales;
    }

    public VigenciasNoFormales getEditarVigenciasNoFormales() {
        return editarVigenciasNoFormales;
    }

    public void setEditarVigenciasNoFormales(VigenciasNoFormales editarVigenciasNoFormales) {
        this.editarVigenciasNoFormales = editarVigenciasNoFormales;
    }

    public String getTablaImprimir() {
        return tablaImprimir;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public VigenciasNoFormales getNuevaVigenciaNoFormal() {
        return nuevaVigenciaNoFormal;
    }

    public void setNuevaVigenciaNoFormal(VigenciasNoFormales nuevaVigenciaNoFormal) {
        this.nuevaVigenciaNoFormal = nuevaVigenciaNoFormal;
    }

    public String getAdiestramientoNF() {
        return AdiestramientoNF;
    }

    public void setAdiestramientoNF(String AdiestramientoNF) {
        this.AdiestramientoNF = AdiestramientoNF;
    }

    public String getMensajeValidacionNF() {
        return mensajeValidacionNF;
    }

    public void setMensajeValidacionNF(String mensajeValidacionNF) {
        this.mensajeValidacionNF = mensajeValidacionNF;
    }

    public String getCualInsertar() {
        return cualInsertar;
    }

    public String getCualNuevo() {
        return cualNuevo;
    }

    public VigenciasNoFormales getDuplicarVigenciaNoFormal() {
        return duplicarVigenciaNoFormal;
    }

    public void setDuplicarVigenciaNoFormal(VigenciasNoFormales duplicarVigenciaNoFormal) {
        this.duplicarVigenciaNoFormal = duplicarVigenciaNoFormal;
    }

    public VigenciasFormales getVigenciaFormalSeleccionada() {
        return vigenciaFormalSeleccionada;
    }

    public void setVigenciaFormalSeleccionada(VigenciasFormales vigenciaFormalSeleccionada) {
        this.vigenciaFormalSeleccionada = vigenciaFormalSeleccionada;
    }

    public VigenciasNoFormales getVigenciaNoFormalSeleccionada() {
        return vigenciaNoFormalSeleccionada;
    }

    public void setVigenciaNoFormalSeleccionada(VigenciasNoFormales vigenciaNoFormalSeleccionada) {
        this.vigenciaNoFormalSeleccionada = vigenciaNoFormalSeleccionada;
    }

    public String getAltoTabla1() {
        return altoTabla1;
    }

    public void setAltoTabla1(String altoTabla1) {
        this.altoTabla1 = altoTabla1;
    }

    public String getAltoTabla2() {
        return altoTabla2;
    }

    public void setAltoTabla2(String altoTabla2) {
        this.altoTabla2 = altoTabla2;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public String getInfoRegistroF() {
        return infoRegistroF;
    }

    public void setInfoRegistroF(String infoRegistroF) {
        this.infoRegistroF = infoRegistroF;
    }

    public String getInfoRegistroNF() {
        return infoRegistroNF;
    }

    public void setInfoRegistroNF(String infoRegistroNF) {
        this.infoRegistroNF = infoRegistroNF;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public String getInfoRegistroEducacion() {
        return infoRegistroEducacion;
    }

    public void setInfoRegistroEducacion(String infoRegistroEducacion) {
        this.infoRegistroEducacion = infoRegistroEducacion;
    }

    public String getInfoRegistroCursos() {
        return infoRegistroCursos;
    }

    public void setInfoRegistroCursos(String infoRegistroCursos) {
        this.infoRegistroCursos = infoRegistroCursos;
    }

    public String getInfoRegistrosProfesion() {
        return infoRegistrosProfesion;
    }

    public void setInfoRegistrosProfesion(String infoRegistrosProfesion) {
        this.infoRegistrosProfesion = infoRegistrosProfesion;
    }

    public String getInfoRegistroInstituciones() {
        return infoRegistroInstituciones;
    }

    public void setInfoRegistroInstituciones(String infoRegistroInstituciones) {
        this.infoRegistroInstituciones = infoRegistroInstituciones;
    }

    public String getInfoRegistroAdiestramientosF() {
        return infoRegistroAdiestramientosF;
    }

    public void setInfoRegistroAdiestramientosF(String infoRegistroAdiestramientosF) {
        this.infoRegistroAdiestramientosF = infoRegistroAdiestramientosF;
    }

    public String getInfoRegistroAdiestramientosNF() {
        return infoRegistroAdiestramientosNF;
    }

    public void setInfoRegistroAdiestramientosNF(String infoRegistroAdiestramientosNF) {
        this.infoRegistroAdiestramientosNF = infoRegistroAdiestramientosNF;
    }

    public String getInfoRegistroInstitucionesF() {
        return infoRegistroInstitucionesF;
    }

    public void setInfoRegistroInstitucionesF(String infoRegistroInstitucionesF) {
        this.infoRegistroInstitucionesF = infoRegistroInstitucionesF;
    }

}
