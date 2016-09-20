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
import Entidades.ParametrosInformes;
import Entidades.TiposTelefonos;
import Entidades.TiposTrabajadores;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarNReportePersonalInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.selectonemenu.SelectOneMenu;
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
    //
    private ParametrosInformes parametroDeInforme;
    private ParametrosInformes nuevoParametroInforme;
    private List<Inforeportes> listaIR;
    private List<Inforeportes> filtrarListInforeportesUsuario;
    private List<Inforeportes> listaIRRespaldo;
    private List<Inforeportes> lovInforeportes;
    private List<Inforeportes> filtrarLovInforeportes;
    private Inforeportes reporteLovSeleccionado;
    private Inforeportes inforreporteSeleccionado;
    //
    private int casilla;
    private ParametrosInformes parametroModificacion;
    private int posicionReporte;
    private String requisitosReporte;
    private InputText empleadoDesdeParametro, empleadoHastaParametro, estadoCivilParametro, tipoTelefonoParametro, estructuraParametro;
    private InputText ciudadParametro, deporteParametro, aficionParametro, idiomaParametro, tipoTrabajadorParametro;

    private String reporteGenerar;
    private int bandera;
    private boolean aceptar;
    private Column codigoIR, reporteIR, tipoIR;
    //
    private int tipoLista;
    //
    //
    private List<Empleados> listEmpleados;
    private List<Empresas> listEmpresas;
    private List<Estructuras> listEstructuras;
    private List<TiposTrabajadores> listTiposTrabajadores;
    private List<EstadosCiviles> listEstadosCiviles;
    private List<TiposTelefonos> listTiposTelefonos;
    private List<Ciudades> listCiudades;
    private List<Deportes> listDeportes;
    private List<Aficiones> listAficiones;
    private List<Idiomas> listIdiomas;
    private Empleados empleadoSeleccionado;
    private Empresas empresaSeleccionada;
    private Estructuras estructuraSeleccionada;
    private TiposTrabajadores tipoTSeleccionado;
    private EstadosCiviles estadoCivilSeleccionado;
    private TiposTelefonos tipoTelefonoSeleccionado;
    private Ciudades ciudadSeleccionada;
    private Deportes deporteSeleccionado;
    private Aficiones aficionSeleccionada;
    private Idiomas idiomaSeleccionado;
    private List<Empleados> filtrarListEmpleados;
    private List<Empresas> filtrarListEmpresas;
    private List<Estructuras> filtrarListEstructuras;
    private List<TiposTrabajadores> filtrarListTiposTrabajadores;
    private List<EstadosCiviles> filtrarListEstadosCiviles;
    private List<TiposTelefonos> filtrarListTiposTelefonos;
    private List<Ciudades> filtrarListCiudades;
    private List<Deportes> filtrarListDeportes;
    private List<Aficiones> filtrarListAficiones;
    private List<Idiomas> filtrarListIdiomas;
    //
    private String estadocivil, tipotelefono, jefediv, estructura, empresa, tipoTrabajador, ciudad, deporte, aficiones, idioma;
    private boolean permitirIndex, cambiosReporte;
    //ALTO SCROLL TABLA
    private String altoTabla;
    private int indice;
    //EXPORTAR REPORTE
    private StreamedContent file;
    //
    private List<Inforeportes> listaInfoReportesModificados;
    private Inforeportes actualInfoReporteTabla;
    private String color, decoracion;
    private String color2, decoracion2;
    private int casillaInforReporte;
    private Date fechaDesde, fechaHasta;
    private BigDecimal emplDesde, emplHasta;
    private boolean activoMostrarTodos, activoBuscarReporte;
    private String infoRegistroEmpleadoDesde, infoRegistroEmpleadoHasta, infoRegistroEmpresa, infoRegistroEstructura, infoRegistroTipoTrabajador, infoRegistroEstadoCivil, infoRegistroTipoTelefono, infoRegistroCiudad, infoRegistroDeporte, infoRegistroAficion, infoRegistroIdioma, infoRegistroJefe;
    private String infoRegistroReportes;
    private String infoRegistro;
    //EXPORTAR REPORTE
    private String pathReporteGenerado = null;
    private String nombreReporte, tipoReporte;
    //BANDERAS
    private boolean estadoReporte;
    private String resultadoReporte;
    //VISUALIZAR REPORTE PDF
    private StreamedContent reporte;
    private String cabezeraVisor;
    private DataTable tabla;

    public ControlNReportePersonal() {
        activoMostrarTodos = true;
        activoBuscarReporte = false;
        color = "black";
        decoracion = "none";
        color2 = "black";
        decoracion2 = "none";
        casillaInforReporte = -1;
        actualInfoReporteTabla = new Inforeportes();
        cambiosReporte = true;
        listaInfoReportesModificados = new ArrayList<Inforeportes>();
        altoTabla = "110";
        parametroDeInforme = null;
        listaIR = null;
        listaIRRespaldo = new ArrayList<Inforeportes>();
        bandera = 0;
        aceptar = true;
        casilla = -1;
        parametroModificacion = new ParametrosInformes();
        tipoLista = 0;
        reporteGenerar = "";
        requisitosReporte = "";
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
        inforreporteSeleccionado = null;
        reporteLovSeleccionado = null;
        //
        permitirIndex = true;
        cabezeraVisor = null;
    }

    @PostConstruct
    public void inicializarAdministrador() {
           System.out.println(this.getClass().getName() + ".iniciarAdministrador()");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarNReportePersonal.obtenerConexion(ses.getId());
            administarReportes.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void iniciarPagina() {
        System.out.println(this.getClass().getName() + ".iniciarPagina()");
        activoMostrarTodos = true;
        activoBuscarReporte = false;
//        listaIR = null;
        getListaIR();
        if (listaIR != null) {
            inforreporteSeleccionado = listaIR.get(0);
            modificarInfoRegistroReportes(listaIR.size());
        } else{
            modificarInfoRegistroReportes(0);
        }
//        listEmpleados = new ArrayList<Empleados>();
    }

    public void requisitosParaReporte() {
        System.out.println(this.getClass().getName() + ".requisitosParaReporte()");
        RequestContext context = RequestContext.getCurrentInstance();
        requisitosReporte = "";
        if (inforreporteSeleccionado.getFecdesde().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Fecha Desde -";
        }
        if (inforreporteSeleccionado.getFechasta().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Fecha Hasta -";
        }
        if (inforreporteSeleccionado.getEmdesde().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Empleado Desde -";
        }
        if (inforreporteSeleccionado.getEmhasta().equals("SI")) {
            requisitosReporte = requisitosReporte + "- Empleado Hasta -";
        }
        if (!requisitosReporte.isEmpty()) {
            RequestContext.getCurrentInstance().update("formDialogos:requisitosReporte");
            RequestContext.getCurrentInstance().execute("PF('requisitosReporte').show()");
        }
    }

    public void seleccionRegistro(Inforeportes reporte) {

        System.out.println(this.getClass().getName() + ".seleccionRegistro()");
        RequestContext context = RequestContext.getCurrentInstance();
        inforreporteSeleccionado = reporte;
        // Resalto Parametros Para Reporte
        defaultPropiedadesParametrosReporte();
        System.out.println("reporteSeleccionado: " + inforreporteSeleccionado);
        System.out.println("reporteSeleccionado.getFecdesde(): " + inforreporteSeleccionado.getFecdesde());
        if (inforreporteSeleccionado.getFecdesde().equals("SI")) {
            color = "red";
            RequestContext.getCurrentInstance().update("formParametros");
        }
        System.out.println("reporteSeleccionado.getFechasta(): " + inforreporteSeleccionado.getFechasta());
        if (inforreporteSeleccionado.getFechasta().equals("SI")) {
            color2 = "red";
            RequestContext.getCurrentInstance().update("formParametros");
        }
        System.out.println("reporteSeleccionado.getEmdesde(): " + inforreporteSeleccionado.getEmdesde());
        if (inforreporteSeleccionado.getEmdesde().equals("SI")) {
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

        System.out.println("reporteSeleccionado.getEmhasta(): " + inforreporteSeleccionado.getEmhasta());
        if (inforreporteSeleccionado.getEmhasta().equals("SI")) {
            empleadoHastaParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametro");
            //empleadoHastaParametro.setStyle("position: absolute; top: 41px; left: 330px; height: 10px; font-size: 11px; width: 90px; color: red;");
            empleadoHastaParametro.setStyle(empleadoHastaParametro.getStyle() + "color: red;");
            RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
        }
//        System.out.println("reporteSeleccionado.getLocalizacion(): " + inforreporteSeleccionado.getLocalizacion());
//        if (inforreporteSeleccionado.getLocalizacion().equals("SI")) {
//            estructuraParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:estructuraParametro");
//            //estructuraParametro.setStyle("position: absolute; top: 20px; left: 625px;height: 10px; font-size: 11px;width: 180px; color: red;");
//            estructuraParametro.setStyle(estructuraParametro.getStyle() + "color: red;");
//            RequestContext.getCurrentInstance().update("formParametros:estructuraParametro");
//        }
//        System.out.println("reporteSeleccionado.getTipotrabajador(): " + inforreporteSeleccionado.getTipotrabajador());
//        if (inforreporteSeleccionado.getTipotrabajador().equals("SI")) {
//            tipoTrabajadorParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:tipoTrabajadorParametro");
//            //tipoTrabajadorParametro.setStyle("position: absolute; top: 43px; left: 625px;height: 10px; font-size: 11px; width: 180px; color: red;");
//            tipoTrabajadorParametro.setStyle(tipoTrabajadorParametro.getStyle() + "color: red;");
//            RequestContext.getCurrentInstance().update("formParametros:tipoTrabajadorParametro");
//        }
//        RequestContext.getCurrentInstance().update("formParametros");
//        RequestContext.getCurrentInstance().update("form:reportesPersonal");
        System.out.println("reporte seleccionado : " + inforreporteSeleccionado.getSecuencia());
    }

    public void dispararDialogoGuardarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");

    }

    public void guardarYSalir() {
        guardarCambios();
        salir();
    }

    public void guardarCambios() {
        try {
            if (cambiosReporte == false) {
                if (parametroDeInforme.getUsuario() != null) {
                    
                    if (parametroDeInforme.getCodigoempleadodesde() == null) {
                        parametroDeInforme.setCodigoempleadodesde(null);
                    }
                    if (parametroDeInforme.getCodigoempleadohasta() == null) {
                        parametroDeInforme.setCodigoempleadohasta(null);
                    }
                    if (parametroDeInforme.getEstadocivil().getSecuencia() == null) {
                        parametroDeInforme.setEstadocivil(null);
                    }
                    if (parametroDeInforme.getTipotelefono().getSecuencia() == null) {
                        parametroDeInforme.setTipotelefono(null);
                    }
                    if (parametroDeInforme.getLocalizacion().getSecuencia() == null) {
                        parametroDeInforme.setLocalizacion(null);
                    }
                    if (parametroDeInforme.getTipotrabajador().getSecuencia() == null) {
                        parametroDeInforme.setTipotrabajador(null);
                    }
                    if (parametroDeInforme.getCiudad().getSecuencia() == null) {
                        parametroDeInforme.setCiudad(null);
                    }
                    if (parametroDeInforme.getDeporte().getSecuencia() == null) {
                        parametroDeInforme.setDeporte(null);
                    }
                    if (parametroDeInforme.getAficion().getSecuencia() == null) {
                        parametroDeInforme.setAficion(null);
                    }
                    if (parametroDeInforme.getIdioma().getSecuencia() == null) {
                        parametroDeInforme.setIdioma(null);
                    }
                    if (parametroDeInforme.getEmpresa().getSecuencia() == null) {
                        parametroDeInforme.setEmpresa(null);
                    }
                    if (parametroDeInforme.getNombregerente().getSecuencia() == null) {
                        parametroDeInforme.setNombregerente(null);
                    }
                    administrarNReportePersonal.modificarParametrosInformes(parametroDeInforme);
                }
            }
            if (!listaInfoReportesModificados.isEmpty()) {
                administrarNReportePersonal.guardarCambiosInfoReportes(listaInfoReportesModificados);
            }
            cambiosReporte = true;
            RequestContext context = RequestContext.getCurrentInstance();
            FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
//            getListaIR();
//            modificarInfoRegistroReportes(listaIR.size());
//            RequestContext.getCurrentInstance().update("form:informacionRegistro");
//            RequestContext.getCurrentInstance().update("form:reportesPersonal");
        } catch (Exception e) {
            System.out.println("Error en guardar Cambios Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void modificarParametroInforme() {
        if (parametroDeInforme.getCodigoempleadodesde() != null && parametroDeInforme.getCodigoempleadohasta() != null
                && parametroDeInforme.getFechadesde() != null && parametroDeInforme.getFechahasta() != null) {
            if (parametroDeInforme.getFechadesde().before(parametroDeInforme.getFechahasta())) {
                parametroModificacion = parametroDeInforme;
                cambiosReporte = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                parametroDeInforme.setFechadesde(fechaDesde);
                parametroDeInforme.setFechahasta(fechaHasta);
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("formParametros");
                RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
            }
        } else {
            parametroDeInforme.setCodigoempleadodesde(emplDesde);
            parametroDeInforme.setCodigoempleadohasta(emplHasta);
            parametroDeInforme.setFechadesde(fechaDesde);
            parametroDeInforme.setFechahasta(fechaHasta);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formParametros");
            RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
        }
    }

    public void posicionCelda(int i) {
        casilla = i;
        if (permitirIndex == true) {
            casillaInforReporte = -1;
            emplDesde = parametroDeInforme.getCodigoempleadodesde();
            fechaDesde = parametroDeInforme.getFechadesde();
            emplHasta = parametroDeInforme.getCodigoempleadohasta();
            fechaHasta = parametroDeInforme.getFechahasta();
            estadocivil = parametroDeInforme.getEstadocivil().getDescripcion();
            tipotelefono = parametroDeInforme.getTipotelefono().getNombre();
            jefediv = parametroDeInforme.getNombregerente().getPersona().getNombreCompleto();
            estructura = parametroDeInforme.getLocalizacion().getNombre();
            tipoTrabajador = parametroDeInforme.getTipotrabajador().getNombre();
            ciudad = parametroDeInforme.getCiudad().getNombre();
            deporte = parametroDeInforme.getDeporte().getNombre();
            aficiones = parametroDeInforme.getAficion().getDescripcion();
            idioma = parametroDeInforme.getIdioma().getNombre();
            empresa = parametroDeInforme.getEmpresa().getNombre();
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

    public void autocompletarGeneral(String campoConfirmar, String valorConfirmar) {
        RequestContext context = RequestContext.getCurrentInstance();
        int indiceUnicoElemento = -1;
        int coincidencias = 0;
        if (campoConfirmar.equalsIgnoreCase("ESTADOCIVIL")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getEstadocivil().setDescripcion(estadocivil);
                for (int i = 0; i < listEstadosCiviles.size(); i++) {
                    if (listEstadosCiviles.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setEstadocivil(listEstadosCiviles.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
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
                parametroDeInforme.setEstadocivil(new EstadosCiviles());
                parametroModificacion = parametroDeInforme;
                listEstadosCiviles.clear();
                getListEstadosCiviles();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("TIPOTELEFONO")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getTipotelefono().setNombre(tipotelefono);
                for (int i = 0; i < listTiposTelefonos.size(); i++) {
                    if (listTiposTelefonos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setTipotelefono(listTiposTelefonos.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
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
                parametroDeInforme.setTipotelefono(new TiposTelefonos());
                parametroModificacion = parametroDeInforme;
                listTiposTelefonos.clear();
                getListTiposTelefonos();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("JEFEDIV")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getNombregerente().getPersona().setNombreCompleto(jefediv);
                for (int i = 0; i < listEmpleados.size(); i++) {
                    if (listEmpleados.get(i).getPersona().getNombreCompleto().startsWith(valorConfirmar)) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setNombregerente(listEmpleados.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
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
                parametroDeInforme.setNombregerente(new Empleados());
                parametroModificacion = parametroDeInforme;
                listEmpleados.clear();
                getListEmpleados();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("ESTRUCTURA")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getLocalizacion().setNombre(estructura);
                for (int i = 0; i < listEstructuras.size(); i++) {
                    if (listEstructuras.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setLocalizacion(listEstructuras.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
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
                parametroDeInforme.setLocalizacion(new Estructuras());
                parametroModificacion = parametroDeInforme;
                listEstructuras.clear();
                getListEstructuras();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                System.out.println("Change");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("TIPOTRABAJADOR")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getTipotrabajador().setNombre(tipoTrabajador);
                for (int i = 0; i < listTiposTrabajadores.size(); i++) {
                    if (listTiposTrabajadores.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setTipotrabajador(listTiposTrabajadores.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
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
                parametroDeInforme.setTipotrabajador(new TiposTrabajadores());
                parametroModificacion = parametroDeInforme;
                listTiposTrabajadores.clear();
                getListTiposTrabajadores();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("CIUDAD")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getCiudad().setNombre(ciudad);
                for (int i = 0; i < listCiudades.size(); i++) {
                    if (listCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setCiudad(listCiudades.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
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
                parametroDeInforme.setCiudad(new Ciudades());
                parametroModificacion = parametroDeInforme;
                listCiudades.clear();
                getListCiudades();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("DEPORTE")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getDeporte().setNombre(deporte);
                for (int i = 0; i < listDeportes.size(); i++) {
                    if (listDeportes.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setDeporte(listDeportes.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
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

                parametroDeInforme.setDeporte(new Deportes());
                parametroModificacion = parametroDeInforme;
                listDeportes.clear();
                getListDeportes();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("AFICION")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getAficion().setDescripcion(aficiones);
                for (int i = 0; i < listAficiones.size(); i++) {
                    if (listAficiones.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setAficion(listAficiones.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
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
                parametroDeInforme.setAficion(new Aficiones());
                parametroModificacion = parametroDeInforme;
                listAficiones.clear();
                getListAficiones();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("IDIOMA")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getIdioma().setNombre(idioma);
                for (int i = 0; i < listIdiomas.size(); i++) {
                    if (listIdiomas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setIdioma(listIdiomas.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
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
                parametroDeInforme.setIdioma(new Idiomas());
                parametroModificacion = parametroDeInforme;
                listIdiomas.clear();
                getListIdiomas();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("EMPRESA")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.getEmpresa().setNombre(empresa);
                for (int i = 0; i < listEmpresas.size(); i++) {
                    if (listEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setEmpresa(listEmpresas.get(indiceUnicoElemento));
                    parametroModificacion = parametroDeInforme;
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
                parametroDeInforme.setEmpresa(new Empresas());
                parametroModificacion = parametroDeInforme;
                listEmpresas.clear();
                getListEmpresas();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("DESDE")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.setCodigoempleadodesde(emplDesde);
                for (int i = 0; i < listEmpleados.size(); i++) {
                    if (listEmpleados.get(i).getCodigoempleadoSTR().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setCodigoempleadodesde(listEmpleados.get(indiceUnicoElemento).getCodigoempleado());
                    parametroModificacion = parametroDeInforme;
                    listEmpresas.clear();
                    getListEmpresas();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:EmpleadoDesdeDialogo");
                    RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
                }
            } else {
                parametroDeInforme.setCodigoempleadodesde(new BigDecimal("0"));
                parametroModificacion = parametroDeInforme;
                listEmpresas.clear();
                getListEmpresas();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
        if (campoConfirmar.equalsIgnoreCase("HASTA")) {
            if (!valorConfirmar.isEmpty()) {
                parametroDeInforme.setCodigoempleadohasta(emplHasta);
                for (int i = 0; i < listEmpleados.size(); i++) {
                    if (listEmpleados.get(i).getCodigoempleadoSTR().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    parametroDeInforme.setCodigoempleadohasta(listEmpleados.get(indiceUnicoElemento).getCodigoempleado());
                    parametroModificacion = parametroDeInforme;
                    listEmpresas.clear();
                    getListEmpresas();
                    cambiosReporte = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:EmpleadoHastaDialogo");
                    RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
                }
            } else {
                parametroDeInforme.setCodigoempleadohasta(new BigDecimal("9999999999999999999999"));
                parametroModificacion = parametroDeInforme;
                listEmpresas.clear();
                getListEmpresas();
                cambiosReporte = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (casilla == 2) {
            RequestContext.getCurrentInstance().update("form:EmpleadoDesdeDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
            modificarInfoRegistroEmpleadoD(listEmpleados.size());
        }
        if (casilla == 4) {
            RequestContext.getCurrentInstance().update("form:EstadoCivilDialogo");
            RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').show()");
            modificarInfoRegistroEstadoCivil(listEstadosCiviles.size());
        }
        if (casilla == 5) {
            RequestContext.getCurrentInstance().update("form:TipoTelefonoDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTelefonoDialogo').show()");
            modificarInfoRegistroTipoTelefono(listTiposTelefonos.size());
        }
        if (casilla == 6) {
            RequestContext.getCurrentInstance().update("form:JefeDivisionDialogo");
            RequestContext.getCurrentInstance().execute("PF('JefeDivisionDialogo').show()");
            modificarInfoRegistroJefe(listEmpleados.size());
        }
        if (casilla == 9) {
            RequestContext.getCurrentInstance().update("form:EmpleadoHastaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
            modificarInfoRegistroEmpleadoH(listEmpleados.size());
        }
        if (casilla == 10) {
            RequestContext.getCurrentInstance().update("form:EstructuraDialogo");
            RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').show()");
            modificarInfoRegistroEstructura(listEstructuras.size());
        }
        if (casilla == 11) {
            RequestContext.getCurrentInstance().update("form:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
            modificarInfoRegistroTipoTrabajador(listTiposTrabajadores.size());
        }
        if (casilla == 12) {
            RequestContext.getCurrentInstance().update("form:CiudadDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
            modificarInfoRegistroCiudad(listCiudades.size());
        }
        if (casilla == 13) {
            RequestContext.getCurrentInstance().update("form:DeportesDialogo");
            RequestContext.getCurrentInstance().execute("PF('DeportesDialogo').show()");
            modificarInfoRegistroDeporte(listDeportes.size());
        }
        if (casilla == 14) {
            RequestContext.getCurrentInstance().update("form:AficionesDialogo");
            RequestContext.getCurrentInstance().execute("PF('AficionesDialogo').show()");
            modificarInfoRegistroAficion(listAficiones.size());
        }
        if (casilla == 15) {
            RequestContext.getCurrentInstance().update("form:IdiomasDialogo");
            RequestContext.getCurrentInstance().execute("PF('IdiomasDialogo').show()");
            modificarInfoRegistroIdioma(listIdiomas.size());
        }
        if (casilla == 16) {
            RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
            modificarInfoRegistroEmpresa(listEmpresas.size());
        }
    }

    public void listasValores(int pos) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (pos == 2) {
            if((listEmpleados == null) || listEmpleados.isEmpty()){
                listEmpleados = null;
            }
            RequestContext.getCurrentInstance().update("form:EmpleadoDesdeDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
            modificarInfoRegistroEmpleadoD(listEmpleados.size());
        }
        if (pos == 4) {
            RequestContext.getCurrentInstance().update("form:EstadoCivilDialogo");
            RequestContext.getCurrentInstance().execute("PF('EstadoCivilDialogo').show()");
            modificarInfoRegistroEstadoCivil(listEstadosCiviles.size());
        }
        if (pos == 5) {
            RequestContext.getCurrentInstance().update("form:TipoTelefonoDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTelefonoDialogo').show()");
            modificarInfoRegistroTipoTelefono(listTiposTelefonos.size());
        }
        if (pos == 6) {
            RequestContext.getCurrentInstance().update("form:JefeDivisionDialogo");
            RequestContext.getCurrentInstance().execute("PF('JefeDivisionDialogo').show()");
            modificarInfoRegistroJefe(listEmpleados.size());
        }
        if (pos == 9) {
            RequestContext.getCurrentInstance().update("form:EmpleadoHastaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
            modificarInfoRegistroEmpleadoH(listEmpleados.size());
        }
        if (pos == 10) {
            RequestContext.getCurrentInstance().update("form:EstructuraDialogo");
            RequestContext.getCurrentInstance().execute("PF('EstructuraDialogo').show()");
            modificarInfoRegistroEstructura(listEstructuras.size());
        }
        if (pos == 11) {
            RequestContext.getCurrentInstance().update("form:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
            modificarInfoRegistroTipoTrabajador(listTiposTrabajadores.size());
        }
        if (pos == 12) {
            RequestContext.getCurrentInstance().update("form:CiudadDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
            modificarInfoRegistroCiudad(listCiudades.size());
        }
        if (pos == 13) {
            RequestContext.getCurrentInstance().update("form:DeportesDialogo");
            RequestContext.getCurrentInstance().execute("PF('DeportesDialogo').show()");
            modificarInfoRegistroDeporte(listDeportes.size());
        }
        if (pos == 14) {
            RequestContext.getCurrentInstance().update("form:AficionesDialogo");
            RequestContext.getCurrentInstance().execute("PF('AficionesDialogo').show()");
            modificarInfoRegistroAficion(listAficiones.size());
        }
        if (pos == 15) {
            RequestContext.getCurrentInstance().update("form:IdiomasDialogo");
            RequestContext.getCurrentInstance().execute("PF('IdiomasDialogo').show()");
            modificarInfoRegistroIdioma(listIdiomas.size());
        }
        if (pos == 16) {
            RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
            modificarInfoRegistroEmpresa(listEmpresas.size());
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

    public void actualizarEmplDesde() {
        permitirIndex = true;
        parametroDeInforme.setCodigoempleadodesde(empleadoSeleccionado.getCodigoempleado());
        parametroModificacion = parametroDeInforme;
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
        parametroDeInforme.setCodigoempleadohasta(empleadoSeleccionado.getCodigoempleado());
        parametroModificacion = parametroDeInforme;
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
        parametroDeInforme.setEmpresa(empresaSeleccionada);
        parametroModificacion = parametroDeInforme;
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
        parametroDeInforme.setLocalizacion(estructuraSeleccionada);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        /*
         RequestContext.getCurrentInstance().update("form:EstructuraDialogo");
         RequestContext.getCurrentInstance().update("form:lovEstructura");
         RequestContext.getCurrentInstance().update("form:aceptarEST");*/
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
        parametroDeInforme.setTipotrabajador(tipoTSeleccionado);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        /*
         RequestContext.getCurrentInstance().update("form:TipoTrabajadorDialogo");
         RequestContext.getCurrentInstance().update("form:lovTipoTrabajador");
         RequestContext.getCurrentInstance().update("form:aceptarTT");*/
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
        parametroDeInforme.setEstadocivil(estadoCivilSeleccionado);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        /*
         RequestContext.getCurrentInstance().update("form:EstadoCivilDialogo");
         RequestContext.getCurrentInstance().update("form:lovEstadoCivil");
         RequestContext.getCurrentInstance().update("form:aceptarEC");*/
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
        parametroDeInforme.setTipotelefono(tipoTelefonoSeleccionado);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        /*
         RequestContext.getCurrentInstance().update("form:TipoTelefonoDialogo");
         RequestContext.getCurrentInstance().update("form:lovTipoTelefono");
         RequestContext.getCurrentInstance().update("form:aceptarTTEL");*/
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
        parametroDeInforme.setNombregerente(empleadoSeleccionado);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        /*
         RequestContext.getCurrentInstance().update("form:JefeDivisionDialogo");
         RequestContext.getCurrentInstance().update("form:lovJefeD");
         RequestContext.getCurrentInstance().update("form:aceptarJD");*/
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
        parametroDeInforme.setCiudad(ciudadSeleccionada);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        /*
         RequestContext.getCurrentInstance().update("form:CiudadDialogo");
         RequestContext.getCurrentInstance().update("form:lovCiudades");
         RequestContext.getCurrentInstance().update("form:aceptarCiu");*/
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
        parametroDeInforme.setDeporte(deporteSeleccionado);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        /*
         RequestContext.getCurrentInstance().update("form:DeportesDialogo");
         RequestContext.getCurrentInstance().update("form:lovDeportes");
         RequestContext.getCurrentInstance().update("form:aceptarDep");*/
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
        parametroDeInforme.setAficion(aficionSeleccionada);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        /*
         RequestContext.getCurrentInstance().update("form:AficionesDialogo");
         RequestContext.getCurrentInstance().update("form:lovAficiones");
         RequestContext.getCurrentInstance().update("form:aceptarAfi");*/
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
        parametroDeInforme.setIdioma(idiomaSeleccionado);
        parametroModificacion = parametroDeInforme;
        cambiosReporte = false;
        RequestContext context = RequestContext.getCurrentInstance();
        /*
         RequestContext.getCurrentInstance().update("form:IdiomasDialogo");
         RequestContext.getCurrentInstance().update("form:lovIdiomas");
         RequestContext.getCurrentInstance().update("form:aceptarId");*/
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
//                listaIR = administrarNReportePersonal.listInforeportesUsuario();
                modificarInfoRegistroReportes(listaIR.size());
                RequestContext.getCurrentInstance().update("form:ReportesDialogo");
                RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
            }
        } catch (Exception e) {
            System.out.println("Error mostrarDialogoBuscarReporte : " + e.toString());
        }
    }
    
     public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTabla = "110";
            codigoIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
//            tipoIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:tipoIR");
//            tipoIR.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:reportesPersonal");
            bandera = 0;
            filtrarListInforeportesUsuario = null;
            tipoLista = 0;
        }
        listaIR = null;
        parametroDeInforme = null;
        parametroModificacion = null;
        listaIRRespaldo = null;
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
        inforreporteSeleccionado = null;
        reporteLovSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }
    
         public void actualizarSeleccionInforeporte() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (bandera == 1) {
            altoTabla = "110";
            FacesContext c = FacesContext.getCurrentInstance();
            codigoIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
            tipoIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:tipoIR");
            tipoIR.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:reportesPersonal");
            bandera = 0;
            filtrarListInforeportesUsuario = null;
            tipoLista = 0;
        }
        defaultPropiedadesParametrosReporte();
        listaIR = new ArrayList<Inforeportes>();
        listaIR.add(inforreporteSeleccionado);
        filtrarListInforeportesUsuario = null;
        inforreporteSeleccionado = new Inforeportes();
        lovInforeportes.clear();
        getLovInforeportes();
        filtrarLovInforeportes = null;
        aceptar = true;
        actualInfoReporteTabla = listaIR.get(0);
        activoBuscarReporte = true;
        activoMostrarTodos = false;
        modificarInfoRegistroReportes(listaIR.size());
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
        RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
        /*
         RequestContext.getCurrentInstance().update("form:ReportesDialogo");
         RequestContext.getCurrentInstance().update("form:lovReportesDialogo");
         RequestContext.getCurrentInstance().update("form:aceptarR");*/
        context.reset("form:lovReportesDialogo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:reportesPersonal");
    }

    public void cancelarSeleccionInforeporte() {
        filtrarListInforeportesUsuario = null;
        reporteLovSeleccionado = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovReportesDialogo:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovReportesDialogo').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ReportesDialogo').hide()");
    }

    public void mostrarTodos() {
        if (cambiosReporte == true) {
            defaultPropiedadesParametrosReporte();
//            listaIR = null;
            listaIR.clear();
//            getListaIR();
            for(int i = 0;i < lovInforeportes.size();i++){
                listaIR.add(lovInforeportes.get(i));
            }
            modificarInfoRegistroReportes(listaIR.size());
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

    public void cancelarModificaciones() {
        if (bandera == 1) {
            altoTabla = "110";
//            defaultPropiedadesParametrosReporte();
            FacesContext c = FacesContext.getCurrentInstance();
            codigoIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
//            tipoIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:tipoIR");
//            tipoIR.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:reportesPersonal");
            bandera = 0;
            filtrarListInforeportesUsuario = null;
            tipoLista = 0;
        }
        defaultPropiedadesParametrosReporte();
        listaIR = null;
//        listaIRRespaldo = null;
        parametroDeInforme = null;
        parametroModificacion = null;
        casilla = -1;
        listaInfoReportesModificados.clear();
        cambiosReporte = true;
        getParametroDeInforme();
//        refrescarParametros();
        getListaIR();
        modificarInfoRegistroReportes(listaIR.size());
        activoMostrarTodos = true;
        activoBuscarReporte = false;
        inforreporteSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
        RequestContext.getCurrentInstance().update("form:BUSCARREPORTE");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
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

     public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "90";
            codigoIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:codigoIR");
            codigoIR.setFilterStyle("width: 85% !important");
            reporteIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:reporteIR");
            reporteIR.setFilterStyle("width: 85% !important");
//            tipoIR = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:reportesPersonal:tipoIR");
//            tipoIR.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:reportesPersonal");
            tipoLista = 1;
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "110";
            codigoIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:codigoIR");
            codigoIR.setFilterStyle("display: none; visibility: hidden;");
            reporteIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:reporteIR");
            reporteIR.setFilterStyle("display: none; visibility: hidden;");
//            tipoIR = (Column) c.getViewRoot().findComponent("form:reportesPersonal:tipoIR");
//            tipoIR.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:reportesPersonal");
            bandera = 0;
            tipoLista = 0;
            filtrarListInforeportesUsuario = null;
            defaultPropiedadesParametrosReporte();
        }

    }
    
    public void modificacionTipoReporte(Inforeportes reporte) {
        inforreporteSeleccionado = reporte;
        cambiosReporte = false;
            if (listaInfoReportesModificados.isEmpty()) {
                listaInfoReportesModificados.add(inforreporteSeleccionado);
            } else {
                if ((!listaInfoReportesModificados.isEmpty()) && (!listaInfoReportesModificados.contains(inforreporteSeleccionado))) {
                    listaInfoReportesModificados.add(inforreporteSeleccionado);
                } else {
                    int posicion = listaInfoReportesModificados.indexOf(inforreporteSeleccionado);
                    listaInfoReportesModificados.set(posicion, inforreporteSeleccionado);
                }
            }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

//    public void generarReporte() throws IOException {
//        if (cambiosReporte == true) {
//            String nombreReporte, tipoReporte;
//            String pathReporteGenerado = null;
//            if (tipoLista == 0) {
//                nombreReporte = listaIR.get(indice).getNombrereporte();
//                tipoReporte = listaIR.get(indice).getTipo();
//            } else {
//                nombreReporte = filtrarListInforeportesUsuario.get(indice).getNombrereporte();
//                tipoReporte = filtrarListInforeportesUsuario.get(indice).getTipo();
//            }
//            if (nombreReporte != null && tipoReporte != null) {
//                pathReporteGenerado = administarReportes.generarReporte(nombreReporte, tipoReporte);
//            }
//            if (pathReporteGenerado != null) {
//                exportarReporte(pathReporteGenerado);
//            }
//        } else {
//            RequestContext context = RequestContext.getCurrentInstance();
//            RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
//        }
//    }
//    public void exportarReporte(String rutaReporteGenerado) throws IOException {
//        File reporte = new File(rutaReporteGenerado);
//        FacesContext ctx = FacesContext.getCurrentInstance();
//        FileInputStream fis = new FileInputStream(reporte);
//        byte[] bytes = new byte[1024];
//        int read;
//        if (!ctx.getResponseComplete()) {
//            String fileName = reporte.getName();
//            HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
//            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
//            ServletOutputStream out = response.getOutputStream();
//
//            while ((read = fis.read(bytes)) != -1) {
//                out.write(bytes, 0, read);
//            }
//            out.flush();
//            out.close();
//            ctx.responseComplete();
//        }
//    }
    
   

//    public void reporteSeleccionado(int i) {
//        System.out.println("Posicion del reporte : " + i);
//    }

    public void defaultPropiedadesParametrosReporte() {

        color = "black";
        decoracion = "none";
        color2 = "black";
        decoracion2 = "none";
//        RequestContext.getCurrentInstance().update("formParametros");
//
//        empleadoDesdeParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoDesdeParametro");
//        empleadoDesdeParametro.setStyle("position: absolute; top: 33px; left: 110px;height: 10px;width: 70px;");
//        RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
//
//        empleadoHastaParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametro");
//        empleadoHastaParametro.setStyle("position: absolute; top: 33px; left: 270px;height: 10px;width: 90px;");
//        RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
//
//        estructuraParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:estructuraParametro");
//        estructuraParametro.setStyle("position: absolute; top: 10px; left: 510px;height: 10px;width: 280px;");
//        RequestContext.getCurrentInstance().update("formParametros:estructuraParametro");
//
//        tipoTrabajadorParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:tipoTrabajadorParametro");
//        tipoTrabajadorParametro.setStyle("position: absolute; top: 31px; left: 510px;height: 10px;width: 280px;");
//        RequestContext.getCurrentInstance().update("formParametros:tipoTrabajadorParametro");
//
//        estadoCivilParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:estadoCivilParametro");
//        estadoCivilParametro.setStyle("position: absolute; top: 75px; left: 110px;height: 10px;width: 120px;");
//        RequestContext.getCurrentInstance().update("formParametros:estadoCivilParametro");
//
//        tipoTelefonoParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:tipoTelefonoParametro");
//        tipoTelefonoParametro.setStyle("position: absolute; top: 96px; left: 110px;height: 10px;width: 120px;");
//        RequestContext.getCurrentInstance().update("formParametros:tipoTelefonoParametro");
//
//        ciudadParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:ciudadParametro");
//        ciudadParametro.setStyle("position: absolute; top: 52px; left: 510px;height: 10px;width: 280px;");
//        RequestContext.getCurrentInstance().update("formParametros:ciudadParametro");
//
//        deporteParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:deporteParametro");
//        deporteParametro.setStyle("position: absolute; top: 73px; left: 510px;height: 10px;width: 280px;");
//        RequestContext.getCurrentInstance().update("formParametros:deporteParametro");
//
//        aficionParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:aficionParametro");
//        aficionParametro.setStyle("position: absolute; top: 94px; left: 510px;height: 10px;width: 280px");
//        RequestContext.getCurrentInstance().update("formParametros:aficionParametro");
//
//        idiomaParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:idiomaParametro");
//        idiomaParametro.setStyle("position: absolute; top: 115px; left: 510px;height: 10px;width: 280px");
//        RequestContext.getCurrentInstance().update("formParametros:idiomaParametro");

    }

        public void cancelarRequisitosReporte() {
        requisitosReporte = "";
    }
    
    public ParametrosInformes getParametroDeInforme() {
        System.out.println(this.getClass().getName() + ".getParametroDeInforme()");
        try {
            if (parametroDeInforme == null) {
                parametroDeInforme = new ParametrosInformes();
                parametroDeInforme = administrarNReportePersonal.parametrosDeReporte();
            }
            if (parametroDeInforme.getEstadocivil() == null) {
                parametroDeInforme.setEstadocivil(new EstadosCiviles());
            }
            if (parametroDeInforme.getTipotelefono() == null) {
                parametroDeInforme.setTipotelefono(new TiposTelefonos());
            }
            if (parametroDeInforme.getLocalizacion() == null) {
                parametroDeInforme.setLocalizacion(new Estructuras());
            }
            if (parametroDeInforme.getTipotrabajador() == null) {
                parametroDeInforme.setTipotrabajador(new TiposTrabajadores());
            }
            if (parametroDeInforme.getCiudad() == null) {
                parametroDeInforme.setCiudad(new Ciudades());
            }
            if (parametroDeInforme.getDeporte() == null) {
                parametroDeInforme.setDeporte(new Deportes());
            }
            if (parametroDeInforme.getAficion() == null) {
                parametroDeInforme.setAficion(new Aficiones());
            }
            if (parametroDeInforme.getIdioma() == null) {
                parametroDeInforme.setIdioma(new Idiomas());
            }
            if (parametroDeInforme.getEmpresa() == null) {
                parametroDeInforme.setEmpresa(new Empresas());
            }
            return parametroDeInforme;
        } catch (Exception e) {
            System.out.println("Error getParametroDeInforme : " + e.toString());
            return null;
        }
    }    
    
     public void generarDocumentoReporte() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (inforreporteSeleccionado != null) {
            System.out.println("generando reporte - ingreso al if");
            nombreReporte = inforreporteSeleccionado.getNombrereporte();
            tipoReporte = inforreporteSeleccionado.getTipo();

            if (nombreReporte != null && tipoReporte != null) {
                System.out.println("generando reporte - ingreso al 2 if");
                pathReporteGenerado = administarReportes.generarReporte(nombreReporte, tipoReporte);
            }
            if (pathReporteGenerado != null) {
                System.out.println("generando reporte - ingreso al 3 if");
                RequestContext.getCurrentInstance().execute("PF('validarDescargaReporte();");
            } else {
                System.out.println("generando reporte - ingreso al 3 if else");
                RequestContext.getCurrentInstance().execute("PF('generandoReporte.hide();");
                RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
                RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show();");
            }
        } else {
            System.out.println("generando reporte - ingreso al if else");
            System.out.println("Reporte Seleccionado es nulo");
        }
    }

    public void generarReporte(Inforeportes reporte) {
        System.out.println(this.getClass().getName() + ".generarReporte()");
        RequestContext context = RequestContext.getCurrentInstance();
//        seleccionRegistro(reporte);
        seleccionRegistro(reporte);
        guardarCambios();
        RequestContext.getCurrentInstance().execute("PF('generandoReporte').show();");
        RequestContext.getCurrentInstance().execute("PF('generarDocumentoReporte();");
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

    public void generarArchivoReporte(JasperPrint print) {
        System.out.println(this.getClass().getName() + ".generarArchivoReporte()");
        if (print != null && tipoReporte != null) {
            pathReporteGenerado = administarReportes.crearArchivoReporte(print, tipoReporte);
            validarDescargaReporte();
        }
    }

    public void exportarReporte() throws IOException {
        System.out.println(this.getClass().getName() + ".exportarReporte()");
        if (pathReporteGenerado != null) {
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
        }
    }

    public void validarDescargaReporte() {
        System.out.println(this.getClass().getName() + ".validarDescargaReporte()");
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().execute("PF('generandoReporte.hide();");
        if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
            System.out.println("validar descarga reporte - ingreso al if 1");
            if (!tipoReporte.equals("PDF")) {
                System.out.println("validar descarga reporte - ingreso al if 2");
                RequestContext.getCurrentInstance().execute("PF('descargarReporte').show();");
            } else {
                System.out.println("validar descarga reporte - ingreso al if 2 else");
                FileInputStream fis;
                try {
                    fis = new FileInputStream(new File(pathReporteGenerado));
                    reporte = new DefaultStreamedContent(fis, "application/pdf");
                } catch (FileNotFoundException ex) {
                    System.out.println("validar descarga reporte - ingreso al catch 1");
                    System.out.println(ex.getCause());
                    reporte = null;
                }
                if (reporte != null) {
                    System.out.println("validar descarga reporte - ingreso al if 3");
                    if (inforreporteSeleccionado != null) {
                        System.out.println("validar descarga reporte - ingreso al if 4");
                        //cabezeraVisor = "Reporte - " + reporteSeleccionado.getNombre();
                        cabezeraVisor = "Reporte - " + nombreReporte;
                    } else {
                        System.out.println("validar descarga reporte - ingreso al if 4 else ");
                        cabezeraVisor = "Reporte - ";
                    }
                    RequestContext.getCurrentInstance().update("formDialogos:verReportePDF");
                    RequestContext.getCurrentInstance().execute("PF('verReportePDF').show();");
                }
                //pathReporteGenerado = null;
            }
        } else {
            System.out.println("validar descarga reporte - ingreso al if 1 else");
            RequestContext.getCurrentInstance().update("formDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show();");
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

    public void recordarSeleccion() {
        System.out.println(this.getClass().getName() + ".recordarSeleccion()");
        if (inforreporteSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tabla = (DataTable) c.getViewRoot().findComponent("form:reportesPersonal");
            tabla.setSelection(inforreporteSeleccionado);
        }
    }


//    
//    public void resaltoParametrosParaReporte(int i) {
//        Inforeportes reporteS = new Inforeportes();
//        if (tipoLista == 0) {
//            reporteS = listaIR.get(i);
//        }
//        if (tipoLista == 1) {
//            reporteS = filtrarListInforeportesUsuario.get(i);
//        }
//        defaultPropiedadesParametrosReporte();
//        if (reporteS.getFecdesde().equals("SI")) {
//            color = "red";
//            decoracion = "underline";
//            RequestContext.getCurrentInstance().update("formParametros");
//        }
//        if (reporteS.getFechasta().equals("SI")) {
//            color2 = "red";
//            decoracion2 = "underline";
//            RequestContext.getCurrentInstance().update("formParametros");
//        }
//        if (reporteS.getEmdesde().equals("SI")) {
//            empleadoDesdeParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoDesdeParametro");
//            empleadoDesdeParametro.setStyle("position: absolute; top: 33px; left: 110px;height: 10px;width: 70px; text-decoration: underline; color: red;");
//            RequestContext.getCurrentInstance().update("formParametros:empleadoDesdeParametro");
//        }
//        if (reporteS.getEmhasta().equals("SI")) {
//            empleadoHastaParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:empleadoHastaParametro");
//            empleadoHastaParametro.setStyle("position: absolute; top: 33px; left: 270px;height: 10px;width: 90px; text-decoration: underline; color: red;");
//            RequestContext.getCurrentInstance().update("formParametros:empleadoHastaParametro");
//        }
//
//        if (reporteS.getLocalizacion().equals("SI")) {
//            estructuraParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:estructuraParametro");
//            estructuraParametro.setStyle("position: absolute; top: 10px; left: 510px;height: 10px;width: 280px; text-decoration: underline; color: red;");
//            RequestContext.getCurrentInstance().update("formParametros:estructuraParametro");
//        }
//        if (reporteS.getTipotrabajador().equals("SI")) {
//            tipoTrabajadorParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:tipoTrabajadorParametro");
//            tipoTrabajadorParametro.setStyle("position: absolute; top: 31px; left: 510px;height: 10px;width: 280px; text-decoration: underline; color: red;");
//            RequestContext.getCurrentInstance().update("formParametros:tipoTrabajadorParametro");
//        }
//
//        if (reporteS.getEstadocivil().equals("SI")) {
//            estadoCivilParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:estadoCivilParametro");
//            estadoCivilParametro.setStyle("position: absolute; top: 75px; left: 110px;height: 10px;width: 120px; text-decoration: underline; color: red;");
//            RequestContext.getCurrentInstance().update("formParametros:estadoCivilParametro");
//        }
//
//        if (reporteS.getTipotelefono().equals("SI")) {
//            tipoTelefonoParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:tipoTelefonoParametro");
//            tipoTelefonoParametro.setStyle("position: absolute; top: 96px; left: 110px;height: 10px;width: 120px; text-decoration: underline; color: red;");
//            RequestContext.getCurrentInstance().update("formParametros:tipoTelefonoParametro");
//        }
//
//        if (reporteS.getCiudad().equals("SI")) {
//            ciudadParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:ciudadParametro");
//            ciudadParametro.setStyle("position: absolute; top: 52px; left: 510px;height: 10px;width: 280px; text-decoration: underline; color: red;");
//            RequestContext.getCurrentInstance().update("formParametros:ciudadParametro");
//        }
//
//        if (reporteS.getDeporte().equals("SI")) {
//            deporteParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:deporteParametro");
//            deporteParametro.setStyle("position: absolute; top: 73px; left: 510px;height: 10px;width: 280px; text-decoration: underline; color: red;");
//            RequestContext.getCurrentInstance().update("formParametros:deporteParametro");
//        }
//
//        if (reporteS.getAficion().equals("SI")) {
//            aficionParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:aficionParametro");
//            aficionParametro.setStyle("position: absolute; top: 94px; left: 510px;height: 10px;width: 280px; text-decoration: underline; color: red;");
//            RequestContext.getCurrentInstance().update("formParametros:aficionParametro");
//        }
//
//        if (reporteS.getIdioma().equals("SI")) {
//            idiomaParametro = (InputText) FacesContext.getCurrentInstance().getViewRoot().findComponent("formParametros:idiomaParametro");
//            idiomaParametro.setStyle("position: absolute; top: 115px; left: 510px;height: 10px;width: 280px text-decoration: underline; color: red;");
//            RequestContext.getCurrentInstance().update("formParametros:idiomaParametro");
//        }
//
//    }

//    public void parametrosDeReporte(int i) {
//        requisitosReporte = "";
//        Inforeportes reporteS = new Inforeportes();
//        if (tipoLista == 0) {
//            reporteS = listaIR.get(i);
//        }
//        if (tipoLista == 1) {
//            reporteS = filtrarListInforeportesUsuario.get(i);
//        }
//        RequestContext context = RequestContext.getCurrentInstance();
//
//        if (reporteS.getFecdesde().equals("SI")) {
//            requisitosReporte = requisitosReporte + "- Fecha Desde -";
//        }
//        if (reporteS.getFechasta().equals("SI")) {
//            requisitosReporte = requisitosReporte + "- Fecha Hasta -";
//        }
//        if (reporteS.getEmdesde().equals("SI")) {
//            requisitosReporte = requisitosReporte + "- Empleado Desde -";
//        }
//        if (reporteS.getEmhasta().equals("SI")) {
//            requisitosReporte = requisitosReporte + "- Empleado Hasta -";
//        }
//        if (reporteS.getLocalizacion().equals("SI")) {
//            requisitosReporte = requisitosReporte + "- Estructura -";
//        }
//        if (reporteS.getTipotrabajador().equals("SI")) {
//            requisitosReporte = requisitosReporte + "- Tipo Trabajador -";
//        }
//        if (reporteS.getEstadocivil().equals("SI")) {
//            requisitosReporte = requisitosReporte + "- Estado Civil -";
//        }
//        if (reporteS.getTipotelefono().equals("SI")) {
//            requisitosReporte = requisitosReporte + "- Tipo Telefono -";
//        }
//        if (reporteS.getCiudad().equals("SI")) {
//            requisitosReporte = requisitosReporte + "- Ciudad -";
//        }
//        if (reporteS.getDeporte().equals("SI")) {
//            requisitosReporte = requisitosReporte + "- Deporte -";
//        }
//        if (reporteS.getAficion().equals("SI")) {
//            requisitosReporte = requisitosReporte + "- Aficion -";
//        }
//        if (reporteS.getIdioma().equals("SI")) {
//            requisitosReporte = requisitosReporte + "- Idioma -";
//        }
//        if (!requisitosReporte.isEmpty()) {
//            RequestContext.getCurrentInstance().update("formDialogos:requisitosReporte");
//            RequestContext.getCurrentInstance().execute("PF('requisitosReporte').show()");
//        }
//    }

   

        //EVENTO FILTRAR
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        modificarInfoRegistroReportes(filtrarListInforeportesUsuario.size());
    }

    public void eventoFiltrarEmpleadoD() {
        modificarInfoRegistroEmpleadoD(filtrarListEmpleados.size());
    }

    public void eventoFiltrarEmpleadoH() {
        modificarInfoRegistroEmpleadoH(filtrarListEmpleados.size());
    }

    public void eventoFiltrarEmpresa() {
        modificarInfoRegistroEmpresa(filtrarListEmpresas.size());
    }

    public void eventoFiltrarEstructura() {
        modificarInfoRegistroEstructura(filtrarListEstructuras.size());
    }

    public void eventoFiltrarTipoTrabajador() {
        modificarInfoRegistroTipoTrabajador(filtrarListTiposTrabajadores.size());
    }

    public void eventoFiltrarEstadoCivil() {
        modificarInfoRegistroEstadoCivil(filtrarListEstadosCiviles.size());
    }

    public void eventoFiltrarTipoTelefono() {
        modificarInfoRegistroTipoTelefono(filtrarListTiposTelefonos.size());
    }

    public void eventoFiltrarCiudad() {
        modificarInfoRegistroCiudad(filtrarListCiudades.size());
    }

    public void eventoFiltrarDeporte() {
        modificarInfoRegistroDeporte(filtrarListDeportes.size());
    }

    public void eventoFiltrarAficion() {
        modificarInfoRegistroAficion(filtrarListAficiones.size());
    }

    public void eventoFiltrarIdioma() {
        modificarInfoRegistroIdioma(filtrarListIdiomas.size());
    }

    public void eventoFiltrarJefe() {
        modificarInfoRegistroJefe(filtrarListEmpleados.size());
    }

    public void eventoFiltrarLovReportes() {
        modificarInfoRegistroLovReportes(filtrarLovInforeportes.size());
    }

    private void modificarInfoRegistroReportes(int valor) {
        infoRegistro = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    private void modificarInfoRegistroEmpleadoD(int valor) {
        infoRegistroEmpleadoDesde = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroEmpleadoDesde");
    }

    private void modificarInfoRegistroEmpleadoH(int valor) {
        infoRegistroEmpleadoHasta = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroHasta");
    }

    private void modificarInfoRegistroEmpresa(int valor) {
        infoRegistroEmpresa = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroEmpresa");
    }

    private void modificarInfoRegistroEstructura(int valor) {
        infoRegistroEstructura = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroEstructura");
    }

    private void modificarInfoRegistroTipoTrabajador(int valor) {
        infoRegistroTipoTrabajador = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroTipoTrabajador");
    }

    private void modificarInfoRegistroEstadoCivil(int valor) {
        infoRegistroEstadoCivil = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroEstadoCivil");
    }

    private void modificarInfoRegistroTipoTelefono(int valor) {
        infoRegistroTipoTelefono = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroTipoTelefono");
    }

    private void modificarInfoRegistroCiudad(int valor) {
        infoRegistroCiudad = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroCiudad");
    }

    private void modificarInfoRegistroDeporte(int valor) {
        infoRegistroDeporte = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroDeporte");
    }

    private void modificarInfoRegistroAficion(int valor) {
        infoRegistroAficion = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroAficion");
    }

    private void modificarInfoRegistroIdioma(int valor) {
        infoRegistroIdioma = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroIdioma");
    }

    private void modificarInfoRegistroJefe(int valor) {
        infoRegistroJefe = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroJefe");
    }

    private void modificarInfoRegistroLovReportes(int valor) {
        infoRegistroReportes = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroReportes");
    }

    ///////////////////////////////SETS Y GETS/////////////////////////////
    

    public void setParametroDeInforme(ParametrosInformes parametroDeInforme) {
        this.parametroDeInforme = parametroDeInforme;
    }

    public List<Inforeportes> getListaIR() {
        try {
            if (listaIR == null) {
                listaIR = administrarNReportePersonal.listInforeportesUsuario();
            }
//            listaIRRespaldo = listaIR;
            modificarInfoRegistroLovReportes(listaIR.size());
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

    public Inforeportes getInforreporteSeleccionado() {
        return inforreporteSeleccionado;
    }

    public void setInforreporteSeleccionado(Inforeportes inforreporteSeleccionado) {
        this.inforreporteSeleccionado = inforreporteSeleccionado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public ParametrosInformes getNuevoParametroInforme() {
        return nuevoParametroInforme;
    }

    public void setNuevoParametroInforme(ParametrosInformes nuevoParametroInforme) {
        this.nuevoParametroInforme = nuevoParametroInforme;
    }

    public ParametrosInformes getParametroModificacion() {
        return parametroModificacion;
    }

    public void setParametroModificacion(ParametrosInformes parametroModificacion) {
        this.parametroModificacion = parametroModificacion;
    }

    public List<Inforeportes> getListaIRRespaldo() {
        return listaIRRespaldo;
    }

    public void setListaIRRespaldo(List<Inforeportes> listaIRRespaldo) {
        this.listaIRRespaldo = listaIRRespaldo;
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
        if (listEmpleados == null || listEmpleados.isEmpty()) {
            listEmpleados = administrarNReportePersonal.listEmpleados();
        }
        return listEmpleados;
    }

    public void setListEmpleados(List<Empleados> listEmpleados) {
        this.listEmpleados = listEmpleados;
    }

    public List<Empresas> getListEmpresas() {
        if (listEmpresas == null || listEmpresas.isEmpty()) {
            listEmpresas = administrarNReportePersonal.listEmpresas();
        }
        return listEmpresas;
    }

    public void setListEmpresas(List<Empresas> listEmpresas) {
        this.listEmpresas = listEmpresas;
    }

    public List<Estructuras> getListEstructuras() {
        if (listEstructuras == null || listEstructuras.isEmpty()) {
            listEstructuras = administrarNReportePersonal.listEstructuras();
        }
        return listEstructuras;
    }

    public void setListEstructuras(List<Estructuras> listEstructuras) {
        this.listEstructuras = listEstructuras;
    }

    public List<TiposTrabajadores> getListTiposTrabajadores() {
        if (listTiposTrabajadores == null || listTiposTrabajadores.isEmpty()) {
            listTiposTrabajadores = administrarNReportePersonal.listTiposTrabajadores();
        }
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
        if (listEstadosCiviles == null || listEstadosCiviles.isEmpty()) {
            listEstadosCiviles = administrarNReportePersonal.listEstadosCiviles();
        }
        return listEstadosCiviles;
    }

    public void setListEstadosCiviles(List<EstadosCiviles> listEstadosCiviles) {
        this.listEstadosCiviles = listEstadosCiviles;
    }

    public List<TiposTelefonos> getListTiposTelefonos() {
        if (listTiposTelefonos == null || listTiposTelefonos.isEmpty()) {
            listTiposTelefonos = administrarNReportePersonal.listTiposTelefonos();
        }
        return listTiposTelefonos;
    }

    public void setListTiposTelefonos(List<TiposTelefonos> listTiposTelefonos) {
        this.listTiposTelefonos = listTiposTelefonos;
    }

    public List<Ciudades> getListCiudades() {
        if (listCiudades == null || listCiudades.isEmpty()) {
            listCiudades = administrarNReportePersonal.listCiudades();
        }
        return listCiudades;
    }

    public void setListCiudades(List<Ciudades> listCiudades) {
        this.listCiudades = listCiudades;
    }

    public List<Deportes> getListDeportes() {
        if (listDeportes == null || listDeportes.isEmpty()) {
            listDeportes = administrarNReportePersonal.listDeportes();
        }
        return listDeportes;
    }

    public void setListDeportes(List<Deportes> listDeportes) {
        this.listDeportes = listDeportes;
    }

    public List<Aficiones> getListAficiones() {
        if (listAficiones == null || listAficiones.isEmpty()) {
            listAficiones = administrarNReportePersonal.listAficiones();
        }
        return listAficiones;
    }

    public void setListAficiones(List<Aficiones> listAficiones) {
        this.listAficiones = listAficiones;
    }

    public List<Idiomas> getListIdiomas() {
        if (listIdiomas == null || listIdiomas.isEmpty()) {
            listIdiomas = administrarNReportePersonal.listIdiomas();
        }
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

    public Inforeportes getActualInfoReporteTabla() {
        getListaIR();
        if (listaIR != null) {
            int tam = listaIR.size();
            if (tam > 0) {
                actualInfoReporteTabla = listaIR.get(0);
            }
        }
        return actualInfoReporteTabla;
    }

    public void setActualInfoReporteTabla(Inforeportes actualInfoReporteTabla) {
        this.actualInfoReporteTabla = actualInfoReporteTabla;
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

    public String getInfoRegistroEmpleadoDesde() {
        return infoRegistroEmpleadoDesde;
    }

    public void setInfoRegistroEmpleadoDesde(String infoRegistroEmpleadoDesde) {
        this.infoRegistroEmpleadoDesde = infoRegistroEmpleadoDesde;
    }

    public String getInfoRegistroEmpleadoHasta() {
        return infoRegistroEmpleadoHasta;
    }

    public void setInfoRegistroEmpleadoHasta(String infoRegistroEmpleadoHasta) {
        this.infoRegistroEmpleadoHasta = infoRegistroEmpleadoHasta;
    }

    public String getInfoRegistroEmpresa() {
        return infoRegistroEmpresa;
    }

    public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
        this.infoRegistroEmpresa = infoRegistroEmpresa;
    }

    public String getInfoRegistroEstructura() {
        return infoRegistroEstructura;
    }

    public void setInfoRegistroEstructura(String infoRegistroEstructura) {
        this.infoRegistroEstructura = infoRegistroEstructura;
    }

    public String getInfoRegistroTipoTrabajador() {
        return infoRegistroTipoTrabajador;
    }

    public void setInfoRegistroTipoTrabajador(String infoRegistroTipoTrabajador) {
        this.infoRegistroTipoTrabajador = infoRegistroTipoTrabajador;
    }

    public String getInfoRegistroEstadoCivil() {
        return infoRegistroEstadoCivil;
    }

    public void setInfoRegistroEstadoCivil(String infoRegistroEstadoCivil) {
        this.infoRegistroEstadoCivil = infoRegistroEstadoCivil;
    }

    public String getInfoRegistroTipoTelefono() {
        return infoRegistroTipoTelefono;
    }

    public void setInfoRegistroTipoTelefono(String infoRegistroTipoTelefono) {
        this.infoRegistroTipoTelefono = infoRegistroTipoTelefono;
    }

    public String getInfoRegistroCiudad() {
        return infoRegistroCiudad;
    }

    public void setInfoRegistroCiudad(String infoRegistroCiudad) {
        this.infoRegistroCiudad = infoRegistroCiudad;
    }

    public String getInfoRegistroDeporte() {
        return infoRegistroDeporte;
    }

    public void setInfoRegistroDeporte(String infoRegistroDeporte) {
        this.infoRegistroDeporte = infoRegistroDeporte;
    }

    public String getInfoRegistroAficion() {
        return infoRegistroAficion;
    }

    public void setInfoRegistroAficion(String infoRegistroAficion) {
        this.infoRegistroAficion = infoRegistroAficion;
    }

    public String getInfoRegistroIdioma() {
        return infoRegistroIdioma;
    }

    public void setInfoRegistroIdioma(String infoRegistroIdioma) {
        this.infoRegistroIdioma = infoRegistroIdioma;
    }

    public String getInfoRegistroJefe() {
        return infoRegistroJefe;
    }

    public void setInfoRegistroJefe(String infoRegistroJefe) {
        this.infoRegistroJefe = infoRegistroJefe;
    }

    public String getInfoRegistroReportes() {
        return infoRegistroReportes;
    }

    public void setInfoRegistroReportes(String infoRegistroReportes) {
        this.infoRegistroReportes = infoRegistroReportes;
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

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
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

    public Inforeportes getReporteLovSeleccionado() {
        return reporteLovSeleccionado;
    }

    public void setReporteLovSeleccionado(Inforeportes reporteLovSeleccionado) {
        this.reporteLovSeleccionado = reporteLovSeleccionado;
    }

    
    
}
