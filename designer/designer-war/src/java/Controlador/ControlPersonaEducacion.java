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
import java.util.Date;
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
    private String infoRegistroF, infoRegistroNF, infoRegistroEducacion, infoRegistroCursos, infoRegistrosProfesion, infoRegistroInstituciones, infoRegistroInstitucionesF, infoRegistroAdiestramientosF, infoRegistroAdiestramientosNF;
    private boolean activarLov;
    private DataTable tablaC, tablaC2;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlPersonaEducacion() {
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
        nuevaVigenciaFormal.setFechavigencia(new Date());
        nuevaVigenciaFormal.setTipoeducacion(new TiposEducaciones());
        nuevaVigenciaFormal.setProfesion(new Profesiones());
        nuevaVigenciaFormal.setInstitucion(new Instituciones());
        nuevaVigenciaFormal.setAdiestramientof(new AdiestramientosF());
        nuevaVigenciaFormal.setFechavigencia(new Date());
        nuevaVigenciaFormal.setFechavencimientotarjeta(new Date());
        nuevaVigenciaFormal.setFechaexpediciontarjeta(new Date());
        nuevaVigenciaNoFormal = new VigenciasNoFormales();
        nuevaVigenciaNoFormal.setCurso(new Cursos());
        nuevaVigenciaNoFormal.setInstitucion(new Instituciones());
        nuevaVigenciaNoFormal.setAdiestramientonf(new AdiestramientosNF());
        nuevaVigenciaNoFormal.setFechavigencia(new Date());
        guardado = true;
        tablaImprimir = ":formExportar:datosVigenciasFormalesExportar";
        nombreArchivo = "VigenciasFormalesXML";
        k = 0;
        cualInsertar = "formularioDialogos:NuevoRegistroVigenciaFormal";
        cualNuevo = "formularioDialogos:nuevaVigenciaFormal";
        m = 0;
        altoTabla1 = "105";
        altoTabla2 = "105";
        activarLov = true;
        paginaAnterior = " ";
        listaTiposEducaciones = null;
        listaProfesiones = null;
        listaInstituciones = null;
        listaAdiestramientosFormales = null;
        listaCursos = null;
        listaAdiestramientosNoFormales = null;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    public void limpiarListasValor() {

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

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        //inicializarCosas(); Inicializar cosas de ser necesario
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        //inicializarCosas(); Inicializar cosas de ser necesario
    }

    //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina();
            System.out.println("navegar('Atras') : " + pag);
        } else {
            String pagActual = "personaeducacion";
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
            controlListaNavegacion.guardarNavegacion(pagActual, pag);
        }
        limpiarListasValor();
        fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
    }

    public void recibirPersona(BigInteger secEmpl, String pagina) {
        paginaAnterior = pagina;
        persona = administrarVigenciasFormales.encontrarPersona(secEmpl);
        getPersona();
        listaVigenciasFormales = null;
        getListaVigenciasFormales();
        listaVigenciasNoFormales = null;
        getListaVigenciasNoFormales();
        if (!listaVigenciasFormales.isEmpty()) {
            vigenciaFormalSeleccionada = listaVigenciasFormales.get(0);
        }
    }

    public String redirigir() {
        return paginaAnterior;
    }

    //Ubicacion Celda.
    public void cambiarIndice(VigenciasFormales vigenciaformal, int celda) {
        vigenciaFormalSeleccionada = vigenciaformal;
        cualCelda = celda;
        CualTabla = 0;
        deshabilitarBotonLov();
        tablaImprimir = ":formExportar:datosVigenciasFormalesExportar";
        nombreArchivo = "VigenciasFormalesXML";
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:exportarXML");
        vigenciaFormalSeleccionada.getSecuencia();
        if (cualCelda == 0) {
            deshabilitarBotonLov();
            vigenciaFormalSeleccionada.getFechavigencia();
        } else if (cualCelda == 1) {
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
        } else if (cualCelda == 1) {
            deshabilitarBotonLov();
            vigenciaFormalSeleccionada.getCalificacionobtenida();
        } else if (cualCelda == 1) {
            deshabilitarBotonLov();
            vigenciaFormalSeleccionada.getTarjetaprofesional();
        } else if (cualCelda == 1) {
            deshabilitarBotonLov();
            vigenciaFormalSeleccionada.getFechaexpediciontarjeta();
        } else if (cualCelda == 1) {
            deshabilitarBotonLov();
            vigenciaFormalSeleccionada.getFechavencimientotarjeta();
        } else if (cualCelda == 1) {
            deshabilitarBotonLov();
            vigenciaFormalSeleccionada.getObservacion();
        }
    }

    public void modificarVigenciasFormales(VigenciasFormales vigenciaformal) {
        vigenciaFormalSeleccionada = vigenciaformal;
        if (tipoLista == 0) {
            if (!listaVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
                if (listaVigenciasFormalesModificar.isEmpty()) {
                    listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                } else if (!listaVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                    listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    deshabilitarBotonLov();
                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        } else if (!listaVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {
            if (listaVigenciasFormalesModificar.isEmpty()) {
                listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
            } else if (!listaVigenciasFormalesModificar.contains(vigenciaFormalSeleccionada)) {
                listaVigenciasFormalesModificar.add(vigenciaFormalSeleccionada);
            }
            if (guardado == true) {
                guardado = false;
                deshabilitarBotonLov();
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void modificarVigenciasNoFormales(VigenciasNoFormales vigencianoformal) {
        vigenciaNoFormalSeleccionada = vigencianoformal;
        if (tipoLista == 0) {
            if (!listaVigenciasNoFormalesCrear.contains(vigenciaNoFormalSeleccionada)) {
                if (listaVigenciasNoFormalesModificar.isEmpty()) {
                    listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
                } else if (!listaVigenciasNoFormalesModificar.contains(vigenciaNoFormalSeleccionada)) {
                    listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    deshabilitarBotonLov();
                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        } else if (!listaVigenciasNoFormalesCrear.contains(vigenciaNoFormalSeleccionada)) {
            if (listaVigenciasNoFormalesModificar.isEmpty()) {
                listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
            } else if (!listaVigenciasNoFormalesModificar.contains(vigenciaNoFormalSeleccionada)) {
                listaVigenciasNoFormalesModificar.add(vigenciaNoFormalSeleccionada);
            }
            if (guardado == true) {
                guardado = false;
                deshabilitarBotonLov();
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

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

            } else if (!listaVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {

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
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
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
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposEducacionesDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposEducacionesDialogo').show()");
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
                RequestContext.getCurrentInstance().update("formularioDialogos:profesionesDialogo");
                RequestContext.getCurrentInstance().execute("PF('profesionesDialogo').show()");
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
                RequestContext.getCurrentInstance().update("formularioDialogos:institucionesDialogo");
                RequestContext.getCurrentInstance().execute("PF('institucionesDialogo').show()");
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
                listaAdiestramientosFormales.clear();
                getListaAdiestramientosFormales();
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:profesionesDialogo");
                RequestContext.getCurrentInstance().execute("PF('profesionesDialogo').show()");
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
            } else if (!listaVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {

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
        RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
    }

    //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO)
    public void asignarIndex(VigenciasFormales vigenciaFormal, int dlg, int LND) {
        vigenciaFormalSeleccionada = vigenciaFormal;
        tipoActualizacion = LND;
        if (dlg == 0) {
            listaTiposEducaciones = null;
            cargarListaTiposEducaciones();
            contarRegistroEducacion();
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposEducacionesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposEducacionesDialogo').show()");
        } else if (dlg == 1) {
            listaProfesiones = null;
            cargarListaProfesiones();
            contarRegistroProfesion();
            RequestContext.getCurrentInstance().update("formularioDialogos:profesionesDialogo");
            RequestContext.getCurrentInstance().execute("PF('profesionesDialogo').show()");
        } else if (dlg == 2) {
            listaInstituciones = null;
            cargarListaInstituciones();
            contarRegistroInstitucionesF();
            RequestContext.getCurrentInstance().update("formularioDialogos:institucionesDialogo");
            RequestContext.getCurrentInstance().execute("PF('institucionesDialogo').show()");
        } else if (dlg == 3) {
            listaAdiestramientosFormales = null;
            cargarListaAdiestramientosF();
            contarRegistroAdiestramientoF();
            RequestContext.getCurrentInstance().update("formularioDialogos:adiestramientosFDialogo");
            RequestContext.getCurrentInstance().execute("PF('adiestramientosFDialogo').show()");
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
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaFormal.setTipoeducacion(seleccionTiposEducaciones);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigenciaFormal");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaFormal.setTipoeducacion(seleccionTiposEducaciones);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaFormal");
        }
        filtradoslistaTiposEducaciones = null;
        seleccionTiposEducaciones = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVTiposEducaciones:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTiposEducaciones').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposEducacionesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposEducacionesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposEducaciones");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTE");
    }

    public void cancelarCambioTiposEducaciones() {
        filtradoslistaTiposEducaciones = null;
        seleccionTiposEducaciones = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVTiposEducaciones:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTiposEducaciones').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposEducacionesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposEducacionesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposEducaciones");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTE");
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
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaFormal.setProfesion(seleccionProfesiones);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigenciaFormal");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaFormal.setProfesion(seleccionProfesiones);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaFormal");
        }
        filtradoslistaProfesiones = null;
        seleccionProfesiones = null;
        aceptar = true;
        vigenciaFormalSeleccionada = null;
        vigenciaFormalSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVProfesiones:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVProfesiones').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('profesionesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:profesionesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVProfesiones");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarP");
    }

    public void cancelarCambioProfesiones() {
        filtradoslistaProfesiones = null;
        seleccionProfesiones = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVProfesiones:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVProfesiones').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('profesionesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:profesionesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVProfesiones");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarP");
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
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaFormal.setInstitucion(seleccionInstituciones);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigenciaFormal");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaFormal.setInstitucion(seleccionInstituciones);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaFormal");
        }
        filtradoslistaInstituciones = null;
        seleccionInstituciones = null;
        aceptar = true;
        vigenciaFormalSeleccionada = null;
        vigenciaFormalSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVInstituciones:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVInstituciones').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('institucionesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:institucionesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVInstituciones");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarI");
    }

    public void cancelarCambioInstituciones() {
        filtradoslistaInstituciones = null;
        seleccionInstituciones = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVInstituciones:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVInstituciones').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('institucionesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:institucionesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVInstituciones");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarI");
    }

    public void cancelarCambioAdiestramientoF() {
        filtradoslistaAdiestramientosFormales = null;
        seleccionAdiestramientosFormales = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVAdiestramientosF:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVAdiestramientosF').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('adiestramientosFDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:adiestramientosFDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVAdiestramientosF");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarAF");
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
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaFormal.setAdiestramientof(seleccionAdiestramientosFormales);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigenciaFormal");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaFormal.setAdiestramientof(seleccionAdiestramientosFormales);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaFormal");
        }
        filtradoslistaAdiestramientosFormales = null;
        seleccionAdiestramientosFormales = null;
        aceptar = true;
        vigenciaFormalSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVAdiestramientosF:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVAdiestramientosF').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('adiestramientosFDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:adiestramientosFDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVAdiestramientosF");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarAF");
    }

    public void activarAceptar() {
        aceptar = false;
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (CualTabla == 0) {
            editarVigenciasFormales = vigenciaFormalSeleccionada;
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFecha");
                RequestContext.getCurrentInstance().execute("PF('editarFecha').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoEducacion");
                RequestContext.getCurrentInstance().execute("PF('editarTipoEducacion').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarProfesion");
                RequestContext.getCurrentInstance().execute("PF('editarProfesion').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarInstitucion");
                RequestContext.getCurrentInstance().execute("PF('editarInstitucion').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarAdiestramientoF");
                RequestContext.getCurrentInstance().execute("PF('editarAdiestramientoF').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValoracion");
                RequestContext.getCurrentInstance().execute("PF('editarValoracion').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNumeroTarjeta");
                RequestContext.getCurrentInstance().execute("PF('editarNumeroTarjeta').show()");
                cualCelda = -1;
            } else if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaExpedicion");
                RequestContext.getCurrentInstance().execute("PF('editarFechaExpedicion').show()");
                cualCelda = -1;
            } else if (cualCelda == 8) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaVencimiento");
                RequestContext.getCurrentInstance().execute("PF('editarFechaVencimiento').show()");
                cualCelda = -1;
            } else if (cualCelda == 9) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarObservacion");
                RequestContext.getCurrentInstance().execute("PF('editarObservacion').show()");
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
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaNF");
                RequestContext.getCurrentInstance().execute("PF('editarFechaNF').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCursoNF");
                RequestContext.getCurrentInstance().execute("PF('editarCursoNF').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTitulo");
                RequestContext.getCurrentInstance().execute("PF('editarTitulo').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarInstitucionNF");
                RequestContext.getCurrentInstance().execute("PF('editarInstitucionNF').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarAdiestramientoNF");
                RequestContext.getCurrentInstance().execute("PF('editarAdiestramientoNF').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCalificacionNF");
                RequestContext.getCurrentInstance().execute("PF('editarCalificacionNF').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarObservacion");
                RequestContext.getCurrentInstance().execute("PF('editarObservacion').show()");
                cualCelda = -1;
            }
            vigenciaNoFormalSeleccionada = null;
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (vigenciaFormalSeleccionada != null) {
            if (CualTabla == 0) {
                RequestContext context = RequestContext.getCurrentInstance();
                if (cualCelda == 1) {
                    listaTiposEducaciones = null;
                    cargarListaTiposEducaciones();
                    contarRegistroEducacion();
                    RequestContext.getCurrentInstance().update("formularioDialogos:tiposEducacionesDialogo");
                    RequestContext.getCurrentInstance().execute("PF('tiposEducacionesDialogo').show()");
                    tipoActualizacion = 0;
                } else if (cualCelda == 2) {
                    listaProfesiones = null;
                    cargarListaProfesiones();
                    contarRegistroProfesion();
                    RequestContext.getCurrentInstance().update("formularioDialogos:profesionesDialogo");
                    RequestContext.getCurrentInstance().execute("PF('profesionesDialogo').show()");
                    tipoActualizacion = 0;
                } else if (cualCelda == 3) {
                    listaInstituciones = null;
                    cargarListaInstituciones();
                    contarRegistroInstitucionesF();
                    RequestContext.getCurrentInstance().update("formularioDialogos:institucionesDialogo");
                    RequestContext.getCurrentInstance().execute("PF('institucionesDialogo').show()");
                    tipoActualizacion = 0;
                } else if (cualCelda == 4) {
                    listaAdiestramientosFormales = null;
                    cargarListaAdiestramientosF();
                    contarRegistroAdiestramientoF();
                    RequestContext.getCurrentInstance().update("formularioDialogos:adiestramientosFDialogo");
                    RequestContext.getCurrentInstance().execute("PF('adiestramientosFDialogo').show()");
                    tipoActualizacion = 0;
                }

            }
        } else if (vigenciaNoFormalSeleccionada != null) {
            if (CualTabla == 1) {
                RequestContext context = RequestContext.getCurrentInstance();
                if (cualCelda == 1) {
                    listaCursos = null;
                    cargarListaCursos();
                    contarRegistroEducacion();
                    RequestContext.getCurrentInstance().update("formularioDialogos:cursosDialogo");
                    RequestContext.getCurrentInstance().execute("PF('cursosDialogo').show()");
                    tipoActualizacion = 0;
                } else if (cualCelda == 3) {
                    listaInstituciones = null;
                    cargarListaInstituciones();
                    contarRegistroInstituciones();
                    RequestContext.getCurrentInstance().update("formularioDialogos:institucionesNFDialogo");
                    RequestContext.getCurrentInstance().execute("PF('institucionesNFDialogo').show()");
                    tipoActualizacion = 0;
                } else if (cualCelda == 4) {
                    listaAdiestramientosNoFormales = null;
                    cargarListaAdiestramientosNF();
                    contarRegistroAdiestramientoNF();
                    RequestContext.getCurrentInstance().update("formularioDialogos:adiestramientosNFDialogo");
                    RequestContext.getCurrentInstance().execute("PF('adiestramientosNFDialogo').show()");
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
            pEFechas.setFilterStyle("width: 85% !important");
            pETiposEducaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pETiposEducaciones");
            pETiposEducaciones.setFilterStyle("width: 85% !important");
            pEProfesiones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEProfesiones");
            pEProfesiones.setFilterStyle("width: 85% !important");
            pEInstituciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEInstituciones");
            pEInstituciones.setFilterStyle("width: 85% !important");
            pEAdiestramientosF = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEAdiestramientosF");
            pEAdiestramientosF.setFilterStyle("width: 85% !important");
            pECalificaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pECalificaciones");
            pECalificaciones.setFilterStyle("width: 85% !important");
            pENumerosTarjetas = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pENumerosTarjetas");
            pENumerosTarjetas.setFilterStyle("width: 85% !important");
            pEFechasExpediciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasExpediciones");
            pEFechasExpediciones.setFilterStyle("width: 85% !important");
            pEFechasVencimientos = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEFechasVencimientos");
            pEFechasVencimientos.setFilterStyle("width: 85% !important");
            pEObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona:pEObservaciones");
            pEObservaciones.setFilterStyle("width: 85% !important");
            altoTabla1 = "85";
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
            bandera = 1;

            ////////////
            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            pEFechasNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEFechasNF");
            pEFechasNF.setFilterStyle("width: 85% !important");
            pECursosNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pECursosNF");
            pECursosNF.setFilterStyle("width: 85% !important");
            pETitulosNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pETitulosNF");
            pETitulosNF.setFilterStyle("width: 85% !important");
            pEInstitucionesNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEInstitucionesNF");
            pEInstitucionesNF.setFilterStyle("width: 85% !important");
            pEAdiestramientosNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEAdiestramientosNF");
            pEAdiestramientosNF.setFilterStyle("width: 85% !important");
            pECalificacionesNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pECalificacionesNF");
            pECalificacionesNF.setFilterStyle("width: 85% !important");
            pEObservacionesNF = (Column) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona:pEObservacionesNF");
            pEObservacionesNF.setFilterStyle("width: 85% !important");
            altoTabla2 = "85";
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
            altoTabla1 = "105";
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
            altoTabla2 = "105";
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
        } else if (CualTabla == 1) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasNoFormalesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "VigenciasNoFormalesPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
        }
    }

    public void exportXLS() throws IOException {
        if (CualTabla == 0) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasFormalesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "VigenciasFormalesXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (CualTabla == 1) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasNoFormalesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "VigenciasNoFormalesXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
        }
    }

    //LIMPIAR NUEVO REGISTRO
    public void limpiarNuevaVigenciaFormal() {
        nuevaVigenciaFormal = new VigenciasFormales();
        nuevaVigenciaFormal.setFechavigencia(new Date());
        nuevaVigenciaFormal.setTipoeducacion(new TiposEducaciones());
        nuevaVigenciaFormal.setProfesion(new Profesiones());
        nuevaVigenciaFormal.setInstitucion(new Instituciones());
        nuevaVigenciaFormal.setAdiestramientof(new AdiestramientosF());
        nuevaVigenciaFormal.setFechavigencia(new Date());
        nuevaVigenciaFormal.setFechavencimientotarjeta(new Date());
        nuevaVigenciaFormal.setFechaexpediciontarjeta(new Date());
    }

    public void limpiarNuevaVigenciaNoFormal() {

        nuevaVigenciaNoFormal = new VigenciasNoFormales();
        nuevaVigenciaNoFormal.setCurso(new Cursos());
        nuevaVigenciaNoFormal.setInstitucion(new Instituciones());
        nuevaVigenciaNoFormal.setAdiestramientonf(new AdiestramientosNF());
        nuevaVigenciaNoFormal.setFechavigencia(new Date());
    }

//    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
//        if (Campo.equals("TIPOEDUCACION")) {
//            if (tipoNuevo == 1) {
//                TipoEducacion = nuevaVigenciaFormal.getTipoeducacion().getNombre();
//            } else if (tipoNuevo == 2) {
//                TipoEducacion = duplicarVigenciaFormal.getTipoeducacion().getNombre();
//            } else if (Campo.equals("PROFESION")) {
//                if (tipoNuevo == 1) {
//                    Profesion = nuevaVigenciaFormal.getProfesion().getDescripcion();
//                } else if (tipoNuevo == 2) {
//                    Profesion = duplicarVigenciaFormal.getProfesion().getDescripcion();
//                }
//            } else if (Campo.equals("INSTITUCION")) {
//                if (tipoNuevo == 1) {
//                    Institucion = nuevaVigenciaFormal.getInstitucion().getDescripcion();
//                } else if (tipoNuevo == 2) {
//                    Institucion = duplicarVigenciaFormal.getInstitucion().getDescripcion();
//                }
//            } else if (Campo.equals("ADIESTRAMIENTOF")) {
//                if (tipoNuevo == 1) {
//                    AdiestramientoF = nuevaVigenciaFormal.getAdiestramientof().getDescripcion();
//                } else if (tipoNuevo == 2) {
//                    AdiestramientoF = duplicarVigenciaFormal.getAdiestramientof().getDescripcion();
//                }
//            }
//        }
//    }
//
//    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
//        int coincidencias = 0;
//        int indiceUnicoElemento = 0;
//        RequestContext context = RequestContext.getCurrentInstance();
//        if (confirmarCambio.equalsIgnoreCase("TIPOEDUCACION")) {
//            if (tipoNuevo == 1) {
//                nuevaVigenciaFormal.getTipoeducacion().setNombre(TipoEducacion);
//            } else if (tipoNuevo == 2) {
//                duplicarVigenciaFormal.getTipoeducacion().setNombre(TipoEducacion);
//            }
//            for (int i = 0; i < listaTiposEducaciones.size(); i++) {
//                if (listaTiposEducaciones.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
//                    indiceUnicoElemento = i;
//                    coincidencias++;
//                }
//            }
//            if (coincidencias == 1) {
//                if (tipoNuevo == 1) {
//                    nuevaVigenciaFormal.setTipoeducacion(listaTiposEducaciones.get(indiceUnicoElemento));
//                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoEducacion");
//                } else if (tipoNuevo == 2) {
//                    duplicarVigenciaFormal.setTipoeducacion(listaTiposEducaciones.get(indiceUnicoElemento));
//                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoEducacion");
//                }
//                listaTiposEducaciones.clear();
//                getListaTiposEducaciones();
//            } else {
//                RequestContext.getCurrentInstance().update("form:tiposEducacionesDialogo");
//                RequestContext.getCurrentInstance().execute("PF('tiposEducacionesDialogo').show()");
//                tipoActualizacion = tipoNuevo;
//                if (tipoNuevo == 1) {
//                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoEducacion");
//                } else if (tipoNuevo == 2) {
//                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoEducacion");
//                }
//            }
//        } else if (confirmarCambio.equalsIgnoreCase("PROFESION")) {
//            if (tipoNuevo == 1) {
//                nuevaVigenciaFormal.getProfesion().setDescripcion(Profesion);
//            } else if (tipoNuevo == 2) {
//                duplicarVigenciaFormal.getProfesion().setDescripcion(Profesion);
//            }
//            for (int i = 0; i < listaProfesiones.size(); i++) {
//                if (listaProfesiones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
//                    indiceUnicoElemento = i;
//                    coincidencias++;
//                }
//            }
//            if (coincidencias == 1) {
//                if (tipoNuevo == 1) {
//                    nuevaVigenciaFormal.setProfesion(listaProfesiones.get(indiceUnicoElemento));
//                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProfesion");
//                } else if (tipoNuevo == 2) {
//                    duplicarVigenciaFormal.setProfesion(listaProfesiones.get(indiceUnicoElemento));
//                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProfesion");
//                }
//                listaProfesiones.clear();
//                getListaProfesiones();
//            } else {
//                RequestContext.getCurrentInstance().update("form:profesionesDialogo");
//                RequestContext.getCurrentInstance().execute("PF('profesionesDialogo').show()");
//                tipoActualizacion = tipoNuevo;
//                if (tipoNuevo == 1) {
//                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProfesion");
//                } else if (tipoNuevo == 2) {
//                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProfesion");
//                }
//            }
//        } else if (confirmarCambio.equalsIgnoreCase("INSTITUCION")) {
//            if (tipoNuevo == 1) {
//                nuevaVigenciaFormal.getInstitucion().setDescripcion(Institucion);
//            } else if (tipoNuevo == 2) {
//                duplicarVigenciaFormal.getInstitucion().setDescripcion(Institucion);
//            }
//            for (int i = 0; i < listaInstituciones.size(); i++) {
//                if (listaInstituciones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
//                    indiceUnicoElemento = i;
//                    coincidencias++;
//                }
//            }
//            if (coincidencias == 1) {
//                if (tipoNuevo == 1) {
//                    nuevaVigenciaFormal.setInstitucion(listaInstituciones.get(indiceUnicoElemento));
//                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaInstitucion");
//                } else if (tipoNuevo == 2) {
//                    duplicarVigenciaFormal.setInstitucion(listaInstituciones.get(indiceUnicoElemento));
//                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarInstitucion");
//                }
//                listaInstituciones.clear();
//                getListaInstituciones();
//            } else {
//                RequestContext.getCurrentInstance().update("form:institucionesDialogo");
//                RequestContext.getCurrentInstance().execute("PF('institucionesDialogo').show()");
//                tipoActualizacion = tipoNuevo;
//                if (tipoNuevo == 1) {
//                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaInstitucion");
//                } else if (tipoNuevo == 2) {
//                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarInstitucion");
//                }
//            }
//        } else if (confirmarCambio.equalsIgnoreCase("ADIESTRAMIENTOF")) {
//            if (tipoNuevo == 1) {
//                nuevaVigenciaFormal.getAdiestramientof().setDescripcion(AdiestramientoF);
//            } else if (tipoNuevo == 2) {
//                duplicarVigenciaFormal.getAdiestramientof().setDescripcion(AdiestramientoF);
//            }
//            for (int i = 0; i < listaAdiestramientosFormales.size(); i++) {
//                if (listaAdiestramientosFormales.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
//                    indiceUnicoElemento = i;
//                    coincidencias++;
//                }
//            }
//            if (coincidencias == 1) {
//                if (tipoNuevo == 1) {
//                    nuevaVigenciaFormal.setAdiestramientof(listaAdiestramientosFormales.get(indiceUnicoElemento));
//                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAdiestramientoF");
//                } else if (tipoNuevo == 2) {
//                    duplicarVigenciaFormal.setAdiestramientof(listaAdiestramientosFormales.get(indiceUnicoElemento));
//                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAdiestramientoF");
//                }
//                listaAdiestramientosFormales.clear();
//                getListaAdiestramientosFormales();
//            } else {
//                RequestContext.getCurrentInstance().update("form:adiestramientosFDialogo");
//                RequestContext.getCurrentInstance().execute("PF('adiestramientosFDialogo').show()");
//                tipoActualizacion = tipoNuevo;
//                if (tipoNuevo == 1) {
//                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAdiestramientoF");
//                } else if (tipoNuevo == 2) {
//                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAdiestramientoF");
//                }
//            }
//        }
//    }
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
                altoTabla1 = "105";
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
            nuevaVigenciaFormal.setPersona(persona);
            listaVigenciasFormalesCrear.add(nuevaVigenciaFormal);
            listaVigenciasFormales.add(nuevaVigenciaFormal);
            contarRegistrosF();
            RequestContext.getCurrentInstance().update("form:infoRegistroF");
            vigenciaFormalSeleccionada = nuevaVigenciaFormal;
            vigenciaNoFormalSeleccionada = null;
            nuevaVigenciaFormal = new VigenciasFormales();
            nuevaVigenciaFormal.setTipoeducacion(new TiposEducaciones());
            nuevaVigenciaFormal.setProfesion(new Profesiones());
            nuevaVigenciaFormal.setInstitucion(new Instituciones());
            nuevaVigenciaFormal.setAdiestramientof(new AdiestramientosF());
            nuevaVigenciaFormal.setFechavigencia(new Date());
            nuevaVigenciaFormal.setFechavencimientotarjeta(new Date());
            nuevaVigenciaFormal.setFechaexpediciontarjeta(new Date());
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
            RequestContext.getCurrentInstance().update("form:datosVigenciasNoFormalesPersona");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciaFormal').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaVigenciaFormal");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaVigenciaFormal').show()");
        }
    }

    //BORRAR VIGENCIA FORMAL
    public void borrarVigenciaFormal() {

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
            contarRegistrosF();
            RequestContext.getCurrentInstance().update("form:infoRegistroF");
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
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
            contarRegistrosNF();
            RequestContext.getCurrentInstance().update("form:infoRegistroNF");
            RequestContext.getCurrentInstance().update("form:datosVigenciasNoFormalesPersona");
            vigenciaNoFormalSeleccionada = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
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
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaFormal");
                RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciaFormal').show()");
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
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaNoFormal");
                RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciaNoFormal').show()");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {

        listaVigenciasFormales.add(duplicarVigenciaFormal);
        listaVigenciasFormalesCrear.add(duplicarVigenciaFormal);
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistrosF();
        RequestContext.getCurrentInstance().update("form:infoRegistroF");
        RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
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
            altoTabla1 = "105";
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
            } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASFORMALES")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
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
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDBNF').show()");
                } else if (resultadoNF == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroNF').show()");
                } else if (resultadoNF == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastroNF').show()");
                } else if (resultadoNF == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastroNF').show()");
                } else if (resultadoNF == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroNF').show()");
                }
            } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASNOFORMALES")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoNF').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistoricoNF').show()");
            }
            vigenciaNoFormalSeleccionada = null;
        }

    }

    public void salir() {
        limpiarListasValor();

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
            altoTabla1 = "105";
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
            altoTabla1 = "105";
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
            altoTabla2 = "105";
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
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
        RequestContext.getCurrentInstance().update("form:datosVigenciasNoFormalesPersona");
    }

    //GUARDAR TODO
    public void guardarTodo() {
        try {
            if (guardado == false) {
                if (!listaVigenciasNoFormalesBorrar.isEmpty()) {
                    administrarVigenciasNoFormales.borrarVigenciaNoFormal(listaVigenciasNoFormalesBorrar);
                    listaVigenciasNoFormalesBorrar.clear();
                }
                if (!listaVigenciasNoFormalesCrear.isEmpty()) {
                    administrarVigenciasNoFormales.crearVigenciaNoFormal(listaVigenciasNoFormalesCrear);
                    listaVigenciasNoFormalesCrear.clear();
                }

                if (!listaVigenciasNoFormalesModificar.isEmpty()) {
                    administrarVigenciasNoFormales.modificarVigenciaNoFormal(listaVigenciasNoFormalesModificar);
                    listaVigenciasNoFormalesModificar.clear();
                }

                if (!listaVigenciasFormalesBorrar.isEmpty()) {
                    System.out.println("Borrando...");
                    administrarVigenciasFormales.borrarVigenciaFormal(listaVigenciasFormalesBorrar);
                    listaVigenciasFormalesBorrar.clear();
                }
                if (!listaVigenciasFormalesCrear.isEmpty()) {
                    administrarVigenciasFormales.crearVigenciaFormal(listaVigenciasFormalesCrear);
                    listaVigenciasFormalesCrear.clear();
                }
                if (!listaVigenciasFormalesModificar.isEmpty()) {
                    administrarVigenciasFormales.modificarVigenciaFormal(listaVigenciasFormalesModificar);
                    listaVigenciasFormalesModificar.clear();
                }

                listaVigenciasNoFormales = null;
                getListaVigenciasNoFormales();
                listaVigenciasFormales = null;
                getListaVigenciasFormales();
                contarRegistrosNF();
                contarRegistrosF();
                vigenciaNoFormalSeleccionada = null;
                vigenciaFormalSeleccionada = null;
                guardado = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:datosVigenciasNoFormalesPersona");
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }

        } catch (Exception e) {
            System.out.println("Error ControlPersonaEducacion.guardarTodo() : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, Por favor intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
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
            } else if (!listaVigenciasNoFormalesCrear.contains(vigenciaNoFormalSeleccionada)) {

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
            RequestContext.getCurrentInstance().update("form:datosVigenciasNoFormalesPersona");
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
                RequestContext.getCurrentInstance().update("formularioDialogos:cursosDialogo");
                RequestContext.getCurrentInstance().execute("PF('cursosDialogo').show()");
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
                RequestContext.getCurrentInstance().update("formularioDialogos:institucionesDialogo");
                RequestContext.getCurrentInstance().execute("PF('institucionesDialogo').show()");
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
                RequestContext.getCurrentInstance().update("formularioDialogos:adiestramientosNFDialogo");
                RequestContext.getCurrentInstance().execute("PF('adiestramientosNFDialogo').show()");
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
            } else if (!listaVigenciasFormalesCrear.contains(vigenciaFormalSeleccionada)) {

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
        RequestContext.getCurrentInstance().update("form:datosVigenciasNoFormalesPersona");
    }

    //Ubicacion Celda.
    public void cambiarIndiceNF(VigenciasNoFormales vigenciaNoFormal, int celdaNF) {

        vigenciaNoFormalSeleccionada = vigenciaNoFormal;
        cualCelda = celdaNF;
        CualTabla = 1;
        tablaImprimir = ":formExportar:datosVigenciasNoFormalesExportar";
        cualNuevo = ":formularioDialogos:nuevaVigenciaFormal";
        cualInsertar = "formularioDialogos:NuevoRegistroVigenciaNoFormal";
        nombreArchivo = "VigenciasNoFormalesXML";
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:exportarXML");
        deshabilitarBotonLov();
        vigenciaNoFormalSeleccionada.getSecuencia();
        if (cualCelda == 0) {
            deshabilitarBotonLov();
            vigenciaNoFormalSeleccionada.getFechavigencia();
        } else if (cualCelda == 1) {
            habilitarBotonLov();
            TipoEducacion = vigenciaNoFormalSeleccionada.getCurso().getNombre();
        } else if (cualCelda == 2) {
            deshabilitarBotonLov();
            TipoEducacion = vigenciaNoFormalSeleccionada.getTitulo();
        } else if (cualCelda == 3) {
            habilitarBotonLov();
            Institucion = vigenciaNoFormalSeleccionada.getInstitucion().getDescripcion();
        } else if (cualCelda == 4) {
            habilitarBotonLov();
            contarRegistroAdiestramientoNF();
            AdiestramientoNF = vigenciaNoFormalSeleccionada.getAdiestramientonf().getDesccripcion();
        } else if (cualCelda == 5) {
            deshabilitarBotonLov();
            vigenciaNoFormalSeleccionada.getCalificacionobtenida();
        } else if (cualCelda == 6) {
            deshabilitarBotonLov();
            vigenciaNoFormalSeleccionada.getObservacion();
        }
    }

    //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO)
    public void asignarIndexNF(VigenciasNoFormales vigenciaNoFormal, int dlg, int LND) {
        vigenciaNoFormalSeleccionada = vigenciaNoFormal;
        tipoActualizacion = LND;
        if (dlg == 0) {
            listaCursos = null;
            cargarListaCursos();
            contarRegistroCursos();
            RequestContext.getCurrentInstance().update("formularioDialogos:cursosDialogo");
            RequestContext.getCurrentInstance().execute("PF('cursosDialogo').show()");
        } else if (dlg == 2) {
            listaInstituciones = null;
            cargarListaInstituciones();
            contarRegistroInstituciones();
            RequestContext.getCurrentInstance().update("formularioDialogos:institucionesNFDialogo");
            RequestContext.getCurrentInstance().execute("PF('institucionesNFDialogo').show()");
        } else if (dlg == 3) {
            listaAdiestramientosNoFormales = null;
            cargarListaAdiestramientosNF();
            habilitarBotonLov();
            contarRegistroAdiestramientoNF();
            RequestContext.getCurrentInstance().update("formularioDialogos:adiestramientosNFDialogo");
            RequestContext.getCurrentInstance().execute("PF('adiestramientosNFDialogo').show()");
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
            RequestContext.getCurrentInstance().update("form:datosVigenciasNoFormalesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaNoFormal.setCurso(seleccionCursos);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigenciaNoFormal");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaNoFormal.setCurso(seleccionCursos);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaNoFormal");
        }
        filtradoslistaCursos = null;
        seleccionCursos = null;
        aceptar = true;
        vigenciaNoFormalSeleccionada = null;
        vigenciaNoFormalSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVCursos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVCursos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('cursosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:cursosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVCursos");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarC");
    }

    public void cancelarCambioCursoNF() {
        filtradoslistaCursos = null;
        seleccionCursos = null;
        aceptar = true;
        vigenciaNoFormalSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVCursos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVCursos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('cursosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:cursosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVCursos");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarC");
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
            RequestContext.getCurrentInstance().update("form:datosVigenciasNoFormalesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaNoFormal.setInstitucion(seleccionInstituciones);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigenciaNoFormal");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaNoFormal.setInstitucion(seleccionInstituciones);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaNoFormal");
        }
        filtradoslistaInstituciones = null;
        seleccionInstituciones = null;
        aceptar = true;
        vigenciaNoFormalSeleccionada = null;
        vigenciaNoFormalSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVInstitucionesNF:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVInstitucionesNF').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('institucionesNFDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:institucionesNFDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVInstitucionesNF");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarINF");
    }

    public void cancelarCambioInstitucionesNF() {
        filtradoslistaInstituciones = null;
        seleccionInstituciones = null;
        aceptar = true;
        vigenciaNoFormalSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVInstitucionesNF:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVInstitucionesNF').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('institucionesNFDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:institucionesNFDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVInstitucionesNF");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarINF");
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
            RequestContext.getCurrentInstance().update("form:datosVigenciasNoFormalesPersona");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaNoFormal.setAdiestramientonf(seleccionAdiestramientosNoFormales);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigenciaNoFormal");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaNoFormal.setAdiestramientonf(seleccionAdiestramientosNoFormales);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaNoFormal");
        }
        filtradoslistaAdiestramientosNoFormales = null;
        seleccionAdiestramientosNoFormales = null;
        aceptar = true;
        vigenciaNoFormalSeleccionada = null;
        vigenciaNoFormalSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVAdiestramientosNF:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVAdiestramientosNF').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('adiestramientosNFDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:adiestramientosNFDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVAdiestramientosNF");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarANF");
    }

    public void cancelarCambioAdiestramientoNF() {
        filtradoslistaAdiestramientosNoFormales = null;
        seleccionAdiestramientosNoFormales = null;
        aceptar = true;
        vigenciaNoFormalSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVAdiestramientosNF:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVAdiestramientosNF').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('adiestramientosNFDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:adiestramientosNFDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVAdiestramientosNF");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarANF");
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
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCursoNF");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaNoFormal.setCurso(listaCursos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCursoNF");
                }
                listaCursos.clear();
                getListaCursos();
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:cursosDialogo");
                RequestContext.getCurrentInstance().execute("PF('cursosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCursoNF");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCursoNF");
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
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaInstitucionNF");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaNoFormal.setInstitucion(listaInstituciones.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarInstitucionNF");
                }
                listaInstituciones.clear();
                getListaInstituciones();
            } else {
                RequestContext.getCurrentInstance().update("form:institucionesNFDialogo");
                RequestContext.getCurrentInstance().execute("PF('institucionesNFDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaInstitucionNF");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarInstitucionNF");
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
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAdiestramientoNF");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaNoFormal.setAdiestramientonf(listaAdiestramientosNoFormales.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAdiestramientoNF");
                }
                listaAdiestramientosNoFormales.clear();
                getListaAdiestramientosNoFormales();
            } else {
                RequestContext.getCurrentInstance().update("form:adiestramientosNFDialogo");
                RequestContext.getCurrentInstance().execute("PF('adiestramientosNFDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAdiestramientoNF");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAdiestramientoNF");
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
                    altoTabla2 = "105";
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
            nuevaVigenciaNoFormal.setPersona(persona);
            listaVigenciasNoFormalesCrear.add(nuevaVigenciaNoFormal);
            if (listaVigenciasNoFormales == null) {
                listaVigenciasNoFormales = new ArrayList<VigenciasNoFormales>();
            }

            listaVigenciasNoFormales.add(nuevaVigenciaNoFormal);
            vigenciaNoFormalSeleccionada = nuevaVigenciaNoFormal;
            vigenciaFormalSeleccionada = null;
            getListaVigenciasFormales();
            contarRegistrosNF();
            RequestContext.getCurrentInstance().update("form:infoRegistroNF");
            RequestContext.getCurrentInstance().update("form:datosVigenciasNoFormalesPersona");
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormalesPersona");
            nuevaVigenciaNoFormal = new VigenciasNoFormales();
            nuevaVigenciaNoFormal.setCurso(new Cursos());
            nuevaVigenciaNoFormal.setInstitucion(new Instituciones());
            nuevaVigenciaNoFormal.setAdiestramientonf(new AdiestramientosNF());

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciaNoFormal').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaVigenciaNoFormal");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaVigenciaNoFormal').show()");
        }
    }

    public void elegirTabla() {
        RequestContext.getCurrentInstance().update("formularioDialogos:elegirTabla");
        RequestContext.getCurrentInstance().execute("PF('elegirTabla').show()");
    }

    public void confirmarDuplicarNF() {

        listaVigenciasNoFormales.add(duplicarVigenciaNoFormal);
        listaVigenciasNoFormalesCrear.add(duplicarVigenciaNoFormal);
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistrosNF();
        RequestContext.getCurrentInstance().update("form:infoRegistroNF");
        RequestContext.getCurrentInstance().update("form:datosVigenciasNoFormalesPersona");
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
            altoTabla2 = "105";
            RequestContext.getCurrentInstance().update("form:datosVigenciasNoFormalesPersona");
            bandera = 0;
            filtradosListaVigenciasNoFormales = null;
            tipoListaNF = 0;

        }
        duplicarVigenciaNoFormal = new VigenciasNoFormales();
    }

    public void dialogoVigenciasFormales() {
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciaFormal");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciaFormal').show()");
    }

    public void dialogoVigenciasNoFormales() {
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciaNoFormal");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciaNoFormal').show()");

    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        deshabilitarBotonLov();
        contarRegistrosF();
    }

    public void contarRegistrosF() {
        RequestContext.getCurrentInstance().update("form:infoRegistroF");
    }

    public void contarRegistroEducacion() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEducacion");
    }

    public void contarRegistroCursos() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCursos");
    }

    public void contarRegistroProfesion() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistrosProfesion");
    }

    public void contarRegistroInstituciones() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroInstituciones");
    }

    public void contarRegistroInstitucionesF() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroInstitucionesF");
    }

    public void contarRegistroAdiestramientoF() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroAdiestramientosF");
    }

    public void contarRegistroAdiestramientoNF() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroAdiestramientosNF");
    }

    //EVENTO FILTRARNF
    public void eventoFiltrarNF() {
        if (tipoListaNF == 0) {
            tipoListaNF = 1;
        }
        deshabilitarBotonLov();
        contarRegistrosNF();
    }

    public void contarRegistrosNF() {
        RequestContext.getCurrentInstance().update("form:infoRegistroNF");
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

    public void cargarListaTiposEducaciones() {
        if (listaTiposEducaciones == null) {
            listaTiposEducaciones = administrarVigenciasFormales.lovTiposEducaciones();
        }
    }

    public void cargarListaProfesiones() {
        if (listaProfesiones == null) {
            listaProfesiones = administrarVigenciasFormales.lovProfesiones();
        }
    }

    public void cargarListaInstituciones() {
        if (listaInstituciones == null) {
            listaInstituciones = administrarVigenciasFormales.lovInstituciones();
        }
    }

    public void cargarListaCursos() {
        if (listaCursos == null) {
            listaCursos = administrarVigenciasNoFormales.lovCursos();
        }
    }

    public void cargarListaAdiestramientosF() {
        if (listaAdiestramientosFormales == null) {
            listaAdiestramientosFormales = administrarVigenciasFormales.lovAdiestramientosF();
        }
    }

    public void cargarListaAdiestramientosNF() {
        if (listaAdiestramientosNoFormales == null) {
            listaAdiestramientosNoFormales = administrarVigenciasNoFormales.lovAdiestramientosNF();
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

    public List<VigenciasFormales> getListaVigenciasFormales() {
        if (listaVigenciasFormales == null) {
            if (persona != null) {
                listaVigenciasFormales = administrarVigenciasFormales.vigenciasFormalesPersona(persona.getSecuencia());
            }
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
            listaVigenciasNoFormales = administrarVigenciasNoFormales.vigenciasNoFormalesPersona(persona.getSecuencia());
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

    public String getInfoRegistroF() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciasFormalesPersona");
        infoRegistroF = String.valueOf(tabla.getRowCount());
        return infoRegistroF;
    }

    public void setInfoRegistroF(String infoRegistroF) {
        this.infoRegistroF = infoRegistroF;
    }

    public String getInfoRegistroNF() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciasNoFormalesPersona");
        infoRegistroNF = String.valueOf(tabla.getRowCount());
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
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVTiposEducaciones");
        infoRegistroEducacion = String.valueOf(tabla.getRowCount());
        return infoRegistroEducacion;
    }

    public void setInfoRegistroEducacion(String infoRegistroEducacion) {
        this.infoRegistroEducacion = infoRegistroEducacion;
    }

    public String getInfoRegistroCursos() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVCursos");
        infoRegistroCursos = String.valueOf(tabla.getRowCount());
        return infoRegistroCursos;
    }

    public void setInfoRegistroCursos(String infoRegistroCursos) {
        this.infoRegistroCursos = infoRegistroCursos;
    }

    public String getInfoRegistrosProfesion() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVProfesiones");
        infoRegistrosProfesion = String.valueOf(tabla.getRowCount());
        return infoRegistrosProfesion;
    }

    public void setInfoRegistrosProfesion(String infoRegistrosProfesion) {
        this.infoRegistrosProfesion = infoRegistrosProfesion;
    }

    public String getInfoRegistroInstituciones() {

        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVInstitucionesNF");
        infoRegistroInstituciones = String.valueOf(tabla.getRowCount());
        return infoRegistroInstituciones;
    }

    public void setInfoRegistroInstituciones(String infoRegistroInstituciones) {
        this.infoRegistroInstituciones = infoRegistroInstituciones;
    }

    public String getInfoRegistroAdiestramientosF() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVAdiestramientosF");
        infoRegistroAdiestramientosF = String.valueOf(tabla.getRowCount());
        return infoRegistroAdiestramientosF;
    }

    public void setInfoRegistroAdiestramientosF(String infoRegistroAdiestramientosF) {
        this.infoRegistroAdiestramientosF = infoRegistroAdiestramientosF;
    }

    public String getInfoRegistroAdiestramientosNF() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVAdiestramientosNF");
        infoRegistroAdiestramientosNF = String.valueOf(tabla.getRowCount());
        return infoRegistroAdiestramientosNF;
    }

    public void setInfoRegistroAdiestramientosNF(String infoRegistroAdiestramientosNF) {
        this.infoRegistroAdiestramientosNF = infoRegistroAdiestramientosNF;
    }

    public String getInfoRegistroInstitucionesF() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVInstituciones");
        infoRegistroInstitucionesF = String.valueOf(tabla.getRowCount());
        return infoRegistroInstitucionesF;
    }

    public void setInfoRegistroInstitucionesF(String infoRegistroInstitucionesF) {
        this.infoRegistroInstitucionesF = infoRegistroInstitucionesF;
    }

}
