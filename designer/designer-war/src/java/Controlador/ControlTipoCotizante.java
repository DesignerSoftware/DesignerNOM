package Controlador;


import Entidades.TiposEntidades;
import Entidades.TiposCotizantes;
import Entidades.DetallesTiposCotizantes;
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
    private List<DetallesTiposCotizantes> detalleTipoCotizanteSeleccionado;
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
    private int index;
    private int indexNF;
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
    //AUTOCOMPLETAR
    private String TipoEntidad;
    //RASTRO
    private BigInteger secRegistro;
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
    //Cual Insertar
    private String cualInsertar;
    //Cual Nuevo Update
    private String cualNuevo;
    private BigInteger secuenciaTipoCotizante;
    //Cambian del Clonar
    private BigInteger clonarCodigo;
    private String clonarDescripcion;
    //ALTO SCROLL TABLA
    private String altoTabla;
    private String altoTablaNF;
    //
    private boolean cambiosPagina;
    //
    private String paginaAnterior;

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
        //Inicializar LOVS
        secRegistro = null;
        //editar
        editarTiposCotizantes = new TiposCotizantes();
        editarDetallesTiposCotizantes = new DetallesTiposCotizantes();
        cambioEditor = false;
        aceptarEditar = true;
        cualCelda = -1;
        tipoLista = 0;
        tipoListaNF = 0;
        //Crear Vigencia Formal
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
        cualInsertar = ":formularioDialogos:NuevoRegistroTipoCotizante";
        cualNuevo = ":formularioDialogos:nuevoRegistroTipoCotizante";
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
    
    public void recibirPagina(String pagina){
        paginaAnterior = pagina;
    }

    public String retornarPagina(){
        return paginaAnterior;
    }
    //Ubicacion Celda Arriba 
    public void cambiarTipoCotizante() {
        //Si ninguna de las 3 listas (crear,modificar,borrar) tiene algo, hace esto
        //{
        CualTabla = 0;
        if (listaDetallesTiposCotizantesCrear.isEmpty() && listaDetallesTiposCotizantesBorrar.isEmpty() && listaDetallesTiposCotizantesModificar.isEmpty()) {
            secuenciaTipoCotizante = tipoCotizanteSeleccionado.getSecuencia();
            listaDetallesTiposCotizantes = null;
            getListaDetallesTiposCotizantes();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");

        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:cambiar");
            RequestContext.getCurrentInstance().execute("PF('cambiar').show()");
        }
    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
    }

    //EVENTO FILTRARNF
    public void eventoFiltrarNF() {
        if (tipoListaNF == 0) {
            tipoListaNF = 1;
        }
    }

    //Ubicacion Celda.
    public void cambiarIndice(int indice, int celda) {
        if (permitirIndex == true) {
            indexNF = -1;
            index = indice;
            cualCelda = celda;
            CualTabla = 0;

            tablaImprimir = ":formExportar:datosTiposCotizantesExportar";
            nombreArchivo = "TiposCotizantesXML";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:exportarXML");
            if (tipoLista == 0) {
                secRegistro = listaTiposCotizantes.get(index).getSecuencia();

            } else {
                secRegistro = filtradosListaTiposCotizantes.get(index).getSecuencia();

            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarCambioLovTipoCotizante() {
        lovFiltradosListaTiposCotizantes = null;
        lovTipoCotizanteSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVTiposCotizantes:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTiposCotizantes').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposCotizantesDialogo').hide()");
    }

    public void cancelarCambioTipoEntidad() {
        filtradoslovListaTiposEntidades = null;
        seleccionTiposEntidades = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVTiposEntidades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTiposEntidades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').hide()");
    }

    public void modificarTiposCotizantes(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaTiposCotizantesCrear.contains(listaTiposCotizantes.get(indice))) {

                    if (listaTiposCotizantesModificar.isEmpty()) {
                        listaTiposCotizantesModificar.add(listaTiposCotizantes.get(indice));
                    } else if (!listaTiposCotizantesModificar.contains(listaTiposCotizantes.get(indice))) {
                        listaTiposCotizantesModificar.add(listaTiposCotizantes.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                indexNF = -1;
                secRegistro = null;

            } else {
                if (!listaTiposCotizantesCrear.contains(filtradosListaTiposCotizantes.get(indice))) {

                    if (listaTiposCotizantesModificar.isEmpty()) {
                        listaTiposCotizantesModificar.add(filtradosListaTiposCotizantes.get(indice));
                    } else if (!listaTiposCotizantesModificar.contains(filtradosListaTiposCotizantes.get(indice))) {
                        listaTiposCotizantesModificar.add(filtradosListaTiposCotizantes.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }

                }
                indexNF = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
        }
    }

    public void seleccionarSubTipoCotizante(String estadoSubTipoCotizante, int indice, int celda) {
        RequestContext context = RequestContext.getCurrentInstance();

        if (tipoLista == 0) {
            if (estadoSubTipoCotizante != null) {

                if (estadoSubTipoCotizante.equals(" ")) {
                    listaTiposCotizantes.get(indice).setSubtipocotizante(null);
                } else if (estadoSubTipoCotizante.equals("1")) {
                    listaTiposCotizantes.get(indice).setSubtipocotizante(Short.valueOf("1"));
                } else if (estadoSubTipoCotizante.equals("2")) {
                    listaTiposCotizantes.get(indice).setSubtipocotizante(Short.valueOf("2"));
                } else if (estadoSubTipoCotizante.equals("3")) {
                    listaTiposCotizantes.get(indice).setSubtipocotizante(Short.valueOf("3"));
                } else if (estadoSubTipoCotizante.equals("4")) {
                    listaTiposCotizantes.get(indice).setSubtipocotizante(Short.valueOf("4"));
                } else if (estadoSubTipoCotizante.equals("5")) {
                    listaTiposCotizantes.get(indice).setSubtipocotizante(Short.valueOf("5"));
                } else if (estadoSubTipoCotizante.equals("6")) {
                    listaTiposCotizantes.get(indice).setSubtipocotizante(Short.valueOf("6"));
                }
            } else {
                listaTiposCotizantes.get(indice).setSubtipocotizante(null);
            }
            if (!listaTiposCotizantesCrear.contains(listaTiposCotizantes.get(indice))) {
                if (listaTiposCotizantesModificar.isEmpty()) {
                    listaTiposCotizantesModificar.add(listaTiposCotizantes.get(indice));
                } else if (!listaTiposCotizantesModificar.contains(listaTiposCotizantes.get(indice))) {
                    listaTiposCotizantesModificar.add(listaTiposCotizantes.get(indice));
                }
            }
        } else {
            if (estadoSubTipoCotizante != null) {
                if (estadoSubTipoCotizante.equals(" ")) {
                    filtradosListaTiposCotizantes.get(indice).setSubtipocotizante(null);
                } else if (estadoSubTipoCotizante.equals("1")) {
                    filtradosListaTiposCotizantes.get(indice).setSubtipocotizante(Short.valueOf("1"));
                } else if (estadoSubTipoCotizante.equals("2")) {
                    filtradosListaTiposCotizantes.get(indice).setSubtipocotizante(Short.valueOf("2"));
                } else if (estadoSubTipoCotizante.equals("3")) {
                    filtradosListaTiposCotizantes.get(indice).setSubtipocotizante(Short.valueOf("3"));
                } else if (estadoSubTipoCotizante.equals("4")) {
                    filtradosListaTiposCotizantes.get(indice).setSubtipocotizante(Short.valueOf("4"));
                } else if (estadoSubTipoCotizante.equals("5")) {
                    filtradosListaTiposCotizantes.get(indice).setSubtipocotizante(Short.valueOf("5"));
                } else if (estadoSubTipoCotizante.equals("6")) {
                    filtradosListaTiposCotizantes.get(indice).setSubtipocotizante(Short.valueOf("6"));
                }
            } else {
                filtradosListaTiposCotizantes.get(indice).setSubtipocotizante(null);
            }
            if (!listaTiposCotizantesCrear.contains(filtradosListaTiposCotizantes.get(indice))) {
                if (listaTiposCotizantesModificar.isEmpty()) {
                    listaTiposCotizantesModificar.add(filtradosListaTiposCotizantes.get(indice));
                } else if (!listaTiposCotizantesModificar.contains(filtradosListaTiposCotizantes.get(indice))) {
                    listaTiposCotizantesModificar.add(filtradosListaTiposCotizantes.get(indice));
                }
            }
        }
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

        RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
        System.out.println("Subtipo: " + listaTiposCotizantes.get(indice).getSubtipocotizante());
    }

    public void seleccionarTipoNuevoTipoCotizante(String estadoSubTipoCotizante, int tipoNuevo) {

        if (tipoNuevo == 1) {
            if (estadoSubTipoCotizante != null) {

                if (estadoSubTipoCotizante.equals(" ")) {
                    nuevoTipoCotizante.setSubtipocotizante(null);
                } else if (estadoSubTipoCotizante.equals("1")) {
                    nuevoTipoCotizante.setSubtipocotizante(new Short("1"));
                } else if (estadoSubTipoCotizante.equals("2")) {
                    nuevoTipoCotizante.setSubtipocotizante(new Short("2"));
                } else if (estadoSubTipoCotizante.equals("3")) {
                    nuevoTipoCotizante.setSubtipocotizante(new Short("3"));
                } else if (estadoSubTipoCotizante.equals("4")) {
                    nuevoTipoCotizante.setSubtipocotizante(new Short("4"));
                } else if (estadoSubTipoCotizante.equals("5")) {
                    nuevoTipoCotizante.setSubtipocotizante(new Short("5"));
                } else if (estadoSubTipoCotizante.equals("6")) {
                    nuevoTipoCotizante.setSubtipocotizante(new Short("6"));
                }
            } else {

                nuevoTipoCotizante.setSubtipocotizante(null);
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoSubTipoCotizacion");
        } else {
            if (estadoSubTipoCotizante != null) {
                if (estadoSubTipoCotizante.equals(" ")) {
                    duplicarTipoCotizante.setSubtipocotizante(null);
                } else if (estadoSubTipoCotizante.equals("1")) {
                    duplicarTipoCotizante.setSubtipocotizante(new Short("1"));
                } else if (estadoSubTipoCotizante.equals("2")) {
                    duplicarTipoCotizante.setSubtipocotizante(new Short("2"));
                } else if (estadoSubTipoCotizante.equals("3")) {
                    duplicarTipoCotizante.setSubtipocotizante(new Short("3"));
                } else if (estadoSubTipoCotizante.equals("4")) {
                    duplicarTipoCotizante.setSubtipocotizante(new Short("4"));
                } else if (estadoSubTipoCotizante.equals("5")) {
                    duplicarTipoCotizante.setSubtipocotizante(new Short("5"));
                } else if (estadoSubTipoCotizante.equals("6")) {
                    duplicarTipoCotizante.setSubtipocotizante(new Short("6"));
                }

            } else {
                duplicarTipoCotizante.setSubtipocotizante(null);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSubTipoCotizacion");
        }

    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (index >= 0 && CualTabla == 0) {
            if (tipoLista == 0) {
                editarTiposCotizantes = listaTiposCotizantes.get(index);
            }
            if (tipoLista == 1) {
                editarTiposCotizantes = filtradosListaTiposCotizantes.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
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
            index = -1;
        } else if (indexNF >= 0 && CualTabla == 1) {
            if (tipoListaNF == 0) {
                editarDetallesTiposCotizantes = listaDetallesTiposCotizantes.get(indexNF);
            }
            if (tipoListaNF == 1) {
                editarDetallesTiposCotizantes = filtradosListaDetallesTiposCotizantes.get(indexNF);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            System.out.println("Cual Tabla: " + CualTabla);
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
            indexNF = -1;
        }

        secRegistro = null;
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (indexNF >= 0 && CualTabla == 1) {
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
        if (bandera == 0 && CualTabla == 0) {
            altoTabla = "75";
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
            RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
            bandera = 1;

        } else if (bandera == 1 && CualTabla == 0) {
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
        } else if (banderaNF == 0 && CualTabla == 1) {
            altoTablaNF = "71";
            dtcTipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcTipoEntidad");
            dtcTipoEntidad.setFilterStyle("width: 85% !important;");
            dtcMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcMinimo");
            dtcMinimo.setFilterStyle("width: 85% !important;");
            dtcMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetallesTiposCotizantes:dtcMaximo");
            dtcMaximo.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
            banderaNF = 1;

        } else if (banderaNF == 1 && CualTabla == 1) {
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
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        if (CualTabla == 0) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposCotizantesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDFTablasAnchas();
            exporter.export(context, tabla, "TiposCotizantesPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
            index = -1;
            secRegistro = null;
        } else {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDetallesTiposCotizantesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDFTablasAnchas();
            exporter.export(context, tabla, "DetallesTiposCotizantesPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexNF = -1;
            secRegistro = null;
        }
    }

    public void exportXLS() throws IOException {
        if (CualTabla == 0) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposCotizantesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "TiposCotizantesXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
            index = -1;
            secRegistro = null;
        } else {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDetallesTiposCotizantesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "DetallesTiposCotizantesXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexNF = -1;
            secRegistro = null;
        }
    }

    //LIMPIAR NUEVO REGISTRO
    public void limpiarNuevoTipoCotizante() {
        nuevoTipoCotizante = new TiposCotizantes();
        nuevoTipoCotizante.setCodigo(BigInteger.valueOf(0));
        nuevoTipoCotizante.setDescripcion(" ");
        index = -1;
        secRegistro = null;

    }

    public void limpiarNuevoDetalleTipoCotizante() {

        nuevoRegistroDetalleTipoCotizante = new DetallesTiposCotizantes();
        indexNF = -1;
        secRegistro = null;
    }

// Agregar Nuevo Tipo Cotizante
    public void agregarNuevoTipoCotizante() {
        int pasa = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTipoCotizante.getCodigo() == BigInteger.valueOf(0)) {
            mensajeValidacion = " * Codigo \n";
            pasa++;
        }
        if (nuevoTipoCotizante.getDescripcion().equals(" ")) {
            mensajeValidacion = mensajeValidacion + " * Descripcion\n";
            pasa++;
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoCotizante");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoCotizante').show()");
        }

        if (pasa == 0) {
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
            System.out.println("Nuevo tipo cotizante Subtipo: " + nuevoTipoCotizante.getSubtipocotizante());
            nuevoTipoCotizante = new TiposCotizantes();
            nuevoTipoCotizante.setCodigo(BigInteger.valueOf(0));
            nuevoTipoCotizante.setDescripcion(" ");
            RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTipoCotizante').hide()");
            index = -1;
            secRegistro = null;
        } else {

        }
    }

//BORRAR VIGENCIA FORMAL
    public void borrarTipoCotizante() {

        if (index >= 0 && CualTabla == 0) {
            if (tipoLista == 0) {
                if (!listaTiposCotizantesModificar.isEmpty() && listaTiposCotizantesModificar.contains(listaTiposCotizantes.get(index))) {
                    int modIndex = listaTiposCotizantesModificar.indexOf(listaTiposCotizantes.get(index));
                    listaTiposCotizantesModificar.remove(modIndex);
                    listaTiposCotizantesBorrar.add(listaTiposCotizantes.get(index));
                } else if (!listaTiposCotizantesCrear.isEmpty() && listaTiposCotizantesCrear.contains(listaTiposCotizantes.get(index))) {
                    int crearIndex = listaTiposCotizantesCrear.indexOf(listaTiposCotizantes.get(index));
                    listaTiposCotizantesCrear.remove(crearIndex);
                } else {
                    listaTiposCotizantesBorrar.add(listaTiposCotizantes.get(index));
                }
                listaTiposCotizantes.remove(index);
            }

            if (tipoLista == 1) {
                if (!listaTiposCotizantesModificar.isEmpty() && listaTiposCotizantesModificar.contains(filtradosListaTiposCotizantes.get(index))) {
                    int modIndex = listaTiposCotizantesModificar.indexOf(filtradosListaTiposCotizantes.get(index));
                    listaTiposCotizantesModificar.remove(modIndex);
                    listaTiposCotizantesBorrar.add(filtradosListaTiposCotizantes.get(index));
                } else if (!listaTiposCotizantesCrear.isEmpty() && listaTiposCotizantesCrear.contains(filtradosListaTiposCotizantes.get(index))) {
                    int crearIndex = listaTiposCotizantesCrear.indexOf(filtradosListaTiposCotizantes.get(index));
                    listaTiposCotizantesCrear.remove(crearIndex);
                } else {
                    listaTiposCotizantesBorrar.add(filtradosListaTiposCotizantes.get(index));
                }
                int CIndex = listaTiposCotizantes.indexOf(filtradosListaTiposCotizantes.get(index));
                listaTiposCotizantes.remove(CIndex);
                filtradosListaTiposCotizantes.remove(index);
                System.out.println("Realizado");
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
            index = -1;
            secRegistro = null;

            RequestContext.getCurrentInstance().update("form:ACEPTAR");

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else if (indexNF >= 0 && CualTabla == 1) {

            if (tipoListaNF == 0) {
                if (!listaDetallesTiposCotizantesModificar.isEmpty() && listaDetallesTiposCotizantesModificar.contains(listaDetallesTiposCotizantesModificar.get(indexNF))) {
                    int modIndex = listaDetallesTiposCotizantesModificar.indexOf(listaDetallesTiposCotizantes.get(indexNF));
                    listaDetallesTiposCotizantesModificar.remove(modIndex);
                    listaDetallesTiposCotizantesBorrar.add(listaDetallesTiposCotizantes.get(indexNF));
                } else if (!listaDetallesTiposCotizantesCrear.isEmpty() && listaDetallesTiposCotizantesCrear.contains(listaDetallesTiposCotizantes.get(indexNF))) {
                    int crearIndex = listaDetallesTiposCotizantesCrear.indexOf(listaDetallesTiposCotizantes.get(indexNF));
                    listaDetallesTiposCotizantesCrear.remove(crearIndex);
                } else {
                    listaDetallesTiposCotizantesBorrar.add(listaDetallesTiposCotizantes.get(indexNF));
                }
                listaDetallesTiposCotizantes.remove(indexNF);
            }

            if (tipoListaNF == 1) {
                if (!listaDetallesTiposCotizantesModificar.isEmpty() && listaDetallesTiposCotizantesModificar.contains(filtradosListaDetallesTiposCotizantes.get(indexNF))) {
                    int modIndex = listaDetallesTiposCotizantesModificar.indexOf(filtradosListaDetallesTiposCotizantes.get(indexNF));
                    listaDetallesTiposCotizantesModificar.remove(modIndex);
                    listaDetallesTiposCotizantesBorrar.add(filtradosListaDetallesTiposCotizantes.get(indexNF));
                } else if (!listaDetallesTiposCotizantesCrear.isEmpty() && listaDetallesTiposCotizantesCrear.contains(filtradosListaDetallesTiposCotizantes.get(indexNF))) {
                    int crearIndex = listaDetallesTiposCotizantesCrear.indexOf(filtradosListaDetallesTiposCotizantes.get(indexNF));
                    listaDetallesTiposCotizantesCrear.remove(crearIndex);
                } else {
                    listaDetallesTiposCotizantesBorrar.add(filtradosListaDetallesTiposCotizantes.get(indexNF));
                }
                int CIndex = listaDetallesTiposCotizantes.indexOf(filtradosListaDetallesTiposCotizantes.get(indexNF));
                listaDetallesTiposCotizantes.remove(CIndex);
                filtradosListaDetallesTiposCotizantes.remove(indexNF);
                System.out.println("Realizado");
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");

            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            indexNF = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    //DUPLICAR TIPO COTIZANTE
    public void duplicarTC() {
        if (index >= 0 && CualTabla == 0) {
            duplicarTipoCotizante = new TiposCotizantes();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTipoCotizante.setSecuencia(l);
                duplicarTipoCotizante.setCodigo(listaTiposCotizantes.get(index).getCodigo());
                duplicarTipoCotizante.setDescripcion(listaTiposCotizantes.get(index).getDescripcion());
                duplicarTipoCotizante.setCotizapension(listaTiposCotizantes.get(index).getCotizapension());
                duplicarTipoCotizante.setCotizasalud(listaTiposCotizantes.get(index).getCotizasalud());
                duplicarTipoCotizante.setCotizariesgo(listaTiposCotizantes.get(index).getCotizariesgo());
                duplicarTipoCotizante.setCotizaparafiscal(listaTiposCotizantes.get(index).getCotizaparafiscal());
                duplicarTipoCotizante.setCotizaesap(listaTiposCotizantes.get(index).getCotizaesap());
                duplicarTipoCotizante.setCotizamen(listaTiposCotizantes.get(index).getCotizamen());
                duplicarTipoCotizante.setExtranjero(listaTiposCotizantes.get(index).getExtranjero());
                duplicarTipoCotizante.setSubtipocotizante(listaTiposCotizantes.get(index).getSubtipocotizante());
                duplicarTipoCotizante.setCodigoalternativo(listaTiposCotizantes.get(index).getCodigoalternativo());
            }
            if (tipoLista == 1) {
                duplicarTipoCotizante.setSecuencia(l);
                duplicarTipoCotizante.setCodigo(filtradosListaTiposCotizantes.get(index).getCodigo());
                duplicarTipoCotizante.setDescripcion(filtradosListaTiposCotizantes.get(index).getDescripcion());
                duplicarTipoCotizante.setCotizapension(filtradosListaTiposCotizantes.get(index).getCotizapension());
                duplicarTipoCotizante.setCotizasalud(filtradosListaTiposCotizantes.get(index).getCotizasalud());
                duplicarTipoCotizante.setCotizariesgo(filtradosListaTiposCotizantes.get(index).getCotizariesgo());
                duplicarTipoCotizante.setCotizaparafiscal(filtradosListaTiposCotizantes.get(index).getCotizaparafiscal());
                duplicarTipoCotizante.setCotizaesap(filtradosListaTiposCotizantes.get(index).getCotizaesap());
                duplicarTipoCotizante.setCotizamen(filtradosListaTiposCotizantes.get(index).getCotizamen());
                duplicarTipoCotizante.setExtranjero(filtradosListaTiposCotizantes.get(index).getExtranjero());
                duplicarTipoCotizante.setSubtipocotizante(filtradosListaTiposCotizantes.get(index).getSubtipocotizante());
                duplicarTipoCotizante.setCodigoalternativo(filtradosListaTiposCotizantes.get(index).getCodigoalternativo());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroTipoCotizante");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTipoCotizante').show()");
            index = -1;
            secRegistro = null;
        } else if (indexNF >= 0 && CualTabla == 1) {
            System.out.println("Entra Duplicar NF");

            duplicarRegistroDetalleTipoCotizante = new DetallesTiposCotizantes();
            m++;
            n = BigInteger.valueOf(m);

            if (tipoListaNF == 0) {
                duplicarRegistroDetalleTipoCotizante.setSecuencia(n);
                duplicarRegistroDetalleTipoCotizante.setTipoentidad(listaDetallesTiposCotizantes.get(indexNF).getTipoentidad());
                duplicarRegistroDetalleTipoCotizante.setMinimosml(listaDetallesTiposCotizantes.get(indexNF).getMinimosml());
                duplicarRegistroDetalleTipoCotizante.setMaximosml(listaDetallesTiposCotizantes.get(indexNF).getMaximosml());
            }
            if (tipoListaNF == 1) {
                duplicarRegistroDetalleTipoCotizante.setSecuencia(n);
                duplicarRegistroDetalleTipoCotizante.setTipoentidad(filtradosListaDetallesTiposCotizantes.get(indexNF).getTipoentidad());
                duplicarRegistroDetalleTipoCotizante.setMinimosml(filtradosListaDetallesTiposCotizantes.get(indexNF).getMinimosml());
                duplicarRegistroDetalleTipoCotizante.setMaximosml(filtradosListaDetallesTiposCotizantes.get(indexNF).getMaximosml());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroDetalleTipoCotizante");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDetalleTipoCotizante').show()");
            indexNF = -1;
            secRegistro = null;

        }
    }

    public void confirmarDuplicar() {

        listaTiposCotizantes.add(duplicarTipoCotizante);
        listaTiposCotizantesCrear.add(duplicarTipoCotizante);
        RequestContext context = RequestContext.getCurrentInstance();

        index = -1;
        secRegistro = null;
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

    }
    //LIMPIAR DUPLICAR

    public void limpiarduplicarTipoCotizante() {
        duplicarTipoCotizante = new TiposCotizantes();
    }
    //LIMPIAR DUPLICAR NO FORMAL

    public void limpiarduplicarRegistroDetalleTipoCotizante() {
        duplicarRegistroDetalleTipoCotizante = new DetallesTiposCotizantes();
    }

    public void verificarRastro() {
        if (CualTabla == 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("lol");
            if (!listaTiposCotizantes.isEmpty()) {
                if (secRegistro != null) {
                    System.out.println("lol 2");
                    int resultado = administrarRastros.obtenerTabla(secRegistro, "TIPOSCOTIZANTES");
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
                if (administrarRastros.verificarHistoricosTabla("TIPOSCOTIZANTES")) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
                } else {
                    RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
                }

            }
            index = -1;
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("NF");
            if (!listaDetallesTiposCotizantes.isEmpty()) {
                if (secRegistro != null) {
                    System.out.println("NF2");
                    int resultadoNF = administrarRastros.obtenerTabla(secRegistro, "DETALLESTIPOSCOTIZANTES");
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
                } else {
                    RequestContext.getCurrentInstance().execute("PF('seleccionarRegistroNF').show()");
                }
            } else {
                if (administrarRastros.verificarHistoricosTabla("DETALLESTIPOSCOTIZANTES")) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoNF').show()");
                } else {
                    RequestContext.getCurrentInstance().execute("PF('errorRastroHistoricoNF').show()");
                }

            }
            index = -1;
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
        index = -1;
        secRegistro = null;

        listaTiposCotizantes = null;

        listaDetallesTiposCotizantesBorrar.clear();
        listaDetallesTiposCotizantesCrear.clear();
        listaDetallesTiposCotizantesModificar.clear();
        indexNF = -1;

        listaDetallesTiposCotizantes = null;
        guardado = true;
        permitirIndex = true;
        cambiosPagina = true;

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
        index = -1;
        secRegistro = null;

        listaTiposCotizantes = null;

        listaDetallesTiposCotizantesBorrar.clear();
        listaDetallesTiposCotizantesCrear.clear();
        listaDetallesTiposCotizantesModificar.clear();
        indexNF = -1;

        listaDetallesTiposCotizantes = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        cambiosPagina = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
        RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
    }

    //GUARDAR
    public void guardarCambiosTiposCotizantes() {
        if (CualTabla == 0) {
            System.out.println("Guardado: " + guardado);
            if (guardado == false) {
                System.out.println("Realizando Operaciones TiposCotizantes");
                if (!listaTiposCotizantesBorrar.isEmpty()) {
                    for (int i = 0; i < listaTiposCotizantesBorrar.size(); i++) {
                        System.out.println("Borrando...");
                        administrarTiposCotizantes.borrarTipoCotizante(listaTiposCotizantesBorrar.get(i));
                    }

                    System.out.println("Entra");
                    listaTiposCotizantesBorrar.clear();
                }
            }
            if (!listaTiposCotizantesCrear.isEmpty()) {
                for (int i = 0; i < listaTiposCotizantesCrear.size(); i++) {
                    System.out.println("Creando...");
                    System.out.println(listaTiposCotizantesCrear.size());
                    administrarTiposCotizantes.crearTipoCotizante(listaTiposCotizantesCrear.get(i));
                }

            }

            System.out.println("LimpiaLista");
            listaTiposCotizantesCrear.clear();

            if (!listaTiposCotizantesModificar.isEmpty()) {
                administrarTiposCotizantes.modificarTipoCotizante(listaTiposCotizantesModificar);
                listaTiposCotizantesModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaTiposCotizantes = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
            guardado = true;
            permitirIndex = true;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:aceptar");
            index = -1;
            secRegistro = null;
            //  k = 0;
        } else {

            System.out.println("Está en la Tabla de Abajo");

            if (guardado == false) {
                System.out.println("Realizando Operaciones DetallesTiposCotizantes");
                if (!listaDetallesTiposCotizantesBorrar.isEmpty()) {
                    for (int i = 0; i < listaDetallesTiposCotizantesBorrar.size(); i++) {
                        System.out.println("Borrando...");
                        administrarDetallesTiposCotizantes.borrarDetalleTipoCotizante(listaDetallesTiposCotizantesBorrar.get(i));
                        System.out.println("Entra");
                        listaDetallesTiposCotizantesBorrar.clear();
                    }
                }
                if (!listaDetallesTiposCotizantesCrear.isEmpty()) {
                    for (int i = 0; i < listaDetallesTiposCotizantesCrear.size(); i++) {
                        System.out.println("Creando...");
                        System.out.println(listaDetallesTiposCotizantesCrear.size());
                        administrarDetallesTiposCotizantes.crearDetalleTipoCotizante(listaDetallesTiposCotizantesCrear.get(i));
                    }
                }

                System.out.println("LimpiaLista");
                listaDetallesTiposCotizantesCrear.clear();
            }
            if (!listaDetallesTiposCotizantesModificar.isEmpty()) {
                administrarDetallesTiposCotizantes.modificarDetalleTipoCotizante(listaDetallesTiposCotizantesModificar);
                listaDetallesTiposCotizantesModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaDetallesTiposCotizantes = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:aceptar");
            //  k = 0;
        }
        System.out.println("Valor k: " + k);
        indexNF = -1;
        secRegistro = null;
    }

    //GUARDAR TODO
    public void guardarTodo() {

        System.out.println("Guardado: " + guardado);
        System.out.println("Guardando Primera Tabla: ");
        if (guardado == false) {
            System.out.println("Realizando Operaciones TiposCotizantes");
            if (!listaTiposCotizantesBorrar.isEmpty()) {
                for (int i = 0; i < listaTiposCotizantesBorrar.size(); i++) {
                    System.out.println("Borrando...");

                    administrarTiposCotizantes.borrarTipoCotizante(listaTiposCotizantesBorrar.get(i));

                    listaTiposCotizantesBorrar.clear();
                }
            }
            if (!listaDetallesTiposCotizantesBorrar.isEmpty()) {
                for (int i = 0; i < listaDetallesTiposCotizantesBorrar.size(); i++) {
                    System.out.println("Borrando...");
                    administrarDetallesTiposCotizantes.borrarDetalleTipoCotizante(listaDetallesTiposCotizantesBorrar.get(i));
                    System.out.println("Entra");
                    listaDetallesTiposCotizantesBorrar.clear();
                }
            }
            if (!listaTiposCotizantesCrear.isEmpty()) {
                for (int i = 0; i < listaTiposCotizantesCrear.size(); i++) {
                    System.out.println("Creando...");
                    System.out.println(listaTiposCotizantesCrear.size());

                    administrarTiposCotizantes.crearTipoCotizante(listaTiposCotizantesCrear.get(i));
                }

            }
            System.out.println("LimpiaLista");
            listaTiposCotizantesCrear.clear();
        }

        if (!listaDetallesTiposCotizantesCrear.isEmpty()) {
            for (int i = 0; i < listaDetallesTiposCotizantesCrear.size(); i++) {
                System.out.println("Creando...");
                System.out.println(listaDetallesTiposCotizantesCrear.size());

                administrarDetallesTiposCotizantes.crearDetalleTipoCotizante(listaDetallesTiposCotizantesCrear.get(i));

            }

            System.out.println("LimpiaLista");
            listaDetallesTiposCotizantesCrear.clear();
        }

        if (!listaTiposCotizantesModificar.isEmpty()) {
            administrarTiposCotizantes.modificarTipoCotizante(listaTiposCotizantesModificar);
            listaTiposCotizantesModificar.clear();
        }
        if (!listaDetallesTiposCotizantesModificar.isEmpty()) {
            administrarDetallesTiposCotizantes.modificarDetalleTipoCotizante(listaDetallesTiposCotizantesModificar);
            listaDetallesTiposCotizantesModificar.clear();
        }

        System.out.println("Se guardaron los datos con exito");
        listaTiposCotizantes = null;
        RequestContext context = RequestContext.getCurrentInstance();
        cambiosPagina = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
        RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
        guardado = true;
        permitirIndex = true;
        FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().update("form:growl");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        indexNF = -1;
        secRegistro = null;
        index = -1;

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

    //<--------------------------------------------METODOS VIGENCIAS NO FORMALES--------------------------------------------->
    //AUTOCOMPLETAR
    public void modificarDetallesTiposCotizantes(int indiceNF, String confirmarCambio, String valorConfirmar) {
        indexNF = indiceNF;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoListaNF == 0) {
                if (!listaDetallesTiposCotizantesCrear.contains(listaDetallesTiposCotizantes.get(indiceNF))) {

                    if (listaDetallesTiposCotizantesModificar.isEmpty()) {
                        listaDetallesTiposCotizantesModificar.add(listaDetallesTiposCotizantes.get(indiceNF));
                    } else if (!listaDetallesTiposCotizantesModificar.contains(listaDetallesTiposCotizantes.get(indiceNF))) {
                        listaDetallesTiposCotizantesModificar.add(listaDetallesTiposCotizantes.get(indiceNF));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
                indexNF = -1;
                secRegistro = null;

            } else {
                if (!listaDetallesTiposCotizantesCrear.contains(filtradosListaDetallesTiposCotizantes.get(indiceNF))) {

                    if (listaDetallesTiposCotizantesModificar.isEmpty()) {
                        listaDetallesTiposCotizantesModificar.add(filtradosListaDetallesTiposCotizantes.get(indiceNF));
                    } else if (!listaDetallesTiposCotizantesModificar.contains(filtradosListaDetallesTiposCotizantes.get(indiceNF))) {
                        listaDetallesTiposCotizantesModificar.add(filtradosListaDetallesTiposCotizantes.get(indiceNF));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
                indexNF = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
        } else if (confirmarCambio.equalsIgnoreCase("TIPOENTIDAD")) {
            if (tipoListaNF == 0) {
                listaDetallesTiposCotizantes.get(indiceNF).getTipoentidad().setNombre(TipoEntidad);
            } else {
                filtradosListaDetallesTiposCotizantes.get(indiceNF).getTipoentidad().setNombre(TipoEntidad);
            }

            for (int i = 0; i < lovListaTiposEntidades.size(); i++) {
                if (lovListaTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaNF == 0) {
                    listaDetallesTiposCotizantes.get(indiceNF).setTipoentidad(lovListaTiposEntidades.get(indiceUnicoElemento));
                } else {
                    filtradosListaDetallesTiposCotizantes.get(indiceNF).setTipoentidad(lovListaTiposEntidades.get(indiceUnicoElemento));
                }
                lovListaTiposEntidades.clear();
                getLovListaTiposEntidades();

                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposEntidadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').show()");
                tipoActualizacion = 0;
            }

        }
        RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
    }

    //Ubicacion Celda.
    public void cambiarIndiceNF(int indiceNF, int celdaNF) {

        if (permitirIndex == true) {
            indexNF = indiceNF;
            cualCelda = celdaNF;
            CualTabla = 1;
            tablaImprimir = ":formExportar:datosDetallesTiposCotizantesExportar";
            cualNuevo = ":formularioDialogos:nuevoDetalleTipoCotizante";
            cualInsertar = "formularioDialogos:NuevoRegistroDetalleTipoCotizante";
            nombreArchivo = "DetallesTiposCotizantesXML";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:exportarXML");

            if (tipoListaNF == 0) {
                secRegistro = listaDetallesTiposCotizantes.get(indexNF).getSecuencia();
                if (cualCelda == 0) {
                    TipoEntidad = listaDetallesTiposCotizantes.get(indexNF).getTipoentidad().getNombre();
                }
            } else {
                secRegistro = filtradosListaDetallesTiposCotizantes.get(indexNF).getSecuencia();
                if (cualCelda == 0) {
                    TipoEntidad = filtradosListaDetallesTiposCotizantes.get(indexNF).getTipoentidad().getNombre();
                }
            }
        }
    }

    //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO)
    public void asignarIndexNF(Integer indiceNF, int dlg, int LND) {
        index = indiceNF;
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
            index = -1;
            secRegistro = null;
            System.out.println("Tipo Actualizacion: " + tipoActualizacion);
        } else if (LND == 2) {
            index = -1;
            secRegistro = null;
            tipoActualizacion = 2;
        }
        if (dlg == 0) {
            RequestContext.getCurrentInstance().update("form:tiposEntidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').show()");
        } else if (dlg == 1) {
            RequestContext.getCurrentInstance().update("form:tiposCotizantesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposCotizantesDialogo').show()");
        }

    }

    public void asignarIndexC(int dlg, int LND) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
            index = -1;
            secRegistro = null;
            System.out.println("Tipo Actualizacion: " + tipoActualizacion);
        } else if (LND == 2) {
            index = -1;
            secRegistro = null;
            tipoActualizacion = 2;
        }
        if (dlg == 0) {
            RequestContext.getCurrentInstance().update("form:tiposEntidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').show()");
        } else if (dlg == 1) {
            RequestContext.getCurrentInstance().update("form:tiposCotizantesDialogo");
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
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposCotizantes");

        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void actualizarTipoEntidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaNF == 0) {
                listaDetallesTiposCotizantes.get(index).setTipoentidad(seleccionTiposEntidades);
                if (!listaDetallesTiposCotizantesCrear.contains(listaDetallesTiposCotizantes.get(index))) {
                    if (listaDetallesTiposCotizantesModificar.isEmpty()) {
                        listaDetallesTiposCotizantesModificar.add(listaDetallesTiposCotizantes.get(index));
                    } else if (!listaDetallesTiposCotizantesModificar.contains(listaDetallesTiposCotizantes.get(index))) {
                        listaDetallesTiposCotizantesModificar.add(listaDetallesTiposCotizantes.get(index));
                    }
                }
            } else {
                filtradosListaDetallesTiposCotizantes.get(index).setTipoentidad(seleccionTiposEntidades);
                if (!listaDetallesTiposCotizantesCrear.contains(filtradosListaDetallesTiposCotizantes.get(index))) {
                    if (listaDetallesTiposCotizantesModificar.isEmpty()) {
                        listaDetallesTiposCotizantesModificar.add(filtradosListaDetallesTiposCotizantes.get(index));
                    } else if (!listaDetallesTiposCotizantesModificar.contains(filtradosListaDetallesTiposCotizantes.get(index))) {
                        listaDetallesTiposCotizantesModificar.add(filtradosListaDetallesTiposCotizantes.get(index));
                    }
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
        indexNF = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVTiposEntidades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTiposEntidades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposEntidades");
    }

    public void autocompletarNuevoyDuplicadoNF(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOENTIDAD")) {
            if (tipoNuevo == 1) {
                nuevoRegistroDetalleTipoCotizante.getTipoentidad().setNombre(TipoEntidad);
            } else if (tipoNuevo == 2) {
                duplicarRegistroDetalleTipoCotizante.getTipoentidad().setNombre(TipoEntidad);
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
                RequestContext.getCurrentInstance().update("form:tiposEntidadesDialogo");
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

    public void valoresBackupAutocompletarNF(int tipoNuevo, String Campo) {
        if (Campo.equals("TIPOENTIDAD")) {
            if (tipoNuevo == 1) {
                TipoEntidad = nuevoRegistroDetalleTipoCotizante.getTipoentidad().getNombre();
            } else if (tipoNuevo == 2) {
                TipoEntidad = duplicarRegistroDetalleTipoCotizante.getTipoentidad().getNombre();
            }
        }
    }

    //CREAR NUEVA VIGENCIA NO FORMAL
    public void agregarNuevaDetalleTipoCotizante() {
        int pasa = 0;
        mensajeValidacionNF = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("Tamaño Lista Vigencias NF Modificar" + listaDetallesTiposCotizantesModificar.size());

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

            if (nuevoRegistroDetalleTipoCotizante.getMinimosml() == null) {
                nuevoRegistroDetalleTipoCotizante.setMinimosml(null);
            }
            if (nuevoRegistroDetalleTipoCotizante.getMaximosml() == null) {
                nuevoRegistroDetalleTipoCotizante.setMaximosml(null);
            }

            listaDetallesTiposCotizantesCrear.add(nuevoRegistroDetalleTipoCotizante);
            listaDetallesTiposCotizantes.add(nuevoRegistroDetalleTipoCotizante);

            nuevoRegistroDetalleTipoCotizante = new DetallesTiposCotizantes();

            RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroDetalleTipoCotizante').hide()");
            indexNF = -1;
            secRegistro = null;
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoDetalleTipoCotizante");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoDetalleTipoCotizante').show()");
        }
    }

    public void tablaNuevoRegistro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("Cual Tabla: " + CualTabla);
        if (tipoCotizanteSeleccionado != null && listaDetallesTiposCotizantes.isEmpty()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:elegirTabla");
            RequestContext.getCurrentInstance().execute("PF('elegirTabla').show()");
        }

        if ((listaTiposCotizantes.isEmpty() || listaDetallesTiposCotizantes.isEmpty())) {
            RequestContext.getCurrentInstance().update("formularioDialogos:elegirTabla");
            RequestContext.getCurrentInstance().execute("PF('elegirTabla').show()");
        } else if (CualTabla == 0) {
            System.out.println("cual = 0");
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroTipoCotizante");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTipoCotizante').show()");
        } else if (CualTabla == 1) {
            System.out.println("cual = 1");
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroDetalleTipoCotizante");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroDetalleTipoCotizante').show()");
        }
    }

    public void confirmarDuplicarNF() {

        listaDetallesTiposCotizantes.add(duplicarRegistroDetalleTipoCotizante);
        listaDetallesTiposCotizantesCrear.add(duplicarRegistroDetalleTipoCotizante);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
        indexNF = -1;
        secRegistro = null;
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
        RequestContext.getCurrentInstance().update("form:DuplicarRegistroDetalleTipoCotizante");
        duplicarRegistroDetalleTipoCotizante = new DetallesTiposCotizantes();
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroDetalleTipoCotizante");
        RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDetalleTipoCotizante').hide()");

    }

    public void dialogoTiposCotizantes() {
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTipoCotizante').show()");
    }

    public void dialogoDetallesTiposCotizantes() {
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroDetalleTipoCotizante').show()");

    }

    //<--------------------------------------------FIN METODOS VIGENCIAS NO FORMALES ----------------------------------------->
//GETTER & SETTER
    public BigInteger getSecuenciaTipoCotizante() {
        return secuenciaTipoCotizante;
    }

    public void setSecuenciaTipoCotizante(BigInteger secuenciaTipoCotizante) {
        this.secuenciaTipoCotizante = secuenciaTipoCotizante;
    }

    public List<TiposCotizantes> getListaTiposCotizantes() {
        if (listaTiposCotizantes == null) {
            RequestContext context = RequestContext.getCurrentInstance();
            listaTiposCotizantes = administrarTiposCotizantes.tiposCotizantes();
            tipoCotizanteSeleccionado = listaTiposCotizantes.get(0);
            RequestContext.getCurrentInstance().update("form:datosTiposCotizantes");
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

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    // Cosas de Vigencias No Formales
    public List<DetallesTiposCotizantes> getListaDetallesTiposCotizantes() {
        if (listaDetallesTiposCotizantes == null) {
            listaDetallesTiposCotizantes = new ArrayList<DetallesTiposCotizantes>();
            listaDetallesTiposCotizantes = administrarDetallesTiposCotizantes.detallesTiposCotizantes(tipoCotizanteSeleccionado.getSecuencia());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosDetallesTiposCotizantes");

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

    public String getCualInsertar() {
        return cualInsertar;
    }

    public String getCualNuevo() {
        return cualNuevo;
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

    public List<DetallesTiposCotizantes> getDetalleTipoCotizanteSeleccionado() {
        return detalleTipoCotizanteSeleccionado;
    }

    public void setDetalleTipoCotizanteSeleccionado(List<DetallesTiposCotizantes> detalleTipoCotizanteSeleccionado) {
        this.detalleTipoCotizanteSeleccionado = detalleTipoCotizanteSeleccionado;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

}
