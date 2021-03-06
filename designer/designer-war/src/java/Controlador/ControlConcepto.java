/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import ControlNavegacion.ListasRecurrentes;
import Entidades.Conceptos;
import Entidades.Empresas;
import Entidades.Terceros;
import Entidades.Unidades;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarConceptosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlConcepto implements Serializable {

    private static Logger log = Logger.getLogger(ControlConcepto.class);

    @EJB
    AdministrarConceptosInterface administrarConceptos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<Conceptos> listaConceptosEmpresa;
    private List<Conceptos> filtradoConceptosEmpresa;
    //Lista de valores de unidades
    private List<Unidades> lovUnidades;
    private Unidades unidadSeleccionada;
    private List<Unidades> filtradoUnidades;
    //Lista de valores de terceros
    private List<Terceros> lovTerceros;
    private Terceros terceroSeleccionado;
    private List<Terceros> filtradoTerceros;
    //Lista de empresas para cargar datos
    private List<Empresas> lovEmpresas;
    private Empresas empresaActual;
    private List<Empresas> filtradoListaEmpresas;
    //Lista completa de conceptos para clonar
    private List<Conceptos> lovConceptosEmpresa;
    private Conceptos conceptoSeleccionadoLOV;
    private List<Conceptos> filtradoConceptosEmpresaLOV;
    private Conceptos conceptoSeleccionado;
    private Empresas backUpEmpresaActual;
    private Map<String, String> conjuntoC;
    private String estadoConceptoEmpresa;
    private String backUpEstadoConceptoEmpresa;
    private boolean verCambioEmpresa;
    private boolean verCambioEstado;
    private boolean verSeleccionConcepto;
    private boolean verMostrarTodos;
    private boolean mostrarTodos;
    private int tipoActualizacion;
    private boolean permitirIndex, activarLov;
    //Activo/Desactivo Crtl + F11
    private int bandera;
    //Columnas Tabla VC
    private Column columnaCodigo, columnaDescripción, columnaNaturaleza, columnaCodigoUnidad, columnaNombreUnidad, columnaCodigoDesprendible, columnaDescripcionDesplendible,
            columnaIndependienteConcepto, columnaConjunto, columnaFechaAcumulado, columnaNombreTercero, columnaEstado, columnaEnvio, columnaCodigoAlternativo;
    //Otros
    private boolean aceptar;
    //modificar
    private List<Conceptos> listaConceptosEmpresaModificar;
    private boolean guardado;
    //crear VC
    private Conceptos nuevoConcepto;
    private List<Conceptos> listaConceptosEmpresaCrear;
    private BigInteger nuevoConceptoSecuencia;
    private int paraNuevoConcepto;
    private String mensajeValidacion;
    //borrar VC
    private List<Conceptos> listaConceptosBorrar;
    //editar celda
    //private Conceptos editarConcepto;
    private int cualCelda, tipoLista;
    //duplicar
    private Conceptos duplicarConcepto;
    //AUTOCOMPLETAR
    private String codigoUnidad, nombreUnidad, tercero, naturaleza;
    //RASTRO
    //CLONAR CONCEPTO
    private Conceptos conceptoOriginal;
    private Conceptos conceptoClon;
    private int cambioConcepto;
    private String paginaAnterior;
    private Map<String, Object> mapParametros;
    //
    private String altoTablaReg, altoTablaH;
    private String infoRegistro;
    private String infoRegistroUnidad, infoRegistroTercero, infoRegistroEmpresa, infoRegistroConcepto;
    //
    private boolean activoDetalle;
    //Advertencias
    private boolean continuarNuevoNat;
    private DataTable tabla;
    //recordar seleccion
    private boolean unaVez;
//   private List<String> listaNavegacion;
    private ListasRecurrentes listasRecurrentes;
    private String msgError;

    public ControlConcepto() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        listasRecurrentes = controlListaNavegacion.getListasRecurrentes();
        activoDetalle = true;
//      altoTablaReg = "205";
        lovUnidades = null;
        lovTerceros = null;
        lovEmpresas = null;
        empresaActual = null;
        backUpEmpresaActual = new Empresas();
        aceptar = true;
        listaConceptosBorrar = new ArrayList<Conceptos>();
        listaConceptosEmpresaCrear = new ArrayList<Conceptos>();
        paraNuevoConcepto = 0;
        listaConceptosEmpresaModificar = new ArrayList<Conceptos>();
        mostrarTodos = true;
        cualCelda = -1;
        tipoLista = 0;
        guardado = true;
        nuevoConcepto = new Conceptos();
        nuevoConcepto.setConjunto(Short.parseShort("45"));
        duplicarConcepto = new Conceptos();
        //CLON
        conceptoClon = new Conceptos();
        conceptoOriginal = new Conceptos();
        permitirIndex = true;
        verCambioEmpresa = false;
        verCambioEstado = false;
        conjuntoC = new LinkedHashMap<String, String>();
        int i = 0;
        conjuntoC.put("45", "45");
        while (i <= 43) {
            i++;
            conjuntoC.put("" + i + "", "" + i + "");
        }
        continuarNuevoNat = false;
        estadoConceptoEmpresa = "S";
        unaVez = true;
        activarLov = true;

        infoRegistroUnidad = "0";
        infoRegistroTercero = "0";
        infoRegistroEmpresa = "0";
        infoRegistroConcepto = "0";
        paginaAnterior = "nominaf";
        mapParametros = new LinkedHashMap<String, Object>();
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    public void limpiarListasValor() {
        lovConceptosEmpresa = null;
        lovEmpresas = null;
        lovTerceros = null;
        lovUnidades = null;
    }

    @PreDestroy
    public void destruyendoce() {
        log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarConceptos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            estadoConceptoEmpresa = "S";
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ": " + e + " " + "Causa: " + e.getCause());
        }
    }

    public void cambiarEstado() {
        if (guardado) {
            if (bandera == 1) {
                cargarTablaDefault();
            }
            listaConceptosEmpresa = null;
            getListaConceptosEmpresa();

            backUpEstadoConceptoEmpresa = estadoConceptoEmpresa;
            RequestContext.getCurrentInstance().update("form:datosConceptos");
            verCambioEstado = false;
        } else {
            verCambioEstado = true;
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
        }
        contarRegistros();
        activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
        cualCelda = -1;
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        inicializarCosas();
        //Inicializar cosas de ser necesario
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        inicializarCosas();
        //Inicializar cosas de ser necesario
    }

    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "concepto";

        if (pag.equals("rastrotabla")) {
            ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
        } else if (pag.equals("rastrotablaH")) {
            ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            controlRastro.historicosTabla("Conceptos", pagActual);
            pag = "rastrotabla";
        } else if (pag.equals("tercero")) {
            ControlTercero controlTercero = (ControlTercero) fc.getApplication().evaluateExpressionGet(fc, "#{controlTercero}", ControlTercero.class);
            controlTercero.recibirPaginaEntrante(pagActual);
        } else if (pag.equals("grupoconcepto")) {
            ControlGrupoConcepto controlGrupoConcepto = (ControlGrupoConcepto) fc.getApplication().evaluateExpressionGet(fc, "#{controlGrupoConcepto}", ControlGrupoConcepto.class);
            controlGrupoConcepto.recibirPaginaEntrante(pagActual);
        } else if (pag.equals("conceptoredondeo")) {
            ControlConceptoRedondeo controlConceptoRedondeo = (ControlConceptoRedondeo) fc.getApplication().evaluateExpressionGet(fc, "#{controlConceptoRedondeo}", ControlConceptoRedondeo.class);
            controlConceptoRedondeo.recibirPaginaEntrante(pagActual);
        } else if (pag.equals("conceptosoporte")) {
            ControlConceptosSoportes controlConceptosSoportes = (ControlConceptosSoportes) fc.getApplication().evaluateExpressionGet(fc, "#{controlConceptosSoportes}", ControlConceptosSoportes.class);
            controlConceptosSoportes.recibirPaginaEntrante(pagActual);
        } else if (pag.equals("unidad")) {
            ControlUnidad controlUnidad = (ControlUnidad) fc.getApplication().evaluateExpressionGet(fc, "#{controlUnidad}", ControlUnidad.class);
            controlUnidad.recibirPaginaEntrante(pagActual);
        }
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
        } else {
            controlListaNavegacion.guardarNavegacion(pagActual, pag);
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
        }
        limpiarListasValor();
    }

    public void inicializarCosas() {
        estadoConceptoEmpresa = "S";
        lovConceptosEmpresa = null;
        backUpEstadoConceptoEmpresa = "S";
        if (lovEmpresas == null) {
            log.info("listaEmpresas esta vacia");
            getLovEmpresas();
        }
        if (empresaActual == null) {
            log.info("empresaActual esta vacia");
            empresaActual = lovEmpresas.get(0);
        }
        listaConceptosEmpresa = null;
        getListaConceptosEmpresa();
    }
//SELECCIONAR NATURALEZA

    public void seleccionarItem(String itemSeleccionado, Conceptos conceptoS, int celda) {
        if (celda == 2) {
            //Si se puede modificar?
            if (administrarConceptos.ValidarUpdateConceptoAcumulados(conceptoS.getSecuencia())) {
                if (itemSeleccionado.equals("NETO")) {
                    conceptoS.setNaturaleza("N");
                } else if (itemSeleccionado.equals("GASTO")) {
                    conceptoS.setNaturaleza("G");
                } else if (itemSeleccionado.equals("DESCUENTO")) {
                    conceptoS.setNaturaleza("D");
                } else if (itemSeleccionado.equals("PAGO")) {
                    conceptoS.setNaturaleza("P");
                } else if (itemSeleccionado.equals("PASIVO")) {
                    conceptoS.setNaturaleza("L");
                }
            } else {
                conceptoS.setNaturaleza(naturaleza);
                refrescar();
                RequestContext.getCurrentInstance().execute("PF('errorNoModificar').show()");
            }
        } else if (celda == 11) {
            if (itemSeleccionado.equals("ACTIVO")) {
                conceptoS.setActivo("S");
            } else if (itemSeleccionado.equals("INACTIVO")) {
                conceptoS.setActivo("N");
            }
        } else if (celda == 12) {
            if (itemSeleccionado.equals("SI")) {
                conceptoS.setEnviotesoreria("S");
            } else if (itemSeleccionado.equals("NO")) {
                conceptoS.setEnviotesoreria("N");
            }
        }
        if (!listaConceptosEmpresaCrear.contains(conceptoS)) {
            if (listaConceptosEmpresaModificar.isEmpty()) {
                listaConceptosEmpresaModificar.add(conceptoS);
            } else if (!listaConceptosEmpresaModificar.contains(conceptoS)) {
                listaConceptosEmpresaModificar.add(conceptoS);
            }
        }
        if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("CODIGOUNIDAD")) {
            if (tipoNuevo == 1) {
                codigoUnidad = nuevoConcepto.getCodigoUnidad();
            } else if (tipoNuevo == 2) {
                codigoUnidad = duplicarConcepto.getCodigoUnidad();
            }
        } else if (Campo.equals("NOMBREUNIDAD")) {
            if (tipoNuevo == 1) {
                nombreUnidad = nuevoConcepto.getNombreUnidad();
            } else if (tipoNuevo == 2) {
                nombreUnidad = duplicarConcepto.getNombreUnidad();
            }
        } else if (Campo.equals("TERCERO")) {
            if (tipoNuevo == 1) {
                tercero = nuevoConcepto.getNombreTercero();
            } else if (tipoNuevo == 2) {
                tercero = duplicarConcepto.getNombreTercero();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        cargarLovs();
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        if (confirmarCambio.equalsIgnoreCase("CODIGOUNIDAD")) {
            if (tipoNuevo == 1) {
                nuevoConcepto.setCodigoUnidad(codigoUnidad);
            } else if (tipoNuevo == 2) {
                duplicarConcepto.setCodigoUnidad(codigoUnidad);
            }
            for (int i = 0; i < lovUnidades.size(); i++) {
                if (lovUnidades.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoConcepto.setUnidad(lovUnidades.get(indiceUnicoElemento).getSecuencia());
                    nuevoConcepto.setNombreUnidad(lovUnidades.get(indiceUnicoElemento).getNombre());
                    nuevoConcepto.setCodigoUnidad(lovUnidades.get(indiceUnicoElemento).getCodigo());
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigoUnidad");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreUnidad");
                } else if (tipoNuevo == 2) {
                    duplicarConcepto.setUnidad(lovUnidades.get(indiceUnicoElemento).getSecuencia());
                    duplicarConcepto.setNombreUnidad(lovUnidades.get(indiceUnicoElemento).getNombre());
                    duplicarConcepto.setCodigoUnidad(lovUnidades.get(indiceUnicoElemento).getCodigo());
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoUnidad");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreUnidad");
                }
                lovUnidades.clear();
                getLovUnidades();
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:unidadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('unidadesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigoUnidad");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreUnidad");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoUnidad");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreUnidad");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("NOMBREUNIDAD")) {
            if (tipoNuevo == 1) {
                nuevoConcepto.setNombreUnidad(nombreUnidad);
            } else if (tipoNuevo == 2) {
                duplicarConcepto.setNombreUnidad(nombreUnidad);
            }
            for (int i = 0; i < lovUnidades.size(); i++) {
                if (lovUnidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoConcepto.setUnidad(lovUnidades.get(indiceUnicoElemento).getSecuencia());
                    nuevoConcepto.setNombreUnidad(lovUnidades.get(indiceUnicoElemento).getNombre());
                    nuevoConcepto.setCodigoUnidad(lovUnidades.get(indiceUnicoElemento).getCodigo());
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigoUnidad");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreUnidad");
                } else if (tipoNuevo == 2) {
                    duplicarConcepto.setUnidad(lovUnidades.get(indiceUnicoElemento).getSecuencia());
                    duplicarConcepto.setNombreUnidad(lovUnidades.get(indiceUnicoElemento).getNombre());
                    duplicarConcepto.setCodigoUnidad(lovUnidades.get(indiceUnicoElemento).getCodigo());
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoUnidad");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreUnidad");
                }
                lovUnidades.clear();
                getLovUnidades();
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:unidadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('unidadesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigoUnidad");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreUnidad");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoUnidad");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreUnidad");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevoConcepto.setNombreTercero(tercero);
                } else if (tipoNuevo == 2) {
                    duplicarConcepto.setNombreTercero(tercero);
                }
                for (int i = 0; i < lovTerceros.size(); i++) {
                    if (lovTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevoConcepto.setTercero(lovTerceros.get(indiceUnicoElemento).getSecuencia());
                        nuevoConcepto.setNombreTercero(lovTerceros.get(indiceUnicoElemento).getNombre());
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTercero");
                    } else if (tipoNuevo == 2) {
                        duplicarConcepto.setTercero(lovTerceros.get(indiceUnicoElemento).getSecuencia());
                        duplicarConcepto.setNombreTercero(lovTerceros.get(indiceUnicoElemento).getNombre());
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTercero");
                    }
                } else {
                    RequestContext.getCurrentInstance().update("formularioDialogos:TercerosDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TercerosDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTercero");
                    } else if (tipoNuevo == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTercero");
                    }
                }
            }
        } else {
            if (tipoNuevo == 1) {
                nuevoConcepto.setTercero(null);
            } else if (tipoNuevo == 2) {
                duplicarConcepto.setTercero(null);
            }
            if (tipoNuevo == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTercero");
            } else if (tipoNuevo == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTercero");
            }
        }
    }

    public void llamarLOVSNuevo_Duplicado(int dlg, int LND) {
        cargarLovs();
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 1) {
            habilitarBotonLov();
            tipoActualizacion = 1;
        } else if (LND == 2) {
            habilitarBotonLov();
            tipoActualizacion = 2;
        }
        activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
        if (dlg == 0) {
            habilitarBotonLov();
            contarRegistrosUnidades();
            RequestContext.getCurrentInstance().update("formularioDialogos:unidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('unidadesDialogo').show()");
        } else if (dlg == 1) {
            habilitarBotonLov();
            contarRegistrosTercero();
            RequestContext.getCurrentInstance().update("formularioDialogos:TercerosDialogo");
            RequestContext.getCurrentInstance().execute("PF('TercerosDialogo').show()");
        }
    }

    public void llamarDialogosLista(Conceptos conceptoS, int columnD) {
        cargarLovs();
        conceptoSeleccionado = conceptoS;
        tipoActualizacion = 0;
        if (columnD == 0) {
            contarRegistrosUnidades();
            RequestContext.getCurrentInstance().update("formularioDialogos:unidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('unidadesDialogo').show()");
        } else if (columnD == 1) {
            contarRegistrosTercero();
            RequestContext.getCurrentInstance().update("formularioDialogos:TercerosDialogo");
            RequestContext.getCurrentInstance().execute("PF('TercerosDialogo').show()");
        }
        habilitarBotonLov();
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        cargarLovs();
        unaVez = true;
        RequestContext context = RequestContext.getCurrentInstance();
        if (conceptoSeleccionado != null) {
            //Si la columna es Unidades
            if (cualCelda == 3 || cualCelda == 4) {
                habilitarBotonLov();
                contarRegistrosUnidades();
                RequestContext.getCurrentInstance().update("formularioDialogos:unidadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('unidadesDialogo').show()");
                tipoActualizacion = 0;
                //Si la columna es tercersos
            } else if (cualCelda == 10) {
                habilitarBotonLov();
                tipoActualizacion = 0;
                contarRegistrosTercero();
                RequestContext.getCurrentInstance().update("formularioDialogos:TercerosDialogo");
                RequestContext.getCurrentInstance().execute("PF('TercerosDialogo').show()");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        /*
         * if(stado != null){ FacesContext fc =
         * FacesContext.getCurrentInstance();
         * fc.getViewRoot().findComponent("form:datosConceptos").processRestoreState(fc,
         * stado); }
         */
        RequestContext context = RequestContext.getCurrentInstance();
        if (conceptoSeleccionado != null) {
            //editarConcepto = conceptoSeleccionado;

            switch (cualCelda) {
                case 0: {
                    deshabilitarBotonLov();
                    RequestContext.getCurrentInstance().update("formularioDialogos:editorCodigo");
                    RequestContext.getCurrentInstance().execute("PF('editorCodigo').show()");
                }
                break;
                case 1: {
                    deshabilitarBotonLov();
                    RequestContext.getCurrentInstance().update("formularioDialogos:editorDescripcion");
                    RequestContext.getCurrentInstance().execute("PF('editorDescripcion').show()");
                }
                break;
                case 2: {
                    deshabilitarBotonLov();
                    RequestContext.getCurrentInstance().update("formularioDialogos:editorNaturaleza");
                    RequestContext.getCurrentInstance().execute("PF('editorNaturaleza').show()");
                }
                break;
                case 3: {
                    habilitarBotonLov();
                    RequestContext.getCurrentInstance().update("formularioDialogos:editorCodigoUnidad");
                    RequestContext.getCurrentInstance().execute("PF('editorCodigoUnidad').show()");
                }
                break;
                case 4: {
                    habilitarBotonLov();
                    RequestContext.getCurrentInstance().update("formularioDialogos:editorNombreUnidad");
                    RequestContext.getCurrentInstance().execute("PF('editorNombreUnidad').show()");
                }
                break;
                case 5: {
                    deshabilitarBotonLov();
                    RequestContext.getCurrentInstance().update("formularioDialogos:editorCodigoDesprendible");
                    RequestContext.getCurrentInstance().execute("PF('editorCodigoDesprendible').show()");
                }
                break;
                case 6: {
                    deshabilitarBotonLov();
                    RequestContext.getCurrentInstance().update("formularioDialogos:editorDescripcionDesprendible");
                    RequestContext.getCurrentInstance().execute("PF('editorDescripcionDesprendible').show()");
                }
                break;
                case 7: {
                    deshabilitarBotonLov();
                    RequestContext.getCurrentInstance().update("formularioDialogos:editorIndependienteTesoreria");
                    RequestContext.getCurrentInstance().execute("PF('editorIndependienteTesoreria').show()");
                }
                break;
                case 8: {
                    deshabilitarBotonLov();
                    RequestContext.getCurrentInstance().update("formularioDialogos:editorConjunto");
                    RequestContext.getCurrentInstance().execute("PF('editorConjunto').show()");
                }
                break;
                case 9: {
                    deshabilitarBotonLov();
                    RequestContext.getCurrentInstance().update("formularioDialogos:editorFechaAcumulacion");
                    RequestContext.getCurrentInstance().execute("PF('editorFechaAcumulacion').show()");
                }
                break;
                case 10: {
                    habilitarBotonLov();
                    RequestContext.getCurrentInstance().update("formularioDialogos:editorTercero");
                    RequestContext.getCurrentInstance().execute("PF('editorTercero').show()");
                }
                break;
                case 11: {
                    deshabilitarBotonLov();
                    RequestContext.getCurrentInstance().update("formularioDialogos:editorEstado");
                    RequestContext.getCurrentInstance().execute("PF('editorEstado').show()");
                }
                break;
                case 12: {
                    deshabilitarBotonLov();
                    RequestContext.getCurrentInstance().update("formularioDialogos:editorEnvioTesoreria");
                    RequestContext.getCurrentInstance().execute("PF('editorEnvioTesoreria').show()");
                }
                break;
                case 13: {
                    deshabilitarBotonLov();
                    RequestContext.getCurrentInstance().update("formularioDialogos:editorCodigoAleternativo");
                    RequestContext.getCurrentInstance().execute("PF('editorCodigoAleternativo').show()");
                }
                break;
            }
            unaVez = true;
            cualCelda = -1;
            activoDetalle = true;
//            RequestContext.getCurrentInstance().update("form:DETALLES");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void modificarConcepto(Conceptos conceptoS, String columCambio, String valor) {
        conceptoSeleccionado = conceptoS;
        cargarLovs();
        tipoActualizacion = 0;
        //  Validación: que el concepto no sea usado para ningun registro de empleado
        int coincidencias = 0;
        int indiceUnicoElemento = 0;

        if (columCambio.equalsIgnoreCase("COD")) {
            BigInteger cod = new BigInteger(valor);
            boolean error = validarCodigo(cod);
            if (error) {
                conceptoSeleccionado.setCodigo(cod);
                RequestContext.getCurrentInstance().update("formularioDialogos:NuevoConceptoDialogo");
                RequestContext.getCurrentInstance().update("formularioDialogos:validacioNuevoCodigo");
                RequestContext.getCurrentInstance().execute("PF('validacioNuevoCodigo').show()");
                refrescar();
            }

        } else if (columCambio.equalsIgnoreCase("N")) {
            coincidencias++;
        } else if (columCambio.equalsIgnoreCase("UNIDADESCODIGO")) {
            conceptoSeleccionado.setCodigoUnidad(codigoUnidad);
            for (int i = 0; i < lovUnidades.size(); i++) {
                if (lovUnidades.get(i).getCodigo().startsWith(valor.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                conceptoSeleccionado.setUnidad(lovUnidades.get(indiceUnicoElemento).getSecuencia());
                conceptoSeleccionado.setCodigoUnidad(lovUnidades.get(indiceUnicoElemento).getCodigo());
                conceptoSeleccionado.setNombreUnidad(lovUnidades.get(indiceUnicoElemento).getNombre());
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:unidadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('unidadesDialogo').show()");
            }

        } else if (columCambio.equalsIgnoreCase("UNIDADESNOMBRE")) {
            conceptoSeleccionado.setNombreUnidad(nombreUnidad);
            for (int i = 0; i < lovUnidades.size(); i++) {
                if (lovUnidades.get(i).getNombre().startsWith(valor.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                conceptoSeleccionado.setUnidad(lovUnidades.get(indiceUnicoElemento).getSecuencia());
                conceptoSeleccionado.setCodigoUnidad(lovUnidades.get(indiceUnicoElemento).getCodigo());
                conceptoSeleccionado.setNombreUnidad(lovUnidades.get(indiceUnicoElemento).getNombre());
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:unidadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('unidadesDialogo').show()");
            }

        } else if (columCambio.equalsIgnoreCase("TERCERO")) {
            if (!valor.isEmpty()) {
                conceptoSeleccionado.setNombreTercero(tercero);
                for (int i = 0; i < lovTerceros.size(); i++) {
                    if (lovTerceros.get(i).getNombre().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    conceptoSeleccionado.setTercero(lovTerceros.get(indiceUnicoElemento).getSecuencia());
                    conceptoSeleccionado.setNombreTercero(lovTerceros.get(indiceUnicoElemento).getNombre());
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("formularioDialogos:TercerosDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TercerosDialogo').show()");
                }
            } else {
                conceptoSeleccionado.setTercero(null);
                coincidencias = 1;
            }
        }
        if (coincidencias == 1) {
            if (!listaConceptosEmpresaCrear.contains(conceptoSeleccionado)) {
                if (listaConceptosEmpresaModificar.isEmpty() || !listaConceptosEmpresaModificar.contains(conceptoSeleccionado)) {
                    listaConceptosEmpresaModificar.add(conceptoSeleccionado);
                }
                if (guardado) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                activoDetalle = true;
            }
        }
        tipoActualizacion = -1;
        deshabilitarBotonLov();
//      RequestContext.getCurrentInstance().update("form:datosConceptos");
    }

    public boolean activarSelec() {
        unaVez = true;
        return false;
    }
    //Ubicacion Celda.

    public void cambiarIndice(Conceptos conceptoS, int celda) {
        log.info("cambiarIndice()");
        if (permitirIndex == true) {
            conceptoSeleccionado = conceptoS;
            cualCelda = celda;
            if (tipoLista == 0) {
                codigoUnidad = conceptoSeleccionado.getCodigoUnidad();
                nombreUnidad = conceptoSeleccionado.getNombreUnidad();
                naturaleza = conceptoSeleccionado.getNaturaleza();
                log.info("cambiarIndice() naturaleza: " + naturaleza);
                tercero = conceptoSeleccionado.getNombreTercero();
                deshabilitarBotonLov();
                activoDetalle = false;
                //unaVez = false;
                switch (cualCelda) {
                    case 3:
                        habilitarBotonLov();
                        break;
                    case 4:
                        habilitarBotonLov();
                        break;
                    case 10:
                        habilitarBotonLov();
                        break;
                    default:
                        break;
                }
            }
        }

        /*
         if (permitirIndex == true) {
         vigenciaTablaSeleccionada = vigenciaDeportes;
         cualCelda = celda;
         if (tipoLista == 0) {
         fechaFin = vigenciaTablaSeleccionada.getFechafinal();
         fechaIni = vigenciaTablaSeleccionada.getFechainicial();
         vigenciaTablaSeleccionada.getSecuencia();
         deshabilitarBotonLov();
         if (cualCelda == 2) {
         contarRegistrosVD();
         deporte = vigenciaTablaSeleccionada.getDeporte().getNombre();
         habilitarBotonLov();
         }
         }
         if (tipoLista == 1) {
         fechaFin = vigenciaTablaSeleccionada.getFechafinal();
         fechaIni = vigenciaTablaSeleccionada.getFechainicial();
         vigenciaTablaSeleccionada.getSecuencia();
         deshabilitarBotonLov();
         if (cualCelda == 2) {
         contarRegistrosVD();
         deporte = vigenciaTablaSeleccionada.getDeporte().getNombre();
         habilitarBotonLov();
         }
         }
         }
         */
    }

    public void actualizarUnidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
//            habilitarBotonLov();
            conceptoSeleccionado.setUnidad(unidadSeleccionada.getSecuencia());
            conceptoSeleccionado.setCodigoUnidad(unidadSeleccionada.getCodigo());
            conceptoSeleccionado.setNombreUnidad(unidadSeleccionada.getNombre());
            if (!listaConceptosEmpresaCrear.contains(conceptoSeleccionado)) {
                if (listaConceptosEmpresaModificar.isEmpty()) {
                    listaConceptosEmpresaModificar.add(conceptoSeleccionado);
                } else if (!listaConceptosEmpresaModificar.contains(conceptoSeleccionado)) {
                    listaConceptosEmpresaModificar.add(conceptoSeleccionado);
                }
            }
            if (guardado) {
                guardado = false;
//                RequestContext.getCurrentInstance().update("form:DETALLES");
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosConceptos");
        } else if (tipoActualizacion == 1) {
//            habilitarBotonLov();
            nuevoConcepto.setUnidad(unidadSeleccionada.getSecuencia());
            nuevoConcepto.setNombreUnidad(unidadSeleccionada.getNombre());
            nuevoConcepto.setCodigoUnidad(unidadSeleccionada.getCodigo());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigoUnidad");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreUnidad");
        } else if (tipoActualizacion == 2) {
//            habilitarBotonLov();
            duplicarConcepto.setUnidad(unidadSeleccionada.getSecuencia());
            duplicarConcepto.setNombreUnidad(unidadSeleccionada.getNombre());
            duplicarConcepto.setCodigoUnidad(unidadSeleccionada.getCodigo());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoUnidad");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreUnidad");
        }

        filtradoUnidades = null;
        unidadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");

        context.reset("formularioDialogos:lovUnidades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovUnidades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('unidadesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovUnidades");
        RequestContext.getCurrentInstance().update("formularioDialogos:unidadesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarU");

    }

    public void cancelarUnidades() {
        filtradoUnidades = null;
        unidadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        activoDetalle = true;
        RequestContext context = RequestContext.getCurrentInstance();
        deshabilitarBotonLov();
//        RequestContext.getCurrentInstance().update("form:DETALLES");
        context.reset("formularioDialogos:lovUnidades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovUnidades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('unidadesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovUnidades");
        RequestContext.getCurrentInstance().update("formularioDialogos:unidadesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarU");
    }

    public void actualizarTercero() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            conceptoSeleccionado.setTercero(terceroSeleccionado.getSecuencia());
            conceptoSeleccionado.setNombreTercero(terceroSeleccionado.getNombre());
            if (!listaConceptosEmpresaCrear.contains(conceptoSeleccionado)) {
                if (listaConceptosEmpresaModificar.isEmpty()) {
                    listaConceptosEmpresaModificar.add(conceptoSeleccionado);
                } else if (!listaConceptosEmpresaModificar.contains(conceptoSeleccionado)) {
                    listaConceptosEmpresaModificar.add(conceptoSeleccionado);
                }
            }

            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosConceptos");
        } else if (tipoActualizacion == 1) {
            nuevoConcepto.setTercero(terceroSeleccionado.getSecuencia());
            nuevoConcepto.setNombreTercero(terceroSeleccionado.getNombre());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTercero");
        } else if (tipoActualizacion == 2) {
            duplicarConcepto.setTercero(terceroSeleccionado.getSecuencia());
            duplicarConcepto.setNombreTercero(terceroSeleccionado.getNombre());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTercero");
        }
        filtradoTerceros = null;
        terceroSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        activoDetalle = true;

//        RequestContext.getCurrentInstance().update("form:DETALLES");
        context.reset("formularioDialogos:lovTerceros:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTerceros').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TercerosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTerceros");
        RequestContext.getCurrentInstance().update("formularioDialogos:TercerosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarT");
    }

    public void cancelarTercero() {
        filtradoTerceros = null;
        terceroSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        activoDetalle = true;
        RequestContext context = RequestContext.getCurrentInstance();
//        RequestContext.getCurrentInstance().update("form:DETALLES");
        context.reset("formularioDialogos:lovTerceros:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTerceros').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('TercerosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTerceros");
        RequestContext.getCurrentInstance().update("formularioDialogos:TercerosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarT");
    }

    public void lovEmpresas() {
        conceptoSeleccionado = null;
        activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
        cualCelda = -1;
        habilitarBotonLov();
        contarRegistrosEmpresa();
        RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').show()");
    }

    public void cambiarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (guardado) {
            backUpEmpresaActual = empresaActual;
            listaConceptosEmpresa = null;
            getListaConceptosEmpresa();
            RequestContext.getCurrentInstance().update("form:datosConceptos");
            RequestContext.getCurrentInstance().update("form:nombreEmpresa");
            RequestContext.getCurrentInstance().update("form:nitEmpresa");
            filtradoListaEmpresas = null;
            aceptar = true;

            context.reset("formularioDialogos:lovEmpresas:globalFilter");
            RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
            RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
            RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
            RequestContext.getCurrentInstance().update("formularioDialogos:EmpresasDialogo");
            RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
            verCambioEmpresa = false;
        } else {
            verCambioEmpresa = true;
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
        }
    }

    public void cancelarCambioEmpresa() {
        filtradoListaEmpresas = null;
        verCambioEmpresa = true;
        empresaActual = backUpEmpresaActual;
        conceptoSeleccionado = null;
        activoDetalle = true;
        RequestContext context = RequestContext.getCurrentInstance();
//        RequestContext.getCurrentInstance().update("form:DETALLES");
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
        RequestContext.getCurrentInstance().update("formularioDialogos:EmpresasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
    }

    public void lovConcepto(int quien) {
        if (quien == 0) {
            if (guardado) {
                lovConceptosEmpresa = null;
                lovConceptosEmpresa = administrarConceptos.consultarConceptosEmpresa(empresaActual.getSecuencia());
                contarRegistrosLovConceptos();
                habilitarBotonLov();
                RequestContext.getCurrentInstance().update("formularioDialogos:ConceptosDialogo");
                RequestContext.getCurrentInstance().execute("PF('ConceptosDialogo').show()");
                verSeleccionConcepto = false;
                cambioConcepto = 0;
            } else {
                verSeleccionConcepto = true;
                RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
            }
        } else {
            contarRegistrosLovConceptos();
            habilitarBotonLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:ConceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('ConceptosDialogo').show()");
            cambioConcepto = 1;
        }
        conceptoSeleccionado = null;
        cualCelda = -1;
        activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
    }

    public void mostrarTodosConceptos() {
        if (guardado) {
            if (bandera == 1) {
                cargarTablaDefault();
            }
            listaConceptosEmpresa.clear();
            for (int i = 0; i < lovConceptosEmpresa.size(); i++) {
                listaConceptosEmpresa.add(lovConceptosEmpresa.get(i));
            }

            mostrarTodos = true;
            verMostrarTodos = false;
            conceptoSeleccionado = null;
            RequestContext.getCurrentInstance().update("form:datosConceptos");
            RequestContext.getCurrentInstance().update("form:mostrarTodos");
        } else {
            verMostrarTodos = true;
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
        }
        cualCelda = -1;
        activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
        contarRegistros();
    }

    public void seleccionConcepto() {
        conceptoSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        if (cambioConcepto == 0) {
            if (bandera == 1) {
                cargarTablaDefault();
            }
            listaConceptosEmpresa.clear();
            listaConceptosEmpresa.add(conceptoSeleccionadoLOV);
            mostrarTodos = false;
            RequestContext.getCurrentInstance().update("form:datosConceptos");
            RequestContext.getCurrentInstance().update("form:mostrarTodos");
        } else {
            conceptoOriginal = conceptoSeleccionadoLOV;
            RequestContext.getCurrentInstance().update("formB:descripcionClon");
        }
        filtradoConceptosEmpresaLOV = null;
        conceptoSeleccionadoLOV = null;
        aceptar = true;
        context.reset("formularioDialogos:lovConceptos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovConceptos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ConceptosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovConceptos");
        RequestContext.getCurrentInstance().update("formularioDialogos:ConceptosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarC");
        contarRegistros();
    }

    public void cancelarSeleccionConcepto() {
        filtradoConceptosEmpresaLOV = null;
        conceptoSeleccionadoLOV = null;
        aceptar = true;
        conceptoOriginal.setInformacionConcepto(null);
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovConceptos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovConceptos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ConceptosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovConceptos");
        RequestContext.getCurrentInstance().update("formularioDialogos:ConceptosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarC");
    }

    public void borrarConcepto() {
        unaVez = true;
        if (conceptoSeleccionado != null) {
            if (!listaConceptosEmpresaModificar.isEmpty() && listaConceptosEmpresaModificar.contains(conceptoSeleccionado)) {
                int modIndex = listaConceptosEmpresaModificar.indexOf(conceptoSeleccionado);
                listaConceptosEmpresaModificar.remove(modIndex);
                listaConceptosBorrar.add(conceptoSeleccionado);
            } else if (!listaConceptosEmpresaCrear.isEmpty() && listaConceptosEmpresaCrear.contains(conceptoSeleccionado)) {
                int crearIndex = listaConceptosEmpresaCrear.indexOf(conceptoSeleccionado);
                listaConceptosEmpresaCrear.remove(crearIndex);
            } else {
                listaConceptosBorrar.add(conceptoSeleccionado);
            }
            listaConceptosEmpresa.remove(conceptoSeleccionado);
            if (tipoLista == 1) {
                filtradoConceptosEmpresa.remove(conceptoSeleccionado);
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosConceptos");
            conceptoSeleccionado = null;
            activoDetalle = true;
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void guardarSalir() {
        guardarCambios();
        refrescar();
        navegar("atras");
    }

    public void cancelarSalir() {
//      refrescar();
        navegar("atras");
    }

    //GUARDAR
    public void guardarCambios() {
        try {
            if (guardado == false) {
                if (!listaConceptosBorrar.isEmpty()) {
                    msgError = administrarConceptos.borrarConceptos(listaConceptosBorrar);
                    listaConceptosBorrar.clear();
                }
                if (!listaConceptosEmpresaCrear.isEmpty()) {
                    msgError = administrarConceptos.crearConceptos(listaConceptosEmpresaCrear);
                    listaConceptosEmpresaCrear.clear();
                }
                if (!listaConceptosEmpresaModificar.isEmpty()) {
                    msgError = administrarConceptos.modificarConceptos(listaConceptosEmpresaModificar);
                    listaConceptosEmpresaModificar.clear();
                }

                if (msgError.equals("EXITO")) {
                    listaConceptosEmpresa = null;
                    getListaConceptosEmpresa();
                    contarRegistros();
                    deshabilitarBotonLov();
                    RequestContext.getCurrentInstance().update("form:informacionRegistro");
                    RequestContext.getCurrentInstance().update("form:datosConceptos");
                    guardado = true;
                    permitirIndex = true;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    paraNuevoConcepto = 0;
                    conceptoSeleccionado = null;
                    activoDetalle = true;
                    unaVez = true;
//                RequestContext.getCurrentInstance().update("form:DETALLES");
                    if (verCambioEmpresa) {
                        cambiarEmpresa();
                    }
                    if (verCambioEstado) {
                        cambiarEstado();
                    }
                    if (verSeleccionConcepto) {
                        lovConcepto(0);
                    }
                    if (verMostrarTodos) {
                        mostrarTodosConceptos();
                    }
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito.");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    listasRecurrentes.getLovConceptos().clear();

                } else {
                    RequestContext.getCurrentInstance().update("formularioDialogos:errorGuardado");
                    RequestContext.getCurrentInstance().execute("PF('errorGuardado').show()");
                }
            }
        } catch (Exception e) {
            msgError = "";
            msgError = e.getMessage();
            RequestContext.getCurrentInstance().update("formularioDialogos:errorGuardado");
            RequestContext.getCurrentInstance().execute("PF('errorGuardado').show()");
//            log.warn("Error guardarCambios : " + e.toString());
//            FacesMessage msg = new FacesMessage("Información", "Se presentó un error en el guardado, intente nuevamente.");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void cancelarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (verCambioEmpresa) {
            empresaActual = backUpEmpresaActual;
            RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
            verCambioEmpresa = false;
        }
        if (verCambioEstado) {
            estadoConceptoEmpresa = backUpEstadoConceptoEmpresa;
            RequestContext.getCurrentInstance().update("form:opciones");
            verCambioEstado = false;
        }
    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    public void activarCtrlF11() {
        unaVez = true;
        conceptoSeleccionado = null;
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
//         altoTablaReg = "185";
            columnaIndependienteConcepto = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaIndependienteConcepto");
            columnaIndependienteConcepto.setFilterStyle("width: 85% !important;");
            columnaCodigo = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaCodigo");
            columnaCodigo.setFilterStyle("width: 85% !important;");
            columnaDescripción = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaDescripción");
            columnaDescripción.setFilterStyle("width: 85% !important;");
            columnaNaturaleza = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaNaturaleza");
            columnaNaturaleza.setFilterStyle("width: 85% !important;");
//            columnaCodigoUnidad = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaCodigoUnidad");
//            columnaCodigoUnidad.setFilterStyle("width: 85% !important;");
            columnaNombreUnidad = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaNombreUnidad");
            columnaNombreUnidad.setFilterStyle("width: 85% !important;");
            columnaCodigoDesprendible = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaCodigoDesprendible");
            columnaCodigoDesprendible.setFilterStyle("width: 85% !important;");
            columnaDescripcionDesplendible = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaDescripcionDesplendible");
            columnaDescripcionDesplendible.setFilterStyle("width: 85% !important;");
            columnaConjunto = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaConjunto");
            columnaConjunto.setFilterStyle("width: 85% !important;");
            columnaFechaAcumulado = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaFechaAcumulado");
            columnaFechaAcumulado.setFilterStyle("width: 85% !important;");
            columnaNombreTercero = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaNombreTercero");
            columnaNombreTercero.setFilterStyle("width: 85% !important;");
            columnaEstado = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaEstado");
            columnaEstado.setFilterStyle("width: 85% !important;");
            columnaEnvio = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaEnvio");
            columnaEnvio.setFilterStyle("width: 85% !important;");
            columnaCodigoAlternativo = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaCodigoAlternativo");
            columnaCodigoAlternativo.setFilterStyle("width: 85% !important;");
            bandera = 1;
            RequestContext.getCurrentInstance().update("form:datosConceptos");

        } else if (bandera == 1) {
            cargarTablaDefault();
        }
        RequestContext.getCurrentInstance().update("form:datosConceptos");
    }

    public boolean validarCodigo(BigInteger cod) {
        int error = 0;
        for (int i = 0; i < lovConceptosEmpresa.size(); i++) {
            if (lovConceptosEmpresa.get(i).getCodigo().equals(cod)) {
                error++;
            }
        }
        if (listaConceptosEmpresaCrear != null) {
            if (!listaConceptosEmpresaCrear.isEmpty()) {
                for (int i = 0; i < listaConceptosEmpresaCrear.size(); i++) {
                    if (listaConceptosEmpresaCrear.get(i).getCodigo().equals(cod)) {
                        error++;
                    }
                }
            }
        }
        if (tipoActualizacion > 0) {
            return (error > 0);
        } else {
            return (error > 1);
        }
    }

    public void agregarNuevoConcepto() {
        int pasa = 0;
        mensajeValidacion = "";
        if (nuevoConcepto.getCodigo() == null) {
            mensajeValidacion = " * Código concepto\n";
            pasa++;
        }
        if (nuevoConcepto.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripción\n";
            pasa++;
        }
        if (nuevoConcepto.getNaturalezaConcepto() == null) {
            mensajeValidacion = mensajeValidacion + " *Naturaleza\n";
            pasa++;
        }
        if (nuevoConcepto.getUnidad() == null) {
            mensajeValidacion = mensajeValidacion + " *Unidad\n";
            pasa++;
        }
        if (pasa == 0) {
            tipoActualizacion = 1;
            //Se valida que el codigo de concepto no exista
            boolean error = validarCodigo(nuevoConcepto.getCodigo());
            if (error == false) {
                if (continuarNuevoNat == false) {
                    if (nuevoConcepto.getNaturalezaConcepto().equals("NETO") && nuevoConcepto.getConjunto() != 45) {
                        pasa++;
                        mensajeValidacion = "La naturaleza del concepto es NETO, Recuerde que usualmente para ella el conjunto de conceptos debe ser 45.";
                    } else if (nuevoConcepto.getNaturalezaConcepto().equals("GASTO") && (nuevoConcepto.getConjunto() > 37 || nuevoConcepto.getConjunto() < 31)) {
                        pasa++;
                        mensajeValidacion = "La naturaleza del concepto es GASTO, Recuerde que usualmente para ella el conjunto de conceptos debe estar entre 31 y 37";
                    } else if (nuevoConcepto.getNaturalezaConcepto().equals("DESCUENTO") && (nuevoConcepto.getConjunto() > 30 || nuevoConcepto.getConjunto() < 21)) {
                        pasa++;
                        mensajeValidacion = "La naturaleza del concepto es DESCUENTO, Recuerde que usualmente para ella el conjunto de conceptos debe estar entre 21 y 30";
                    } else if (nuevoConcepto.getNaturalezaConcepto().equals("PAGO") && nuevoConcepto.getConjunto() > 20) {
                        pasa++;
                        mensajeValidacion = "La naturaleza del concepto es PAGO, Recuerde que usualmente para ella el conjunto de conceptos debe estar entre 1 y 20";
                    } else if (nuevoConcepto.getNaturalezaConcepto().equals("PASIVO") && (nuevoConcepto.getConjunto() > 44 || nuevoConcepto.getConjunto() < 38)) {
                        pasa++;
                        mensajeValidacion = "La naturaleza del concepto es PASIVO, Recuerde que usualmente para ella el conjunto de conceptos debe estar entre 38 y 44";
                    }
                }

                if (pasa == 0) {
                    if (bandera == 1) {
                        cargarTablaDefault();
                    }
                    paraNuevoConcepto++;
                    nuevoConceptoSecuencia = BigInteger.valueOf(paraNuevoConcepto);
                    nuevoConcepto.setSecuencia(nuevoConceptoSecuencia);
                    nuevoConcepto.setEmpresa(empresaActual.getSecuencia());
                    nuevoConcepto.setNombreEmpresa(empresaActual.getNombre());
                    nuevoConcepto.setNitEmpresa(empresaActual.getNit());

                    if (nuevoConcepto.getNaturalezaConcepto().equals("NETO")) {
                        nuevoConcepto.setNaturaleza("N");
                    } else if (nuevoConcepto.getNaturalezaConcepto().equals("GASTO")) {
                        nuevoConcepto.setNaturaleza("G");
                    } else if (nuevoConcepto.getNaturalezaConcepto().equals("DESCUENTO")) {
                        nuevoConcepto.setNaturaleza("D");
                    } else if (nuevoConcepto.getNaturalezaConcepto().equals("PAGO")) {
                        nuevoConcepto.setNaturaleza("P");
                    } else if (nuevoConcepto.getNaturalezaConcepto().equals("PASIVO")) {
                        nuevoConcepto.setNaturaleza("L");
                    }

                    if (nuevoConcepto.getEstadoConcepto().equals("ACTIVO")) {
                        nuevoConcepto.setActivo("S");
                    } else if (nuevoConcepto.getEstadoConcepto().equals("INACTIVO")) {
                        nuevoConcepto.setActivo("N");
                    }

                    if (nuevoConcepto.getEnviarConcepto().equals("SI")) {
                        nuevoConcepto.setEnviotesoreria("S");
                    } else if (nuevoConcepto.getEnviarConcepto().equals("NO")) {
                        nuevoConcepto.setEnviotesoreria("N");
                    }

                    if (nuevoConcepto.isIndependienteConcepto()) {
                        nuevoConcepto.setIndependiente("S");
                    } else {
                        nuevoConcepto.setIndependiente("N");
                    }

                    listaConceptosEmpresaCrear.add(nuevoConcepto);
                    listaConceptosEmpresa.add(nuevoConcepto);
                    contarRegistros();
                    nuevoConcepto = new Conceptos();
                    nuevoConcepto.setUnidad(null);
                    nuevoConcepto.setTercero(null);
                    nuevoConcepto.setConjunto(Short.parseShort("45"));

                    if (guardado) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                    RequestContext.getCurrentInstance().execute("PF('NuevoConceptoDialogo').hide()");
                    RequestContext.getCurrentInstance().update("formularioDialogos:NuevoConceptoDialogo");
                    conceptoSeleccionado = null;
                    activoDetalle = true;
                    continuarNuevoNat = false;
                    tipoActualizacion = -1;
//                    RequestContext.getCurrentInstance().update("form:DETALLES");
                    RequestContext.getCurrentInstance().update("form:datosConceptos");
                } else {
                    RequestContext.getCurrentInstance().update("formularioDialogos:validacioNaturaleza");
                    RequestContext.getCurrentInstance().execute("PF('validacioNaturaleza').show()");
                }
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacioNuevoCodigo");
                RequestContext.getCurrentInstance().execute("PF('validacioNuevoCodigo').show()");
            }
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacioNuevoConcepto");
            RequestContext.getCurrentInstance().execute("PF('validacioNuevoConcepto').show()");
        }
    }
    //LIMPIAR NUEVO REGISTRO

    public void limpiarNuevoConcepto() {
        System.out.println("Controlador.ControlConcepto.limpiarNuevoConcepto()");
        nuevoConcepto = new Conceptos();
        nuevoConcepto.setUnidad(null);
        nuevoConcepto.setTercero(null);
        activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
    }

    public void duplicarRegistro() {
        unaVez = true;
        RequestContext context = RequestContext.getCurrentInstance();
        if (conceptoSeleccionado != null) {
            duplicarConcepto = new Conceptos();
            // duplicarConcepto.setConjunto(Short.parseShort("45"));
            duplicarConcepto.setEmpresa(conceptoSeleccionado.getEmpresa());
            duplicarConcepto.setCodigo(conceptoSeleccionado.getCodigo());
            duplicarConcepto.setDescripcion(conceptoSeleccionado.getDescripcion());
            duplicarConcepto.setNaturaleza(conceptoSeleccionado.getNaturaleza());
            duplicarConcepto.setUnidad(conceptoSeleccionado.getUnidad());
            duplicarConcepto.setCodigodesprendible(conceptoSeleccionado.getCodigodesprendible());
            duplicarConcepto.setIndependiente(conceptoSeleccionado.getIndependiente());
            duplicarConcepto.setConjunto(conceptoSeleccionado.getConjunto());
            duplicarConcepto.setContenidofechahasta(conceptoSeleccionado.getContenidofechahasta());
            duplicarConcepto.setTercero(conceptoSeleccionado.getTercero());
            duplicarConcepto.setActivo(conceptoSeleccionado.getActivo());
            duplicarConcepto.setEnviotesoreria(conceptoSeleccionado.getEnviotesoreria());
            duplicarConcepto.setCodigoalternativo(conceptoSeleccionado.getCodigoalternativo());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConcepto");
            RequestContext.getCurrentInstance().execute("PF('DuplicarConceptoDialogo').show()");
            activoDetalle = true;
//            RequestContext.getCurrentInstance().update("form:DETALLES");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        mensajeValidacion = "";
        RequestContext context = RequestContext.getCurrentInstance();
        if (duplicarConcepto.getCodigo() == null) {
            mensajeValidacion = " * Código concepto\n";
            pasa++;
        }
        if (duplicarConcepto.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripción\n";
            pasa++;
        }
        if (duplicarConcepto.getUnidad() == null) {
            mensajeValidacion = mensajeValidacion + " *Unidad\n";
            pasa++;
        }
        if (duplicarConcepto.getNaturalezaConcepto() == null) {
            mensajeValidacion = mensajeValidacion + " *Naturaleza\n";
            pasa++;
        }
        if (pasa == 0) {
            tipoActualizacion = 2;
            //Se valida que el codigo de concepto no exista
            boolean error = validarCodigo(duplicarConcepto.getCodigo());
            if (error == false) {
                if (continuarNuevoNat == false) {
                    if (duplicarConcepto.getNaturalezaConcepto().equals("NETO") && duplicarConcepto.getConjunto() != 45) {
                        pasa++;
                        mensajeValidacion = "La naturaleza del concepto es NETO, Recuerde que usualmente para ella el conjunto de conceptos debe ser 45.";
                    } else if (duplicarConcepto.getNaturalezaConcepto().equals("GASTO") && (duplicarConcepto.getConjunto() > 37 || duplicarConcepto.getConjunto() < 31)) {
                        pasa++;
                        mensajeValidacion = "La naturaleza del concepto es GASTO, Recuerde que usualmente para ella el conjunto de conceptos debe estar entre 31 y 37";
                    } else if (duplicarConcepto.getNaturalezaConcepto().equals("DESCUENTO") && (duplicarConcepto.getConjunto() > 30 || duplicarConcepto.getConjunto() < 21)) {
                        pasa++;
                        mensajeValidacion = "La naturaleza del concepto es DESCUENTO, Recuerde que usualmente para ella el conjunto de conceptos debe estar entre 21 y 30";
                    } else if (duplicarConcepto.getNaturalezaConcepto().equals("PAGO") && duplicarConcepto.getConjunto() > 20) {
                        pasa++;
                        mensajeValidacion = "La naturaleza del concepto es PAGO, Recuerde que usualmente para ella el conjunto de conceptos debe estar entre 1 y 20";
                    } else if (duplicarConcepto.getNaturalezaConcepto().equals("PASIVO") && (duplicarConcepto.getConjunto() > 44 || duplicarConcepto.getConjunto() < 38)) {
                        pasa++;
                        mensajeValidacion = "La naturaleza del concepto es PASIVO, Recuerde que usualmente para ella el conjunto de conceptos debe estar entre 38 y 44";
                    }
                }
                if (pasa == 0) {
                    paraNuevoConcepto++;
                    nuevoConceptoSecuencia = BigInteger.valueOf(paraNuevoConcepto);
                    duplicarConcepto.setSecuencia(nuevoConceptoSecuencia);
                    listaConceptosEmpresa.add(duplicarConcepto);
                    listaConceptosEmpresaCrear.add(duplicarConcepto);
                    contarRegistros();
                    conceptoSeleccionado = null;
                    activoDetalle = true;
                    if (guardado) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }

                    if (bandera == 1) {
                        cargarTablaDefault();
                    }
                    tipoActualizacion = -1;
                    continuarNuevoNat = false;
                    duplicarConcepto = new Conceptos();
                    RequestContext.getCurrentInstance().update("form:datosConceptos");
//                    RequestContext.getCurrentInstance().update("form:DETALLES");
                    RequestContext.getCurrentInstance().execute("PF('DuplicarConceptoDialogo').hide()");
                } else {
                    RequestContext.getCurrentInstance().update("formularioDialogos:validacioNaturaleza");
                    RequestContext.getCurrentInstance().execute("PF('validacioNaturaleza').show()");
                }
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacioNuevoCodigo");
                RequestContext.getCurrentInstance().execute("PF('validacioNuevoCodigo').show()");
            }
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacioNuevoConcepto");
            RequestContext.getCurrentInstance().execute("PF('validacioNuevoConcepto').show()");
        }
    }
    //LIMPIAR DUPLICAR

    public void limpiarduplicar() {
        duplicarConcepto = new Conceptos();
    }
    //Para continuar con tras validar naturaleza

    public void activarContinuar() {
        continuarNuevoNat = true;
        if (tipoActualizacion == 1) {
            agregarNuevoConcepto();
        } else if (tipoActualizacion == 2) {
            confirmarDuplicar();
        }
    }

    public void salir() {
        navegar("atras");
    }

    public void refrescar() {
        unaVez = true;
        if (bandera == 1) {
            cargarTablaDefault();
        }
        activoDetalle = true;
        aceptar = true;
        listaConceptosBorrar.clear();
        listaConceptosEmpresaCrear.clear();
        listaConceptosEmpresaModificar.clear();
        paraNuevoConcepto = 0;
        mostrarTodos = true;
        cualCelda = -1;
        tipoLista = 0;
        guardado = true;
        nuevoConcepto = new Conceptos();
        nuevoConcepto.setUnidad(null);
        nuevoConcepto.setTercero(null);
        nuevoConcepto.setConjunto(Short.parseShort("45"));
        duplicarConcepto = new Conceptos();
        conceptoClon = new Conceptos();
        conceptoOriginal = new Conceptos();
        permitirIndex = true;
        continuarNuevoNat = false;
        estadoConceptoEmpresa = "S";

        if (verCambioEmpresa) {
            cambiarEmpresa();
        }
        if (verCambioEstado) {
            cambiarEstado();
        }

        if (verSeleccionConcepto) {
            lovConcepto(0);
        }
        listaConceptosEmpresa = null;
//      if (listaConceptosEmpresa != null) {
//         listaConceptosEmpresa.clear();
//         if (lovConceptosEmpresa == null) {
//            lovConceptosEmpresa = administrarConceptos.consultarConceptosEmpresa(empresaActual.getSecuencia());
//         }
//         if (lovConceptosEmpresa != null) {
//            for (int i = 0; i < lovConceptosEmpresa.size(); i++) {
//               listaConceptosEmpresa.add(lovConceptosEmpresa.get(i));
//            }
//         }
//      }

        contarRegistros();
        deshabilitarBotonLov();
        conceptoSeleccionado = null;
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("formB:mostrarTodos");
        RequestContext.getCurrentInstance().update("form:datosConceptos");
        RequestContext.getCurrentInstance().update("formB:codigoConceptoClon");
        RequestContext.getCurrentInstance().update("formB:descripcioConceptoClon");
        RequestContext.getCurrentInstance().update("formB:descripcionClon");
        RequestContext.getCurrentInstance().update("form:opciones");
    }

    public void cargarTablaDefault() {
        FacesContext c = FacesContext.getCurrentInstance();
//      altoTablaReg = "205";
        columnaCodigo = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaCodigo");
        columnaCodigo.setFilterStyle("display: none; visibility: hidden;");
        columnaDescripción = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaDescripción");
        columnaDescripción.setFilterStyle("display: none; visibility: hidden;");
        columnaNaturaleza = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaNaturaleza");
        columnaNaturaleza.setFilterStyle("display: none; visibility: hidden;");
        columnaCodigoUnidad = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaCodigoUnidad");
        columnaCodigoUnidad.setFilterStyle("display: none; visibility: hidden;");
        columnaNombreUnidad = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaNombreUnidad");
        columnaNombreUnidad.setFilterStyle("display: none; visibility: hidden;");
        columnaCodigoDesprendible = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaCodigoDesprendible");
        columnaCodigoDesprendible.setFilterStyle("display: none; visibility: hidden;");
        columnaDescripcionDesplendible = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaDescripcionDesplendible");
        columnaDescripcionDesplendible.setFilterStyle("display: none; visibility: hidden;");
        columnaConjunto = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaConjunto");
        columnaConjunto.setFilterStyle("display: none; visibility: hidden;");
        columnaFechaAcumulado = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaFechaAcumulado");
        columnaFechaAcumulado.setFilterStyle("display: none; visibility: hidden;");
        columnaNombreTercero = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaNombreTercero");
        columnaNombreTercero.setFilterStyle("display: none; visibility: hidden;");
        columnaEstado = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaEstado");
        columnaEstado.setFilterStyle("display: none; visibility: hidden;");
        columnaEnvio = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaEnvio");
        columnaEnvio.setFilterStyle("display: none; visibility: hidden;");
        columnaCodigoAlternativo = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaCodigoAlternativo");
        columnaCodigoAlternativo.setFilterStyle("display: none; visibility: hidden;");
        columnaIndependienteConcepto = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaIndependienteConcepto");
        columnaIndependienteConcepto.setFilterStyle("display: none; visibility: hidden;");
        bandera = 0;
        filtradoConceptosEmpresa = null;
        tipoLista = 0;
    }

    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
    public void verificarRastro() {
        unaVez = true;
        RequestContext context = RequestContext.getCurrentInstance();
        //FacesContext fc = FacesContext.getCurrentInstance(); 
        //fc.getViewRoot().findComponent("form:datosConceptos").processSaveState(fc);
        //Object estado = tabla.saveState(fc);

        //RequestContext.getCurrentInstance().execute("PF('focusField(InputId);"); 
        if (conceptoSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(conceptoSeleccionado.getSecuencia(), "CONCEPTOS");
            switch (resultado) {
                case 1:
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
                    break;
                case 2:
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
                    break;
                case 3:
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
                    break;
                case 4:
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
                    break;
                case 5:
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
                    break;
                default:
                    break;
            }
        } else if (administrarRastros.verificarHistoricosTabla("CONCEPTOS")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        activoDetalle = true;
//        RequestContext.getCurrentInstance().update("form:DETALLES");
    }
//
//    //EXPORTAR

    public void exportPDF() throws IOException {
        System.out.println("Controlador.ControlConcepto.exportPDF()");
        DataTable tablaE = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConceptosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tablaE, "ConceptosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        activoDetalle = true;

////        RequestContext.getCurrentInstance().update("form:DETALLES");
//        DataTable tablaE = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConceptosExportar");
//        FacesContext context = FacesContext.getCurrentInstance();
//        Exporter exporter = new ExportarPDF();
//        exporter.export(context, tablaE, "ConceptosPDF", false, false, "UTF-8", null, null);
//        context.responseComplete();
        System.out.println("Sali del export PDF");
    }
//

    public void descargarxls() {
        RequestContext.getCurrentInstance().execute("PF('descargarReporte').show()");
    }

    public void exportXLS() throws IOException {
        System.out.println("Controlador.ControlConcepto.exportXLS()");
        DataTable tablaE = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConceptosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tablaE, "Conceptos", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void activarAceptar() {
        aceptar = false;
    }

    //CLONAR
    public void clonarConcepto() {
        if (conceptoClon.getCodigo() != null && conceptoClon.getDescripcion() != null && conceptoOriginal.getSecuencia() != null) {
            tipoActualizacion = 3;
            //Se valida que el codigo de concepto no exista
            boolean error = validarCodigo(conceptoClon.getCodigo());
            if (error == false) {
                administrarConceptos.clonarConcepto(conceptoOriginal.getSecuencia(), conceptoClon.getCodigo(), conceptoClon.getDescripcion());
                conceptoClon = new Conceptos();
                conceptoOriginal = new Conceptos();
                listaConceptosEmpresa = null;
                getListaConceptosEmpresa();
                listasRecurrentes.getLovConceptos().clear();
                FacesMessage msg = new FacesMessage("Información", "Concepto clonado con exito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                contarRegistros();
                lovConceptosEmpresa = null;
                RequestContext.getCurrentInstance().update("formB:codigoConceptoClon");
                RequestContext.getCurrentInstance().update("formB:descripcioConceptoClon");
                RequestContext.getCurrentInstance().update("formB:descripcionClon");
                RequestContext.getCurrentInstance().update("form:datosConceptos");
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacioNuevoCodigo");
                RequestContext.getCurrentInstance().execute("PF('validacioNuevoCodigo').show()");
            }
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacioNuevoClon");
            RequestContext.getCurrentInstance().execute("PF('validacioNuevoClon').show()");
        }
        tipoActualizacion = -1;
    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        conceptoSeleccionado = null;
        contarRegistros();
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosEmpresa() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpresa");
    }

    public void contarRegistrosTercero() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTercero");
    }

    public void contarRegistrosLovConceptos() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroConcepto");
    }

    public void contarRegistrosUnidades() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroUnidad");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

//   public void recordarSeleccion() {
//
//      if (conceptoSeleccionado != null) {
//         if (listaConceptosEmpresa.contains(conceptoSeleccionado)) {
//            if (!listaConceptosEmpresa.get(0).equals(conceptoSeleccionado)) {
//
//               List<Conceptos> listaAux = new ArrayList<Conceptos>();
//               for (int i = 0; i < listaConceptosEmpresa.size(); i++) {
//                  if (!listaConceptosEmpresa.get(i).equals(conceptoSeleccionado)) {
//                     listaAux.add(listaConceptosEmpresa.get(i));
//                  }
//               }
//               listaConceptosEmpresa.set(0, conceptoSeleccionado);
//               for (int n = 0; n < listaAux.size(); n++) {
//                  listaConceptosEmpresa.set((n + 1), listaAux.get(n));
//               }
//            }
//         }
//         FacesContext c = FacesContext.getCurrentInstance();
//         tabla = (DataTable) c.getViewRoot().findComponent("form:datosConceptos");
//         tabla.setSelection(conceptoSeleccionado);
//         conceptoSeleccionado = listaConceptosEmpresa.get(0);
//      } else {
//         unaVez = true;
//      }
//      RequestContext.getCurrentInstance().update("form:informacionRegistro");
//      log.info("conceptoSeleccionado: " + conceptoSeleccionado);
//   }
    public void verDetalle(Conceptos conceptoS) {
        conceptoSeleccionado = conceptoS;
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "detalleconcepto");
    }

    public void cargarLovs() {
        if (lovUnidades == null) {
            lovUnidades = administrarConceptos.consultarLOVUnidades();
            RequestContext.getCurrentInstance().update("formularioDialogos:unidadesDialogo");
            RequestContext.getCurrentInstance().update("formularioDialogos:lovUnidades");
        }
        if (lovTerceros == null) {
            lovTerceros = administrarConceptos.consultarLOVTerceros(empresaActual.getSecuencia());
            RequestContext.getCurrentInstance().update("formularioDialogos:TercerosDialogo");
            RequestContext.getCurrentInstance().update("formularioDialogos:lovTerceros");
        }
    }

    //GETTER AND SETTER/////////////////////////////////////////////////////////////////**////////////////////////////////////////////////////
    public List<Conceptos> getListaConceptosEmpresa() {

        if (listaConceptosEmpresa == null) {
            System.out.println("Controlador.ControlConcepto.getListaConceptosEmpresa()");
            //Si la consulta no es para todos los conceptos (activos o inactivos))
            if (!estadoConceptoEmpresa.equals("TODOS")) {
                System.out.println("Entre a !estadoConceptoEmpresa.equals(\"TODOS\")");
                listaConceptosEmpresa = administrarConceptos.consultarConceptosEmpresaActivos_Inactivos(empresaActual.getSecuencia(), estadoConceptoEmpresa);
                //LLenamos la lista de valores para todos los conceptos de la empresa solo una vez
                if (lovConceptosEmpresa == null) {
                    System.out.println("Entre a if(lovConceptosEmpresa == null)");
                    lovConceptosEmpresa = administrarConceptos.consultarConceptosEmpresa(empresaActual.getSecuencia());
                    System.out.println("Saliendo If lovConceptosEmpresa: " + lovConceptosEmpresa.size());
                }
            } else {//Si la consulta es para TODOS:
                System.out.println("Else");
                listaConceptosEmpresa = administrarConceptos.consultarConceptosEmpresa(empresaActual.getSecuencia());
                //LLenamos la lista de valores para todos los conceptos de la empresa solo una vez
                if (lovConceptosEmpresa == null) {
                    System.out.println("lovConceptosEmpresa == null");
                    lovConceptosEmpresa = new ArrayList<Conceptos>();
                    for (int i = 0; i < listaConceptosEmpresa.size(); i++) {
                        lovConceptosEmpresa.add(listaConceptosEmpresa.get(i));
                    }
                }
            }
        }
        return listaConceptosEmpresa;
    }

    public void setListaConceptosEmpresa(List<Conceptos> listaConceptosEmpresa) {
        this.listaConceptosEmpresa = listaConceptosEmpresa;
    }

    public List<Conceptos> getFiltradoConceptosEmpresa() {
        return filtradoConceptosEmpresa;
    }

    public void setFiltradoConceptosEmpresa(List<Conceptos> filtradoConceptosEmpresa) {
        this.filtradoConceptosEmpresa = filtradoConceptosEmpresa;
    }

    public List<Empresas> getLovEmpresas() {
        if (lovEmpresas == null) {
            lovEmpresas = administrarConceptos.consultarEmpresas();
        }
        if (empresaActual == null) {
            empresaActual = lovEmpresas.get(0);
        }
        //backUpEmpresaActual = empresaActual;
        return lovEmpresas;
    }

    public void setLovEmpresas(List<Empresas> listaEmpresas) {
        this.lovEmpresas = listaEmpresas;
    }

    public Empresas getEmpresaActual() {
        getLovEmpresas();
        return empresaActual;
    }

    public void setEmpresaActual(Empresas empresaActual) {
        this.empresaActual = empresaActual;
    }

    public Map<String, String> getConjuntoC() {
        return conjuntoC;
    }

    public void setConjuntoC(Map<String, String> conjuntoC) {
        this.conjuntoC = conjuntoC;
    }

    public List<Unidades> getLovUnidades() {
        return lovUnidades;
    }

    public void setLovUnidades(List<Unidades> listaUnidades) {
        this.lovUnidades = listaUnidades;
    }

    public Unidades getUnidadSeleccionada() {
        return unidadSeleccionada;
    }

    public void setUnidadSeleccionada(Unidades unidadSeleccionada) {
        this.unidadSeleccionada = unidadSeleccionada;
    }

    public List<Unidades> getFiltradoUnidades() {
        return filtradoUnidades;
    }

    public void setFiltradoUnidades(List<Unidades> filtradoUnidades) {
        this.filtradoUnidades = filtradoUnidades;
    }

    public List<Terceros> getLovTerceros() {
        return lovTerceros;
    }

    public void setLovTerceros(List<Terceros> listaTerceros) {
        this.lovTerceros = listaTerceros;
    }

    public List<Terceros> getFiltradoTerceros() {
        return filtradoTerceros;
    }

    public void setFiltradoTerceros(List<Terceros> filtradoTerceros) {
        this.filtradoTerceros = filtradoTerceros;
    }

    public Terceros getTerceroSeleccionado() {
        return terceroSeleccionado;
    }

    public void setTerceroSeleccionado(Terceros terceroSeleccionado) {
        this.terceroSeleccionado = terceroSeleccionado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public Conceptos getDuplicarConcepto() {
        return duplicarConcepto;
    }

    public void setDuplicarConcepto(Conceptos duplicarConcepto) {
        this.duplicarConcepto = duplicarConcepto;
    }

    public Conceptos getNuevoConcepto() {
        return nuevoConcepto;
    }

    public void setNuevoConcepto(Conceptos nuevoConcepto) {
        this.nuevoConcepto = nuevoConcepto;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public List<Empresas> getFiltradoListaEmpresas() {
        return filtradoListaEmpresas;
    }

    public void setFiltradoListaEmpresas(List<Empresas> filtradoListaEmpresas) {
        this.filtradoListaEmpresas = filtradoListaEmpresas;
    }

    public String getEstadoConceptoEmpresa() {
        return estadoConceptoEmpresa;
    }

    public void setEstadoConceptoEmpresa(String estadoConceptoEmpresa) {
        this.estadoConceptoEmpresa = estadoConceptoEmpresa;
    }

    public List<Conceptos> getLovConceptosEmpresa() {
        return lovConceptosEmpresa;
    }

    public void setLovConceptosEmpresa(List<Conceptos> listaConceptosEmpresaLOV) {
        this.lovConceptosEmpresa = listaConceptosEmpresaLOV;
    }

    public List<Conceptos> getFiltradoConceptosEmpresaLOV() {
        return filtradoConceptosEmpresaLOV;
    }

    public void setFiltradoConceptosEmpresaLOV(List<Conceptos> filtradoConceptosEmpresaLOV) {
        this.filtradoConceptosEmpresaLOV = filtradoConceptosEmpresaLOV;
    }

    public Conceptos getConceptoSeleccionadoLOV() {
        return conceptoSeleccionadoLOV;
    }

    public void setConceptoSeleccionadoLOV(Conceptos conceptoSeleccionado) {
        this.conceptoSeleccionadoLOV = conceptoSeleccionado;
    }

    public boolean isMostrarTodos() {
        return mostrarTodos;
    }

    public Conceptos getConceptoOriginal() {
        return conceptoOriginal;
    }

    public void setConceptoOriginal(Conceptos conceptoOriginal) {
        this.conceptoOriginal = conceptoOriginal;
    }

    public Conceptos getConceptoClon() {
        return conceptoClon;
    }

    public void setConceptoClon(Conceptos conceptoClon) {
        this.conceptoClon = conceptoClon;
    }

    public Conceptos getConceptoSeleccionado() {
        return conceptoSeleccionado;
    }

    public void setConceptoSeleccionado(Conceptos seleccionConceptoEmpresa) {
        this.conceptoSeleccionado = seleccionConceptoEmpresa;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getAltoTablaReg() {
        columnaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConceptos:columnaCodigo");
        if (columnaCodigo.getFilterStyle().startsWith("width: 85%")) {
            altoTablaReg = "" + 6;
        } else {
            bandera = 0;
            altoTablaReg = "" + 7;
        }
        return altoTablaReg;
    }

    public void setAltoTablaReg(String altoTablaReg) {
        this.altoTablaReg = altoTablaReg;
    }

    public String getAltoTablaH() {
        columnaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConceptos:columnaCodigo");
        if (columnaCodigo.getFilterStyle().startsWith("width: 85%")) {
            altoTablaH = "" + 170;
        } else {
            bandera = 0;
            altoTablaH = "" + 190;
        }
        return altoTablaH;
    }

    public void setAltoTablaH(String altoTablaH) {
        this.altoTablaH = altoTablaH;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosConceptos");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroUnidad() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovUnidades");
        infoRegistroUnidad = String.valueOf(tabla.getRowCount());
        return infoRegistroUnidad;
    }

    public void setInfoRegistroUnidad(String infoRegistroUnidad) {
        this.infoRegistroUnidad = infoRegistroUnidad;
    }

    public String getInfoRegistroTercero() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTerceros");
        infoRegistroTercero = String.valueOf(tabla.getRowCount());
        return infoRegistroTercero;
    }

    public void setInfoRegistroTercero(String infoRegistroTercero) {
        this.infoRegistroTercero = infoRegistroTercero;
    }

    public String getInfoRegistroEmpresa() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovEmpresas");
        infoRegistroEmpresa = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpresa;
    }

    public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
        this.infoRegistroEmpresa = infoRegistroEmpresa;
    }

    public String getInfoRegistroConcepto() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovConceptos");
        infoRegistroConcepto = String.valueOf(tabla.getRowCount());
        return infoRegistroConcepto;
    }

    public void setInfoRegistroConcepto(String infoRegistroConcepto) {
        this.infoRegistroConcepto = infoRegistroConcepto;
    }

    public boolean isActivoDetalle() {
        return activoDetalle;
    }

    public void setActivoDetalle(boolean activoDetalle) {
        this.activoDetalle = activoDetalle;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

}
