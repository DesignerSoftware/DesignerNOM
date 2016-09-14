package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.CentrosCostos;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.Organigramas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEstructurasPlantasInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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
 * @author PROYECTO01
 */
@ManagedBean
@SessionScoped
public class ControlEstructuraPlanta implements Serializable {

    @EJB
    AdministrarEstructurasPlantasInterface administrarEstructuraPlanta;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //
    private List<Organigramas> listaOrganigramas;
    private List<Organigramas> filtrarListaOrganigramas;
    private Organigramas organigramaSeleccionado;
    //
    private List<Estructuras> listaEstructuras;
    private List<Estructuras> filtrarListaEstructuras;
    private Estructuras estructuraSeleccionada;
    //
    //Activo/Desactivo Crtl + F11
    private int bandera, banderaEstructura;
    //Columnas Tabla 
    private Column organigramaFecha, organigramaCodigo, organigramaEmpresa, organigramaNIT, organigramaEstado;
    private Column estructuraCodigo, estructuraEstructura, estructuraCantidadControlar, estructuraCantidadActivo, estructuraCentroCosto, estructuraEstructuraPadre;
    //Otros
    private boolean aceptar;
    //Organigramas
    private Organigramas nuevoOrganigrama;
    private Organigramas duplicarOrganigrama;
    private List<Organigramas> listOrganigramasCrear;
    private List<Organigramas> listOrganigramasModificar;
    private List<Organigramas> listOrganigramasBorrar;
    //modificar
    private List<Estructuras> listEstructurasModificar;
    private boolean guardado, guardadoEstructura;
    //crear 
    private Estructuras nuevoEstructura;
    private List<Estructuras> listEstructurasCrear;
    private BigInteger l;
    private int k;
    //borrar 
    private List<Estructuras> listEstructurasBorrar;
    //editar celda
    private Organigramas editarOrganigrama;
    private Estructuras editarEstructura;
    private int cualCelda, tipoLista, cualCeldaEstructura, tipoListaEstructura;
    //duplicar
    private Estructuras duplicarEstructura;
    private String msnConfirmarRastro, msnConfirmarRastroHistorico;
    private String nombreTablaRastro;
    private String nombreXML, nombreTabla;
    private String centroCosto, estructuraPadre;
    ///////////LOV///////////
    private List<CentrosCostos> lovCentrosCostos;
    private List<CentrosCostos> filtrarLovCentrosCostos;
    private CentrosCostos centroCostoSeleccionado;
    private List<Estructuras> lovEstructurasPadres;
    private List<Estructuras> filtrarLovEstructurasPadres;
    private Estructuras estructuraPadreSeleccionado;
    private boolean permitirIndexEstructura;
    private int tipoActualizacion;
    private Long auxCodigoEstructura;
    private String auxNombreEstructura;
    //LOVS
    private List<Empresas> lovEmpresas;
    private List<Empresas> filtradoEmpresas;
    private Empresas empresaSeleccionada;
    private String infoRegistroEmpresa;
    //
    public boolean tablaActiva;
    private boolean cambiosPagina;
    private String altoTablaOrganigrama, altoTablaEstructura;
    //
    private String paginaAnterior;
    //
    private boolean activoEstructura;
    //
    private Date fechaParametro;
    private Date fechaOrganigrama;
    //
    private String infoRegistroOr, infoRegistroEs, infoRegistroCentroCosto, infoRegistroEstructuraPa;
    //
    private DataTable tabla;
    private boolean activarLOV;
    //AUTOCOMPLETAR
    private String auxEmpresa;
    private Short auxCodigo;
    private Date auxFecha;
    private Short codigoEmpresa;
    public String permitirCambioBotonLov;

    public ControlEstructuraPlanta() {
        organigramaSeleccionado = null;
        estructuraSeleccionada = null;
        activoEstructura = true;
        paginaAnterior = "";
        //altos tablas
        altoTablaOrganigrama = "65";
        altoTablaEstructura = "210";
        //Permitir index
        permitirIndexEstructura = true;
        //lovs
        lovCentrosCostos = null;
        centroCostoSeleccionado = new CentrosCostos();
        lovEstructurasPadres = new ArrayList<Estructuras>();
        estructuraPadreSeleccionado = new Estructuras();
        //listas de tablas
        listaOrganigramas = null;
        listaEstructuras = null;

        infoRegistroOr = "";
        infoRegistroEs = "";
        infoRegistroCentroCosto = "";
        infoRegistroEstructuraPa = "";
        //Otros|
        aceptar = true;
        cambiosPagina = true;
        tipoActualizacion = -1;
        k = 0;
        //borrar 
        listEstructurasBorrar = new ArrayList<Estructuras>();
        //crear 
        listEstructurasCrear = new ArrayList<Estructuras>();
        //modificar 
        listEstructurasModificar = new ArrayList<Estructuras>();
        listOrganigramasModificar = new ArrayList<Organigramas>();
        listOrganigramasCrear = new ArrayList<Organigramas>();
        listOrganigramasBorrar = new ArrayList<Organigramas>();
        //editar
        editarOrganigrama = new Organigramas();
        editarEstructura = new Estructuras();
        //Cual Celda
        cualCelda = -1;
        cualCeldaEstructura = -1;
        //Tipo Lista
        tipoListaEstructura = 0;
        tipoLista = 0;
        //guardar
        guardado = true;
        guardadoEstructura = true;
        //Crear 
        nuevoEstructura = new Estructuras();
        nuevoEstructura.setCentrocosto(new CentrosCostos());
        nuevoEstructura.setEstructurapadre(new Estructuras());

        //Duplicar
        duplicarEstructura = new Estructuras();

        //Banderas
        bandera = 0;
        banderaEstructura = 0;

        activarLOV = true;
        auxCodigo = new Short("0");
        codigoEmpresa = new Short("0");
        auxEmpresa = "";
        auxFecha = new Date();
        auxCodigoEstructura = new Long("0000");
        auxNombreEstructura = "";
        permitirCambioBotonLov = "SIapagarCelda";

        nuevoOrganigrama = new Organigramas();
        nuevoOrganigrama.setEmpresa(new Empresas());
        nuevoOrganigrama.setEstado("A");

        duplicarOrganigrama = new Organigramas();
        duplicarOrganigrama.setEmpresa(new Empresas());
        duplicarOrganigrama.setEstado("A");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarEstructuraPlanta.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public String valorPaginaAnterior() {
        return paginaAnterior;
    }

    public void inicializarPagina(String paginaLlamado) {
        paginaAnterior = paginaLlamado;
        getListaOrganigramas();
        contarRegistrosOr();
        RequestContext.getCurrentInstance().update("form:infoRegistroOrg");
        if (listaOrganigramas != null) {
            if (!listaOrganigramas.isEmpty()) {
                organigramaSeleccionado = listaOrganigramas.get(0);
                getListaEstructuras();
                contarRegistrosEs();
            }
        } else {
            System.out.println("El getListaOrganigramas() no trajo datos");
        }
    }

    public boolean validarFechasRegistro() {
        boolean retorno = true;
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);

        if (organigramaSeleccionado.getFecha() != null) {
            if (organigramaSeleccionado.getFecha().after(fechaParametro)) {
                retorno = true;
            } else {
                retorno = false;
            }
        } else {
            retorno = false;
        }

        return retorno;
    }

    public void modificacionesFechas(Organigramas organigramas, int c) {
        organigramaSeleccionado = organigramas;
        boolean variable = validarFechasRegistro();
        if (variable == true) {
            cambiarIndice(organigramas, c);
            modificarOrganigrama(organigramas);
        } else {
            organigramaSeleccionado.setFecha(fechaOrganigrama);

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosOrganigramas");
            PrimefacesContextUI.ejecutar("PF('errorFecha').show()");
        }
    }

    public boolean validarCamposNulosEstructura(int i) {
        boolean retorno = true;
        if (i == 0) {
            if (estructuraSeleccionada.getCodigo() == null) {
                retorno = false;
            }
            if (estructuraSeleccionada.getNombre() == null) {
                retorno = false;
            } else if (estructuraSeleccionada.getNombre().isEmpty()) {
                retorno = false;
            }
        }
        if (i == 1) {
            if (nuevoEstructura.getCodigo() == null) {
                retorno = false;
            }
            if (nuevoEstructura.getNombre() == null) {
                retorno = false;
            } else if (nuevoEstructura.getNombre().isEmpty()) {
                retorno = false;
            }
        }
        if (i == 2) {

            if (duplicarEstructura.getCodigo() == null) {
                retorno = false;
            }
            if (duplicarEstructura.getNombre() == null) {
                retorno = false;
            } else if (duplicarEstructura.getNombre().isEmpty()) {
                retorno = false;
            }
        }
        return retorno;
    }

    //AUTOCOMPLETAR
    public void modificarOrganigrama(Organigramas organigrama, String column, Object valor) {
        organigramaSeleccionado = organigrama;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (column.equalsIgnoreCase("N")) {
            if (!listOrganigramasCrear.contains(organigramaSeleccionado)) {
                if (listOrganigramasModificar.isEmpty()) {
                    listOrganigramasModificar.add(organigramaSeleccionado);
                } else if (!listOrganigramasModificar.contains(organigramaSeleccionado)) {
                    listOrganigramasModificar.add(organigramaSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
            RequestContext.getCurrentInstance().update("form:datosOrganigramas");
        } else if (column.equalsIgnoreCase("EMPRESA")) {
            activarBotonLOV();
            organigramaSeleccionado.getEmpresa().setNombre(auxEmpresa);
            for (int i = 0; i < lovEmpresas.size(); i++) {
                String empresa = (String) valor;
                if (lovEmpresas.get(i).getNombre().startsWith(empresa.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                organigramaSeleccionado.setEmpresa(lovEmpresas.get(indiceUnicoElemento));
                lovEmpresas.clear();
                getLovEmpresas();
            } else {
                contarRegistrosLovEmp(0);
                RequestContext.getCurrentInstance().update("form:EmpresasDialogo");
                PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (column.equalsIgnoreCase("COD")) {
            organigramaSeleccionado.setCodigo(auxCodigo);
            Short cod = (Short) valor;
            System.out.println(" modificar cod = " + cod);
            for (int i = 0; i < listaOrganigramas.size(); i++) {
                if (listaOrganigramas.get(i).getCodigo().equals(cod)) {
                    System.out.println(" modificar codigo 1 igual");
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias > 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacionCodigoRepetido");
                PrimefacesContextUI.ejecutar("PF('validacionCodigoRepetido').show()");
                tipoActualizacion = 0;
            } else {
                organigramaSeleccionado.setCodigo(cod);
                coincidencias = 1;
            }
        } else if (column.equalsIgnoreCase("F")) {
            organigramaSeleccionado.setFecha(auxFecha);
            Date fecha = (Date) valor;
            System.out.println(" modificar fecha = " + fecha);
            for (int i = 0; i < listaOrganigramas.size(); i++) {
                if (listaOrganigramas.get(i).getFecha().equals(fecha)
                        && listaOrganigramas.get(i).getEmpresa().equals(organigramaSeleccionado.getEmpresa())) {
                    System.out.println(" modificar fecha 1 igual para Empresa");
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias > 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacionFechaRepetida");
                PrimefacesContextUI.ejecutar("PF('validacionFechaRepetida').show()");
                tipoActualizacion = 0;
            } else {
                organigramaSeleccionado.setFecha(fecha);
                coincidencias = 1;
            }
        }
        if (coincidencias == 1) {
            if (!listOrganigramasCrear.contains(organigramaSeleccionado)) {
                if (listOrganigramasModificar.isEmpty()) {
                    listOrganigramasModificar.add(organigramaSeleccionado);
                } else if (!listOrganigramasModificar.contains(organigramaSeleccionado)) {
                    listOrganigramasModificar.add(organigramaSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        }
        RequestContext.getCurrentInstance().update("form:datosOrganigramas");
    }

    public void modificarOrganigrama(Organigramas organigrama) {
        listOrganigramasModificar.add(organigramaSeleccionado);

        if (!listOrganigramasCrear.contains(organigramaSeleccionado)) {
            if (listOrganigramasModificar.isEmpty()) {
                listOrganigramasModificar.add(organigramaSeleccionado);
            } else if (!listOrganigramasModificar.contains(organigramaSeleccionado)) {
                listOrganigramasModificar.add(organigramaSeleccionado);
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }

        cambiosPagina = false;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public boolean validarCodigoNoExistente(int i) {
        boolean retorno = true;
        int conteo = 0;
        if (i == 0) {
            Estructuras temporal = null;
            temporal = estructuraSeleccionada;

            for (int j = 0; j < listaEstructuras.size(); j++) {
                if (listaEstructuras.get(j).getCodigo() == temporal.getCodigo()) {
                    conteo++;
                }
            }
            if (conteo > 1) {
                retorno = false;
            }
        }
        if (i == 1) {
            for (int j = 0; j < listaEstructuras.size(); j++) {
                if (listaEstructuras.get(j).getCodigo() == nuevoEstructura.getCodigo()) {
                    conteo++;
                }
            }
            if (conteo > 1) {
                retorno = false;
            }
        }
        if (i == 2) {
            for (int j = 0; j < listaEstructuras.size(); j++) {
                if (listaEstructuras.get(j).getCodigo() == duplicarEstructura.getCodigo()) {
                    conteo++;
                }
            }
            if (conteo > 1) {
                retorno = false;
            }
        }
        return retorno;
    }

    public void procesoModificacionEstructura(Estructuras estructura) {
        estructuraSeleccionada = estructura;
        boolean respuesta = validarCamposNulosEstructura(0);
        if (respuesta == true) {
            if (validarCodigoNoExistente(0) == true) {
                modificarEstructura();
            } else {
                estructuraSeleccionada.setCodigo(auxCodigoEstructura);

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:datosEstructura");
                PrimefacesContextUI.ejecutar("PF('errorCodigo').show()");
            }
        } else {
            estructuraSeleccionada.setCodigo(auxCodigoEstructura);
            estructuraSeleccionada.setNombre(auxNombreEstructura);

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosEstructura");
            PrimefacesContextUI.ejecutar("PF('errorDatosNullEstructura').show()");
        }
    }

    public void modificarEstructura() {

        if (estructuraSeleccionada.getNombre().length() >= 1 && estructuraSeleccionada.getNombre().length() <= 50) {
            if (!listEstructurasCrear.contains(estructuraSeleccionada)) {
                if (listEstructurasModificar.isEmpty()) {
                    listEstructurasModificar.add(estructuraSeleccionada);
                } else if (!listEstructurasModificar.contains(estructuraSeleccionada)) {
                    listEstructurasModificar.add(estructuraSeleccionada);
                }
                if (guardadoEstructura == true) {
                    guardadoEstructura = false;
                }
            }

            estructuraSeleccionada = null;
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosEstructura");
        } else {
            estructuraSeleccionada.setNombre(auxNombreEstructura);

            estructuraSeleccionada = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosEstructura");
        }
    }

    public void modificarEstructura(Estructuras estructura, String confirmarCambio, String valorConfirmar) {
        estructuraSeleccionada = estructura;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("PADRE")) {
            estructuraSeleccionada.getEstructurapadre().setNombre(estructuraPadre);

            for (int i = 0; i < lovEstructurasPadres.size(); i++) {
                if (lovEstructurasPadres.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                estructuraSeleccionada.setEstructurapadre(lovEstructurasPadres.get(indiceUnicoElemento));

                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndexEstructura = false;
                RequestContext.getCurrentInstance().update("form:EstructuraPadreDialogo");
                PrimefacesContextUI.ejecutar("PF('EstructuraPadreDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (confirmarCambio.equalsIgnoreCase("CENTROCOSTO")) {
            estructuraSeleccionada.getCentrocosto().setCodigoNombre(centroCosto);

            for (int i = 0; i < lovCentrosCostos.size(); i++) {
                if (lovCentrosCostos.get(i).getCodigoNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                estructuraSeleccionada.setCentrocosto(lovCentrosCostos.get(indiceUnicoElemento));

                cambiosPagina = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndexEstructura = false;
                RequestContext.getCurrentInstance().update("form:CentroCostoDialogo");
                PrimefacesContextUI.ejecutar("PF('CentroCostoDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (!listEstructurasCrear.contains(estructuraSeleccionada)) {
                if (listEstructurasModificar.isEmpty()) {
                    listEstructurasModificar.add(estructuraSeleccionada);
                } else if (!listEstructurasModificar.contains(estructuraSeleccionada)) {
                    listEstructurasModificar.add(estructuraSeleccionada);
                }
                if (guardadoEstructura == true) {
                    guardadoEstructura = false;
                }
            }

        }
        RequestContext.getCurrentInstance().update("form:datosEstructura");
    }

    public void posicionOrganigrama() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        Organigramas organigrama = listaOrganigramas.get(indice);
        cambiarIndice(organigrama, columna);
    }

    public void posicionEstructura() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        estructuraSeleccionada = listaEstructuras.get(indice);
        cambiarIndiceEstructura(estructuraSeleccionada, columna);
    }

    public void cambiarIndice(Organigramas organigrama, int celda) {
        if (guardadoEstructura == true) {
            RequestContext context = RequestContext.getCurrentInstance();
            organigramaSeleccionado = organigrama;
            cualCelda = celda;
            cualCeldaEstructura = -1;
            estructuraSeleccionada = null;
            if (cualCelda == 2) {
                activarBotonLOV();
                permitirCambioBotonLov = "NOapagarCelda";
            } else {
                permitirCambioBotonLov = "SoloHacerNull";
            }
            if (cualCelda == 0) {
                auxFecha = organigramaSeleccionado.getFecha();
            }
            auxEmpresa = organigramaSeleccionado.getEmpresa().getNombre();
            auxCodigo = organigramaSeleccionado.getCodigo();
            fechaOrganigrama = organigramaSeleccionado.getFecha();
            codigoEmpresa = organigramaSeleccionado.getEmpresa().getCodigo();

            listaEstructuras = null;
            getListaEstructuras();
            contarRegistrosEs();
            RequestContext.getCurrentInstance().update("form:infoRegistroEst");
            RequestContext.getCurrentInstance().update("form:datosEstructura");
            if (banderaEstructura == 1) {
                restaurarTablaEstr();
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }
    }

    public void cambiarIndiceDefault() {
        System.out.println("cambiarIndiceDefault()");
        if (permitirCambioBotonLov.equals("SoloHacerNull")) {
            anularBotonLOV();
        } else if (permitirCambioBotonLov.equals("SIapagarCelda")) {
            anularBotonLOV();
            cualCelda = -1;
        } else if (permitirCambioBotonLov.equals("NOapagarCelda")) {
            activarBotonLOV();
        }
        permitirCambioBotonLov = "SIapagarCelda";
    }

    public void cambiarIndiceEstructura(Estructuras estructuras, int celda) {
        if (permitirIndexEstructura == true) {
            estructuraSeleccionada = estructuras;
            cualCeldaEstructura = celda;
            estructuraPadre = estructuraSeleccionada.getEstructurapadre().getNombre();
            centroCosto = estructuraSeleccionada.getCentrocosto().getCodigoNombre();
            auxCodigoEstructura = estructuraSeleccionada.getCodigo();
            auxNombreEstructura = estructuraSeleccionada.getNombre();
        }
        if (cualCeldaEstructura == 4 || cualCeldaEstructura == 5) {
            activarLOV = false;
        } else {
            activarLOV = true;
        }
        RequestContext.getCurrentInstance().update("form:listaValores");
    }
    //GUARDAR

    public void guardarYSalir() {
        guardarGeneral();
        salir();
    }

    public void cancelarYSalir() {

        cancelarModificacionGeneral();
        salir();
    }

    public void guardarGeneral() {
        if (guardado == false || guardadoEstructura == false) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (guardado == false) {
                guardarCambiosOrganigrama();
            }
            if (guardadoEstructura == false) {
                guardarCambiosEstructura();
            }
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            cambiosPagina = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void guardarCambiosOrganigrama() {
        if (!listOrganigramasModificar.isEmpty()) {
            administrarEstructuraPlanta.modificarOrganigramas(listOrganigramasModificar);
            listOrganigramasModificar.clear();
        }
        listaOrganigramas = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosOrganigramas");
        guardado = true;
        RequestContext.getCurrentInstance().update("form:aceptar");
        k = 0;
        organigramaSeleccionado = null;
    }

    public void guardarCambiosEstructura() {
        if (!listEstructurasBorrar.isEmpty()) {
            administrarEstructuraPlanta.borrarEstructura(listEstructurasBorrar);
            listEstructurasBorrar.clear();
        }
        if (!listEstructurasCrear.isEmpty()) {
            administrarEstructuraPlanta.crearEstructura(listEstructurasCrear);
            listEstructurasCrear.clear();
        }
        if (!listEstructurasModificar.isEmpty()) {
            administrarEstructuraPlanta.editarEstructura(listEstructurasModificar);
            listEstructurasModificar.clear();
        }
        listaEstructuras = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosEstructura");
        guardadoEstructura = true;
        RequestContext.getCurrentInstance().update("form:aceptar");
        k = 0;
        estructuraSeleccionada = null;
    }

    //CANCELAR MODIFICACIONES
    /**
     * Cancela las modificaciones realizas en la pagina
     */
    public void cancelarModificacionGeneral() {
        cambiosPagina = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        if (guardado == false) {
            cancelarModificacionOrganigrama();
            contarRegistrosOr();
            RequestContext.getCurrentInstance().update("form:infoRegistroOrg");
            RequestContext.getCurrentInstance().update("form:datosOrganigramas");
        }
        if (guardadoEstructura == false) {
            cancelarModificacionEstructura();
            RequestContext.getCurrentInstance().update("form:infoRegistroEst");
            RequestContext.getCurrentInstance().update("form:datosEstructura");
        }
        contarRegistrosEs();
        activarLOV = true;
        estructuraSeleccionada = null;
        organigramaSeleccionado = null;
        listaEstructuras = null;
        contarRegistrosEs();
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void cancelarModificacionOrganigrama() {
        if (bandera == 1) {
            restaurarTabla();
        }
        listOrganigramasBorrar.clear();
        listOrganigramasCrear.clear();
        listOrganigramasModificar.clear();
        organigramaSeleccionado = null;
        k = 0;
        listaOrganigramas = null;
        getListaOrganigramas();
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosOrganigramas");
    }

    public void cancelarModificacionEstructura() {
        if (banderaEstructura == 1) {
            restaurarTablaEstr();
        }
        listEstructurasBorrar.clear();
        listEstructurasCrear.clear();
        listEstructurasModificar.clear();
        k = 0;
        listaEstructuras = null;
        guardadoEstructura = true;
        permitirIndexEstructura = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosEstructura");
    }

    public void restaurarTabla() {
        altoTablaOrganigrama = "65";
        organigramaFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOrganigramas:organigramaFecha");
        organigramaFecha.setFilterStyle("display: none; visibility: hidden;");
        organigramaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOrganigramas:organigramaCodigo");
        organigramaCodigo.setFilterStyle("display: none; visibility: hidden;");
        organigramaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOrganigramas:organigramaEmpresa");
        organigramaEmpresa.setFilterStyle("display: none; visibility: hidden;");
        organigramaNIT = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOrganigramas:organigramaNIT");
        organigramaNIT.setFilterStyle("display: none; visibility: hidden;");
        organigramaEstado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOrganigramas:organigramaEstado");
        organigramaEstado.setFilterStyle("display: none; visibility: hidden;");
        RequestContext.getCurrentInstance().update("form:datosOrganigramas");
        bandera = 0;
        filtrarListaOrganigramas = null;
        tipoLista = 0;
    }

    public void restaurarTablaEstr() {
        altoTablaEstructura = "210";
        estructuraEstructura = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEstructura:estructuraEstructura");
        estructuraEstructura.setFilterStyle("display: none; visibility: hidden;");
        estructuraCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEstructura:estructuraCodigo");
        estructuraCodigo.setFilterStyle("display: none; visibility: hidden;");
        estructuraCantidadControlar = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEstructura:estructuraCantidadControlar");
        estructuraCantidadControlar.setFilterStyle("display: none; visibility: hidden;");
        estructuraCantidadActivo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEstructura:estructuraCantidadActivo");
        estructuraCantidadActivo.setFilterStyle("display: none; visibility: hidden;");
        estructuraCentroCosto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEstructura:estructuraCentroCosto");
        estructuraCentroCosto.setFilterStyle("display: none; visibility: hidden;");
        estructuraEstructuraPadre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEstructura:estructuraEstructuraPadre");
        estructuraEstructuraPadre.setFilterStyle("display: none; visibility: hidden;");
        RequestContext.getCurrentInstance().update("form:datosEstructura");
        banderaEstructura = 0;
        filtrarListaEstructuras = null;
        tipoListaEstructura = 0;
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (estructuraSeleccionada != null) {
            editarEstructura = estructuraSeleccionada;

            if (cualCeldaEstructura == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoEstructuraD");
                PrimefacesContextUI.ejecutar("PF('editarCodigoEstructuraD').show()");
                cualCeldaEstructura = -1;
            } else if (cualCeldaEstructura == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEstructuraEstructuraD");
                PrimefacesContextUI.ejecutar("PF('editarEstructuraEstructuraD').show()");
                cualCeldaEstructura = -1;
            } else if (cualCeldaEstructura == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCantidadControlarEstructuraD");
                PrimefacesContextUI.ejecutar("PF('editarCantidadControlarEstructuraD').show()");
                cualCeldaEstructura = -1;
            } else if (cualCeldaEstructura == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCantidadActivosEstructuraD");
                PrimefacesContextUI.ejecutar("PF('editarCantidadActivosEstructuraD').show()");
                cualCeldaEstructura = -1;
            } else if (cualCeldaEstructura == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCentroCostoEstructuraD");
                PrimefacesContextUI.ejecutar("PF('editarCentroCostoEstructuraD').show()");
                cualCeldaEstructura = -1;
            } else if (cualCeldaEstructura == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEstructuraPadreEstructuraD");
                PrimefacesContextUI.ejecutar("PF('editarEstructuraPadreEstructuraD').show()");
                cualCeldaEstructura = -1;
            }
        } else if (organigramaSeleccionado != null) {
            editarOrganigrama = organigramaSeleccionado;

            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaOrganigramaD");
                PrimefacesContextUI.ejecutar("PF('editarFechaOrganigramaD').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoOrganigramaD");
                PrimefacesContextUI.ejecutar("PF('editarCodigoOrganigramaD').show()");
                cualCelda = -1;

            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresaOrganigramaD");
                PrimefacesContextUI.ejecutar("PF('editarEmpresaOrganigramaD').show()");
                cualCelda = -1;

            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNITOrganigramaD");
                PrimefacesContextUI.ejecutar("PF('editarNITOrganigramaD').show()");
                cualCelda = -1;

            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEstadoOrganigramaD");
                PrimefacesContextUI.ejecutar("PF('editarEstadoOrganigramaD').show()");
                cualCelda = -1;

            }
        } else {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        }
    }

    public void dialogoNuevoRegistro() {
        RequestContext context = RequestContext.getCurrentInstance();
        activoEstructura = false;
        codigoNuevoEstructura();
        lovEstructurasPadres = null;
        lovEstructurasPadres = administrarEstructuraPlanta.lovEstructuras();
        RequestContext.getCurrentInstance().update("formularioDialogos:DialogNuevaEstructura");
        PrimefacesContextUI.ejecutar("PF('DialogNuevaEstructura').show()");
    }

    public void codigoNuevoEstructura() {
        String code = "";
        int tam = listaEstructuras.size();
        if (tam > 0) {
            int newCode = listaEstructuras.get(tam - 1).getCodigo().intValue() + 1;
            code = String.valueOf(newCode);
        } else {
            code = "1";
        }
        nuevoEstructura.setCodigo(new Long(code));
    }

//CREAR 
    public void agregarNuevoEstructura() {
        boolean respueta = validarCamposNulosEstructura(1);
        if (respueta == true) {
            if (validarCodigoNoExistente(1) == true) {
                if (banderaEstructura == 1) {
                    restaurarTablaEstr();
                }
                k++;
                l = BigInteger.valueOf(k);
                nuevoEstructura.setSecuencia(l);
                nuevoEstructura.setOrganigrama(organigramaSeleccionado);

                if (listaEstructuras == null) {
                    listaEstructuras = new ArrayList<Estructuras>();
                }
                listEstructurasCrear.add(nuevoEstructura);
                listaEstructuras.add(nuevoEstructura);
                modificarInfoRegistroEs(listaEstructuras.size());
                RequestContext.getCurrentInstance().update("form:infoRegistroEst");
                estructuraSeleccionada = listaEstructuras.get(listaEstructuras.indexOf(nuevoEstructura));
                cambiosPagina = false;
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:datosEstructura");
                PrimefacesContextUI.ejecutar("PF('DialogNuevaEstructura').hide()");
                nuevoEstructura = new Estructuras();
                nuevoEstructura.setCentrocosto(new CentrosCostos());
                nuevoEstructura.setEstructurapadre(new Estructuras());
                activarLOV = true;
                RequestContext.getCurrentInstance().update("form:listaValores");
                if (guardadoEstructura == true) {
                    guardadoEstructura = false;
                    RequestContext.getCurrentInstance().update("form:aceptar");
                }
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.ejecutar("PF('errorCodigo').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('errorDatosNullEstructura').show()");
        }
    }

    //LIMPIAR NUEVO REGISTRO
    public void limpiarNuevaEstructura() {
        nuevoEstructura = new Estructuras();
        nuevoEstructura.setCentrocosto(new CentrosCostos());
        nuevoEstructura.setEstructurapadre(new Estructuras());
    }

    //DUPLICAR VC
    /**
     */
    public void verificarRegistroDuplicar() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (estructuraSeleccionada != null) {
            duplicarEstructuraM();
        } else if (organigramaSeleccionado != null) {
            duplicarOrg();
        } else {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        }
    }

    public void duplicarEstructuraM() {
        RequestContext context = RequestContext.getCurrentInstance();
        duplicarEstructura = new Estructuras();
        duplicarEstructura.setCodigo(estructuraSeleccionada.getCodigo());
        duplicarEstructura.setNombre(estructuraSeleccionada.getNombre());
        duplicarEstructura.setCentrocosto(estructuraSeleccionada.getCentrocosto());
        duplicarEstructura.setEstructurapadre(estructuraSeleccionada.getEstructurapadre());
        duplicarEstructura.setCantidadCargosControlar(estructuraSeleccionada.getCantidadCargosControlar());
        duplicarEstructura.setCantidadCargosEmplActivos(estructuraSeleccionada.getCantidadCargosEmplActivos());
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        RequestContext.getCurrentInstance().update("formularioDialogos:DialogDuplicarEstructura");
        PrimefacesContextUI.ejecutar("PF('DialogDuplicarEstructura').show()");
    }

    public void confirmarDuplicarEstructura() {
        boolean respueta = validarCamposNulosEstructura(2);
        if (respueta == true) {
            if (validarCodigoNoExistente(2) == true) {
                if (banderaEstructura == 1) {
                    restaurarTablaEstr();
                }
                k++;
                l = BigInteger.valueOf(k);
                duplicarEstructura.setSecuencia(l);
                duplicarEstructura.setOrganigrama(organigramaSeleccionado);

                listaEstructuras.add(duplicarEstructura);
                listEstructurasCrear.add(duplicarEstructura);
                estructuraSeleccionada = listaEstructuras.get(listaEstructuras.indexOf(nuevoEstructura));
                modificarInfoRegistroEs(listaEstructuras.size());
                RequestContext.getCurrentInstance().update("form:infoRegistroEst");
                cambiosPagina = false;
                RequestContext context = RequestContext.getCurrentInstance();
                activarLOV = true;
                RequestContext.getCurrentInstance().update("form:listaValores");
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:datosEstructura");
                PrimefacesContextUI.ejecutar("PF('DialogDuplicarEstructura').hide()");
                estructuraSeleccionada = null;
                if (guardadoEstructura == true) {
                    guardadoEstructura = false;
                    //RequestContext.getCurrentInstance().update("form:aceptar");
                }

                duplicarEstructura = new Estructuras();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.ejecutar("PF('errorCodigo').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.ejecutar("PF('errorDatosNullEstructura').show()");
        }
    }

    public void limpiarDuplicarEstructura() {
        duplicarEstructura = new Estructuras();
        duplicarEstructura.setEstructurapadre(new Estructuras());
        duplicarEstructura.setCentrocosto(new CentrosCostos());
    }

    public void limpiarMSNRastros() {
        msnConfirmarRastro = "";
        msnConfirmarRastroHistorico = "";
        nombreTablaRastro = "";
    }

    public void borrarEstructura() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listEstructurasModificar.isEmpty() && listEstructurasModificar.contains(estructuraSeleccionada)) {
            int modIndex = listEstructurasModificar.indexOf(estructuraSeleccionada);
            listEstructurasModificar.remove(modIndex);
            listEstructurasBorrar.add(estructuraSeleccionada);
        } else if (!listEstructurasCrear.isEmpty() && listEstructurasCrear.contains(estructuraSeleccionada)) {
            int crearIndex = listEstructurasCrear.indexOf(estructuraSeleccionada);
            listEstructurasCrear.remove(crearIndex);
        } else {
            listEstructurasBorrar.add(estructuraSeleccionada);
        }
        listaEstructuras.remove(estructuraSeleccionada);
        if (tipoListaEstructura == 1) {
            filtrarListaEstructuras.remove(estructuraSeleccionada);
        }
        modificarInfoRegistroEs(listaEstructuras.size());
        RequestContext.getCurrentInstance().update("form:infoRegistroEst");
        cambiosPagina = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosEstructura");
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        estructuraSeleccionada = null;

        if (guardadoEstructura == true) {
            guardadoEstructura = false;
            //RequestContext.getCurrentInstance().update("form:aceptar");
        }
    }
    //CREAR ORGANIGRAMA

    public void agregarNuevoOrganigrama() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoOrganigrama.getEmpresa().getSecuencia() == null || nuevoOrganigrama.getCodigo() == null) {
            RequestContext.getCurrentInstance().update("form:validacioNuevoOrganigrama");
            PrimefacesContextUI.ejecutar("PF('validacioNuevoOrganigrama').show()");
        } else {
            boolean continuar = true;
            for (int i = 0; i < listaOrganigramas.size(); i++) {
                if (listaOrganigramas.get(i).getCodigo().equals(nuevoOrganigrama.getCodigo())) {
                    System.out.println(" nuevo codigo 1 igual");
                    RequestContext.getCurrentInstance().update("formularioDialogos:validacionCodigoRepetido");
                    PrimefacesContextUI.ejecutar("PF('validacionCodigoRepetido').show()");
                    continuar = false;
                    break;
                } else if (listaOrganigramas.get(i).getFecha().equals(nuevoOrganigrama.getFecha())
                        && listaOrganigramas.get(i).getEmpresa().equals(nuevoOrganigrama.getEmpresa())) {
                    System.out.println(" nuevo fecha 1 igual para Empresa");
                    RequestContext.getCurrentInstance().update("formularioDialogos:validacionFechaRepetida");
                    PrimefacesContextUI.ejecutar("PF('validacionFechaRepetida').show()");
                    continuar = false;
                    break;
                }
            }
            if (continuar) {
                //AGREGAR REGISTRO A LA LISTA ORGANIGRAMAS.
                k++;
                l = BigInteger.valueOf(k);
                nuevoOrganigrama.setSecuencia(l);
                listOrganigramasCrear.add(nuevoOrganigrama);
                listaOrganigramas.add(nuevoOrganigrama);
                organigramaSeleccionado = listaOrganigramas.get(listaOrganigramas.indexOf(nuevoOrganigrama));
                contarRegistrosOr();
                anularBotonLOV();
                nuevoOrganigrama = new Organigramas();
                nuevoOrganigrama.setEmpresa(new Empresas());
                nuevoOrganigrama.setEstado("A");
                if (bandera == 1) {
                    restaurarTabla();
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                RequestContext.getCurrentInstance().update("form:datosOrganigramas");
                PrimefacesContextUI.ejecutar("PF('NuevoRegistroOrganigramas').hide()");
                RequestContext.getCurrentInstance().update("form:arbolEstructuras");
            }
        }
    }

    //LIMPIAR NUEVO REGISTRO
    public void limpiarNuevoOrganigrama() {
        nuevoOrganigrama = new Organigramas();
        nuevoOrganigrama.setEmpresa(new Empresas());
        nuevoOrganigrama.setEstado("A");
    }

    //DUPLICAR
    public void duplicarOrg() {
        duplicarOrganigrama = new Organigramas();
        duplicarOrganigrama.setFecha(organigramaSeleccionado.getFecha());
        duplicarOrganigrama.setCodigo(organigramaSeleccionado.getCodigo());
        duplicarOrganigrama.setEmpresa(organigramaSeleccionado.getEmpresa());
        duplicarOrganigrama.setEstado(organigramaSeleccionado.getEstado());
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOrganigrama");
        PrimefacesContextUI.ejecutar("PF('DuplicarRegistroOrganigramas').show()");
    }

    public void confirmarDuplicar() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (duplicarOrganigrama.getEmpresa().getSecuencia() == null || duplicarOrganigrama.getCodigo() == null) {
            RequestContext.getCurrentInstance().update("form:validacioNuevoOrganigrama");
            PrimefacesContextUI.ejecutar("PF('validacioNuevoOrganigrama').show()");
        } else {
            boolean continuar = true;
            for (int i = 0; i < listaOrganigramas.size(); i++) {
                if (listaOrganigramas.get(i).getCodigo().equals(duplicarOrganigrama.getCodigo())) {
                    System.out.println(" nuevo codigo 1 igual");
                    RequestContext.getCurrentInstance().update("formularioDialogos:validacionCodigoRepetido");
                    PrimefacesContextUI.ejecutar("PF('validacionCodigoRepetido').show()");
                    continuar = false;
                    break;
                } else if (listaOrganigramas.get(i).getFecha().equals(duplicarOrganigrama.getFecha())
                        && listaOrganigramas.get(i).getEmpresa().equals(duplicarOrganigrama.getEmpresa())) {
                    System.out.println(" nuevo fecha 1 igual para Empresa");
                    RequestContext.getCurrentInstance().update("formularioDialogos:validacionFechaRepetida");
                    PrimefacesContextUI.ejecutar("PF('validacionFechaRepetida').show()");
                    continuar = false;
                    break;
                }
            }
            if (continuar) {
                k++;
                l = BigInteger.valueOf(k);
                duplicarOrganigrama.setSecuencia(l);
                listaOrganigramas.add(duplicarOrganigrama);
                listOrganigramasCrear.add(duplicarOrganigrama);
                organigramaSeleccionado = listaOrganigramas.get(listaOrganigramas.indexOf(duplicarOrganigrama));
                contarRegistrosOr();
                anularBotonLOV();
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                if (bandera == 1) {
                    restaurarTabla();
                }
                RequestContext.getCurrentInstance().update("form:datosOrganigramas");
                duplicarOrganigrama = new Organigramas();
                PrimefacesContextUI.ejecutar("PF('DuplicarRegistroOrganigramas').hide()");
                contarRegistrosOr();
            }
        }
    }
    //LIMPIAR DUPLICAR

    public void limpiarduplicarVTC() {
        duplicarOrganigrama = new Organigramas();
        duplicarOrganigrama.setEmpresa(new Empresas());
    }

    //BORRAR
    public void borrarOrganigrama() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listOrganigramasModificar.isEmpty() && listOrganigramasModificar.contains(organigramaSeleccionado)) {
            listOrganigramasModificar.remove(organigramaSeleccionado);
            listOrganigramasBorrar.add(organigramaSeleccionado);
        } else if (!listOrganigramasCrear.isEmpty() && listOrganigramasCrear.contains(organigramaSeleccionado)) {
            listOrganigramasCrear.remove(organigramaSeleccionado);
        } else {
            listOrganigramasBorrar.add(organigramaSeleccionado);
        }
        listaOrganigramas.remove(organigramaSeleccionado);
        if (tipoLista == 1) {
            filtrarListaOrganigramas.remove(organigramaSeleccionado);
        }
        contarRegistrosOr();
        anularBotonLOV();
        RequestContext.getCurrentInstance().update("form:datosOrganigramas");
        organigramaSeleccionado = null;
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void borradoGeneral() {
        if (estructuraSeleccionada != null) {
            borrarEstructura();
        } else if (organigramaSeleccionado != null) {
            borrarOrganigrama();
        } else {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        }
    }

    //LOV EMPRESAS
    public void actualizarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            organigramaSeleccionado.setEmpresa(empresaSeleccionada);
            if (!listOrganigramasCrear.contains(organigramaSeleccionado)) {
                if (listOrganigramasModificar.isEmpty()) {
                    listOrganigramasModificar.add(organigramaSeleccionado);
                } else if (!listOrganigramasModificar.contains(organigramaSeleccionado)) {
                    listOrganigramasModificar.add(organigramaSeleccionado);
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:datosOrganigramas");
        } else if (tipoActualizacion == 1) {
            nuevoOrganigrama.setEmpresa(empresaSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOrganigrama");
        } else if (tipoActualizacion == 2) {
            duplicarOrganigrama.setEmpresa(empresaSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOrganigrama");
        }
        filtradoEmpresas = null;
        empresaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;

        context.reset("form:lovEmpresas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpresas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:EmpresasDialogo");
        RequestContext.getCurrentInstance().update("form:lovEmpresas");
        RequestContext.getCurrentInstance().update("form:aceptarE");
    }

    public void cancelarEmpresa() {
        filtradoEmpresas = null;
        empresaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEmpresas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpresas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:EmpresasDialogo");
        RequestContext.getCurrentInstance().update("form:lovEmpresas");
        RequestContext.getCurrentInstance().update("form:aceptarE");
    }

    //GUARDAR
    public void guardarCambiosOrganigramas() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (!listOrganigramasBorrar.isEmpty()) {
                    for (int i = 0; i < listOrganigramasBorrar.size(); i++) {
                        administrarEstructuraPlanta.borrarOrganigrama(listOrganigramasBorrar.get(i));
                    }
                    listOrganigramasBorrar.clear();
                }
                if (!listOrganigramasCrear.isEmpty()) {
                    for (int i = 0; i < listOrganigramasCrear.size(); i++) {
                        administrarEstructuraPlanta.crearOrganigrama(listOrganigramasCrear.get(i));
                    }
                    listOrganigramasCrear.clear();
                }
                if (!listOrganigramasModificar.isEmpty()) {
                    administrarEstructuraPlanta.modificarOrganigrama(listOrganigramasModificar);
                    listOrganigramasModificar.clear();
                }
                listaOrganigramas = null;
                getListaOrganigramas();
                organigramaSeleccionado = null;
                RequestContext.getCurrentInstance().update("form:datosOrganigramas");
                contarRegistrosOr();
                guardado = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }
        } catch (Exception e) {
            System.out.println("Error guardarCambiosOrganigramas Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    /**
     * Metodo que activa el filtrado por medio de la opcion en el tollbar o por
     * medio de la tecla Crtl+F11
     */
    public void activarCtrlF11() {
        if (bandera == 0) {
            altoTablaOrganigrama = "45";
            organigramaFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOrganigramas:organigramaFecha");
            organigramaFecha.setFilterStyle("width: 85% !important");
            organigramaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOrganigramas:organigramaCodigo");
            organigramaCodigo.setFilterStyle("width: 85% !important");
            organigramaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOrganigramas:organigramaEmpresa");
            organigramaEmpresa.setFilterStyle("width: 85% !important");
            organigramaNIT = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOrganigramas:organigramaNIT");
            organigramaNIT.setFilterStyle("width: 85% !important");
            organigramaEstado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOrganigramas:organigramaEstado");
            organigramaEstado.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosOrganigramas");
            bandera = 1;
        } else if (bandera == 1) {
            restaurarTabla();
        }
        if (banderaEstructura == 0) {
            altoTablaEstructura = "190";
            estructuraEstructura = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEstructura:estructuraEstructura");
            estructuraEstructura.setFilterStyle("width: 85% !important");
            estructuraCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEstructura:estructuraCodigo");
            estructuraCodigo.setFilterStyle("width: 85% !important");
            estructuraCantidadControlar = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEstructura:estructuraCantidadControlar");
            estructuraCantidadControlar.setFilterStyle("width: 85% !important");
            estructuraCantidadActivo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEstructura:estructuraCantidadActivo");
            estructuraCantidadActivo.setFilterStyle("width: 85% !important");
            estructuraCentroCosto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEstructura:estructuraCentroCosto");
            estructuraCentroCosto.setFilterStyle("width: 85% !important");
            estructuraEstructuraPadre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEstructura:estructuraEstructuraPadre");
            estructuraEstructuraPadre.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosEstructura");
            banderaEstructura = 1;
        } else if (banderaEstructura == 1) {
            restaurarTablaEstr();
        }
    }

    //SALIR
    /**
     * Metodo que cierra la sesion y limpia los datos en la pagina
     */
    public void salir() {
        if (bandera == 1) {
            restaurarTabla();
        }
        if (banderaEstructura == 1) {
            restaurarTablaEstr();
        }
        listOrganigramasModificar.clear();
        listEstructurasBorrar.clear();
        listEstructurasCrear.clear();
        listEstructurasModificar.clear();
        organigramaSeleccionado = null;
        estructuraSeleccionada = null;
        k = 0;
        listaOrganigramas = null;
        listaEstructuras = null;
        guardado = true;
        guardadoEstructura = true;
        cambiosPagina = true;
        lovCentrosCostos = null;
        lovEstructurasPadres = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        altoTablaOrganigrama = "65";
        altoTablaEstructura = "210";
        RequestContext.getCurrentInstance().update("form:datosEstructura");
        RequestContext.getCurrentInstance().update("form:datosOrganigramas");
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (estructuraSeleccionada != null) {
            if (cualCeldaEstructura == 2 || cualCeldaEstructura == 4) {
                modificarInfoRegistrosCCosto(lovCentrosCostos.size());
                RequestContext.getCurrentInstance().update("form:CentroCostoDialogo");
                PrimefacesContextUI.ejecutar("PF('CentroCostoDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCeldaEstructura == 5) {
                cargarLovEstructurasPadres(organigramaSeleccionado.getSecuencia(), estructuraSeleccionada.getSecuencia());
                modificarInfoRegistroEsPa(lovEstructurasPadres.size());
                RequestContext.getCurrentInstance().update("form:EstructuraPadreDialogo");
                PrimefacesContextUI.ejecutar("PF('EstructuraPadreDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (organigramaSeleccionado != null) {
            if (cualCelda == 2) {
                activarBotonLOV();
                contarRegistrosLovEmp(0);
                RequestContext.getCurrentInstance().update("form:EmpresasDialogo");
                PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').show()");
            }
        }
    }

    public void dispararDialogo() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        estructuraSeleccionada = listaEstructuras.get(indice);
        cambiarIndiceEstructura(estructuraSeleccionada, columna);
        tipoActualizacion = 0;
        RequestContext contextt = RequestContext.getCurrentInstance();
        contextt.update("form:CentroCostoDialogo");
        contextt.execute("CentroCostoDialogo').show()");
    }

    public void asignarIndex(Estructuras estructura, int dlg, int LND) {
        RequestContext context = RequestContext.getCurrentInstance();
        estructuraSeleccionada = estructura;
        tipoActualizacion = LND;
        
        if (dlg == 0) {
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            modificarInfoRegistrosCCosto(lovCentrosCostos.size());
            RequestContext.getCurrentInstance().update("form:CentroCostoDialogo");
            PrimefacesContextUI.ejecutar("PF('CentroCostoDialogo').show()");
        }
        if (dlg == 1) {
            activarLOV = true;
            cargarLovEstructurasPadres(organigramaSeleccionado.getSecuencia(), estructuraSeleccionada.getSecuencia());
            RequestContext.getCurrentInstance().update("form:listaValores");
            modificarInfoRegistroEsPa(lovEstructurasPadres.size());
            RequestContext.getCurrentInstance().update("form:EstructuraPadreDialogo");
            PrimefacesContextUI.ejecutar("PF('EstructuraPadreDialogo').show()");
        }
    }

    public void asignarIndex(int dlg, int LND) {
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;

        if (dlg == 0) {
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            modificarInfoRegistrosCCosto(lovCentrosCostos.size());
            RequestContext.getCurrentInstance().update("form:CentroCostoDialogo");
            PrimefacesContextUI.ejecutar("PF('CentroCostoDialogo').show()");
        }
        if (dlg == 1) {
            activarLOV = true;
            cargarLovEstructurasPadres(organigramaSeleccionado.getSecuencia(), new BigInteger("0"));
            RequestContext.getCurrentInstance().update("form:listaValores");
            modificarInfoRegistroEsPa(lovEstructurasPadres.size());
            RequestContext.getCurrentInstance().update("form:EstructuraPadreDialogo");
            PrimefacesContextUI.ejecutar("PF('EstructuraPadreDialogo').show()");
        }
    }

    //ASIGNAR INDEX PARA LA TABLA PRINCIPAL
    public void asignarIndexTabla(Organigramas organigrama) {
        organigramaSeleccionado = organigrama;
        tipoActualizacion = 0;
        activarBotonLOV();
        contarRegistrosLovEmp(0);
        RequestContext.getCurrentInstance().update("form:EmpresasDialogo");
        PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').show()");
    }

    //ASIGNAR INDEX PARA DIALOGOS COMUNES (tipoAct = NUEVO - DUPLICADO)
    public void asignarIndexDialogos(int tipoAct) {
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = tipoAct;
        anularBotonLOV();
        contarRegistrosLovEmp(0);
        RequestContext.getCurrentInstance().update("form:EmpresasDialogo");
        PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').show()");
    }

    public void valoresBackupAutocompletarGeneral(int tipoNuevo, String Campo) {

        if (Campo.equals("PROCESOPRODUCTIVO")) {
            if (tipoNuevo == 1) {
                centroCosto = nuevoEstructura.getCentrocosto().getNombre();
            } else if (tipoNuevo == 2) {
                centroCosto = duplicarEstructura.getCentrocosto().getNombre();
            }
        }
        if (Campo.equals("PADRE")) {
            if (tipoNuevo == 1) {
                estructuraPadre = nuevoEstructura.getEstructurapadre().getNombre();
            } else if (tipoNuevo == 2) {
                estructuraPadre = duplicarEstructura.getEstructurapadre().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoNuevo == 1) {
            nuevoOrganigrama.getEmpresa().setNombre(auxEmpresa);
        } else if (tipoNuevo == 2) {
            duplicarOrganigrama.getEmpresa().setNombre(auxEmpresa);
        }
        for (int i = 0; i < lovEmpresas.size(); i++) {
            if (lovEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                indiceUnicoElemento = i;
                coincidencias++;
            }
        }
        if (coincidencias == 1) {
            if (tipoNuevo == 1) {
                nuevoOrganigrama.setEmpresa(lovEmpresas.get(indiceUnicoElemento));
                RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresa");
                RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNit");
            } else if (tipoNuevo == 2) {
                duplicarOrganigrama.setEmpresa(lovEmpresas.get(indiceUnicoElemento));
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresa");
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNit");
            }
            lovEmpresas.clear();
            getLovEmpresas();
        } else {
            RequestContext.getCurrentInstance().update("form:EmpresasDialogo");
            PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresa");
                RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNit");
            } else if (tipoNuevo == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresa");
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNit");
            }
        }
    }

    public void autocompletarNuevoyDuplicadoEstructura(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("CENTROCOSTO")) {
            if (tipoNuevo == 1) {
                nuevoEstructura.getCentrocosto().setNombre(centroCosto);
            } else if (tipoNuevo == 2) {
                duplicarEstructura.getCentrocosto().setNombre(centroCosto);
            }
            for (int i = 0; i < lovCentrosCostos.size(); i++) {
                if (lovCentrosCostos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoEstructura.setCentrocosto(lovCentrosCostos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEstructuraCentroCosto");
                } else if (tipoNuevo == 2) {
                    duplicarEstructura.setCentrocosto(lovCentrosCostos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstructuraCentroCosto");
                }
            } else {
                RequestContext.getCurrentInstance().update("form:ProcesoProductivoDialogo");
                PrimefacesContextUI.ejecutar("PF('ProcesoProductivoDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEstructuraCentroCosto");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstructuraCentroCosto");
                }
            }
        }
        if (confirmarCambio.equalsIgnoreCase("PADRE")) {
            if (tipoNuevo == 1) {
                nuevoEstructura.getEstructurapadre().setNombre(estructuraPadre);
            } else if (tipoNuevo == 2) {
                duplicarEstructura.getEstructurapadre().setNombre(estructuraPadre);
            }
            for (int i = 0; i < lovEstructurasPadres.size(); i++) {
                if (lovEstructurasPadres.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoEstructura.setEstructurapadre(lovEstructurasPadres.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEstructuraEstructuraPadre");
                } else if (tipoNuevo == 2) {
                    duplicarEstructura.setEstructurapadre(lovEstructurasPadres.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstructuraEstructuraPadre");
                }
            } else {
                RequestContext.getCurrentInstance().update("form:TipoEmpresaDialogo");
                PrimefacesContextUI.ejecutar("PF('TipoEmpresaDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEstructuraEstructuraPadre");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstructuraEstructuraPadre");
                }
            }
        }
    }

    public void actualizarCentroCosto() {
        System.out.println("tipoActualizacion : " + tipoActualizacion);
        System.out.println("centroCostoSeleccionado : " + centroCostoSeleccionado.getNombre());
        System.out.println("estructuraSeleccionado : " + estructuraSeleccionada);
        if (tipoActualizacion == 0) {
            estructuraSeleccionada.setCentrocosto(centroCostoSeleccionado);
            if (!listEstructurasCrear.contains(estructuraSeleccionada)) {
                if (listEstructurasModificar.isEmpty()) {
                    listEstructurasModificar.add(estructuraSeleccionada);
                } else if (!listEstructurasModificar.contains(estructuraSeleccionada)) {
                    listEstructurasModificar.add(estructuraSeleccionada);
                }
            }

            if (guardadoEstructura == true) {
                guardadoEstructura = false;
            }
            permitirIndexEstructura = true;
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosEstructura");
        } else if (tipoActualizacion == 1) {
            nuevoEstructura.setCentrocosto(centroCostoSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEstructuraCentroCosto");
        } else if (tipoActualizacion == 2) {
            duplicarEstructura.setCentrocosto(centroCostoSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstructuraCentroCosto");
        }
        filtrarLovCentrosCostos = null;
        centroCostoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();

        RequestContext.getCurrentInstance().update("form:CentroCostoDialogo");
        RequestContext.getCurrentInstance().update("form:lovCentroCosto");
        RequestContext.getCurrentInstance().update("form:aceptarCC");

        context.reset("form:lovCentroCosto:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovCentroCosto').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('CentroCostoDialogo').hide()");
    }

    public void cancelarCambioCentroCosto() {
        filtrarLovCentrosCostos = null;
        centroCostoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndexEstructura = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovCentroCosto:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovCentroCosto').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('CentroCostoDialogo').hide()");
    }

    public void actualizarEstructuraPadre() {
        if (tipoActualizacion == 0) {
            estructuraSeleccionada.setEstructurapadre(estructuraPadreSeleccionado);
            if (!listEstructurasCrear.contains(estructuraSeleccionada)) {
                if (listEstructurasModificar.isEmpty()) {
                    listEstructurasModificar.add(estructuraSeleccionada);
                } else if (!listEstructurasModificar.contains(estructuraSeleccionada)) {
                    listEstructurasModificar.add(estructuraSeleccionada);
                }
            }

            if (guardadoEstructura == true) {
                guardadoEstructura = false;
            }
            permitirIndexEstructura = true;
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosEstructura");
        } else if (tipoActualizacion == 1) {
            nuevoEstructura.setEstructurapadre(estructuraPadreSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEstructuraEstructuraPadre");
        } else if (tipoActualizacion == 2) {
            duplicarEstructura.setEstructurapadre(estructuraPadreSeleccionado);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstructuraEstructuraPadre");
        }
        filtrarLovEstructurasPadres = null;
        estructuraPadreSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();

        RequestContext.getCurrentInstance().update("form:EstructuraPadreDialogo");
        RequestContext.getCurrentInstance().update("form:lovEstructuraPadre");
        RequestContext.getCurrentInstance().update("form:aceptarEP");

        context.reset("form:lovEstructuraPadre:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEstructuraPadre').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EstructuraPadreDialogo').hide()");
    }

    public void cancelarCambioEstructuraPadre() {
        filtrarLovEstructurasPadres = null;
        estructuraPadreSeleccionado = null;
        aceptar = true;
        estructuraSeleccionada = null;
        tipoActualizacion = -1;
        permitirIndexEstructura = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEstructuraPadre:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEstructuraPadre').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EstructuraPadreDialogo').hide()");
    }

    /**
     * Metodo que activa el boton aceptar de la pantalla y dialogos
     */
    public void activarAceptar() {
        aceptar = false;
    }
    //EXPORTAR

    public String exportXML() {
        if (estructuraSeleccionada != null) {
            nombreTabla = ":formExportarE:datosEstructuraExportar";
            nombreXML = "Estructuras_XML";
        } else if (organigramaSeleccionado != null) {
            nombreTabla = ":formExportarO:datosOrganigramasExportar";
            nombreXML = "Organigramas_XML";
        }
        return nombreTabla;
    }

    /**
     * Metodo que exporta datos a PDF
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void validarExportPDF() throws IOException {
        if (estructuraSeleccionada != null) {
            exportPDF_E();
        } else if (organigramaSeleccionado != null) {
            exportPDF_O();
        }
    }

    public void exportPDF_O() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarO:datosOrganigramasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Organigramas_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportPDF_E() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarE:datosEstructuraExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Estructuras_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    /**
     * Metodo que exporta datos a XLS
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void validarExportXLS() throws IOException {
        if (estructuraSeleccionada != null) {
            exportXLS_E();
        } else if (organigramaSeleccionado != null) {
            exportXLS_O();
        }
    }

    public void exportXLS_O() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarO:datosOrganigramasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Organigramas_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS_E() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarE:datosEstructuraExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Estructuras_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void anularLOV() {
        activarLOV = true;
    }
    //EVENTO FILTRAR

    /**
     * Evento que cambia la lista reala a la filtrada
     */
    public void anularBotonLOV() {
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void activarBotonLOV() {
        activarLOV = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void eventoFiltrarO() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        organigramaSeleccionado = null;
        modificarInfoRegistroOr(filtrarListaOrganigramas.size());
    }

    public void eventoFiltrarE() {
        if (tipoListaEstructura == 0) {
            tipoListaEstructura = 1;
        }
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        estructuraSeleccionada = null;
        modificarInfoRegistroEs(filtrarListaEstructuras.size());
    }

    private void modificarInfoRegistroOr(int valor) {
        infoRegistroOr = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroOrg");
    }

    private void modificarInfoRegistroEs(int valor) {
        infoRegistroEs = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroEst");
    }

    public void contarRegistrosOr() {
        if (listaOrganigramas != null) {
            modificarInfoRegistroOr(listaOrganigramas.size());
        } else {
            modificarInfoRegistroOr(0);
        }
    }

    public void contarRegistrosEs() {
        if (listaEstructuras != null) {
            modificarInfoRegistroEs(listaEstructuras.size());
        } else {
            modificarInfoRegistroEs(0);
        }
        RequestContext.getCurrentInstance().update("form:infoRegistroEst");
    }

    public void contarRegistrosLovEmp(int tipoListaLOV) {
        if (tipoListaLOV == 1) {
            infoRegistroEmpresa = String.valueOf(filtradoEmpresas.size());
        } else if (lovEmpresas != null) {
            infoRegistroEmpresa = String.valueOf(lovEmpresas.size());
        } else {
            infoRegistroEmpresa = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("form:infoRegistroEmpresa");
    }

    private void modificarInfoRegistrosCCosto(int valor) {
        infoRegistroCentroCosto = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroCentroCosto");
    }

    private void modificarInfoRegistroEsPa(int valor) {
        infoRegistroEstructuraPa = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroEstructura");
    }

    public void eventoFiltrarCCosto() {
        modificarInfoRegistrosCCosto(filtrarLovCentrosCostos.size());
    }

    public void eventoFiltrarEsPa() {
        modificarInfoRegistroEsPa(filtrarLovEstructurasPadres.size());
    }

    public void eventoFiltrarLovEmp() {
        contarRegistrosLovEmp(1);
    }

    public void verificarNuevaExtructura() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (organigramaSeleccionado != null) {
            RequestContext.getCurrentInstance().update("formularioDialogos:DialogNuevaEstructura");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEstructura");
            PrimefacesContextUI.ejecutar("PF('DialogNuevaEstructura').show()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaExtructura");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaExtructura').show()");
        }
    }

    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
    public void verificarRastro() {
        if (organigramaSeleccionado == null && estructuraSeleccionada == null) {
            PrimefacesContextUI.ejecutar("PF('verificarRastrosTablasH').show()");
        } else if (estructuraSeleccionada != null) {
            verificarRastroEstructura();
        } else if (organigramaSeleccionado != null) {
            verificarRastroOrganigrama();
        }
    }

    public void verificarRastroOrganigrama() {
        RequestContext context = RequestContext.getCurrentInstance();
        int resultado = administrarRastros.obtenerTabla(organigramaSeleccionado.getSecuencia(), "ORGANIGRAMAS");
        if (resultado == 1) {
            PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
        } else if (resultado == 2) {
            nombreTablaRastro = "Organigramas";
            msnConfirmarRastro = "La tabla ORGANIGRAMAS tiene rastros para el registro seleccionado, ¿desea continuar?";
            RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
            PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
        } else if (resultado == 3) {
            PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
        } else if (resultado == 4) {
            PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
        } else if (resultado == 5) {
            PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
        }
    }

    public void verificarRastroOrganigramaH() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (administrarRastros.verificarHistoricosTabla("ORGANIGRAMAS")) {
            nombreTablaRastro = "Organigramas";
            msnConfirmarRastroHistorico = "La tabla ORGANIGRAMAS tiene rastros historicos, ¿Desea continuar?";
            RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
            PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
        } else {
            PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
        }
    }

    public void verificarRastroEstructura() {
        RequestContext context = RequestContext.getCurrentInstance();
        int resultado = administrarRastros.obtenerTabla(estructuraSeleccionada.getSecuencia(), "ESTRUCTURAS");
        if (resultado == 1) {
            PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
        } else if (resultado == 2) {
            nombreTablaRastro = "Estructuras";
            msnConfirmarRastro = "La tabla ESTRUCTURAS tiene rastros para el registro seleccionado, ¿desea continuar?";
            RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
            PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
        } else if (resultado == 3) {
            PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
        } else if (resultado == 4) {
            PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
        } else if (resultado == 5) {
            PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
        }
    }

    public void verificarRastroEstructuraH() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (administrarRastros.verificarHistoricosTabla("ESTRUCTURAS")) {
            nombreTablaRastro = "Estructuras";
            msnConfirmarRastroHistorico = "La tabla ESTRUCTURAS tiene rastros historicos, ¿Desea continuar?";
            RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
            PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
        } else {
            PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
        }
    }

    public void recordarSeleccionOr() {
        if (organigramaSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tabla = (DataTable) c.getViewRoot().findComponent("form:datosOrganigramas");
            tabla.setSelection(organigramaSeleccionado);
        }
    }

    public void recordarSeleccionEx() {
        if (estructuraSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tabla = (DataTable) c.getViewRoot().findComponent("form:datosEstructura");
            tabla.setSelection(estructuraSeleccionada);
        }
    }

    //GETTERS AND SETTERS
    public List<Organigramas> getListaOrganigramas() {
        try {
            if (listaOrganigramas == null) {
                listaOrganigramas = administrarEstructuraPlanta.listaOrganigramas();
            }
            return listaOrganigramas;

        } catch (Exception e) {
            System.out.println("Error...!! getListaOrganigramas " + e.toString());
            return null;
        }
    }

    public void setListaOrganigramas(List<Organigramas> setListaOrganigramas) {
        this.listaOrganigramas = setListaOrganigramas;
    }

    public List<Organigramas> getFiltrarListaOrganigramas() {
        return filtrarListaOrganigramas;
    }

    public void setFiltrarListaOrganigramas(List<Organigramas> setFiltrarListaOrganigramas) {
        this.filtrarListaOrganigramas = setFiltrarListaOrganigramas;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public Organigramas getEditarOrganigrama() {
        return editarOrganigrama;
    }

    public void setEditarOrganigrama(Organigramas setEditarOrganigrama) {
        this.editarOrganigrama = setEditarOrganigrama;
    }

    public List<Estructuras> getListaEstructuras() {
        if (listaEstructuras == null) {
            if (organigramaSeleccionado != null) {
                listaEstructuras = administrarEstructuraPlanta.listaEstructurasPorSecuenciaOrganigrama(organigramaSeleccionado.getSecuencia());
            }

            if (listaEstructuras != null) {
                for (int i = 0; i < listaEstructuras.size(); i++) {
                    if (listaEstructuras.get(i).getCentrocosto() == null) {
                        listaEstructuras.get(i).setCentrocosto(new CentrosCostos());
                    }
                    if (listaEstructuras.get(i).getEstructurapadre() == null) {
                        listaEstructuras.get(i).setEstructurapadre(new Estructuras());
                    }
                }
            }
        }
        return listaEstructuras;
    }

    public void setListaEstructuras(List<Estructuras> setListaEstructuras) {
        this.listaEstructuras = setListaEstructuras;
    }

    public List<Estructuras> getFiltrarListaEstructuras() {
        return filtrarListaEstructuras;
    }

    public void setFiltrarListaEstructuras(List<Estructuras> setFiltrarListaEstructuras) {
        this.filtrarListaEstructuras = setFiltrarListaEstructuras;
    }

    public List<Organigramas> getListOrganigramasModificar() {
        return listOrganigramasModificar;
    }

    public void setListOrganigramasModificar(List<Organigramas> setListOrganigramasModificar) {
        this.listOrganigramasModificar = setListOrganigramasModificar;
    }

    public List<Estructuras> getListEstructurasModificar() {
        return listEstructurasModificar;
    }

    public void setListEstructurassModificar(List<Estructuras> setListEstructurassModificar) {
        this.listEstructurasModificar = setListEstructurassModificar;
    }

    public Estructuras getNuevoEstructura() {
        return nuevoEstructura;
    }

    public void setNuevoEstructura(Estructuras setNuevoEstructura) {
        this.nuevoEstructura = setNuevoEstructura;
    }

    public List<Estructuras> getListEstructurasCrear() {
        return listEstructurasCrear;
    }

    public void setListEstructurasCrear(List<Estructuras> setListEstructurasCrear) {
        this.listEstructurasCrear = setListEstructurasCrear;
    }

    public List<Estructuras> getListEstructurasBorrar() {
        return listEstructurasBorrar;
    }

    public void setListEstructurasBorrar(List<Estructuras> setListEstructurasBorrar) {
        this.listEstructurasBorrar = setListEstructurasBorrar;
    }

    public Estructuras getEditarEstructura() {
        return editarEstructura;
    }

    public void setEditarEstructura(Estructuras setEditarEstructura) {
        this.editarEstructura = setEditarEstructura;
    }

    public Estructuras getDuplicarEstructura() {
        return duplicarEstructura;
    }

    public void setDuplicarEstructura(Estructuras setDuplicarEstructura) {
        this.duplicarEstructura = setDuplicarEstructura;
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

    public String getNombreXML() {
        return nombreXML;
    }

    public void setNombreXML(String nombreXML) {
        this.nombreXML = nombreXML;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public void setNombreTabla(String setNombreTabla) {
        this.nombreTabla = setNombreTabla;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

    public String getAltoTablaOrganigrama() {
        return altoTablaOrganigrama;
    }

    public void setAltoTablaOrganigrama(String setAltoTablaOrganigrama) {
        this.altoTablaOrganigrama = setAltoTablaOrganigrama;
    }

    public String getAltoTablaEstructura() {
        return altoTablaEstructura;
    }

    public void setAltoTablaEstructura(String setAltoTablaEstructura) {
        this.altoTablaEstructura = setAltoTablaEstructura;
    }

    public List<CentrosCostos> getLovCentrosCostos() {
        if (lovCentrosCostos == null) {
            if (organigramaSeleccionado != null) {
                System.out.println("organigramaSeleccionado : " + organigramaSeleccionado);
                System.out.println("organigramaSeleccionado.getEmpresa() : " + organigramaSeleccionado.getEmpresa());
                System.out.println("organigramaSeleccionado.getEmpresa().getSecuencia() : " + organigramaSeleccionado.getEmpresa().getSecuencia());
                lovCentrosCostos = administrarEstructuraPlanta.lovCentrosCostos(organigramaSeleccionado.getEmpresa().getSecuencia());
            } else {
                lovCentrosCostos = new ArrayList<CentrosCostos>();
            }
        }
        return lovCentrosCostos;
    }

    public void setLovCentrosCostos(List<CentrosCostos> setLovCentrosCostos) {
        this.lovCentrosCostos = setLovCentrosCostos;
    }

    public List<CentrosCostos> getFiltrarLovCentrosCostos() {
        return filtrarLovCentrosCostos;
    }

    public void setFiltrarLovCentrosCostos(List<CentrosCostos> setFiltrarLovCentrosCostos) {
        this.filtrarLovCentrosCostos = setFiltrarLovCentrosCostos;
    }

    public CentrosCostos getCentroCostoSeleccionado() {
        return centroCostoSeleccionado;
    }

    public void setCentroCostoSeleccionado(CentrosCostos setCentroCostoSeleccionado) {
        this.centroCostoSeleccionado = setCentroCostoSeleccionado;
    }

    public void cargarLovEstructurasPadres(BigInteger secOrganigrama, BigInteger secEstructura) {
        lovEstructurasPadres = administrarEstructuraPlanta.lovEstructurasPadres(secOrganigrama, secEstructura);
    }

    public List<Estructuras> getLovEstructurasPadres() {
        return lovEstructurasPadres;
    }

    public void setLovEstructurasPadres(List<Estructuras> setLovEstructurasPadres) {
        this.lovEstructurasPadres = setLovEstructurasPadres;
    }

    public List<Estructuras> getFiltrarLovEstructurasPadres() {
        return filtrarLovEstructurasPadres;
    }

    public void setFiltrarLovEstructurasPadres(List<Estructuras> setFiltrarLovEstructurasPadres) {
        this.filtrarLovEstructurasPadres = setFiltrarLovEstructurasPadres;
    }

    public Estructuras getEstructuraPadreSeleccionado() {
        return estructuraPadreSeleccionado;
    }

    public void setEstructuraPadreSeleccionado(Estructuras setEstructuraPadreSeleccionado) {
        this.estructuraPadreSeleccionado = setEstructuraPadreSeleccionado;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public boolean isActivoEstructura() {
        return activoEstructura;
    }

    public void setActivoEstructura(boolean setActivoEstructura) {
        this.activoEstructura = setActivoEstructura;
    }

    public Organigramas getOrganigramaSeleccionado() {
        return organigramaSeleccionado;
    }

    public void setOrganigramaSeleccionado(Organigramas organigramaTablaSeleccionado) {
        this.organigramaSeleccionado = organigramaTablaSeleccionado;
    }

    public Estructuras getEstructuraSeleccionada() {
        return estructuraSeleccionada;
    }

    public void setEstructuraSeleccionada(Estructuras estructuraTablaSeleccionado) {
        this.estructuraSeleccionada = estructuraTablaSeleccionado;
    }

    public String getInfoRegistroCentroCosto() {
        return infoRegistroCentroCosto;
    }

    public void setInfoRegistroCentroCosto(String infoRegistroCentroCosto) {
        this.infoRegistroCentroCosto = infoRegistroCentroCosto;
    }

    public String getInfoRegistroEstructuraPa() {
        return infoRegistroEstructuraPa;
    }

    public void setInfoRegistroEstructuraPa(String infoRegistroEstructura) {
        this.infoRegistroEstructuraPa = infoRegistroEstructura;
    }

    public String getInfoRegistroEs() {
        return infoRegistroEs;
    }

    public void setInfoRegistroEs(String infoRegistroEs) {
        this.infoRegistroEs = infoRegistroEs;
    }

    public String getInfoRegistroOr() {
        return infoRegistroOr;
    }

    public void setInfoRegistroOr(String infoRegistroOr) {
        this.infoRegistroOr = infoRegistroOr;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }

    public List<Empresas> getLovEmpresas() {
        if (lovEmpresas == null) {
            lovEmpresas = administrarEstructuraPlanta.consultarEmpresas();
        }
        return lovEmpresas;
    }

    public void setLovEmpresas(List<Empresas> listaEmpresas) {
        this.lovEmpresas = listaEmpresas;
    }

    public Empresas getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public List<Empresas> getFiltradoEmpresas() {
        return filtradoEmpresas;
    }

    public void setFiltradoEmpresas(List<Empresas> filtradoEmpresas) {
        this.filtradoEmpresas = filtradoEmpresas;
    }

    public String getInfoRegistroEmpresa() {
        return infoRegistroEmpresa;
    }

    public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
        this.infoRegistroEmpresa = infoRegistroEmpresa;
    }

    public Organigramas getDuplicarOrganigrama() {
        return duplicarOrganigrama;
    }

    public void setDuplicarOrganigrama(Organigramas duplicarOrganigrama) {
        this.duplicarOrganigrama = duplicarOrganigrama;
    }

    public Organigramas getNuevoOrganigrama() {
        return nuevoOrganigrama;
    }

    public void setNuevoOrganigrama(Organigramas nuevoOrganigrama) {
        this.nuevoOrganigrama = nuevoOrganigrama;
    }
}
