package Controlador;

import Entidades.Aficiones;
import Entidades.Ciudades;
import Entidades.Deportes;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.EstadosCiviles;
import Entidades.Estructuras;
import Entidades.Idiomas;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import Entidades.Personas;
import Entidades.TiposTelefonos;
import Entidades.TiposTrabajadores;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarInforeportesInterface;
import InterfaceAdministrar.AdministrarNReportePersonalInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class ControlNReportePersonal implements Serializable {

    @EJB
    AdministrarNReportePersonalInterface administrarNReportePersonal;
    @EJB
    AdministarReportesInterface administarReportes;
    @EJB
    AdministrarInforeportesInterface administrarInforeportes;
    //PARAMETROS REPORTES
    private ParametrosReportes parametroDeReporte;
    private ParametrosReportes parametroModificacion;
    //INFOREPORTES
    private List<Inforeportes> listaIR;
    private Inforeportes reporteSeleccionado;
    private List<Inforeportes> filtrarListInforeportesUsuario;
    private List<Inforeportes> listaInfoReportesModificados;
    //INFOREPORTES LOV
    private List<Inforeportes> lovInforeportes;
    private Inforeportes reporteSeleccionadoLov;
    private List<Inforeportes> filtrarLovInforeportes;
    //EMPLEADOS
    private List<Empleados> listEmpleados;
    private Empleados empleadoSeleccionado;
    private List<Empleados> filtrarListEmpleados;
    //EMPRESAS
    private List<Empresas> listEmpresas;
    private Empresas empresaSeleccionada;
    private List<Empresas> filtrarListEmpresas;
    //ESTRUCTURAS
    private List<Estructuras> listEstructuras;
    private Estructuras estructuraSeleccionada;
    private List<Estructuras> filtrarListEstructuras;
    //TIPOS TRABAJADORES
    private List<TiposTrabajadores> listTiposTrabajadores;
    private TiposTrabajadores tipoTSeleccionado;
    private List<TiposTrabajadores> filtrarListTiposTrabajadores;
    //ESTADOS CIVILES
    private List<EstadosCiviles> listEstadosCiviles;
    private EstadosCiviles estadoCivilSeleccionado;
    private List<EstadosCiviles> filtrarListEstadosCiviles;
    //TIPOS TELEFONOS
    private List<TiposTelefonos> listTiposTelefonos;
    private TiposTelefonos tipoTelefonoSeleccionado;
    private List<TiposTelefonos> filtrarListTiposTelefonos;
    //CIUDADES
    private List<Ciudades> listCiudades;
    private Ciudades ciudadSeleccionada;
    private List<Ciudades> filtrarListCiudades;
    //DEPORTES
    private List<Deportes> listDeportes;
    private Deportes deporteSeleccionado;
    private List<Deportes> filtrarListDeportes;
    //AFICIONES
    private List<Aficiones> listAficiones;
    private Aficiones aficionSeleccionada;
    private List<Aficiones> filtrarListAficiones;
    //
    private List<Idiomas> listIdiomas;
    private Idiomas idiomaSeleccionado;
    private List<Idiomas> filtrarListIdiomas;

    private ParametrosReportes nuevoParametroInforme;
    //
    private int casilla;
    private int posicionReporte;
    private String requisitosReporte;
    //INPUTS
    private InputText empleadoDesdeParametro, empleadoHastaParametro, estadoCivilParametro, tipoTelefonoParametro, estructuraParametro;
    private InputText ciudadParametro, deporteParametro, aficionParametro, idiomaParametro, tipoTrabajadorParametro, solicitudParametro;
    private InputText jefeDivisionParametro, rodamientoParametro, empresaParametro;

    private String reporteGenerar;
    private int bandera;
    private boolean aceptar;
    private Column codigoIR, reporteIR, tipoIR;
    //INFOREGISTRO
    private String infoRegistroEmpleadoDesde, infoRegistroEmpleadoHasta, infoRegistroEmpresa, infoRegistroEstructura;
    private String infoRegistroReportes, infoRegistroTipoTrabajador, infoRegistroEstadoCivil, infoRegistroTipoTelefono;
    private String infoRegistro, infoRegistroCiudad, infoRegistroDeporte, infoRegistroAficion, infoRegistroIdioma, infoRegistroJefe;
    //
    private String solicitud, estadocivil, tipotelefono, jefediv, estructura, empresa, tipoTrabajador, ciudad, deporte, aficiones, idioma, rodamiento;
    private int tipoLista;
    private boolean permitirIndex, cambiosReporte;
    //ALTO SCROLL TABLA
    private String altoTabla;
    private int indice;
    //EXPORTAR REPORTE
    private StreamedContent file;
    //
    private String color, decoracion;
    private String color2, decoracion2;
    private int casillaInforReporte;
    private Date fechaDesde, fechaHasta;
    private BigDecimal emplDesde, emplHasta;
    private boolean activoMostrarTodos, activoBuscarReporte, activarEnvio;
    //EXPORTAR REPORTE
    private String pathReporteGenerado = null;
    private String nombreReporte, tipoReporte;
    //BANDERAS
    private boolean estadoReporte;
    private String resultadoReporte;
    //VISUALIZAR REPORTE PDF
    private StreamedContent reporte;
    private String cabezeraVisor;
    //
    private DataTable tabla;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<>();

    public ControlNReportePersonal() {
        System.out.println("Controlador.ControlNReportePersonal.<init>()");
        activoMostrarTodos = true;
        activoBuscarReporte = false;
        activarEnvio = true;
        color = "black";
        decoracion = "none";
        color2 = "black";
        decoracion2 = "none";
        casillaInforReporte = -1;
        reporteSeleccionado = null;
        cambiosReporte = true;
        listaInfoReportesModificados = new ArrayList<>();
        parametroDeReporte = null;
        bandera = 0;
        aceptar = true;
        casilla = -1;
        parametroModificacion = new ParametrosReportes();
        reporteGenerar = "";
        requisitosReporte = "";
        altoTabla = "140";
        listaIR = null;
        tipoLista = 0;
        posicionReporte = -1;
        //
        listEmpleados = null;
        listEmpresas = null;
        listEstructuras = null;
        listTiposTrabajadores = null;
        listAficiones = null;
        listCiudades = null;
        listDeportes = null;
        listEstadosCiviles = null;
        listIdiomas = null;
        listTiposTelefonos = null;
        //
        empleadoSeleccionado = new Empleados();
        empresaSeleccionada = new Empresas();
        estructuraSeleccionada = new Estructuras();
        tipoTSeleccionado = new TiposTrabajadores();
        aficionSeleccionada = new Aficiones();
        ciudadSeleccionada = new Ciudades();
        deporteSeleccionado = new Deportes();
        estadoCivilSeleccionado = new EstadosCiviles();
        idiomaSeleccionado = new Idiomas();
        tipoTelefonoSeleccionado = new TiposTelefonos();
        reporteSeleccionadoLov = null;
        //
        permitirIndex = true;
        cabezeraVisor = null;
        mapParametros.put("paginaAnterior", paginaAnterior);
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
        } else {
            String pagActual = "nreportepersonal";
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
            controlListaNavegacion.adicionarPagina(pagActual);
        }
        fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
    }

    @PostConstruct
    public void inicializarAdministrador() {
        System.out.println(this.getClass().getName() + ".iniciarAdministrador()");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarNReportePersonal.obtenerConexion(ses.getId());
            administarReportes.obtenerConexion(ses.getId());
            administrarInforeportes.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getMessage());
        }
    }

    public void iniciarPagina() {
        System.out.println(this.getClass().getName() + ".iniciarPagina()");
        activoMostrarTodos = true;
        activoBuscarReporte = false;
        activarEnvio = true;
        getListaIR();
    }
//

    public void guardarCambios() {
        System.out.println(this.getClass().getName() + ".guardarCambios()");
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("cambiosReporte " + cambiosReporte);
        try {
            if (!cambiosReporte) {
                System.out.println("Entre a if (cambiosReporte == false)");
                if (parametroDeReporte.getUsuario() != null) {

                    if (parametroDeReporte.getCodigoempleadodesde() == null) {
                        parametroDeReporte.setCodigoempleadodesde(null);
                    }
                    if (parametroDeReporte.getCodigoempleadohasta() == null) {
                        parametroDeReporte.setCodigoempleadohasta(null);
                    }
                    if (parametroDeReporte.getEstadocivil().getSecuencia() == null) {
                        parametroDeReporte.setEstadocivil(null);
                    }
                    if (parametroDeReporte.getTipotelefono().getSecuencia() == null) {
                        parametroDeReporte.setTipotelefono(null);
                    }
                    if (parametroDeReporte.getLocalizacion().getSecuencia() == null) {
                        parametroDeReporte.setLocalizacion(null);
                    }
                    if (parametroDeReporte.getTipotrabajador().getSecuencia() == null) {
                        parametroDeReporte.setTipotrabajador(null);
                    }
                    if (parametroDeReporte.getCiudad().getSecuencia() == null) {
                        parametroDeReporte.setCiudad(null);
                    }
                    if (parametroDeReporte.getDeporte().getSecuencia() == null) {
                        parametroDeReporte.setDeporte(null);
                    }
                    if (parametroDeReporte.getAficion().getSecuencia() == null) {
                        parametroDeReporte.setAficion(null);
                    }
                    if (parametroDeReporte.getIdioma().getSecuencia() == null) {
                        parametroDeReporte.setIdioma(null);
                    }
                    if (parametroDeReporte.getEmpresa().getSecuencia() == null) {
                        parametroDeReporte.setEmpresa(null);
                    }
                    if (parametroDeReporte.getNombregerente().getSecuencia() == null) {
                        parametroDeReporte.setNombregerente(null);
                    }
                    administrarNReportePersonal.modificarParametrosReportes(parametroDeReporte);
                }
                if (!listaInfoReportesModificados.isEmpty()) {
                    administrarNReportePersonal.guardarCambiosInfoReportes(listaInfoReportesModificados);
                }
                cambiosReporte = true;
                FacesMessage msg = new FacesMessage("Información", "Los datos se guardarón con Éxito.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                System.out.println("Ingrese al else");
                if (parametroDeReporte.getFechadesde().after(parametroDeReporte.getFechahasta())) {
                    fechaDesde = parametroDeReporte.getFechadesde();
                    fechaHasta = parametroDeReporte.getFechahasta();
                    System.out.println("parametroDeReporte.getFechadesde: " + parametroDeReporte.getFechadesde());
                    System.out.println("parametroDeReporte.getFechahasta: " + parametroDeReporte.getFechahasta());
                    parametroDeReporte.setFechadesde(fechaDesde);
                    parametroDeReporte.setFechahasta(fechaHasta);
                    RequestContext.getCurrentInstance().update("formParametros");
                    RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
                }
            }

        } catch (Exception e) {
            System.out.println("Error en guardar Cambios Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (casilla >= 1) {
            if (casilla == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaDesde");
                RequestContext.getCurrentInstance().execute("PF('editarFechaDesde').show()");
            }
            if (casilla == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:empleadoDesde");
                RequestContext.getCurrentInstance().execute("PF('empleadoDesde').show()");
            }
            if (casilla == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:solicitud");
                RequestContext.getCurrentInstance().execute("PF('solicitud').show()");
            }
            if (casilla == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:estadoCivil");
                RequestContext.getCurrentInstance().execute("PF('estadoCivil').show()");
            }
            if (casilla == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:tipoTelefono");
                RequestContext.getCurrentInstance().execute("PF('tipoTelefono').show()");
            }
            if (casilla == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:jefeDivision");
                RequestContext.getCurrentInstance().execute("PF('jefeDivision').show()");
            }
            if (casilla == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:rodamiento");
                RequestContext.getCurrentInstance().execute("PF('rodamiento').show()");
            }
            if (casilla == 8) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaHasta");
                RequestContext.getCurrentInstance().execute("PF('editarFechaHasta').show()");
            }
            if (casilla == 9) {
                RequestContext.getCurrentInstance().update("formularioDialogos:empleadoHasta");
                RequestContext.getCurrentInstance().execute("PF('empleadoHasta').show()");
            }
            if (casilla == 10) {
                RequestContext.getCurrentInstance().update("formularioDialogos:estructura");
                RequestContext.getCurrentInstance().execute("PF('estructura').show()");
            }
            if (casilla == 11) {
                RequestContext.getCurrentInstance().update("formularioDialogos:tipoTrabajador");
                RequestContext.getCurrentInstance().execute("PF('tipoTrabajador').show()");
            }
            if (casilla == 12) {
                RequestContext.getCurrentInstance().update("formularioDialogos:ciudad");
                RequestContext.getCurrentInstance().execute("PF('ciudad').show()");
            }
            if (casilla == 13) {
                RequestContext.getCurrentInstance().update("formularioDialogos:deporte");
                RequestContext.getCurrentInstance().execute("PF('deporte').show()");
            }
            if (casilla == 14) {
                RequestContext.getCurrentInstance().update("formularioDialogos:aficion");
                RequestContext.getCurrentInstance().execute("PF('aficion').show()");
            }
            if (casilla == 15) {
                RequestContext.getCurrentInstance().update("formularioDialogos:idioma");
                RequestContext.getCurrentInstance().execute("PF('idioma').show()");
            }
            if (casilla == 16) {
                RequestContext.getCurrentInstance().update("formularioDialogos:empresa");
                RequestContext.getCurrentInstance().execute("PF('empresa').show()");
            }
            casilla = -1;
        }
        if (casillaInforReporte >= 1) {
            if (casillaInforReporte == 1) {
                RequestContext.getCurrentInstance().update("formParametros:infoReporteCodigoD");
                RequestContext.getCurrentInstance().execute("PF('infoReporteCodigoD').show()");
            }
            if (casillaInforReporte == 2) {
                RequestContext.getCurrentInstance().update("formParametros:infoReporteNombreD");
                RequestContext.getCurrentInstance().execute("PF('infoReporteNombreD').show()");
            }
            casillaInforReporte = -1;
        }
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (casilla == 2) {
            if ((listEmpleados == null) || listEmpleados.isEmpty()) {
                listEmpleados = null;
            }
            RequestContext.getCurrentInstance().update("form:EmpleadoDesdeDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
            contarRegistrosEmpleadoD();
        }
        if (casilla == 4) {
            RequestContext.getCurrentInstance().update("form:EstadoCivilDialogo");
            RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').show()");
            contarRegistrosEstadoCivil();
        }
        if (casilla == 5) {
            RequestContext.getCurrentInstance().update("form:TipoTelefonoDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTelefonoDialogo').show()");
            contarRegistrosTipoTelefono();
        }
        if (casilla == 6) {
            RequestContext.getCurrentInstance().update("form:JefeDivisionDialogo");
            RequestContext.getCurrentInstance().execute("PF('JefeDivisionDialogo').show()");
            contarRegistrosJefe();
        }
        if (casilla == 9) {
            RequestContext.getCurrentInstance().update("form:EmpleadoHastaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
            contarRegistrosEmpleadoH();
        }
        if (casilla == 10) {
            RequestContext.getCurrentInstance().update("form:EstructuraDialogo");
            RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').show()");
            contarRegistrosEstructura();
        }
        if (casilla == 11) {
            RequestContext.getCurrentInstance().update("form:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
            contarRegistrosTipoTrabajador();
        }
        if (casilla == 12) {
            RequestContext.getCurrentInstance().update("form:CiudadDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
            contarRegistrosCiudad();
        }
        if (casilla == 13) {
            RequestContext.getCurrentInstance().update("form:DeportesDialogo");
            RequestContext.getCurrentInstance().execute("PF('DeportesDialogo').show()");
            contarRegistrosDeporte();
        }
        if (casilla == 14) {
            RequestContext.getCurrentInstance().update("form:AficionesDialogo");
            RequestContext.getCurrentInstance().execute("PF('AficionesDialogo').show()");
            contarRegistrosAficion();
        }
        if (casilla == 15) {
            RequestContext.getCurrentInstance().update("form:IdiomasDialogo");
            RequestContext.getCurrentInstance().execute("PF('IdiomasDialogo').show()");
            contarRegistrosIdioma();
        }
        if (casilla == 16) {
            RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
            contarRegistrosEmpresa();
        }
    }

    public void activarCtrlF11() {
        System.out.println(this.getClass().getName() + ".activarCtrlF11()");
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "120";
            codigoIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:codigoIR");
            codigoIR.setFilterStyle("width: 85% !important");
            reporteIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:reporteIR");
            reporteIR.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:reportesPersonal");
            bandera = 1;
        } else if (bandera == 1) {
            cerrarFiltrado();
            defaultPropiedadesParametrosReporte();
        }
    }

    public void cerrarFiltrado() {
        FacesContext c = FacesContext.getCurrentInstance();
        System.out.println("Desactivar");
        altoTabla = "140";
        codigoIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:codigoIR");
        codigoIR.setFilterStyle("display: none; visibility: hidden;");
        reporteIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:reporteIR");
        reporteIR.setFilterStyle("display: none; visibility: hidden;");
        RequestContext.getCurrentInstance().update("form:reportesPersonal");
        bandera = 0;
        tipoLista = 0;
        filtrarListInforeportesUsuario = null;
    }

    public void salir() {
        System.out.println("Controlador.ControlNReportePersonal.salir()");
        if (bandera == 1) {
            cerrarFiltrado();
        }
        listaIR = null;
        parametroDeReporte = null;
        parametroModificacion = null;
        listEmpleados = null;
        listEmpresas = null;
        listEstructuras = null;
        listTiposTrabajadores = null;
        listAficiones = null;
        listCiudades = null;
        listDeportes = null;
        listTiposTelefonos = null;
        listIdiomas = null;
        listEstadosCiviles = null;
        casilla = -1;
        listaInfoReportesModificados.clear();
        cambiosReporte = true;
        reporteSeleccionado = null;
        reporteSeleccionadoLov = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void cancelarModificaciones() {
        if (bandera == 1) {
            cerrarFiltrado();
        }
        defaultPropiedadesParametrosReporte();
        listaIR = null;
        parametroDeReporte = null;
        parametroModificacion = null;
        casilla = -1;
        listaInfoReportesModificados.clear();
        cambiosReporte = true;
        getParametroDeReporte();
        getListaIR();
        activoMostrarTodos = true;
        activoBuscarReporte = false;
        reporteSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
        RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:ENVIOCORREO");
        RequestContext.getCurrentInstance().update("form:reportesPersonal");
        RequestContext.getCurrentInstance().update("formParametros:fechaDesdeParametro");
        RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
        RequestContext.getCurrentInstance().update("formParametros:solicitudParametro");
        RequestContext.getCurrentInstance().update("formParametros:estadoCivilParametro");
        RequestContext.getCurrentInstance().update("formParametros:tipoTelefonoParametro");
        RequestContext.getCurrentInstance().update("formParametros:jefeDivisionParametro");
        RequestContext.getCurrentInstance().update("formParametros:rodamientoParametro");
        RequestContext.getCurrentInstance().update("formParametros:fondoCumpleParametro");
        RequestContext.getCurrentInstance().update("formParametros:fechaHastaParametro");
        RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
        RequestContext.getCurrentInstance().update("formParametros:estructuraParametro");
        RequestContext.getCurrentInstance().update("formParametros:tipoTrabajadorParametro");
        RequestContext.getCurrentInstance().update("formParametros:ciudadParametro");
        RequestContext.getCurrentInstance().update("formParametros:deporteParametro");
        RequestContext.getCurrentInstance().update("formParametros:aficionParametro");
        RequestContext.getCurrentInstance().update("formParametros:idiomaParametro");
        RequestContext.getCurrentInstance().update("formParametros:personalParametro");
        RequestContext.getCurrentInstance().update("formParametros:empresaParametro");
    }

    public void seleccionRegistro() {
        System.out.println(this.getClass().getName() + ".seleccionRegistro()");
        activarEnvioCorreo();
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("inforreporteSeleccionado: " + reporteSeleccionado);
        // Resalto Parametros Para Reporte
        defaultPropiedadesParametrosReporte();
        if (reporteSeleccionado.getFecdesde().equals("SI")) {
            color = "red";
            RequestContext.getCurrentInstance().update("formParametros");
        }
        System.out.println("reporteSeleccionado.getFechasta(): " + reporteSeleccionado.getFechasta());
        if (reporteSeleccionado.getFechasta().equals("SI")) {
            color2 = "red";
            RequestContext.getCurrentInstance().update("formParametros");
        }
        System.out.println("reporteSeleccionado.getEmdesde(): " + reporteSeleccionado.getEmdesde());
        if (reporteSeleccionado.getEmdesde().equals("SI")) {
            empleadoDesdeParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoDesdeParametro");
            //empleadoDesdeParametro.setStyle("position: absolute; top: 41px; left: 150px; height: 10px; font-size: 11px; width: 90px; color: red;");
            if (!empleadoDesdeParametro.getStyle().contains(" color: red;")) {
                empleadoDesdeParametro.setStyle(empleadoDesdeParametro.getStyle() + " color: red;");
            }
        } else {
            try {
                if (empleadoDesdeParametro.getStyle().contains(" color: red;")) {

                    System.out.println("reeemplazarr " + empleadoDesdeParametro.getStyle().replace(" color: red;", ""));
                    empleadoDesdeParametro.setStyle(empleadoDesdeParametro.getStyle().replace(" color: red;", ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
        System.out.println("inforreporteSeleccionado.getEmhasta(): " + reporteSeleccionado.getEmhasta());
        if (reporteSeleccionado.getEmhasta().equals("SI")) {
            empleadoHastaParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametro");
            empleadoHastaParametro.setStyle(empleadoHastaParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
        }

        System.out.println("inforreporteSeleccionado.getSolicitudParametro(): " + reporteSeleccionado.getSolicitud());
        if (reporteSeleccionado.getLocalizacion().equals("SI")) {
            solicitudParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:solicitudParametro");
            solicitudParametro.setStyle(solicitudParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:solicitudParametro");
        }

        System.out.println("inforreporteSeleccionado.getLocalizacion(): " + reporteSeleccionado.getLocalizacion());
        if (reporteSeleccionado.getLocalizacion().equals("SI")) {
            estructuraParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:estructuraParametro");
            estructuraParametro.setStyle(estructuraParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:estructuraParametro");
        }

        System.out.println("inforreporteSeleccionado.getTipotrabajador(): " + reporteSeleccionado.getTipotrabajador());
        if (reporteSeleccionado.getTipotrabajador().equals("SI")) {
            tipoTrabajadorParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:tipoTrabajadorParametro");
            tipoTrabajadorParametro.setStyle(tipoTrabajadorParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:tipoTrabajadorParametro");
        }

        System.out.println("inforreporteSeleccionado.getEstadocivil(): " + reporteSeleccionado.getEstadocivil());
        if (reporteSeleccionado.getEstadocivil().equals("SI")) {
            estadoCivilParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:estadoCivilParametro");
            estadoCivilParametro.setStyle(estadoCivilParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:estadoCivilParametro");
        }

        System.out.println("inforreporteSeleccionado.getTipotelefono(): " + reporteSeleccionado.getTipotelefono());
        if (reporteSeleccionado.getTipotelefono().equals("SI")) {
            tipoTelefonoParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:tipoTelefonoParametro");
            tipoTelefonoParametro.setStyle(tipoTrabajadorParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:tipoTelefonoParametro");
        }

        System.out.println("inforreporteSeleccionado.getJefedivision(): " + reporteSeleccionado.getJefedivision());
        if (reporteSeleccionado.getJefedivision().equals("SI")) {
            jefeDivisionParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:jefeDivisionParametro");
            jefeDivisionParametro.setStyle(jefeDivisionParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:jefeDivisionParametro");
        }

        System.out.println("inforreporteSeleccionado.getCiudad(): " + reporteSeleccionado.getCiudad());
        if (reporteSeleccionado.getCiudad().equals("SI")) {
            ciudadParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:ciudadParametro");
            ciudadParametro.setStyle(ciudadParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:ciudadParametro");
        }

        System.out.println("inforreporteSeleccionado.getDeporte(): " + reporteSeleccionado.getDeporte());
        if (reporteSeleccionado.getDeporte().equals("SI")) {
            deporteParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:deporteParametro");
            deporteParametro.setStyle(deporteParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:deporteParametro");
        }

        System.out.println("inforreporteSeleccionado.getAficion(): " + reporteSeleccionado.getAficion());
        if (reporteSeleccionado.getAficion().equals("SI")) {
            aficionParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:aficionParametro");
            aficionParametro.setStyle(aficionParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:aficionParametro");
        }

        System.out.println("inforreporteSeleccionado.getIdioma(): " + reporteSeleccionado.getIdioma());
        if (reporteSeleccionado.getIdioma().equals("SI")) {
            idiomaParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:idiomaParametro");
            idiomaParametro.setStyle(idiomaParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:idiomaParametro");
        }
        RequestContext.getCurrentInstance().update("formParametros");
        System.out.println("reporte seleccionado : " + reporteSeleccionado.getSecuencia());
    }

    public void requisitosParaReporte() {
        System.out.println(this.getClass().getName() + ".requisitosParaReporte()");
        RequestContext context = RequestContext.getCurrentInstance();
        requisitosReporte = "";
        if (reporteSeleccionado.getFecdesde().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Fecha Desde -";
        }
        if (reporteSeleccionado.getFechasta().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Fecha Hasta -";
        }
        if (reporteSeleccionado.getEmdesde().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Empleado Desde -";
        }
        if (reporteSeleccionado.getEmhasta().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Empleado Hasta -";
        }
        if (reporteSeleccionado.getSolicitud().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Observaciones -";
        }
        if (reporteSeleccionado.getTipotelefono().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Tipo Telefono -";
        }
        if (reporteSeleccionado.getJefedivision().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Jefe División RH-";
        }
        if (reporteSeleccionado.getRodamiento().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Rodamiento -";
        }
        if (reporteSeleccionado.getLocalizacion().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Estructura -";
        }
        if (reporteSeleccionado.getTrabajador().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Tipo Trabajador -";
        }
        if (reporteSeleccionado.getCiudad().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Ciudad -";
        }
        if (reporteSeleccionado.getDeporte().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Deporte -";
        }
        if (reporteSeleccionado.getAficion().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Afición -";
        }
        if (reporteSeleccionado.getIdioma().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Idioma -";
        }

        if (!requisitosReporte.isEmpty()) {
            RequestContext.getCurrentInstance().update("formDialogos:requisitosReporte");
            RequestContext.getCurrentInstance().execute("PF('requisitosReporte').show()");
        }
    }

    public void modificacionTipoReporte(Inforeportes reporte) {
        System.out.println(this.getClass().getName() + ".modificacionTipoReporte()");
        reporteSeleccionado = reporte;
        System.out.println("reporteSeleccionado: " + reporteSeleccionado);
        System.out.println("Tipo reporteSeleccionado: " + reporteSeleccionado.getTipo());
        cambiosReporte = false;
        if (listaInfoReportesModificados.isEmpty()) {
            listaInfoReportesModificados.add(reporteSeleccionado);
        } else if ((!listaInfoReportesModificados.isEmpty()) && (!listaInfoReportesModificados.contains(reporteSeleccionado))) {
            listaInfoReportesModificados.add(reporteSeleccionado);
        } else {
            int posicion = listaInfoReportesModificados.indexOf(reporteSeleccionado);
            listaInfoReportesModificados.set(posicion, reporteSeleccionado);
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void dispararDialogoGuardarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
    }

    public void guardarYSalir() {
        guardarCambios();
        salir();
    }

    public void modificarParametroInforme() {
        if (parametroDeReporte.getFechadesde() != null && parametroDeReporte.getFechahasta() != null) {
            if (parametroDeReporte.getFechadesde().before(parametroDeReporte.getFechahasta())) {
                System.out.println("Entre al segundo if");
                parametroModificacion = parametroDeReporte;
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                System.out.println("entre a else del segundo if");
                cambiosReporte = true;
            }
        }
    }

    public void posicionCelda(int i) {
        casilla = i;
        if (permitirIndex == true) {
//            casillaInforReporte = -1;
            switch (casilla) {
                case 1:
                    fechaDesde = parametroDeReporte.getFechadesde();
                    break;
                case 2:
                    emplDesde = parametroDeReporte.getCodigoempleadodesde();
                    break;
                case 3:
                    solicitud = parametroDeReporte.getObservaciones();
                    break;
                case 4:
                    estadocivil = parametroDeReporte.getEstadocivil().getDescripcion();
                    break;
                case 5:
                    tipotelefono = parametroDeReporte.getTipotelefono().getNombre();
                    break;
                case 6:
                    jefediv = parametroDeReporte.getNombregerente().getPersona().getNombreCompleto();
                    break;
                case 7:
                    rodamiento = parametroDeReporte.getRodamineto();
                    break;
                case 8:
                    fechaHasta = parametroDeReporte.getFechahasta();
                    break;
                case 9:
                    emplHasta = parametroDeReporte.getCodigoempleadohasta();
                    break;
                case 10:
                    estructura = parametroDeReporte.getLocalizacion().getNombre();
                    break;
                case 11:
                    tipoTrabajador = parametroDeReporte.getTipotrabajador().getNombre();
                    break;
                case 12:
                    ciudad = parametroDeReporte.getCiudad().getNombre();
                    break;
                case 13:
                    deporte = parametroDeReporte.getDeporte().getNombre();
                    break;
                case 14:
                    aficiones = parametroDeReporte.getAficion().getDescripcion();
                    break;
                case 15:
                    idioma = parametroDeReporte.getIdioma().getNombre();
                    break;
                case 16:
                    empresa = parametroDeReporte.getEmpresa().getNombre();
                    break;
                default:
                    break;
            }
        }
    }

    public void autocompletarGeneral(String campoConfirmar, String valorConfirmar) {
        RequestContext context = RequestContext.getCurrentInstance();
        int indiceUnicoElemento = -1;
        int coincidencias = 0;
        if (campoConfirmar.equalsIgnoreCase("ESTADOCIVIL")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getEstadocivil().setDescripcion(estadocivil);
                for (int i = 0; i < listEstadosCiviles.size(); i++) {
                    if (listEstadosCiviles.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setEstadocivil(listEstadosCiviles.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listEstadosCiviles.clear();
                    getListEstadosCiviles();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:EstadoCivilDialogo");
                    RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').show()");
                }
            } else {
                parametroDeReporte.setEstadocivil(new EstadosCiviles());
                parametroModificacion = parametroDeReporte;
                listEstadosCiviles.clear();
                getListEstadosCiviles();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("TIPOTELEFONO")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getTipotelefono().setNombre(tipotelefono);
                for (int i = 0; i < listTiposTelefonos.size(); i++) {
                    if (listTiposTelefonos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setTipotelefono(listTiposTelefonos.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listTiposTelefonos.clear();
                    getListTiposTelefonos();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:TipoTelefonoDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TipoTelefonoDialogo').show()");
                }
            } else {
                parametroDeReporte.setTipotelefono(new TiposTelefonos());
                parametroModificacion = parametroDeReporte;
                listTiposTelefonos.clear();
                getListTiposTelefonos();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("JEFEDIV")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getNombregerente().getPersona().setNombreCompleto(jefediv);
                for (int i = 0; i < listEmpleados.size(); i++) {
                    if (listEmpleados.get(i).getPersona().getNombreCompleto().startsWith(valorConfirmar)) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setNombregerente(listEmpleados.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listEmpleados.clear();
                    getListEmpleados();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:JefeDivisionDialogo");
                    RequestContext.getCurrentInstance().execute("PF('JefeDivisionDialogo').show()");
                }
            } else {
                parametroDeReporte.setNombregerente(new Empleados());
                parametroModificacion = parametroDeReporte;
                listEmpleados.clear();
                getListEmpleados();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("ESTRUCTURA")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getLocalizacion().setNombre(estructura);
                for (int i = 0; i < listEstructuras.size(); i++) {
                    if (listEstructuras.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setLocalizacion(listEstructuras.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listEstructuras.clear();
                    getListEstructuras();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:EstructuraDialogo");
                    RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').show()");
                }
            } else {
                parametroDeReporte.setLocalizacion(new Estructuras());
                parametroModificacion = parametroDeReporte;
                listEstructuras.clear();
                getListEstructuras();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                System.out.println("Change");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("TIPOTRABAJADOR")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getTipotrabajador().setNombre(tipoTrabajador);
                for (int i = 0; i < listTiposTrabajadores.size(); i++) {
                    if (listTiposTrabajadores.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setTipotrabajador(listTiposTrabajadores.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listTiposTrabajadores.clear();
                    getListTiposTrabajadores();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:TipoTrabajadorDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
                }
            } else {
                parametroDeReporte.setTipotrabajador(new TiposTrabajadores());
                parametroModificacion = parametroDeReporte;
                listTiposTrabajadores.clear();
                getListTiposTrabajadores();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("CIUDAD")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getCiudad().setNombre(ciudad);
                for (int i = 0; i < listCiudades.size(); i++) {
                    if (listCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setCiudad(listCiudades.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listCiudades.clear();
                    getListCiudades();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:CiudadDialogo");
                    RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
                }
            } else {
                parametroDeReporte.setCiudad(new Ciudades());
                parametroModificacion = parametroDeReporte;
                listCiudades.clear();
                getListCiudades();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("DEPORTE")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getDeporte().setNombre(deporte);
                for (int i = 0; i < listDeportes.size(); i++) {
                    if (listDeportes.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setDeporte(listDeportes.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listDeportes.clear();
                    getListDeportes();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:DeportesDialogo");
                    RequestContext.getCurrentInstance().execute("PF('DeportesDialogo').show()");
                }
            } else {

                parametroDeReporte.setDeporte(new Deportes());
                parametroModificacion = parametroDeReporte;
                listDeportes.clear();
                getListDeportes();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("AFICION")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getAficion().setDescripcion(aficiones);
                for (int i = 0; i < listAficiones.size(); i++) {
                    if (listAficiones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setAficion(listAficiones.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listAficiones.clear();
                    getListAficiones();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");

                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:AficionesDialogo");
                    RequestContext.getCurrentInstance().execute("PF('AficionesDialogo').show()");
                }
            } else {
                parametroDeReporte.setAficion(new Aficiones());
                parametroModificacion = parametroDeReporte;
                listAficiones.clear();
                getListAficiones();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("IDIOMA")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getIdioma().setNombre(idioma);
                for (int i = 0; i < listIdiomas.size(); i++) {
                    if (listIdiomas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setIdioma(listIdiomas.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listIdiomas.clear();
                    getListIdiomas();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:IdiomasDialogo");
                    RequestContext.getCurrentInstance().execute("PF('IdiomasDialogo').show()");
                }
            } else {
                parametroDeReporte.setIdioma(new Idiomas());
                parametroModificacion = parametroDeReporte;
                listIdiomas.clear();
                getListIdiomas();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("EMPRESA")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeReporte.getEmpresa().setNombre(empresa);
                for (int i = 0; i < listEmpresas.size(); i++) {
                    if (listEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeReporte.setEmpresa(listEmpresas.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeReporte;
                    listEmpresas.clear();
                    getListEmpresas();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
                    RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
                }
            } else {
                parametroDeReporte.setEmpresa(new Empresas());
                parametroModificacion = parametroDeReporte;
                listEmpresas.clear();
                getListEmpresas();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void modificarParametroEmpleadoDesde(BigDecimal empldesde) {
        if (empldesde.equals(BigDecimal.valueOf(0))) {
            parametroDeReporte.setCodigoempleadodesde(BigDecimal.valueOf(0));
        }
        cambiosReporte = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void modificarParametroEmpleadoHasta(BigDecimal emphasta) {
        String h = "99999999999999999999999999";
        BigDecimal b = new BigDecimal(h);
        if (emphasta.equals(b)) {
            parametroDeReporte.setCodigoempleadodesde(b);
        }
        cambiosReporte = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void cargarEmpleados() {
        if (listEmpleados == null || listEmpleados.isEmpty()) {
            listEmpleados = administrarNReportePersonal.listEmpleados();
        }
    }

    public void cargarEmpresas() {
        if (listEmpresas == null || listEmpresas.isEmpty()) {
            listEmpresas = administrarNReportePersonal.listEmpresas();
        }
    }

    public void cargarEstructuras() {
        if (listEstructuras == null || listEstructuras.isEmpty()) {
            listEstructuras = administrarNReportePersonal.listEstructuras();
        }
    }

    public void cargarTiposTrabajadores() {
        if (listTiposTrabajadores == null || listTiposTrabajadores.isEmpty()) {
            listTiposTrabajadores = administrarNReportePersonal.listTiposTrabajadores();
        }
    }

    public void cargarEstadosCiviles() {
        if (listEstadosCiviles == null || listEstadosCiviles.isEmpty()) {
            listEstadosCiviles = administrarNReportePersonal.listEstadosCiviles();
        }
    }

    public void cargarTiposTelefonos() {
        if (listTiposTelefonos == null || listTiposTelefonos.isEmpty()) {
            listTiposTelefonos = administrarNReportePersonal.listTiposTelefonos();
        }
    }

    public void cargarCiudades() {
        if (listCiudades == null || listCiudades.isEmpty()) {
            listCiudades = administrarNReportePersonal.listCiudades();
        }
    }

    public void cargarDeporte() {
        if (listDeportes == null || listDeportes.isEmpty()) {
            listDeportes = administrarNReportePersonal.listDeportes();
        }
    }

    public void cargarAficion() {
        if (listAficiones == null || listAficiones.isEmpty()) {
            listAficiones = administrarNReportePersonal.listAficiones();
        }
    }

    public void cargarIdioma() {
        if (listIdiomas == null || listIdiomas.isEmpty()) {
            listIdiomas = administrarNReportePersonal.listIdiomas();
        }
    }

    public void dialogosParametros(int pos) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (pos == 2) {
            if ((listEmpleados == null) || listEmpleados.isEmpty()) {
                listEmpleados = null;
            }
            cargarEmpleados();
            RequestContext.getCurrentInstance().update("form:EmpleadoDesdeDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
            contarRegistrosEmpleadoD();
        }
        if (pos == 4) {
            cargarEstadosCiviles();
            RequestContext.getCurrentInstance().update("form:EstadoCivilDialogo");
            RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').show()");
            contarRegistrosEstadoCivil();
        }
        if (pos == 5) {
            cargarTiposTelefonos();
            RequestContext.getCurrentInstance().update("form:TipoTelefonoDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTelefonoDialogo').show()");
            contarRegistrosTipoTelefono();
        }
        if (pos == 6) {
            cargarEmpleados();
            RequestContext.getCurrentInstance().update("form:JefeDivisionDialogo");
            RequestContext.getCurrentInstance().execute("PF('JefeDivisionDialogo').show()");
            contarRegistrosJefe();
        }
        if (pos == 9) {
            cargarEmpleados();
            RequestContext.getCurrentInstance().update("form:EmpleadoHastaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
            contarRegistrosEmpleadoH();
        }
        if (pos == 10) {
            cargarEstructuras();
            RequestContext.getCurrentInstance().update("form:EstructuraDialogo");
            RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').show()");
            contarRegistrosEstructura();
        }
        if (pos == 11) {
            cargarTiposTrabajadores();
            RequestContext.getCurrentInstance().update("form:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
            contarRegistrosTipoTrabajador();
        }
        if (pos == 12) {
            cargarCiudades();
            RequestContext.getCurrentInstance().update("form:CiudadDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
            contarRegistrosCiudad();
        }
        if (pos == 13) {
            cargarDeporte();
            RequestContext.getCurrentInstance().update("form:DeportesDialogo");
            RequestContext.getCurrentInstance().execute("PF('DeportesDialogo').show()");
            contarRegistrosDeporte();
        }
        if (pos == 14) {
            cargarAficion();
            RequestContext.getCurrentInstance().update("form:AficionesDialogo");
            RequestContext.getCurrentInstance().execute("PF('AficionesDialogo').show()");
            contarRegistrosAficion();
        }
        if (pos == 15) {
            cargarIdioma();
            RequestContext.getCurrentInstance().update("form:IdiomasDialogo");
            RequestContext.getCurrentInstance().execute("PF('IdiomasDialogo').show()");
            contarRegistrosIdioma();
        }
        if (pos == 16) {
            cargarEmpresas();
            RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
            contarRegistrosEmpresa();
        }

    }

//    public void refrescarParametros() {
//        defaultPropiedadesParametrosReporte();
//        parametroDeInforme = null;
//        parametroModificacion = null;
//        listEstructuras = null;
//        listCiudades = null;
//        listEmpleados = null;
//        listEmpresas = null;
//        listTiposTrabajadores = null;
//        listAficiones = null;
//        listDeportes = null;
//        listEstadosCiviles = null;
//        listIdiomas = null;
//        listTiposTelefonos = null;
//        parametroDeInforme = null;
//
//        parametroDeInforme = administrarNReportePersonal.parametrosDeReporte();
//
//        if (parametroDeInforme.getEstadocivil() == null) {
//            parametroDeInforme.setEstadocivil(new EstadosCiviles());
//        }
//        if (parametroDeInforme.getTipotelefono() == null) {
//            parametroDeInforme.setTipotelefono(new TiposTelefonos());
//        }
//        if (parametroDeInforme.getLocalizacion() == null) {
//            parametroDeInforme.setLocalizacion(new Estructuras());
//        }
//        if (parametroDeInforme.getTipotrabajador() == null) {
//            parametroDeInforme.setTipotrabajador(new TiposTrabajadores());
//        }
//        if (parametroDeInforme.getCiudad() == null) {
//            parametroDeInforme.setCiudad(new Ciudades());
//        }
//        if (parametroDeInforme.getDeporte() == null) {
//            parametroDeInforme.setDeporte(new Deportes());
//        }
//        if (parametroDeInforme.getAficion() == null) {
//            parametroDeInforme.setAficion(new Aficiones());
//        }
//        if (parametroDeInforme.getIdioma() == null) {
//            parametroDeInforme.setIdioma(new Idiomas());
//        }
//        if (parametroDeInforme.getEmpresa() == null) {
//            parametroDeInforme.setEmpresa(new Empresas());
//        }
//
//        RequestContext context = RequestContext.getCurrentInstance();
//        RequestContext.getCurrentInstance().update("formParametros:fechaDesdeParametro");
//        RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
//        RequestContext.getCurrentInstance().update("formParametros:solicitudParametro");
//        RequestContext.getCurrentInstance().update("formParametros:estadoCivilParametro");
//        RequestContext.getCurrentInstance().update("formParametros:tipoTelefonoParametro");
//        RequestContext.getCurrentInstance().update("formParametros:jefeDivisionParametro");
//        RequestContext.getCurrentInstance().update("formParametros:rodamientoParametro");
//        RequestContext.getCurrentInstance().update("formParametros:fondoCumpleParametro");
//        RequestContext.getCurrentInstance().update("formParametros:fechaHastaParametro");
//        RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
//        RequestContext.getCurrentInstance().update("formParametros:estructuraParametro");
//        RequestContext.getCurrentInstance().update("formParametros:tipoTrabajadorParametro");
//
//        RequestContext.getCurrentInstance().update("formParametros:ciudadParametro");
//        RequestContext.getCurrentInstance().update("formParametros:deporteParametro");
//        RequestContext.getCurrentInstance().update("formParametros:aficionParametro");
//        RequestContext.getCurrentInstance().update("formParametros:idiomaParametro");
//
//        RequestContext.getCurrentInstance().update("formParametros:personalParametro");
//        RequestContext.getCurrentInstance().update("formParametros:empresaParametro");
//
//    }
    public void activarAceptar() {
        aceptar = false;
    }

    public void activarEnvioCorreo() {
        activarEnvio = reporteSeleccionado == null;
        RequestContext.getCurrentInstance().update("form:ENVIOCORREO");
    }

    public void actualizarEmplDesde() {
        permitirIndex = true;
        parametroDeReporte.setCodigoempleadodesde(empleadoSeleccionado.getCodigoempleado());
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEmpleadoDesde:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");

        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarListEmpleados = null;
    }

    public void cancelarCambioEmplDesde() {
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarListEmpleados = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEmpleadoDesde:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");
    }

    public void actualizarEmplHasta() {
        permitirIndex = true;
        parametroDeReporte.setCodigoempleadohasta(empleadoSeleccionado.getCodigoempleado());
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEmpleadoHasta:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");

        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarListEmpleados = null;
    }

    public void cancelarCambioEmplHasta() {
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarListEmpleados = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEmpleadoHasta:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");
    }

    public void actualizarEmpresa() {
        permitirIndex = true;
        parametroDeReporte.setEmpresa(empresaSeleccionada);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        /*RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
         RequestContext.getCurrentInstance().update("form:lovEmpresa");
         RequestContext.getCurrentInstance().update("form:aceptarEmp");*/
        context.reset("form:lovEmpresa:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:empresaParametro");
        empresaSeleccionada = null;
        aceptar = true;
        filtrarListEmpresas = null;

    }

    public void cancelarEmpresa() {
        empresaSeleccionada = null;
        aceptar = true;
        filtrarListEmpresas = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEmpresa:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
    }

    public void actualizarEstructura() {
        parametroDeReporte.setLocalizacion(estructuraSeleccionada);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEstructura:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEstructura').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:estructuraParametro");
        estructuraSeleccionada = null;
        aceptar = true;
        filtrarListEstructuras = null;
        permitirIndex = true;
    }

    public void cancelarEstructura() {
        estructuraSeleccionada = null;
        aceptar = true;
        filtrarListEstructuras = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEstructura:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEstructura').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').hide()");
    }

    public void actualizarTipoTrabajador() {
        parametroDeReporte.setTipotrabajador(tipoTSeleccionado);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoTrabajador:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:tipoTrabajadorParametro");
        tipoTSeleccionado = null;
        aceptar = true;
        filtrarListTiposTrabajadores = null;
        permitirIndex = true;
    }

    public void cancelarTipoTrabajador() {
        tipoTSeleccionado = null;
        aceptar = true;
        filtrarListTiposTrabajadores = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoTrabajador:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').hide()");
    }

    public void actualizarEstadoCivil() {
        permitirIndex = true;
        parametroDeReporte.setEstadocivil(estadoCivilSeleccionado);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEstadoCivil:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEstadoCivil').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:estadoCivilParametro");
        estadoCivilSeleccionado = null;
        aceptar = true;
        filtrarListEstadosCiviles = null;
    }

    public void cancelarEstadoCivil() {
        estadoCivilSeleccionado = null;
        aceptar = true;
        filtrarListEstadosCiviles = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEstadoCivil:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEstadoCivil').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').hide()");
    }

    public void actualizarTipoTelefono() {
        permitirIndex = true;
        parametroDeReporte.setTipotelefono(tipoTelefonoSeleccionado);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoTelefono:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoTelefono').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoTelefonoDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:tipoTelefonoParametro");
        tipoTelefonoSeleccionado = null;
        aceptar = true;
        filtrarListTiposTelefonos = null;
    }

    public void cancelarTipoTelefono() {
        tipoTelefonoSeleccionado = null;
        aceptar = true;
        filtrarListTiposTelefonos = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoTelefono:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoTelefono').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TipoTelefonoDialogo').hide()");
    }

    public void actualizarJefeDivision() {
        permitirIndex = true;
        parametroDeReporte.setNombregerente(empleadoSeleccionado);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovJefeD:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovJefeD').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('JefeDivisionDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:jefeDivisionParametro");
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarListEmpleados = null;
    }

    public void cancelarJefeDivision() {
        empleadoSeleccionado = null;
        aceptar = true;
        filtrarListEmpleados = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovJefeD:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovJefeD').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('JefeDivisionDialogo').hide()");
    }

    public void actualizarCiudad() {
        permitirIndex = true;
        parametroDeReporte.setCiudad(ciudadSeleccionada);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovCiudades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:ciudadParametro");
        ciudadSeleccionada = null;
        aceptar = true;
        filtrarListCiudades = null;
    }

    public void cancelarCiudad() {
        ciudadSeleccionada = null;
        aceptar = true;
        filtrarListCiudades = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovCiudades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCiudades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').hide()");
    }

    public void actualizarDeporte() {
        permitirIndex = true;
        parametroDeReporte.setDeporte(deporteSeleccionado);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovDeportes:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovDeportes').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('DeportesDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:deporteParametro");
        deporteSeleccionado = null;
        aceptar = true;
        filtrarListDeportes = null;
    }

    public void cancelarDeporte() {
        deporteSeleccionado = null;
        aceptar = true;
        filtrarListDeportes = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovDeportes:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovDeportes').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('DeportesDialogo').hide()");
    }

    public void actualizarAficion() {
        permitirIndex = true;
        parametroDeReporte.setAficion(aficionSeleccionada);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovAficiones:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovAficiones').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('AficionesDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:aficionParametro");
        aficionSeleccionada = null;
        aceptar = true;
        filtrarListAficiones = null;
    }

    public void cancelarAficion() {
        aficionSeleccionada = null;
        aceptar = true;
        filtrarListAficiones = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovAficiones:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovAficiones').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('AficionesDialogo').hide()");
    }

    public void actualizarIdioma() {
        permitirIndex = true;
        parametroDeReporte.setIdioma(idiomaSeleccionado);
        parametroModificacion = parametroDeReporte;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovIdiomas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovIdiomas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('IdiomasDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("formParametros:idiomaParametro");
        idiomaSeleccionado = null;
        aceptar = true;
        filtrarListIdiomas = null;
    }

    public void cancelarIdioma() {
        idiomaSeleccionado = null;
        aceptar = true;
        filtrarListIdiomas = null;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovIdiomas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovIdiomas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('IdiomasDialogo').hide()");
    }

    public void mostrarDialogoGenerarReporte() {
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formDialogos:reporteAGenerar");
        RequestContext.getCurrentInstance().execute("PF('reporteAGenerar').show()");
    }

    public void cancelarGenerarReporte() {
        reporteGenerar = "";
        posicionReporte = -1;
    }

    public void mostrarDialogoBuscarReporte() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cambiosReporte == true) {
                contarRegistrosLovReportes();
                contarRegistros();
                RequestContext.getCurrentInstance().update("form:ReportesDialogo");
                RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
            }
        } catch (Exception e) {
            System.out.println("Error mostrarDialogoBuscarReporte : " + e.toString());
        }
    }

    public void actualizarSeleccionInforeporte() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (bandera == 1) {
            cerrarFiltrado();
        }
        defaultPropiedadesParametrosReporte();
        listaIR.clear();
        listaIR.add(reporteSeleccionadoLov);
        filtrarListInforeportesUsuario = null;
        filtrarLovInforeportes = null;
        reporteSeleccionado = null;
        reporteSeleccionadoLov = null;
        aceptar = true;
        activoBuscarReporte = true;
        activoMostrarTodos = false;
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
        RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
        context.reset("form:lovReportesDialogo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:reportesPersonal");
    }

    public void cancelarSeleccionInforeporte() {
        filtrarListInforeportesUsuario = null;
        reporteSeleccionadoLov = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovReportesDialogo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
    }

    public void mostrarTodos() {
        if (cambiosReporte == true) {
            defaultPropiedadesParametrosReporte();
            listaIR.clear();
            for (int i = 0; i < lovInforeportes.size(); i++) {
                listaIR.add(lovInforeportes.get(i));
            }
            contarRegistros();
            RequestContext context = RequestContext.getCurrentInstance();
            activoBuscarReporte = false;
            activoMostrarTodos = true;
            RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
            RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
            RequestContext.getCurrentInstance().update("form:reportesPersonal");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
        }
    }

    public void cancelarYSalir() {
        cancelarModificaciones();
        salir();
    }

    public void seleccionarTipoReporte(Inforeportes reporte, String tipor) {
        reporteSeleccionado = reporte;
        if (tipor.equalsIgnoreCase("PDF")) {
            reporteSeleccionado.setTipo("PDF");
        } else if (tipor.equalsIgnoreCase("HTML")) {
            reporteSeleccionado.setTipo("HTML");
        } else if (tipor.equalsIgnoreCase("XLS")) {
            reporteSeleccionado.setTipo("XLS");
        } else if (tipor.equalsIgnoreCase("XLSX")) {
            reporteSeleccionado.setTipo("XLSX");
        } else if (tipor.equalsIgnoreCase("CSV")) {
            reporteSeleccionado.setTipo("CSV");
        } else if (tipor.equalsIgnoreCase("DOCX")) {
            reporteSeleccionado.setTipo("DOCX");
        }

        if (listaInfoReportesModificados.isEmpty()) {
            System.out.println("Ingrese al if");
            listaInfoReportesModificados.add(reporteSeleccionado);
        } else if ((!listaInfoReportesModificados.isEmpty()) && (!listaInfoReportesModificados.contains(reporteSeleccionado))) {
            System.out.println("Ingrese al else if");
            listaInfoReportesModificados.add(reporteSeleccionado);
        } else {
            System.out.println("Ingrese al else");
            int posicion = listaInfoReportesModificados.indexOf(reporteSeleccionado);
            listaInfoReportesModificados.set(posicion, reporteSeleccionado);
        }
        System.out.println("Saliendo del metodo...Tipo reporteSeleccionado: " + reporteSeleccionado.getTipo());
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void defaultPropiedadesParametrosReporte() {
        color = "black";
        decoracion = "none";
        color2 = "black";
        decoracion2 = "none";
    }

    public void cancelarRequisitosReporte() {
        requisitosReporte = "";
    }

    public void generarDocumentoReporte() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (reporteSeleccionado != null) {
            System.out.println("generando reporte - ingreso al if");
            nombreReporte = reporteSeleccionado.getNombrereporte();
            tipoReporte = reporteSeleccionado.getTipo();
            System.out.println("tipoReporte: " + tipoReporte);
            if (nombreReporte != null && tipoReporte != null) {
                System.out.println("generando reporte - ingreso al 2 if");
                pathReporteGenerado = administarReportes.generarReporte(nombreReporte, tipoReporte);
            }
            if (pathReporteGenerado != null) {
                System.out.println("generando reporte - ingreso al 3 if");
                validarDescargaReporte();
            } else {
                System.out.println("generando reporte - ingreso al 3 if else");
                RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
                RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
                RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
            }
        } else {
            System.out.println("generando reporte - ingreso al if else");
            System.out.println("Reporte Seleccionado es nulo");
        }
    }

    public void generarReporte(Inforeportes reporte) {
        System.out.println(this.getClass().getName() + ".generarReporte()");
        reporteSeleccionado = reporte;
        System.out.println("inforreporteSeleccionado: " + reporteSeleccionado.getTipo());
        seleccionRegistro();
        generarDocumentoReporte();
    }

    public AsynchronousFilllListener listener() {
        System.out.println(this.getClass().getName() + ".listener()");
        return new AsynchronousFilllListener() {
            //RequestContext context = c;

            @Override
            public void reportFinished(JasperPrint jp) {
                System.out.println(this.getClass().getName() + ".listener().reportFinished()");
                try {
                    estadoReporte = true;
                    resultadoReporte = "Exito";
                    /*
                     * final RequestContext currentInstance =
                     * RequestContext.getCurrentInstance();
                     * Renderer.instance().render(template);
                     * RequestContext.setCurrentInstance(currentInstance)
                     */
                    // RequestContext.getCurrentInstance().execute("PF('formDialogos:generandoReporte");
                    //generarArchivoReporte(jp);
                } catch (Exception e) {
                    System.out.println("ControlNReportePersonal reportFinished ERROR: " + e.toString());
                }
            }

            @Override
            public void reportCancelled() {
                System.out.println(this.getClass().getName() + ".listener().reportCancelled()");
                estadoReporte = true;
                resultadoReporte = "Cancelación";
            }

            @Override
            public void reportFillError(Throwable e) {
                System.out.println(this.getClass().getName() + ".listener().reportFillError()");
                if (e.getCause() != null) {
                    pathReporteGenerado = "ControlNReportePersonal reportFillError Error: " + e.toString() + "\n" + e.getCause().toString();
                } else {
                    pathReporteGenerado = "ControlNReportePersonal reportFillError Error: " + e.toString();
                }
                estadoReporte = true;
                resultadoReporte = "Se estallo";
            }

        };
    }

    public void exportarReporte() throws IOException {
        System.out.println(this.getClass().getName() + ".exportarReporte()");
        System.out.println("pathReporteGenerado: " + pathReporteGenerado);
        if (pathReporteGenerado != null || !pathReporteGenerado.startsWith("Error:")) {
            File reporteF = new File(pathReporteGenerado);
            FacesContext ctx = FacesContext.getCurrentInstance();
            FileInputStream fis = new FileInputStream(reporteF);
            byte[] bytes = new byte[1024];
            int read;
            if (!ctx.getResponseComplete()) {
                String fileName = reporteF.getName();
                HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
                response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
                ServletOutputStream out = response.getOutputStream();

                while ((read = fis.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                out.flush();
                out.close();
                ctx.responseComplete();
            }
        } else {
            System.out.println("validar descarga reporte - ingreso al if 1 else");
            RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
        }
    }

    public void generarArchivoReporte(JasperPrint print) {
        System.out.println(this.getClass().getName() + ".generarArchivoReporte()");
        if (print != null && tipoReporte != null) {
            pathReporteGenerado = administarReportes.crearArchivoReporte(print, tipoReporte);
            validarDescargaReporte();
        }
    }

    public void validarDescargaReporte() {
        System.out.println(this.getClass().getName() + ".validarDescargaReporte()");
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
        if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
            System.out.println("validar descarga reporte - ingreso al if 1");
            if (!tipoReporte.equals("PDF")) {
                System.out.println("validar descarga reporte - ingreso al if 2");
                RequestContext.getCurrentInstance().execute("PF('descargarReporte').show()");
            } else {
                System.out.println("validar descarga reporte - ingreso al if 2 else");
                FileInputStream fis;
                try {
                    System.out.println("pathReporteGenerado : " + pathReporteGenerado);
                    fis = new FileInputStream(new File(pathReporteGenerado));
                    System.out.println("fis : " + fis);
                    reporte = new DefaultStreamedContent(fis, "application/pdf");
                } catch (FileNotFoundException ex) {
                    System.out.println("validar descarga reporte - ingreso al catch 1");
                    System.out.println(ex.getCause());
                    reporte = null;
                }
                if (reporte != null) {
                    System.out.println("validar descarga reporte - ingreso al if 3");
                    if (reporteSeleccionado != null) {
                        System.out.println("validar descarga reporte - ingreso al if 4");
                        //cabezeraVisor = "Reporte - " + reporteSeleccionado.getNombre();
                        cabezeraVisor = "Reporte - " + nombreReporte;
                    } else {
                        System.out.println("validar descarga reporte - ingreso al if 4 else ");
                        cabezeraVisor = "Reporte - ";
                    }
                    RequestContext.getCurrentInstance().update("formDialogos:verReportePDF");
                    RequestContext.getCurrentInstance().execute("PF('verReportePDF').show()");
                }
                //pathReporteGenerado = null;
            }
        } else {
            System.out.println("validar descarga reporte - ingreso al if 1 else");
            RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
        }
    }

    public void reiniciarStreamedContent() {
        System.out.println(this.getClass().getName() + ".reiniciarStreamedContent()");
        reporte = null;
    }

    public void cancelarReporte() {
        System.out.println(this.getClass().getName() + ".cancelarReporte()");
        administarReportes.cancelarReporte();
    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        contarRegistros();
    }

    //CONTAR REGISTROS
    public void contarRegistros() {
        System.out.println("Controlador.ControlNReportePersonal.contarRegistros()");
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosEmpleadoD() {
        RequestContext.getCurrentInstance().update("form:infoRegistroEmpleadoDesde");
    }

    public void contarRegistrosEmpleadoH() {
        RequestContext.getCurrentInstance().update("form:infoRegistroEmpleadoHasta");
    }

    public void contarRegistrosEmpresa() {
        RequestContext.getCurrentInstance().update("form:infoRegistroEmpresa");
    }

    public void contarRegistrosEstructura() {
        RequestContext.getCurrentInstance().update("form:infoRegistroEstructura");
    }

    public void contarRegistrosTipoTrabajador() {
        RequestContext.getCurrentInstance().update("form:infoRegistroTipoTrabajador");
    }

    public void contarRegistrosEstadoCivil() {
        RequestContext.getCurrentInstance().update("form:infoRegistroEstadoCivil");
    }

    public void contarRegistrosTipoTelefono() {
        RequestContext.getCurrentInstance().update("form:infoRegistroTipoTelefono");
    }

    public void contarRegistrosCiudad() {
        RequestContext.getCurrentInstance().update("form:infoRegistroCiudad");
    }

    public void contarRegistrosDeporte() {
        RequestContext.getCurrentInstance().update("form:infoRegistroDeporte");
    }

    public void contarRegistrosAficion() {
        RequestContext.getCurrentInstance().update("form:infoRegistroAficion");
    }

    public void contarRegistrosIdioma() {
        RequestContext.getCurrentInstance().update("form:infoRegistroIdioma");
    }

    public void contarRegistrosJefe() {
        RequestContext.getCurrentInstance().update("form:infoRegistroJefe");
    }

    public void contarRegistrosLovReportes() {
        System.out.println("Controlador.ControlNReportePersonal.contarRegistrosLovReportes()");
        RequestContext.getCurrentInstance().update("form:infoRegistroReportes");
    }

    ///////////////////////////////SETS Y GETS/////////////////////////////
    public ParametrosReportes getParametroDeReporte() {
        try {
            if (parametroDeReporte == null) {
                parametroDeReporte = new ParametrosReportes();
                parametroDeReporte = administrarNReportePersonal.parametrosDeReporte();
            }
            if (parametroDeReporte.getEstadocivil() == null) {
                parametroDeReporte.setEstadocivil(new EstadosCiviles());
            }
            if (parametroDeReporte.getTipotelefono() == null) {
                parametroDeReporte.setTipotelefono(new TiposTelefonos());
            }
            if (parametroDeReporte.getLocalizacion() == null) {
                parametroDeReporte.setLocalizacion(new Estructuras());
            }
            if (parametroDeReporte.getTipotrabajador() == null) {
                parametroDeReporte.setTipotrabajador(new TiposTrabajadores());
            }
            if (parametroDeReporte.getCiudad() == null) {
                parametroDeReporte.setCiudad(new Ciudades());
            }
            if (parametroDeReporte.getDeporte() == null) {
                parametroDeReporte.setDeporte(new Deportes());
            }
            if (parametroDeReporte.getAficion() == null) {
                parametroDeReporte.setAficion(new Aficiones());
            }
            if (parametroDeReporte.getIdioma() == null) {
                parametroDeReporte.setIdioma(new Idiomas());
            }
            if (parametroDeReporte.getEmpresa() == null) {
                parametroDeReporte.setEmpresa(new Empresas());
            }
            if (parametroDeReporte.getNombregerente() == null) {
                System.out.println("Controlador.ControlNReportePersonal.getParametroDeReporte()");
                parametroDeReporte.setNombregerente(new Empleados());
                parametroDeReporte.getNombregerente().setPersona(new Personas());
            }
            return parametroDeReporte;
        } catch (Exception e) {
            System.out.println("Error getParametroDeInforme : " + e.toString());
            return null;
        }
    }

    public void setParametroDeReporte(ParametrosReportes parametroDeReporte) {
        this.parametroDeReporte = parametroDeReporte;
    }

    public List<Inforeportes> getListaIR() {
        try {
            if (listaIR == null) {
                listaIR = administrarNReportePersonal.listInforeportesUsuario();
            }
            return listaIR;
        } catch (Exception e) {
            System.out.println("Error getListInforeportesUsuario : " + e);
            return null;
        }
    }

    public void setListaIR(List<Inforeportes> listaIR) {
        this.listaIR = listaIR;
    }

    public List<Inforeportes> getFiltrarListInforeportesUsuario() {
        return filtrarListInforeportesUsuario;
    }

    public void setFiltrarListInforeportesUsuario(List<Inforeportes> filtrarListInforeportesUsuario) {
        this.filtrarListInforeportesUsuario = filtrarListInforeportesUsuario;
    }

    public Inforeportes getReporteSeleccionado() {
        return reporteSeleccionado;
    }

    public void setReporteSeleccionado(Inforeportes reporteSeleccionado) {
        this.reporteSeleccionado = reporteSeleccionado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public ParametrosReportes getNuevoParametroInforme() {
        return nuevoParametroInforme;
    }

    public void setNuevoParametroInforme(ParametrosReportes nuevoParametroInforme) {
        this.nuevoParametroInforme = nuevoParametroInforme;
    }

    public ParametrosReportes getParametroModificacion() {
        return parametroModificacion;
    }

    public void setParametroModificacion(ParametrosReportes parametroModificacion) {
        this.parametroModificacion = parametroModificacion;
    }

    public String getReporteGenerar() {
        if (posicionReporte != -1) {
            reporteGenerar = listaIR.get(posicionReporte).getNombre();
        }
        return reporteGenerar;
    }

    public void setReporteGenerar(String reporteGenerar) {
        this.reporteGenerar = reporteGenerar;
    }

    public String getRequisitosReporte() {
        return requisitosReporte;
    }

    public void setRequisitosReporte(String requisitosReporte) {
        this.requisitosReporte = requisitosReporte;
    }

    public List<Empleados> getListEmpleados() {
        return listEmpleados;
    }

    public void setListEmpleados(List<Empleados> listEmpleados) {
        this.listEmpleados = listEmpleados;
    }

    public List<Empresas> getListEmpresas() {
        return listEmpresas;
    }

    public void setListEmpresas(List<Empresas> listEmpresas) {
        this.listEmpresas = listEmpresas;
    }

    public List<Estructuras> getListEstructuras() {
        return listEstructuras;
    }

    public void setListEstructuras(List<Estructuras> listEstructuras) {
        this.listEstructuras = listEstructuras;
    }

    public List<TiposTrabajadores> getListTiposTrabajadores() {
        return listTiposTrabajadores;
    }

    public void setListTiposTrabajadores(List<TiposTrabajadores> listTiposTrabajadores) {
        this.listTiposTrabajadores = listTiposTrabajadores;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public Empresas getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public Estructuras getEstructuraSeleccionada() {
        return estructuraSeleccionada;
    }

    public void setEstructuraSeleccionada(Estructuras estructuraSeleccionada) {
        this.estructuraSeleccionada = estructuraSeleccionada;
    }

    public TiposTrabajadores getTipoTSeleccionado() {
        return tipoTSeleccionado;
    }

    public void setTipoTSeleccionado(TiposTrabajadores tipoTSeleccionado) {
        this.tipoTSeleccionado = tipoTSeleccionado;
    }

    public List<Empleados> getFiltrarListEmpleados() {
        return filtrarListEmpleados;
    }

    public void setFiltrarListEmpleados(List<Empleados> filtrarListEmpleados) {
        this.filtrarListEmpleados = filtrarListEmpleados;
    }

    public List<Empresas> getFiltrarListEmpresas() {
        return filtrarListEmpresas;
    }

    public void setFiltrarListEmpresas(List<Empresas> filtrarListEmpresas) {
        this.filtrarListEmpresas = filtrarListEmpresas;
    }

    public List<Estructuras> getFiltrarListEstructuras() {
        return filtrarListEstructuras;
    }

    public void setFiltrarListEstructuras(List<Estructuras> filtrarListEstructuras) {
        this.filtrarListEstructuras = filtrarListEstructuras;
    }

    public List<TiposTrabajadores> getFiltrarListTiposTrabajadores() {
        return filtrarListTiposTrabajadores;
    }

    public void setFiltrarListTiposTrabajadores(List<TiposTrabajadores> filtrarListTiposTrabajadores) {
        this.filtrarListTiposTrabajadores = filtrarListTiposTrabajadores;
    }

    public List<EstadosCiviles> getListEstadosCiviles() {
        return listEstadosCiviles;
    }

    public void setListEstadosCiviles(List<EstadosCiviles> listEstadosCiviles) {
        this.listEstadosCiviles = listEstadosCiviles;
    }

    public List<TiposTelefonos> getListTiposTelefonos() {
        return listTiposTelefonos;
    }

    public void setListTiposTelefonos(List<TiposTelefonos> listTiposTelefonos) {
        this.listTiposTelefonos = listTiposTelefonos;
    }

    public List<Ciudades> getListCiudades() {
        return listCiudades;
    }

    public void setListCiudades(List<Ciudades> listCiudades) {
        this.listCiudades = listCiudades;
    }

    public List<Deportes> getListDeportes() {
        return listDeportes;
    }

    public void setListDeportes(List<Deportes> listDeportes) {
        this.listDeportes = listDeportes;
    }

    public List<Aficiones> getListAficiones() {
        return listAficiones;
    }

    public void setListAficiones(List<Aficiones> listAficiones) {
        this.listAficiones = listAficiones;
    }

    public List<Idiomas> getListIdiomas() {
        return listIdiomas;
    }

    public void setListIdiomas(List<Idiomas> listIdiomas) {
        this.listIdiomas = listIdiomas;
    }

    public EstadosCiviles getEstadoCivilSeleccionado() {
        return estadoCivilSeleccionado;
    }

    public void setEstadoCivilSeleccionado(EstadosCiviles estadoCivilSeleccionado) {
        this.estadoCivilSeleccionado = estadoCivilSeleccionado;
    }

    public TiposTelefonos getTipoTelefonoSeleccionado() {
        return tipoTelefonoSeleccionado;
    }

    public void setTipoTelefonoSeleccionado(TiposTelefonos tipoTelefonoSeleccionado) {
        this.tipoTelefonoSeleccionado = tipoTelefonoSeleccionado;
    }

    public Ciudades getCiudadSeleccionada() {
        return ciudadSeleccionada;
    }

    public void setCiudadSeleccionada(Ciudades ciudadSeleccionada) {
        this.ciudadSeleccionada = ciudadSeleccionada;
    }

    public Deportes getDeporteSeleccionado() {
        return deporteSeleccionado;
    }

    public void setDeporteSeleccionado(Deportes deporteSeleccionado) {
        this.deporteSeleccionado = deporteSeleccionado;
    }

    public Aficiones getAficionSeleccionada() {
        return aficionSeleccionada;
    }

    public void setAficionSeleccionada(Aficiones aficionSeleccionada) {
        this.aficionSeleccionada = aficionSeleccionada;
    }

    public List<EstadosCiviles> getFiltrarListEstadosCiviles() {
        return filtrarListEstadosCiviles;
    }

    public void setFiltrarListEstadosCiviles(List<EstadosCiviles> filtrarListEstadosCiviles) {
        this.filtrarListEstadosCiviles = filtrarListEstadosCiviles;
    }

    public List<TiposTelefonos> getFiltrarListTiposTelefonos() {
        return filtrarListTiposTelefonos;
    }

    public void setFiltrarListTiposTelefonos(List<TiposTelefonos> filtrarListTiposTelefonos) {
        this.filtrarListTiposTelefonos = filtrarListTiposTelefonos;
    }

    public List<Ciudades> getFiltrarListCiudades() {
        return filtrarListCiudades;
    }

    public void setFiltrarListCiudades(List<Ciudades> filtrarListCiudades) {
        this.filtrarListCiudades = filtrarListCiudades;
    }

    public List<Deportes> getFiltrarListDeportes() {
        return filtrarListDeportes;
    }

    public void setFiltrarListDeportes(List<Deportes> filtrarListDeportes) {
        this.filtrarListDeportes = filtrarListDeportes;
    }

    public List<Aficiones> getFiltrarListAficiones() {
        return filtrarListAficiones;
    }

    public void setFiltrarListAficiones(List<Aficiones> filtrarListAficiones) {
        this.filtrarListAficiones = filtrarListAficiones;
    }

    public List<Idiomas> getFiltrarListIdiomas() {
        return filtrarListIdiomas;
    }

    public void setFiltrarListIdiomas(List<Idiomas> filtrarListIdiomas) {
        this.filtrarListIdiomas = filtrarListIdiomas;
    }

    public Idiomas getIdiomaSeleccionado() {
        return idiomaSeleccionado;
    }

    public void setIdiomaSeleccionado(Idiomas idiomaSeleccionado) {
        this.idiomaSeleccionado = idiomaSeleccionado;
    }

    public boolean isCambiosReporte() {
        return cambiosReporte;
    }

    public void setCambiosReporte(boolean cambiosReporte) {
        this.cambiosReporte = cambiosReporte;
    }

    public List<Inforeportes> getListaInfoReportesModificados() {
        return listaInfoReportesModificados;
    }

    public void setListaInfoReportesModificados(List<Inforeportes> listaInfoReportesModificados) {
        this.listaInfoReportesModificados = listaInfoReportesModificados;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDecoracion() {
        return decoracion;
    }

    public void setDecoracion(String decoracion) {
        this.decoracion = decoracion;
    }

    public String getColor2() {
        return color2;
    }

    public void setColor2(String color) {
        this.color2 = color;
    }

    public String getDecoracion2() {
        return decoracion2;
    }

    public void setDecoracion2(String decoracion) {
        this.decoracion2 = decoracion;
    }

    public boolean isActivoMostrarTodos() {
        return activoMostrarTodos;
    }

    public void setActivoMostrarTodos(boolean activoMostrarTodos) {
        this.activoMostrarTodos = activoMostrarTodos;
    }

    public boolean isActivoBuscarReporte() {
        return activoBuscarReporte;
    }

    public void setActivoBuscarReporte(boolean activoBuscarReporte) {
        this.activoBuscarReporte = activoBuscarReporte;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:reportesPersonal");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public String getInfoRegistroEmpleadoDesde() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovEmpleadoDesde");
        infoRegistroEmpleadoDesde = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpleadoDesde;
    }

    public String getInfoRegistroEmpleadoHasta() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovEmpleadoHasta");
        infoRegistroEmpleadoHasta = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpleadoHasta;
    }

    public String getInfoRegistroEmpresa() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovEmpresa");
        infoRegistroEmpresa = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpresa;
    }

    public String getInfoRegistroEstructura() {

        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovEstructura");
        infoRegistroEstructura = String.valueOf(tabla.getRowCount());
        return infoRegistroEstructura;
    }

    public String getInfoRegistroTipoTrabajador() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovTipoTrabajador");
        infoRegistroTipoTrabajador = String.valueOf(tabla.getRowCount());
        return infoRegistroTipoTrabajador;
    }

    public String getInfoRegistroEstadoCivil() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovEstadoCivil");
        infoRegistroEstadoCivil = String.valueOf(tabla.getRowCount());
        return infoRegistroEstadoCivil;
    }

    public String getInfoRegistroTipoTelefono() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovTipoTelefono");
        infoRegistroTipoTelefono = String.valueOf(tabla.getRowCount());
        return infoRegistroTipoTelefono;
    }

    public String getInfoRegistroCiudad() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovCiudades");
        infoRegistroCiudad = String.valueOf(tabla.getRowCount());
        return infoRegistroCiudad;
    }

    public String getInfoRegistroDeporte() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovDeportes");
        infoRegistroDeporte = String.valueOf(tabla.getRowCount());
        return infoRegistroDeporte;
    }

    public String getInfoRegistroAficion() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovAficiones");
        infoRegistroAficion = String.valueOf(tabla.getRowCount());
        return infoRegistroAficion;
    }

    public String getInfoRegistroIdioma() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovIdiomas");
        infoRegistroIdioma = String.valueOf(tabla.getRowCount());
        return infoRegistroIdioma;
    }

    public String getInfoRegistroJefe() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovJefeD");
        infoRegistroJefe = String.valueOf(tabla.getRowCount());
        return infoRegistroJefe;
    }

    public String getInfoRegistroReportes() {
        FacesContext c = FacesContext.getCurrentInstance();
        tabla = (DataTable) c.getViewRoot().findComponent("form:lovReportesDialogo");
        infoRegistroReportes = String.valueOf(tabla.getRowCount());
        return infoRegistroReportes;
    }

    public List<Inforeportes> getLovInforeportes() {
        lovInforeportes = administrarNReportePersonal.listInforeportesUsuario();
        return lovInforeportes;
    }

    public void setLovInforeportes(List<Inforeportes> lovInforeportes) {
        this.lovInforeportes = lovInforeportes;
    }

    public List<Inforeportes> getFiltrarLovInforeportes() {
        return filtrarLovInforeportes;
    }

    public void setFiltrarLovInforeportes(List<Inforeportes> filtrarLovInforeportes) {
        this.filtrarLovInforeportes = filtrarLovInforeportes;
    }

    public String getPathReporteGenerado() {
        return pathReporteGenerado;
    }

    public void setPathReporteGenerado(String pathReporteGenerado) {
        this.pathReporteGenerado = pathReporteGenerado;
    }

    public StreamedContent getReporte() {
        return reporte;
    }

    public void setReporte(StreamedContent reporte) {
        this.reporte = reporte;
    }

    public String getCabezeraVisor() {
        return cabezeraVisor;
    }

    public void setCabezeraVisor(String cabezeraVisor) {
        this.cabezeraVisor = cabezeraVisor;
    }

    public Inforeportes getReporteSeleccionadoLov() {
        return reporteSeleccionadoLov;
    }

    public void setReporteSeleccionadoLov(Inforeportes reporteSeleccionadoLov) {
        this.reporteSeleccionadoLov = reporteSeleccionadoLov;
    }

    public boolean isActivarEnvio() {
        return activarEnvio;
    }

    public void setActivarEnvio(boolean activarEnvio) {
        this.activarEnvio = activarEnvio;
    }

}
