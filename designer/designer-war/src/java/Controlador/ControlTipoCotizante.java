package Controlador;

import Entidades.TiposEntidades;
import Entidades.TiposCotizantes;
import Entidades.DetallesTiposCotizantes;
import Exportar.ExportarPDF;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarDetallesTiposCotizantesInterface;
import InterfaceAdministrar.AdministrarTiposCotizantesInterface;
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
public class ControlTipoCotizante implements Serializable {

    @EJB
    AdministrarTiposCotizantesInterface administrarTiposCotizantes;
    @EJB
    AdministrarDetallesTiposCotizantesInterface administrarDetallesTiposCotizantes;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //LISTA TIPOS COTIZANTES
    private List<TiposCotizantes> listaTiposCotizantes;
    private List<TiposCotizantes> filtradosListaTiposCotizantes;
    private TiposCotizantes tipoCotizanteSeleccionado;
    private TiposCotizantes clonarTipoCotizante;
    //LISTA DETALLES TIPO COTIZANTE
    private List<DetallesTiposCotizantes> listaDetallesTiposCotizantes;
    private List<DetallesTiposCotizantes> filtradosListaDetallesTiposCotizantes;
    private DetallesTiposCotizantes detalleTipoCotizanteSeleccionado;
    //L.O.V ListaEntidades
    private List<TiposEntidades> lovListaTiposEntidades;
    private List<TiposEntidades> filtradoslovListaTiposEntidades;
    private TiposEntidades seleccionTiposEntidades;
    //L.O.V LISTA TIPOS COTIZANTES
    private List<TiposCotizantes> lovListaTiposCotizantes;
    private List<TiposCotizantes> lovFiltradosListaTiposCotizantes;
    private TiposCotizantes lovTipoCotizanteSeleccionado;
    //OTROS
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private int banderaNF;
    private boolean permitirIndex;
    //Crear Vigencias 
    public TiposCotizantes nuevoTipoCotizante;
    public DetallesTiposCotizantes nuevoRegistroDetalleTipoCotizante;
    private List<TiposCotizantes> listaTiposCotizantesCrear;
    private int k;
    private BigInteger l;
    private int m;
    private BigInteger n;
    private String mensajeValidacion;
    private String mensajeValidacionNF;
    //Crear Detalles tipos cotizantes
    private List<DetallesTiposCotizantes> listaDetallesTiposCotizantesCrear;
    //Modificar Tipos Cotizantes
    private List<TiposCotizantes> listaTiposCotizantesModificar;
    private boolean guardado, guardarOk;
    //Modificar Detalles Tipos Cotizantes
    private List<DetallesTiposCotizantes> listaDetallesTiposCotizantesModificar;
    //Borrar TiposCotizantes
    private List<TiposCotizantes> listaTiposCotizantesBorrar;
    //Borrar Detalles Tipos Cotizantes
    private List<DetallesTiposCotizantes> listaDetallesTiposCotizantesBorrar;
    //editar celda
    private TiposCotizantes editarTiposCotizantes;
    private DetallesTiposCotizantes editarDetallesTiposCotizantes;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista, tipoListaNF;
    //Columnas Tabla Tipos Cotizantes
    private Column tcCodigo, tcDescripcion, tcPension, tcSalud, tcRiesgo, tcParafiscal, tcEsap, tcMen, tcExtranjero, tcSubtipoCotizante, tcCodigoAlternativo;
    //Columnas Tabla Detalles Tipos Cotizantes
    private Column dtcTipoEntidad, dtcMinimo, dtcMaximo;
    //Duplicar
    private TiposCotizantes duplicarTipoCotizante;
    private DetallesTiposCotizantes duplicarRegistroDetalleTipoCotizante;
    //Cual Tabla
    private int CualTabla;
    //Tabla a Imprimir
    private String tablaImprimir, nombreArchivo;
    //Cambian del Clonar
    private BigInteger secuenciaClonado;
    private BigInteger clonarCodigo;
    private String clonarDescripcion;
    private String altoTabla;
    private String altoTablaNF;
    private boolean cambiosPagina;
    private String paginaAnterior, infoRegistroTipoC, infoRegistroDetalleTC;
    private String infoRegistroLovTE, infoRegistroLovTC;

    public ControlTipoCotizante() {
        cambiosPagina = true;
        altoTabla = "95";
        altoTablaNF = "95";
        permitirIndex = true;
        //secuenciaPersona = BigInteger.valueOf(10668967);
        aceptar = true;
        listaDetallesTiposCotizantesBorrar = new ArrayList<DetallesTiposCotizantes>();
        listaDetallesTiposCotizantesCrear = new ArrayList<DetallesTiposCotizantes>();
        listaDetallesTiposCotizantesModificar = new ArrayList<DetallesTiposCotizantes>();
        listaTiposCotizantesBorrar = new ArrayList<TiposCotizantes>();
        listaTiposCotizantesCrear = new ArrayList<TiposCotizantes>();
        listaTiposCotizantesModificar = new ArrayList<TiposCotizantes>();
        tipoCotizanteSeleccionado = null;
        editarTiposCotizantes = new TiposCotizantes();
        editarDetallesTiposCotizantes = new DetallesTiposCotizantes();
        cambioEditor = false;
        aceptarEditar = true;
        cualCelda = -1;
        tipoLista = 0;
        tipoListaNF = 0;
        nuevoTipoCotizante = new TiposCotizantes();
        clonarTipoCotizante = new TiposCotizantes();
        duplicarTipoCotizante = new TiposCotizantes();
        nuevoTipoCotizante.setCodigo(BigInteger.valueOf(0));
        nuevoTipoCotizante.setDescripcion(" ");
        nuevoRegistroDetalleTipoCotizante = new DetallesTiposCotizantes();
        duplicarRegistroDetalleTipoCotizante = new DetallesTiposCotizantes();
        guardado = true;
        tablaImprimir = ":formExportar:datosTiposCotizantesExportar";
        nombreArchivo = "TiposCotizantesXML";
        k = 0;
        m = 0;
        paginaAnterior = "";
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposCotizantes.obtenerConexion(ses.getId());
            administrarDetallesTiposCotizantes.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPagina(String pagina) {
        listaTiposCotizantes = null;
        getListaTiposCotizantes();
        if (listaTiposCotizantes != null) {
            if (!listaTiposCotizantes.isEmpty()) {
                tipoCotizanteSeleccionado = listaTiposCotizantes.get(0);
                listaDetallesTiposCotizantes = null;
                getListaDetallesTiposCotizantes();
            }
        }
        paginaAnterior = pagina;
    }

    public String retornarPagina() {
        return paginaAnterior;
    }

    public void cambiarTipoCotizante() {
        CualTabla = 0;
        if (listaDetallesTiposCotizantesCrear.isEmpty() && listaDetallesTiposCotizantesBorrar.isEmpty() && listaDetallesTiposCotizantesModificar.isEmpty()) {
            tipoCotizanteSeleccionado.getSecuencia();
            listaDetallesTiposCotizantes = null;
            getListaDetallesTiposCotizantes();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
        }
    }

    //Ubicacion Celda.
    public void cambiarIndice(TiposCotizantes tipo, int celda) {

        tipoCotizanteSeleccionado = tipo;
        detalleTipoCotizanteSeleccionado = null;
        cualCelda = celda;
        CualTabla = 0;
        tipoCotizanteSeleccionado.getSecuencia();
        tablaImprimir = ":formExportar:datosTiposCotizantesExportar";
        nombreArchivo = "TiposCotizantesXML";
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:exportarXML");
        if (cualCelda == 0) {
            tipoCotizanteSeleccionado.getCodigo();
        } else if (cualCelda == 1) {
            tipoCotizanteSeleccionado.getDescripcion();
        } else if (cualCelda == 2) {
            tipoCotizanteSeleccionado.getCodigoalternativo();
        }
        cambiarTipoCotizante();
        contarRegistrosDetallesTipoC();
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void modificarTiposCotizantes(TiposCotizantes tipo) {
        tipoCotizanteSeleccionado = tipo;
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaTiposCotizantesCrear.contains(tipoCotizanteSeleccionado)) {
            if (listaTiposCotizantesModificar.isEmpty()) {
                listaTiposCotizantesModificar.add(tipoCotizanteSeleccionado);
            } else if (!listaTiposCotizantesModificar.contains(tipoCotizanteSeleccionado)) {
                listaTiposCotizantesModificar.add(tipoCotizanteSeleccionado);
            }
        }
        if (guardado == true) {
            guardado = false;
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
        System.out.println("tipo cotizante Subtipo: " + tipoCotizanteSeleccionado.getSubtipocotizante());
        System.out.println("tipo cotizante Subtipo String: " + tipoCotizanteSeleccionado.getEstadoSubTipoCotizante());
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (CualTabla == 0) {
            editarTiposCotizantes = tipoCotizanteSeleccionado;
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigo");
                RequestContext.getCurrentInstance().execute("PF('editarCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editarDescripcion').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoAlternativo");
                RequestContext.getCurrentInstance().execute("PF('editarCodigoAlternativo').show()");
                cualCelda = -1;
            }
        } else if (CualTabla == 1) {
            editarDetallesTiposCotizantes = detalleTipoCotizanteSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoEntidad");
                RequestContext.getCurrentInstance().execute("PF('editarTipoEntidad').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarMinimo");
                RequestContext.getCurrentInstance().execute("PF('editarMinimo').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarMaximo");
                RequestContext.getCurrentInstance().execute("PF('editarMaximo').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (CualTabla == 1) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposEntidadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    //FILTRADO
    public void activarCtrlF11() {
        System.out.println("TipoLista= " + tipoLista);
        if (bandera == 0 && banderaNF == 0) {
            altoTabla = "75";
            altoTablaNF = "75";
            System.out.println("Activar");
            System.out.println("TipoLista= " + tipoLista);
            tcCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcCodigo");
            tcCodigo.setFilterStyle("width: 85% !important;");
            tcDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcDescripcion");
            tcDescripcion.setFilterStyle("width: 85% !important;");
            tcPension = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcPension");
            tcPension.setFilterStyle("width: 85% !important;");
            tcSalud = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcSalud");
            tcSalud.setFilterStyle("width: 85% !important;");
            tcRiesgo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcRiesgo");
            tcRiesgo.setFilterStyle("width: 85% !important;");
            tcParafiscal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcParafiscal");
            tcParafiscal.setFilterStyle("width: 85% !important;");
            tcEsap = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcEsap");
            tcEsap.setFilterStyle("width: 85% !important;");
            tcMen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcMen");
            tcMen.setFilterStyle("width: 85% !important;");
            tcExtranjero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcExtranjero");
            tcExtranjero.setFilterStyle("width: 85% !important;");
            tcSubtipoCotizante = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcSubtipoCotizante");
            tcSubtipoCotizante.setFilterStyle("width: 85% !important;");
            tcCodigoAlternativo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcCodigoAlternativo");
            tcCodigoAlternativo.setFilterStyle("width: 85% !important;");
            dtcTipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcTipoEntidad");
            dtcTipoEntidad.setFilterStyle("width: 85% !important;");
            dtcMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcMinimo");
            dtcMinimo.setFilterStyle("width: 85% !important;");
            dtcMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcMaximo");
            dtcMaximo.setFilterStyle("width: 85% !important;");
            bandera = 1;
            banderaNF = 1;
            RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
            RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
            tipoLista = 1;
            tipoListaNF = 1;

        } else if (bandera == 1 && banderaNF == 1) {
            altoTabla = "95";
            altoTablaNF = "95";
            tcCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcCodigo");
            tcCodigo.setFilterStyle("display: none; visibility: hidden;");
            tcDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcDescripcion");
            tcDescripcion.setFilterStyle("display: none; visibility: hidden;");
            tcPension = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcPension");
            tcPension.setFilterStyle("display: none; visibility: hidden;");
            tcSalud = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcSalud");
            tcSalud.setFilterStyle("display: none; visibility: hidden;");
            tcRiesgo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcRiesgo");
            tcRiesgo.setFilterStyle("display: none; visibility: hidden;");
            tcParafiscal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcParafiscal");
            tcParafiscal.setFilterStyle("display: none; visibility: hidden;");
            tcEsap = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcEsap");
            tcEsap.setFilterStyle("display: none; visibility: hidden;");
            tcMen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcMen");
            tcMen.setFilterStyle("display: none; visibility: hidden;");
            tcExtranjero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcExtranjero");
            tcExtranjero.setFilterStyle("display: none; visibility: hidden;");
            tcSubtipoCotizante = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcSubtipoCotizante");
            tcSubtipoCotizante.setFilterStyle("display: none; visibility: hidden;");
            tcCodigoAlternativo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcCodigoAlternativo");
            tcCodigoAlternativo.setFilterStyle("display: none; visibility: hidden;");
            dtcTipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcTipoEntidad");
            dtcTipoEntidad.setFilterStyle("display: none; visibility: hidden;");
            dtcMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcMinimo");
            dtcMinimo.setFilterStyle("display: none; visibility: hidden;");
            dtcMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcMaximo");
            dtcMaximo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
            RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
            bandera = 0;
            banderaNF = 0;
            filtradosListaTiposCotizantes = null;
            filtradosListaDetallesTiposCotizantes = null;
            tipoLista = 0;
            tipoListaNF = 0;
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        if (CualTabla == 0) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposCotizantesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "TiposCotizantesPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (CualTabla == 1) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDetallesTiposCotizantesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "DetallesTiposCotizantesPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
        }
    }

    public void exportXLS() throws IOException {
        if (CualTabla == 0) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposCotizantesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "TiposCotizantesXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
        } else if (CualTabla == 1) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDetallesTiposCotizantesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "DetallesTiposCotizantesXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
        }
    }

    //LIMPIAR NUEVO REGISTRO
    public void limpiarNuevoTipoCotizante() {
        nuevoTipoCotizante = new TiposCotizantes();
        nuevoTipoCotizante.setCodigo(BigInteger.valueOf(0));
        nuevoTipoCotizante.setDescripcion(" ");
    }

    public void limpiarNuevoDetalleTipoCotizante() {
        nuevoRegistroDetalleTipoCotizante = new DetallesTiposCotizantes();
    }

// Agregar Nuevo Tipo Cotizante
    public void agregarNuevoTipoCotizante() {
        int pasa = 0;
        int duplicados = 0;
        mensajeValidacion = " ";
        if (nuevoTipoCotizante.getCodigo().equals(BigInteger.valueOf(0)) == true) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int i = 0; i < listaTiposCotizantes.size(); i++) {
                if (nuevoTipoCotizante.getCodigo().equals(listaTiposCotizantes.get(i).getCodigo()) == true) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya existe. Por favor ingrese un código válido";
            } else {
                pasa++;
            }
        }
        if (nuevoTipoCotizante.getDescripcion().equals(" ")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            pasa++;
        }
        if (pasa == 2) {
            if (bandera == 1) {
                altoTabla = "95";
                tcCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcCodigo");
                tcCodigo.setFilterStyle("display: none; visibility: hidden;");
                tcDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcDescripcion");
                tcDescripcion.setFilterStyle("display: none; visibility: hidden;");
                tcPension = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcPension");
                tcPension.setFilterStyle("display: none; visibility: hidden;");
                tcSalud = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcSalud");
                tcSalud.setFilterStyle("display: none; visibility: hidden;");
                tcRiesgo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcRiesgo");
                tcRiesgo.setFilterStyle("display: none; visibility: hidden;");
                tcParafiscal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcParafiscal");
                tcParafiscal.setFilterStyle("display: none; visibility: hidden;");
                tcEsap = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcEsap");
                tcEsap.setFilterStyle("display: none; visibility: hidden;");
                tcMen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcMen");
                tcMen.setFilterStyle("display: none; visibility: hidden;");
                tcExtranjero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcExtranjero");
                tcExtranjero.setFilterStyle("display: none; visibility: hidden;");
                tcSubtipoCotizante = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcSubtipoCotizante");
                tcSubtipoCotizante.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
                bandera = 0;
                filtradosListaTiposCotizantes = null;
                tipoLista = 0;
            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS FORMALES.
            k++;
            l = BigInteger.valueOf(k);
            nuevoTipoCotizante.setSecuencia(l);
            listaTiposCotizantesCrear.add(nuevoTipoCotizante);
            listaTiposCotizantes.add(nuevoTipoCotizante);
            tipoCotizanteSeleccionado = nuevoTipoCotizante;
            contarRegistrosTipoC();
            nuevoTipoCotizante = new TiposCotizantes();
            nuevoTipoCotizante.setCodigo(BigInteger.valueOf(0));
            nuevoTipoCotizante.setDescripcion(" ");
            RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTipoCotizante').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoCotizante");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoCotizante').show()");
            mensajeValidacion = " ";
        }
    }

//BORRAR VIGENCIA FORMAL
    public void borrarTipoCotizante() {

        if (CualTabla == 0) {
            if (listaDetallesTiposCotizantes.size() > 0) {
                if (!listaDetallesTiposCotizantes.isEmpty()) {
                    RequestContext.getCurrentInstance().execute("PF('errorBorrado').show()");
                }
            } else {
                if (!listaTiposCotizantesModificar.isEmpty() && listaTiposCotizantesModificar.contains(tipoCotizanteSeleccionado)) {
                    int modIndex = listaTiposCotizantesModificar.indexOf(tipoCotizanteSeleccionado);
                    listaTiposCotizantesModificar.remove(modIndex);
                    listaTiposCotizantesBorrar.add(tipoCotizanteSeleccionado);
                } else if (!listaTiposCotizantesCrear.isEmpty() && listaTiposCotizantesCrear.contains(tipoCotizanteSeleccionado)) {
                    int crearIndex = listaTiposCotizantesCrear.indexOf(tipoCotizanteSeleccionado);
                    listaTiposCotizantesCrear.remove(crearIndex);
                } else {
                    listaTiposCotizantesBorrar.add(tipoCotizanteSeleccionado);
                }
                listaTiposCotizantes.remove(tipoCotizanteSeleccionado);
                if (tipoLista == 1) {
                    filtradosListaTiposCotizantes.remove(tipoCotizanteSeleccionado);
                }

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
                tipoCotizanteSeleccionado = null;
                contarRegistrosTipoC();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }

        } else if (CualTabla == 1) {

            if (!listaDetallesTiposCotizantesModificar.isEmpty() && listaDetallesTiposCotizantesModificar.contains(detalleTipoCotizanteSeleccionado)) {
                int modIndex = listaDetallesTiposCotizantesModificar.indexOf(detalleTipoCotizanteSeleccionado);
                listaDetallesTiposCotizantesModificar.remove(modIndex);
                listaDetallesTiposCotizantesBorrar.add(detalleTipoCotizanteSeleccionado);
            } else if (!listaDetallesTiposCotizantesCrear.isEmpty() && listaDetallesTiposCotizantesCrear.contains(detalleTipoCotizanteSeleccionado)) {
                int crearIndex = listaDetallesTiposCotizantesCrear.indexOf(detalleTipoCotizanteSeleccionado);
                listaDetallesTiposCotizantesCrear.remove(crearIndex);
            } else {
                listaDetallesTiposCotizantesBorrar.add(detalleTipoCotizanteSeleccionado);
            }
            listaDetallesTiposCotizantes.remove(detalleTipoCotizanteSeleccionado);
            if (tipoListaNF == 1) {
                filtradosListaDetallesTiposCotizantes.remove(detalleTipoCotizanteSeleccionado);
            }
            detalleTipoCotizanteSeleccionado = null;
            contarRegistrosDetallesTipoC();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    //DUPLICAR TIPO COTIZANTE
    public void duplicarTC() {
        if (CualTabla == 0) {
            duplicarTipoCotizante = new TiposCotizantes();
            k++;
            l = BigInteger.valueOf(k);
            duplicarTipoCotizante.setSecuencia(l);
            duplicarTipoCotizante.setCodigo(tipoCotizanteSeleccionado.getCodigo());
            duplicarTipoCotizante.setDescripcion(tipoCotizanteSeleccionado.getDescripcion());
            duplicarTipoCotizante.setCotizapension(tipoCotizanteSeleccionado.getCotizapension());
            duplicarTipoCotizante.setCotizasalud(tipoCotizanteSeleccionado.getCotizasalud());
            duplicarTipoCotizante.setCotizariesgo(tipoCotizanteSeleccionado.getCotizariesgo());
            duplicarTipoCotizante.setCotizaparafiscal(tipoCotizanteSeleccionado.getCotizaparafiscal());
            duplicarTipoCotizante.setCotizaesap(tipoCotizanteSeleccionado.getCotizaesap());
            duplicarTipoCotizante.setCotizamen(tipoCotizanteSeleccionado.getCotizamen());
            duplicarTipoCotizante.setExtranjero(tipoCotizanteSeleccionado.getExtranjero());
            duplicarTipoCotizante.setSubtipocotizante(tipoCotizanteSeleccionado.getSubtipocotizante());
            duplicarTipoCotizante.setCodigoalternativo(tipoCotizanteSeleccionado.getCodigoalternativo());
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroTipoCotizante");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTipoCotizante').show()");
        } else if (CualTabla == 1) {
            System.out.println("Entra Duplicar NF");
            duplicarRegistroDetalleTipoCotizante = new DetallesTiposCotizantes();
            m++;
            n = BigInteger.valueOf(m);
            duplicarRegistroDetalleTipoCotizante.setSecuencia(n);
            duplicarRegistroDetalleTipoCotizante.setTipoentidad(detalleTipoCotizanteSeleccionado.getTipoentidad());
            duplicarRegistroDetalleTipoCotizante.setMinimosml(detalleTipoCotizanteSeleccionado.getMinimosml());
            duplicarRegistroDetalleTipoCotizante.setMaximosml(detalleTipoCotizanteSeleccionado.getMaximosml());
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroDetalleTipoCotizante");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDetalleTipoCotizante').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {

        int pasa = 0;
        int duplicados = 0;
        mensajeValidacion = " ";
        if (duplicarTipoCotizante.getCodigo().equals(BigInteger.valueOf(0)) == true) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int i = 0; i < listaTiposCotizantes.size(); i++) {
                if (duplicarTipoCotizante.getCodigo().equals(listaTiposCotizantes.get(i).getCodigo()) == true) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya existe. Por favor ingrese un código válido";
            } else {
                pasa++;
            }
        }
        if (duplicarTipoCotizante.getDescripcion().equals(" ")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            pasa++;
        }

        if (pasa == 2) {
            listaTiposCotizantes.add(duplicarTipoCotizante);
            listaTiposCotizantesCrear.add(duplicarTipoCotizante);
            tipoCotizanteSeleccionado = duplicarTipoCotizante;
            contarRegistrosTipoC();
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                altoTabla = "73";
                System.out.println("Desactivar");
                System.out.println("TipoLista= " + tipoLista);
                tcCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcCodigo");
                tcCodigo.setFilterStyle("display: none; visibility: hidden;");
                tcDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcDescripcion");
                tcDescripcion.setFilterStyle("display: none; visibility: hidden;");
                tcPension = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcPension");
                tcPension.setFilterStyle("display: none; visibility: hidden;");
                tcSalud = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcSalud");
                tcSalud.setFilterStyle("display: none; visibility: hidden;");
                tcRiesgo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcRiesgo");
                tcRiesgo.setFilterStyle("display: none; visibility: hidden;");
                tcParafiscal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcParafiscal");
                tcParafiscal.setFilterStyle("display: none; visibility: hidden;");
                tcEsap = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcEsap");
                tcEsap.setFilterStyle("display: none; visibility: hidden;");
                tcMen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcMen");
                tcMen.setFilterStyle("display: none; visibility: hidden;");
                tcExtranjero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcExtranjero");
                tcExtranjero.setFilterStyle("display: none; visibility: hidden;");
                tcSubtipoCotizante = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcSubtipoCotizante");
                tcSubtipoCotizante.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
                bandera = 0;
                filtradosListaTiposCotizantes = null;
                tipoLista = 0;
            }
            RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
            duplicarTipoCotizante = new TiposCotizantes();
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroTipoCotizante");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTipoCotizante').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionDuplicarTipoCotizante");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarTipoCotizante').show()");
            mensajeValidacion = "";
        }
    }

    public void limpiarduplicarTipoCotizante() {
        duplicarTipoCotizante = new TiposCotizantes();
    }

    public void limpiarduplicarRegistroDetalleTipoCotizante() {
        duplicarRegistroDetalleTipoCotizante = new DetallesTiposCotizantes();
    }

    public void verificarRastro() {
        if (CualTabla == 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (tipoCotizanteSeleccionado != null) {
                int resultado = administrarRastros.obtenerTabla(tipoCotizanteSeleccionado.getSecuencia(), "TIPOSCOTIZANTES");
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
            } else if (administrarRastros.verificarHistoricosTabla("TIPOSCOTIZANTES")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }
        } else if (CualTabla == 1) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (detalleTipoCotizanteSeleccionado != null) {
                int resultadoNF = administrarRastros.obtenerTabla(detalleTipoCotizanteSeleccionado.getSecuencia(), "DETALLESTIPOSCOTIZANTES");
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
            } else if (administrarRastros.verificarHistoricosTabla("DETALLESTIPOSCOTIZANTES")) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoNF').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistoricoNF').show()");
            }
        }
    }

    public void salir() {
        if (bandera == 1) {
            altoTabla = "95";
            tcCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcCodigo");
            tcCodigo.setFilterStyle("display: none; visibility: hidden;");
            tcDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcDescripcion");
            tcDescripcion.setFilterStyle("display: none; visibility: hidden;");
            tcPension = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcPension");
            tcPension.setFilterStyle("display: none; visibility: hidden;");
            tcSalud = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcSalud");
            tcSalud.setFilterStyle("display: none; visibility: hidden;");
            tcRiesgo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcRiesgo");
            tcRiesgo.setFilterStyle("display: none; visibility: hidden;");
            tcParafiscal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcParafiscal");
            tcParafiscal.setFilterStyle("display: none; visibility: hidden;");
            tcEsap = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcEsap");
            tcEsap.setFilterStyle("display: none; visibility: hidden;");
            tcMen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcMen");
            tcMen.setFilterStyle("display: none; visibility: hidden;");
            tcExtranjero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcExtranjero");
            tcExtranjero.setFilterStyle("display: none; visibility: hidden;");
            tcSubtipoCotizante = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcSubtipoCotizante");
            tcSubtipoCotizante.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
            bandera = 0;
            filtradosListaTiposCotizantes = null;
            tipoLista = 0;
        }

        if (banderaNF == 1) {
            altoTablaNF = "95";
            dtcTipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcTipoEntidad");
            dtcTipoEntidad.setFilterStyle("display: none; visibility: hidden;");
            dtcMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcMinimo");
            dtcMinimo.setFilterStyle("display: none; visibility: hidden;");
            dtcMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcMaximo");
            dtcMaximo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
            banderaNF = 0;
            filtradosListaDetallesTiposCotizantes = null;
            tipoListaNF = 0;
        }

        listaTiposCotizantesBorrar.clear();
        listaTiposCotizantesCrear.clear();
        listaTiposCotizantesModificar.clear();
        tipoCotizanteSeleccionado = null;
        listaTiposCotizantes = null;
        listaDetallesTiposCotizantesBorrar.clear();
        listaDetallesTiposCotizantesCrear.clear();
        listaDetallesTiposCotizantesModificar.clear();
        listaDetallesTiposCotizantes = null;
        detalleTipoCotizanteSeleccionado = null;
        guardado = true;
        permitirIndex = true;
        cambiosPagina = true;
        limpiarCamposClonar();
    }

    //CANCELAR MODIFICACIONES
    public void cancelarModificacion() {

        if (bandera == 1) {
            altoTabla = "95";
            tcCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcCodigo");
            tcCodigo.setFilterStyle("display: none; visibility: hidden;");
            tcDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcDescripcion");
            tcDescripcion.setFilterStyle("display: none; visibility: hidden;");
            tcPension = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcPension");
            tcPension.setFilterStyle("display: none; visibility: hidden;");
            tcSalud = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcSalud");
            tcSalud.setFilterStyle("display: none; visibility: hidden;");
            tcRiesgo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcRiesgo");
            tcRiesgo.setFilterStyle("display: none; visibility: hidden;");
            tcParafiscal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcParafiscal");
            tcParafiscal.setFilterStyle("display: none; visibility: hidden;");
            tcEsap = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcEsap");
            tcEsap.setFilterStyle("display: none; visibility: hidden;");
            tcMen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcMen");
            tcMen.setFilterStyle("display: none; visibility: hidden;");
            tcExtranjero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcExtranjero");
            tcExtranjero.setFilterStyle("display: none; visibility: hidden;");
            tcSubtipoCotizante = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposCotizantes:tcSubtipoCotizante");
            tcSubtipoCotizante.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
            bandera = 0;
            filtradosListaTiposCotizantes = null;
            tipoLista = 0;
        }

        if (banderaNF == 1) {
            altoTablaNF = "95";
            dtcTipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcTipoEntidad");

            dtcTipoEntidad.setFilterStyle("display: none; visibility: hidden;");
            dtcMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcMinimo");
            dtcMinimo.setFilterStyle("display: none; visibility: hidden;");
            dtcMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcMaximo");
            dtcMaximo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
            banderaNF = 0;
            filtradosListaDetallesTiposCotizantes = null;
            tipoListaNF = 0;
        }
        listaTiposCotizantesBorrar.clear();
        listaTiposCotizantesCrear.clear();
        listaTiposCotizantesModificar.clear();
        tipoCotizanteSeleccionado = null;
        listaTiposCotizantes = null;
        listaDetallesTiposCotizantesBorrar.clear();
        listaDetallesTiposCotizantesCrear.clear();
        listaDetallesTiposCotizantesModificar.clear();
        detalleTipoCotizanteSeleccionado = null;
        listaDetallesTiposCotizantes = null;
        contarRegistrosDetallesTipoC();
        contarRegistrosTipoC();
        limpiarCamposClonar();
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        cambiosPagina = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
        RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
    }

    //GUARDAR TODO
    public void guardarTodo() {
        try {
            if (!listaTiposCotizantesBorrar.isEmpty()) {
                administrarTiposCotizantes.borrarTipoCotizante(listaTiposCotizantesBorrar);
                listaTiposCotizantesBorrar.clear();
            }
            if (!listaTiposCotizantesCrear.isEmpty()) {
                System.out.println(listaTiposCotizantesCrear.size());
                administrarTiposCotizantes.crearTipoCotizante(listaTiposCotizantesCrear);
                listaTiposCotizantesCrear.clear();
            }

            if (!listaTiposCotizantesModificar.isEmpty()) {
                administrarTiposCotizantes.modificarTipoCotizante(listaTiposCotizantesModificar);
                listaTiposCotizantesModificar.clear();
            }
            listaTiposCotizantes = null;
            getListaTiposCotizantes();
//            if (listaTiposCotizantes != null) {
//                if (!listaTiposCotizantes.isEmpty()) {
//                    tipoCotizanteSeleccionado = listaTiposCotizantes.get(0);
//                }
//            }

            if (!listaDetallesTiposCotizantesBorrar.isEmpty()) {
                administrarDetallesTiposCotizantes.borrarDetalleTipoCotizante(listaDetallesTiposCotizantesBorrar);
                listaDetallesTiposCotizantesBorrar.clear();
            }
            if (!listaDetallesTiposCotizantesCrear.isEmpty()) {
                administrarDetallesTiposCotizantes.crearDetalleTipoCotizante(listaDetallesTiposCotizantesCrear);
                listaDetallesTiposCotizantesCrear.clear();
            }
            if (!listaDetallesTiposCotizantesModificar.isEmpty()) {
                administrarDetallesTiposCotizantes.modificarDetalleTipoCotizante(listaDetallesTiposCotizantesModificar);
                listaDetallesTiposCotizantesModificar.clear();
            }

            listaDetallesTiposCotizantes = null;
            getListaDetallesTiposCotizantes();
            guardado = true;
            permitirIndex = true;
            k = 0;
            detalleTipoCotizanteSeleccionado = null;
            limpiarCamposClonar();
            RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
            RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");

            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception e) {
            System.out.println("entró al catch de guardarTodo : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Hubo un error en el guardado, por favor intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void modificarClonarTiposCotizantes(String confirmarCambio, String valorConfirmar) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("COTIZANTE")) {

            for (int i = 0; i < lovListaTiposCotizantes.size(); i++) {
                if (lovListaTiposCotizantes.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                clonarTipoCotizante = lovListaTiposCotizantes.get(indiceUnicoElemento);
                lovListaTiposCotizantes.clear();
                getLovListaTiposCotizantes();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("form:tiposCotizantesDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposCotizantesDialogo').show()");
                tipoActualizacion = 0;
            }

        }
        RequestContext.getCurrentInstance().update("form:ClonarTipoCotizante");
        RequestContext.getCurrentInstance().update("form:ClonarTipoCotizanteDescripcion");
    }

    public void modificarDetallesTiposCotizantes(DetallesTiposCotizantes detalle) {
        detalleTipoCotizanteSeleccionado = detalle;
        if (!listaDetallesTiposCotizantesCrear.contains(detalleTipoCotizanteSeleccionado)) {

            if (listaDetallesTiposCotizantesModificar.isEmpty()) {
                listaDetallesTiposCotizantesModificar.add(detalleTipoCotizanteSeleccionado);
            } else if (!listaDetallesTiposCotizantesModificar.contains(detalleTipoCotizanteSeleccionado)) {
                listaDetallesTiposCotizantesModificar.add(detalleTipoCotizanteSeleccionado);
            }
            if (guardado == true) {
                guardado = false;
            }
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
    }

    //Ubicacion Celda.
    public void cambiarIndiceNF(DetallesTiposCotizantes detalle, int celdaNF) {

        if (permitirIndex == true) {
            detalleTipoCotizanteSeleccionado = detalle;
            cualCelda = celdaNF;
            CualTabla = 1;
            detalleTipoCotizanteSeleccionado.getSecuencia();
            tipoCotizanteSeleccionado = null;
            tablaImprimir = ":formExportar:datosDetallesTiposCotizantesExportar";
            nombreArchivo = "DetallesTiposCotizantesXML";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:exportarXML");

            if (cualCelda == 0) {
                detalleTipoCotizanteSeleccionado.getTipoentidad().getNombre();
            } else if (cualCelda == 1) {
                detalleTipoCotizanteSeleccionado.getMinimosml();
            } else if (cualCelda == 2) {
                detalleTipoCotizanteSeleccionado.getMaximosml();
            }
        }
    }

    public void asignarIndexNF(DetallesTiposCotizantes detalle, int dlg, int LND) {
        detalleTipoCotizanteSeleccionado = detalle;
        tipoActualizacion = LND;
        if (dlg == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposEntidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').show()");
        } else if (dlg == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposCotizantesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposCotizantesDialogo').show()");
        }

    }

    public void asignarIndexC(int dlg, int LND) {
        tipoActualizacion = LND;
        if (dlg == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposEntidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').show()");
        } else if (dlg == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposCotizantesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposCotizantesDialogo').show()");
        }

    }

    public void actualizarTipoCotizante() {
        RequestContext context = RequestContext.getCurrentInstance();
        clonarTipoCotizante = lovTipoCotizanteSeleccionado;
        RequestContext.getCurrentInstance().update("form:ClonarTipoCotizante");
        RequestContext.getCurrentInstance().update("form:ClonarTipoCotizanteDescripcion");
        context.reset("formularioDialogos:LOVTiposCotizantes:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTiposCotizantes').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposCotizantesDialogo').hide()");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void cancelarCambioLovTipoCotizante() {
        lovFiltradosListaTiposCotizantes = null;
        lovTipoCotizanteSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVTiposCotizantes:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTiposCotizantes').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposCotizantesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposCotizantesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposCotizantes");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTC");

    }

    public void actualizarTipoEntidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            detalleTipoCotizanteSeleccionado.setTipoentidad(seleccionTiposEntidades);
            if (!listaDetallesTiposCotizantesCrear.contains(detalleTipoCotizanteSeleccionado)) {
                if (listaDetallesTiposCotizantesModificar.isEmpty()) {
                    listaDetallesTiposCotizantesModificar.add(detalleTipoCotizanteSeleccionado);
                } else if (!listaDetallesTiposCotizantesModificar.contains(detalleTipoCotizanteSeleccionado)) {
                    listaDetallesTiposCotizantesModificar.add(detalleTipoCotizanteSeleccionado);
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
        } else if (tipoActualizacion == 1) {
            nuevoRegistroDetalleTipoCotizante.setTipoentidad(seleccionTiposEntidades);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoRegistroDetalleTipoCotizante");
        } else if (tipoActualizacion == 2) {
            duplicarRegistroDetalleTipoCotizante.setTipoentidad(seleccionTiposEntidades);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroDetalleTipoCotizante");
        }
        filtradoslovListaTiposEntidades = null;
        seleccionTiposEntidades = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVTiposEntidades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTiposEntidades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposEntidadesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposEntidades");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTE");
    }

    public void cancelarCambioTipoEntidad() {
        filtradoslovListaTiposEntidades = null;
        seleccionTiposEntidades = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVTiposEntidades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTiposEntidades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposEntidadesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposEntidades");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTE");
    }

    public void autocompletarNuevoyDuplicadoNF(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOENTIDAD")) {
            if (tipoNuevo == 1) {
                nuevoRegistroDetalleTipoCotizante.getTipoentidad().setNombre(detalleTipoCotizanteSeleccionado.getTipoentidad().getNombre());
            } else if (tipoNuevo == 2) {
                duplicarRegistroDetalleTipoCotizante.getTipoentidad().setNombre(detalleTipoCotizanteSeleccionado.getTipoentidad().getNombre());
            }
            for (int i = 0; i < lovListaTiposEntidades.size(); i++) {
                if (lovListaTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoRegistroDetalleTipoCotizante.setTipoentidad(lovListaTiposEntidades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoEntidad");
                } else if (tipoNuevo == 2) {
                    duplicarRegistroDetalleTipoCotizante.setTipoentidad(lovListaTiposEntidades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoEntidad");
                }
                lovListaTiposEntidades.clear();
                getLovListaTiposEntidades();

                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposEntidadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoEntidad");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoEntidad");
                }
            }
        }
    }

    public void agregarNuevaDetalleTipoCotizante() {
        int pasa = 0;
        mensajeValidacionNF = " ";
        for (int i = 0; i < listaDetallesTiposCotizantes.size(); i++) {
            if (nuevoRegistroDetalleTipoCotizante.getTipoentidad().getSecuencia().equals(listaDetallesTiposCotizantes.get(i).getTipoentidad().getSecuencia()) == true) {
                pasa++;
                mensajeValidacionNF = "El Tipo Cotizante ya tiene un registro con ese tipo de entidad. Por favor seleccione otro ";
            }
        }
        if (pasa == 0) {
            if (bandera == 1 && CualTabla == 1) {
                altoTablaNF = "95";
                dtcTipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcTipoEntidad");
                dtcTipoEntidad.setFilterStyle("display: none; visibility: hidden;");
                dtcMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcMinimo");
                dtcMinimo.setFilterStyle("display: none; visibility: hidden;");
                dtcMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcMaximo");
                dtcMaximo.setFilterStyle("display: none; visibility: hidden;");

                RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
                bandera = 0;
                filtradosListaDetallesTiposCotizantes = null;
                tipoListaNF = 0;
            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS FORMALES.
            k++;
            l = BigInteger.valueOf(k);
            nuevoRegistroDetalleTipoCotizante.setSecuencia(l);
            nuevoRegistroDetalleTipoCotizante.setTipocotizante(tipoCotizanteSeleccionado);
            listaDetallesTiposCotizantesCrear.add(nuevoRegistroDetalleTipoCotizante);
            listaDetallesTiposCotizantes.add(nuevoRegistroDetalleTipoCotizante);
            contarRegistrosDetallesTipoC();
            detalleTipoCotizanteSeleccionado = nuevoRegistroDetalleTipoCotizante;
            nuevoRegistroDetalleTipoCotizante = new DetallesTiposCotizantes();

            RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroDetalleTipoCotizante').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoDetalleTipoCotizante");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoDetalleTipoCotizante').show()");
        }
    }

    public void tablaNuevoRegistro() {
        RequestContext.getCurrentInstance().update("formularioDialogos:elegirTabla");
        RequestContext.getCurrentInstance().execute("PF('elegirTabla').show()");
    }

    public void confirmarDuplicarNF() {

        int pasa = 0;
        mensajeValidacionNF = " ";
        for (int i = 0; i < listaDetallesTiposCotizantes.size(); i++) {
            if (duplicarRegistroDetalleTipoCotizante.getTipoentidad().getSecuencia().equals(listaDetallesTiposCotizantes.get(i).getTipoentidad().getSecuencia()) == true) {
                pasa++;
                mensajeValidacionNF = "El Tipo Cotizante ya tiene un registro con ese tipo de entidad. Por favor seleccione otro ";
            }
        }

        if (pasa == 0) {
            listaDetallesTiposCotizantes.add(duplicarRegistroDetalleTipoCotizante);
            listaDetallesTiposCotizantesCrear.add(duplicarRegistroDetalleTipoCotizante);
            detalleTipoCotizanteSeleccionado = duplicarRegistroDetalleTipoCotizante;
            contarRegistrosDetallesTipoC();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                altoTablaNF = "95";
                dtcTipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcTipoEntidad");
                dtcTipoEntidad.setFilterStyle("display: none; visibility: hidden;");
                dtcMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcMinimo");
                dtcMinimo.setFilterStyle("display: none; visibility: hidden;");
                dtcMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcMaximo");
                dtcMaximo.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
                bandera = 0;
                filtradosListaDetallesTiposCotizantes = null;
                tipoListaNF = 0;
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroDetalleTipoCotizante");
            duplicarRegistroDetalleTipoCotizante = new DetallesTiposCotizantes();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroDetalleTipoCotizante");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDetalleTipoCotizante').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionDuplicarDetalleTipoCotizante");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarDetalleTipoCotizante').show()");
        }
    }

    public void dialogoTiposCotizantes() {
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroTipoCotizante");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTipoCotizante').show()");
    }

    public void dialogoDetallesTiposCotizantes() {
        RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroDetalleTipoCotizante");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroDetalleTipoCotizante').show()");

    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistrosTipoC();
    }

    public void eventoFiltrarNF() {
        if (tipoListaNF == 0) {
            tipoListaNF = 1;
        }
        contarRegistrosDetallesTipoC();
    }

    public void contarRegistrosTipoC() {
        RequestContext.getCurrentInstance().update("form:infoRegistroTipoC");
    }

    public void contarRegistrosDetallesTipoC() {
        RequestContext.getCurrentInstance().update("form:infoRegistroDetallesTC");
    }

    public void contarRegistrosLovTE() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroLovTE");
    }

    public void contarRegistrosLovTC() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroLovTC");
    }

    public void clonarTipoCotizante() {
        int auxiliar = 0;

        for (int i = 0; i < listaTiposCotizantes.size(); i++) {
            if (clonarCodigo.equals(listaTiposCotizantes.get(i).getCodigo()) == true) {
                auxiliar++;
            }
        }
        if (auxiliar == 0) {
            BigInteger auxsecuenciaClonado;
            auxsecuenciaClonado = administrarTiposCotizantes.clonarTipoCotizante(clonarTipoCotizante.getCodigo(), clonarCodigo, clonarDescripcion, secuenciaClonado);
            System.out.println("secuenciaClonado : " + auxsecuenciaClonado);
            RequestContext.getCurrentInstance().execute("PF('confirmarClonar').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('datosClonadoRepetidos').show()");
        }

    }

    public void recargarTablas() {
        listaTiposCotizantes = null;
        getListaTiposCotizantes();
        if (listaTiposCotizantes != null) {
            if (!listaTiposCotizantes.isEmpty()) {
                tipoCotizanteSeleccionado = listaTiposCotizantes.get(0);
            }
        }
        listaDetallesTiposCotizantes = null;
        getListaDetallesTiposCotizantes();
        contarRegistrosTipoC();
        contarRegistrosDetallesTipoC();
        RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
        RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
        limpiarCamposClonar();

    }

    public void limpiarCamposClonar() {
        clonarTipoCotizante = new TiposCotizantes();
        clonarCodigo = null;
        clonarDescripcion = "";
        RequestContext.getCurrentInstance().update("form:ClonarTipoCotizante");
        RequestContext.getCurrentInstance().update("form:ClonarTipoCotizanteDescripcion");
        RequestContext.getCurrentInstance().update("form:ClonarNuevoTipoCotizante");
        RequestContext.getCurrentInstance().update("form:ClonarDescripcionNuevoTipoCotizante");
    }

//GETTER & SETTER
    public List<TiposCotizantes> getListaTiposCotizantes() {
        if (listaTiposCotizantes == null) {
            listaTiposCotizantes = administrarTiposCotizantes.tiposCotizantes();
        }
        return listaTiposCotizantes;
    }

    public void setListaTiposCotizantes(List<TiposCotizantes> listaTiposCotizantes) {
        this.listaTiposCotizantes = listaTiposCotizantes;
    }

    public List<TiposCotizantes> getFiltradosListaTiposCotizantes() {
        return filtradosListaTiposCotizantes;
    }

    public void setFiltradosListaTiposCotizantes(List<TiposCotizantes> filtradosListaTiposCotizantes) {
        this.filtradosListaTiposCotizantes = filtradosListaTiposCotizantes;
    }

    public List<TiposEntidades> getLovListaTiposEntidades() {
        if (lovListaTiposEntidades == null) {
            lovListaTiposEntidades = administrarDetallesTiposCotizantes.lovTiposEntidades();
        }
        return lovListaTiposEntidades;
    }

    public void setLovListaTiposEntidades(List<TiposEntidades> listaTiposEntidades) {
        this.lovListaTiposEntidades = listaTiposEntidades;
    }

    public List<TiposEntidades> getFiltradoslovListaTiposEntidades() {
        return filtradoslovListaTiposEntidades;
    }

    public void setFiltradoslovListaTiposEntidades(List<TiposEntidades> filtradoslovListaTiposEntidades) {
        this.filtradoslovListaTiposEntidades = filtradoslovListaTiposEntidades;
    }

    public TiposEntidades getSeleccionTiposEntidades() {
        return seleccionTiposEntidades;
    }

    public void setSeleccionTiposEntidades(TiposEntidades seleccionTiposEntidades) {
        this.seleccionTiposEntidades = seleccionTiposEntidades;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public TiposCotizantes getEditarTiposCotizantes() {
        return editarTiposCotizantes;
    }

    public void setEditarTiposCotizantes(TiposCotizantes editarTiposCotizantes) {
        this.editarTiposCotizantes = editarTiposCotizantes;
    }

    public TiposCotizantes getNuevoTipoCotizante() {
        return nuevoTipoCotizante;
    }

    public void setNuevoTipoCotizante(TiposCotizantes nuevoTipoCotizante) {
        this.nuevoTipoCotizante = nuevoTipoCotizante;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public TiposCotizantes getDuplicarTipoCotizante() {
        return duplicarTipoCotizante;
    }

    public void setDuplicarTipoCotizante(TiposCotizantes duplicarTipoCotizante) {
        this.duplicarTipoCotizante = duplicarTipoCotizante;
    }

    public List<DetallesTiposCotizantes> getListaDetallesTiposCotizantes() {
        if (listaDetallesTiposCotizantes == null) {
            if (tipoCotizanteSeleccionado != null) {
                listaDetallesTiposCotizantes = administrarDetallesTiposCotizantes.detallesTiposCotizantes(tipoCotizanteSeleccionado.getSecuencia());
            }
        }
        return listaDetallesTiposCotizantes;
    }

    public void setListaDetallesTiposCotizantes(List<DetallesTiposCotizantes> listaDetallesTiposCotizantes) {
        this.listaDetallesTiposCotizantes = listaDetallesTiposCotizantes;
    }

    public List<DetallesTiposCotizantes> getFiltradosListaDetallesTiposCotizantes() {
        return filtradosListaDetallesTiposCotizantes;
    }

    public void setFiltradosListaDetallesTiposCotizantes(List<DetallesTiposCotizantes> filtradosListaDetallesTiposCotizantes) {
        this.filtradosListaDetallesTiposCotizantes = filtradosListaDetallesTiposCotizantes;
    }

    public DetallesTiposCotizantes getEditarDetallesTiposCotizantes() {
        return editarDetallesTiposCotizantes;
    }

    public void setEditarDetallesTiposCotizantes(DetallesTiposCotizantes editarDetallesTiposCotizantes) {
        this.editarDetallesTiposCotizantes = editarDetallesTiposCotizantes;
    }

    public String getTablaImprimir() {
        return tablaImprimir;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public DetallesTiposCotizantes getNuevaDetalleTipoCotizante() {
        return nuevoRegistroDetalleTipoCotizante;
    }

    public void setNuevaDetalleTipoCotizante(DetallesTiposCotizantes nuevoRegistroDetalleTipoCotizante) {
        this.nuevoRegistroDetalleTipoCotizante = nuevoRegistroDetalleTipoCotizante;
    }

    public String getMensajeValidacionNF() {
        return mensajeValidacionNF;
    }

    public void setMensajeValidacionNF(String mensajeValidacionNF) {
        this.mensajeValidacionNF = mensajeValidacionNF;
    }

    public DetallesTiposCotizantes getDuplicarDetalleTipoCotizante() {
        return duplicarRegistroDetalleTipoCotizante;
    }

    public void setDuplicarDetalleTipoCotizante(DetallesTiposCotizantes duplicarRegistroDetalleTipoCotizante) {
        this.duplicarRegistroDetalleTipoCotizante = duplicarRegistroDetalleTipoCotizante;
    }

    public TiposCotizantes getTipoCotizanteSeleccionado() {
        return tipoCotizanteSeleccionado;
    }

    public void setTipoCotizanteSeleccionado(TiposCotizantes tipoCotizanteSeleccionado) {
        this.tipoCotizanteSeleccionado = tipoCotizanteSeleccionado;
    }

    public TiposCotizantes getClonarTipoCotizante() {
        return clonarTipoCotizante;
    }

    public void setClonarTipoCotizante(TiposCotizantes clonarTipoCotizante) {
        this.clonarTipoCotizante = clonarTipoCotizante;
    }

    public List<TiposCotizantes> getLovListaTiposCotizantes() {
        if (lovListaTiposCotizantes == null) {
            lovListaTiposCotizantes = administrarTiposCotizantes.tiposCotizantes();
        }
        return lovListaTiposCotizantes;
    }

    public void setLovListaTiposCotizantes(List<TiposCotizantes> lovListaTiposCotizantes) {
        this.lovListaTiposCotizantes = lovListaTiposCotizantes;
    }

    public List<TiposCotizantes> getLovFiltradosListaTiposCotizantes() {
        return lovFiltradosListaTiposCotizantes;
    }

    public void setLovFiltradosListaTiposCotizantes(List<TiposCotizantes> lovFiltradosListaTiposCotizantes) {
        this.lovFiltradosListaTiposCotizantes = lovFiltradosListaTiposCotizantes;
    }

    public TiposCotizantes getLovTipoCotizanteSeleccionado() {
        return lovTipoCotizanteSeleccionado;
    }

    public void setLovTipoCotizanteSeleccionado(TiposCotizantes lovTipoCotizanteSeleccionado) {
        this.lovTipoCotizanteSeleccionado = lovTipoCotizanteSeleccionado;
    }

    public BigInteger getClonarCodigo() {
        return clonarCodigo;
    }

    public void setClonarCodigo(BigInteger clonarCodigo) {
        this.clonarCodigo = clonarCodigo;
    }

    public String getClonarDescripcion() {
        return clonarDescripcion;
    }

    public void setClonarDescripcion(String clonarDescripcion) {
        this.clonarDescripcion = clonarDescripcion;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getAltoTablaNF() {
        return altoTablaNF;
    }

    public void setAltoTablaNF(String altoTablaNF) {
        this.altoTablaNF = altoTablaNF;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

    public DetallesTiposCotizantes getDetalleTipoCotizanteSeleccionado() {
        return detalleTipoCotizanteSeleccionado;
    }

    public void setDetalleTipoCotizanteSeleccionado(DetallesTiposCotizantes detalleTipoCotizanteSeleccionado) {
        this.detalleTipoCotizanteSeleccionado = detalleTipoCotizanteSeleccionado;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getInfoRegistroTipoC() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposCotizantes");
        infoRegistroTipoC = String.valueOf(tabla.getRowCount());
        return infoRegistroTipoC;
    }

    public void setInfoRegistroTipoC(String infoRegistroTipoC) {
        this.infoRegistroTipoC = infoRegistroTipoC;
    }

    public String getInfoRegistroDetalleTC() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosDetallesTiposCotizantes");
        infoRegistroDetalleTC = String.valueOf(tabla.getRowCount());
        return infoRegistroDetalleTC;
    }

    public void setInfoRegistroDetalleTC(String infoRegistroDetalleTC) {
        this.infoRegistroDetalleTC = infoRegistroDetalleTC;
    }

    public String getInfoRegistroLovTE() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVTiposEntidades");
        infoRegistroLovTE = String.valueOf(tabla.getRowCount());
        return infoRegistroLovTE;
    }

    public void setInfoRegistroLovTE(String infoRegistroLovTE) {
        this.infoRegistroLovTE = infoRegistroLovTE;
    }

    public String getInfoRegistroLovTC() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVTiposCotizantes");
        infoRegistroLovTC = String.valueOf(tabla.getRowCount());
        return infoRegistroLovTC;
    }

    public void setInfoRegistroLovTC(String infoRegistroLovTC) {
        this.infoRegistroLovTC = infoRegistroLovTC;
    }

    public BigInteger getSecuenciaClonado() {
        return secuenciaClonado;
    }

    public void setSecuenciaClonado(BigInteger secuenciaClonado) {
        this.secuenciaClonado = secuenciaClonado;
    }
}
