package Controlador;

import Entidades.*;
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
public class ControlConcepto implements Serializable {

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
    public Conceptos nuevoConcepto;
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
    private String codigoUnidad, nombreUnidad, tercero;
    //RASTRO
    //CLONAR CONCEPTO
    private Conceptos conceptoOriginal;
    private Conceptos conceptoClon;
    private int cambioConcepto;
    private String paginaAnterior;
    //
    private String altoTabla;
    private String infoRegistro;
    private String infoRegistroUnidad, infoRegistroTercero, infoRegistroEmpresa, infoRegistroConcepto;
    //
    private boolean activoDetalle;
    //Advertencias
    private boolean continuarNuevoNat;
    private DataTable tabla;
    //recordar seleccion
    private boolean unaVez;

    public ControlConcepto() {

        conceptoSeleccionado = null;
        activoDetalle = true;
        altoTabla = "205";
        lovUnidades = null;
        lovTerceros = null;
        lovEmpresas = null;
        empresaActual = null;
        backUpEmpresaActual = new Empresas();
        //Otros
        aceptar = true;
        //borrar aficiones
        listaConceptosBorrar = new ArrayList<Conceptos>();
        //crear aficiones
        listaConceptosEmpresaCrear = new ArrayList<Conceptos>();
        paraNuevoConcepto = 0;
        //modificar aficiones
        listaConceptosEmpresaModificar = new ArrayList<Conceptos>();
        //editar
        //editarConcepto = new Conceptos();
        mostrarTodos = true;
        cualCelda = -1;
        tipoLista = 0;
        //guardar
        guardado = true;
        //Crear VC
        nuevoConcepto = new Conceptos();
        nuevoConcepto.setUnidad(new Unidades());
        nuevoConcepto.setTercero(new Terceros());
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
        paginaAnterior = "";
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarConceptos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            estadoConceptoEmpresa = "S";
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e + " " + "Causa: " + e.getCause());
        }
    }

    public void cambiarEstado() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (guardado) {
            if (bandera == 1) {
                cargarTablaDefault();
            }
            listaConceptosEmpresa = null;
            getListaConceptosEmpresa();

            backUpEstadoConceptoEmpresa = estadoConceptoEmpresa;
            context.update("form:datosConceptos");
            verCambioEstado = false;
        } else {
            verCambioEstado = true;
            context.execute("confirmarGuardar.show()");
        }
        contarRegistros();
        activoDetalle = true;
        context.update("form:DETALLES");
        cualCelda = -1;
    }

    public void recibirPaginaEntrante(String pagina) {
        System.out.println(this.getClass().getName() + ".recibirPaginaEntrante()");
        paginaAnterior = pagina;
        estadoConceptoEmpresa = "S";
        lovConceptosEmpresa = null;
        backUpEstadoConceptoEmpresa = "S";
        if (lovEmpresas == null) {
            System.out.println("listaEmpresas esta vacia");
            getLovEmpresas();
        }
        if (empresaActual == null) {
            System.out.println("empresaActual esta vacia");
            empresaActual = lovEmpresas.get(0);
        }
        listaConceptosEmpresa = null;
        getListaConceptosEmpresa();
        contarRegistros();
        deshabilitarBotonLov();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosConceptos");
    }

    public String redirigir() {
        return paginaAnterior;
    }
    //SELECCIONAR NATURALEZA

    public void seleccionarItem(String itemSeleccionado, Conceptos conceptoS, int celda) {
        if (celda == 2) {
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
                codigoUnidad = nuevoConcepto.getUnidad().getCodigo();
            } else if (tipoNuevo == 2) {
                codigoUnidad = duplicarConcepto.getUnidad().getCodigo();
            }
        } else if (Campo.equals("NOMBREUNIDAD")) {
            if (tipoNuevo == 1) {
                nombreUnidad = nuevoConcepto.getUnidad().getNombre();
            } else if (tipoNuevo == 2) {
                nombreUnidad = duplicarConcepto.getUnidad().getNombre();
            }
        } else if (Campo.equals("TERCERO")) {
            if (tipoNuevo == 1) {
                tercero = nuevoConcepto.getTercero().getNombre();
            } else if (tipoNuevo == 2) {
                tercero = duplicarConcepto.getTercero().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        cargarLovs();
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("CODIGOUNIDAD")) {
            if (tipoNuevo == 1) {
                nuevoConcepto.getUnidad().setCodigo(codigoUnidad);
            } else if (tipoNuevo == 2) {
                duplicarConcepto.getUnidad().setCodigo(codigoUnidad);
            }
            for (int i = 0; i < lovUnidades.size(); i++) {
                if (lovUnidades.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoConcepto.setUnidad(lovUnidades.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevoCodigoUnidad");
                    context.update("formularioDialogos:nuevoNombreUnidad");
                } else if (tipoNuevo == 2) {
                    duplicarConcepto.setUnidad(lovUnidades.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarCodigoUnidad");
                    context.update("formularioDialogos:duplicarNombreUnidad");
                }
                lovUnidades.clear();
                getLovUnidades();
            } else {
                context.update("formularioDialogos:unidadesDialogo");
                context.execute("unidadesDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevoCodigoUnidad");
                    context.update("formularioDialogos:nuevoNombreUnidad");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarCodigoUnidad");
                    context.update("formularioDialogos:duplicarNombreUnidad");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("NOMBREUNIDAD")) {
            if (tipoNuevo == 1) {
                nuevoConcepto.getUnidad().setNombre(nombreUnidad);
            } else if (tipoNuevo == 2) {
                duplicarConcepto.getUnidad().setNombre(nombreUnidad);
            }
            for (int i = 0; i < lovUnidades.size(); i++) {
                if (lovUnidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoConcepto.setUnidad(lovUnidades.get(indiceUnicoElemento));
                    context.update("formularioDialogos:nuevoCodigoUnidad");
                    context.update("formularioDialogos:nuevoNombreUnidad");
                } else if (tipoNuevo == 2) {
                    duplicarConcepto.setUnidad(lovUnidades.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarCodigoUnidad");
                    context.update("formularioDialogos:duplicarNombreUnidad");
                }
                lovUnidades.clear();
                getLovUnidades();
            } else {
                context.update("formularioDialogos:unidadesDialogo");
                context.execute("unidadesDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevoCodigoUnidad");
                    context.update("formularioDialogos:nuevoNombreUnidad");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarCodigoUnidad");
                    context.update("formularioDialogos:duplicarNombreUnidad");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevoConcepto.getTercero().setNombre(tercero);
                } else if (tipoNuevo == 2) {
                    duplicarConcepto.getTercero().setNombre(tercero);
                }
                for (int i = 0; i < lovTerceros.size(); i++) {
                    if (lovTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevoConcepto.setTercero(lovTerceros.get(indiceUnicoElemento));
                        context.update("formularioDialogos:nuevoTercero");
                    } else if (tipoNuevo == 2) {
                        duplicarConcepto.setTercero(lovTerceros.get(indiceUnicoElemento));
                        context.update("formularioDialogos:duplicarTercero");
                    }
                    lovTerceros.clear();
                    getLovTerceros();
                } else {
                    context.update("formularioDialogos:TercerosDialogo");
                    context.execute("TercerosDialogo.show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        context.update("formularioDialogos:nuevoTercero");
                    } else if (tipoNuevo == 2) {
                        context.update("formularioDialogos:duplicarTercero");
                    }
                }
            }
        } else {
            lovTerceros.clear();
            getLovTerceros();
            if (tipoNuevo == 1) {
                nuevoConcepto.setTercero(new Terceros());
            } else if (tipoNuevo == 2) {
                duplicarConcepto.setTercero(new Terceros());
            }
            if (tipoNuevo == 1) {
                context.update("formularioDialogos:nuevoTercero");
            } else if (tipoNuevo == 2) {
                context.update("formularioDialogos:duplicarTercero");
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
        context.update("form:DETALLES");
        if (dlg == 0) {
            habilitarBotonLov();
            modificarInfoRegistroUnidades(lovUnidades.size());
            context.update("formularioDialogos:unidadesDialogo");
            context.execute("unidadesDialogo.show()");
        } else if (dlg == 1) {
            habilitarBotonLov();
            modificarInforegistroTercero(lovTerceros.size());
            context.update("formularioDialogos:TercerosDialogo");
            context.execute("TercerosDialogo.show()");
        }
    }

    public void llamarDialogosLista(Conceptos conceptoS, int columnD) {
        cargarLovs();
        RequestContext context = RequestContext.getCurrentInstance();
        conceptoSeleccionado = conceptoS;
        tipoActualizacion = 0;
        if (columnD == 0) {
            modificarInfoRegistroUnidades(lovUnidades.size());
            context.update("formularioDialogos:unidadesDialogo");
            context.execute("unidadesDialogo.show()");
        } else if (columnD == 1) {
            modificarInforegistroTercero(lovTerceros.size());
            context.update("formularioDialogos:TercerosDialogo");
            context.execute("TercerosDialogo.show()");
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
                modificarInfoRegistroUnidades(lovUnidades.size());
                context.update("formularioDialogos:unidadesDialogo");
                context.execute("unidadesDialogo.show()");
                tipoActualizacion = 0;
                //Si la columna es tercersos
            } else if (cualCelda == 10) {
                habilitarBotonLov();
                tipoActualizacion = 0;
                modificarInforegistroTercero(lovTerceros.size());
                context.update("formularioDialogos:TercerosDialogo");
                context.execute("TercerosDialogo.show()");
            }
        } else {
            context.execute("seleccionarRegistro.show()");
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
                    context.update("formularioDialogos:editorCodigo");
                    context.execute("editorCodigo.show()");
                }
                break;
                case 1: {
                    deshabilitarBotonLov();
                    context.update("formularioDialogos:editorDescripcion");
                    context.execute("editorDescripcion.show()");
                }
                break;
                case 2: {
                    deshabilitarBotonLov();
                    context.update("formularioDialogos:editorNaturaleza");
                    context.execute("editorNaturaleza.show()");
                }
                break;
                case 3: {
                    habilitarBotonLov();
                    context.update("formularioDialogos:editorCodigoUnidad");
                    context.execute("editorCodigoUnidad.show()");
                }
                break;
                case 4: {
                    habilitarBotonLov();
                    context.update("formularioDialogos:editorNombreUnidad");
                    context.execute("editorNombreUnidad.show()");
                }
                break;
                case 5: {
                    deshabilitarBotonLov();
                    context.update("formularioDialogos:editorCodigoDesprendible");
                    context.execute("editorCodigoDesprendible.show()");
                }
                break;
                case 6: {
                    deshabilitarBotonLov();
                    context.update("formularioDialogos:editorDescripcionDesprendible");
                    context.execute("editorDescripcionDesprendible.show()");
                }
                break;
                case 7: {
                    deshabilitarBotonLov();
                    context.update("formularioDialogos:editorIndependienteTesoreria");
                    context.execute("editorIndependienteTesoreria.show()");
                }
                break;
                case 8: {
                    deshabilitarBotonLov();
                    context.update("formularioDialogos:editorConjunto");
                    context.execute("editorConjunto.show()");
                }
                break;
                case 9: {
                    deshabilitarBotonLov();
                    context.update("formularioDialogos:editorFechaAcumulacion");
                    context.execute("editorFechaAcumulacion.show()");
                }
                break;
                case 10: {
                    habilitarBotonLov();
                    context.update("formularioDialogos:editorTercero");
                    context.execute("editorTercero.show()");
                }
                break;
                case 11: {
                    deshabilitarBotonLov();
                    context.update("formularioDialogos:editorEstado");
                    context.execute("editorEstado.show()");
                }
                break;
                case 12: {
                    deshabilitarBotonLov();
                    context.update("formularioDialogos:editorEnvioTesoreria");
                    context.execute("editorEnvioTesoreria.show()");
                }
                break;
                case 13: {
                    deshabilitarBotonLov();
                    context.update("formularioDialogos:editorCodigoAleternativo");
                    context.execute("editorCodigoAleternativo.show()");
                }
                break;
            }
            unaVez = true;
            cualCelda = -1;
            activoDetalle = true;
            context.update("form:DETALLES");
        } else {
            context.execute("seleccionarRegistro.show()");
        }
    }

    public void modificarConcepto(Conceptos conseptoS, String columCambio, String valor) {
        cargarLovs();
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = 0;
        //  Validación: que el concepto no sea usado para ningun registro de empleado
        boolean conceptoUtilizado;

        conceptoUtilizado = administrarConceptos.ValidarUpdateConceptoAcumulados(conseptoS.getSecuencia());

        //Si se puede modificar
        if (conceptoUtilizado) {
            conceptoSeleccionado = conseptoS;
            int coincidencias = 0;
            int indiceUnicoElemento = 0;

            if (columCambio.equalsIgnoreCase("COD")) {
                BigInteger cod = new BigInteger(valor);
                boolean error = validarCodigo(cod);
                if (error) {
                    conseptoS.setCodigo(cod);
                    context.update("formularioDialogos:NuevoConceptoDialogo");
                    context.update("formularioDialogos:validacioNuevoCodigo");
                    context.execute("validacioNuevoCodigo.show()");
                    refrescar();
                }

            } else if (columCambio.equalsIgnoreCase("N")) {
                coincidencias++;

            } else if (columCambio.equalsIgnoreCase("UNIDADESCODIGO")) {
                conseptoS.getUnidad().setCodigo(codigoUnidad);
                for (int i = 0; i < lovUnidades.size(); i++) {
                    if (lovUnidades.get(i).getCodigo().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    conseptoS.setUnidad(lovUnidades.get(indiceUnicoElemento));
                } else {
                    permitirIndex = false;
                    context.update("formularioDialogos:unidadesDialogo");
                    context.execute("unidadesDialogo.show()");
                }

            } else if (columCambio.equalsIgnoreCase("UNIDADESNOMBRE")) {
                conseptoS.getUnidad().setNombre(nombreUnidad);
                for (int i = 0; i < lovUnidades.size(); i++) {
                    if (lovUnidades.get(i).getNombre().startsWith(valor.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    conseptoS.setUnidad(lovUnidades.get(indiceUnicoElemento));
                } else {
                    permitirIndex = false;
                    context.update("formularioDialogos:unidadesDialogo");
                    context.execute("unidadesDialogo.show()");
                }

            } else if (columCambio.equalsIgnoreCase("TERCERO")) {
                if (!valor.isEmpty()) {
                    conseptoS.getTercero().setNombre(tercero);
                    for (int i = 0; i < lovTerceros.size(); i++) {
                        if (lovTerceros.get(i).getNombre().startsWith(valor.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }
                    if (coincidencias == 1) {
                        conseptoS.setTercero(lovTerceros.get(indiceUnicoElemento));
                    } else {
                        permitirIndex = false;
                        context.update("formularioDialogos:TercerosDialogo");
                        context.execute("TercerosDialogo.show()");
                    }
                } else {
                    conseptoS.setTercero(new Terceros());
                    coincidencias = 1;
                }
            }

            if (coincidencias == 1) {
                if (!listaConceptosEmpresaCrear.contains(conseptoS)) {

                    if (listaConceptosEmpresaModificar.isEmpty() || !listaConceptosEmpresaModificar.contains(conseptoS)) {
                        listaConceptosEmpresaModificar.add(conseptoS);
                    }
                    if (guardado) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                    activoDetalle = true;
                    context.update("form:DETALLES");
                }
            }
        } else {
            refrescar();
            context.execute("errorNoModificar.show()");
        }
        tipoActualizacion = -1;
        deshabilitarBotonLov();
        context.update("form:datosConceptos");
    }

    public boolean activarSelec() {
        unaVez = true;
        return false;
    }
    //Ubicacion Celda.

    public void cambiarIndice(Conceptos conceptoS, int celda) {
        if (permitirIndex == true) {
            conceptoSeleccionado = conceptoS;
            cualCelda = celda;
            if (tipoLista == 0) {
                codigoUnidad = conceptoSeleccionado.getUnidad().getCodigo();
                nombreUnidad = conceptoSeleccionado.getUnidad().getNombre();
                tercero = conceptoSeleccionado.getTercero().getNombre();
                deshabilitarBotonLov();
                activoDetalle = false;
                //unaVez = false;
                if (cualCelda == 3) {
                    habilitarBotonLov();
                } else if (cualCelda == 4) {
                    habilitarBotonLov();
                } else if (cualCelda == 10) {
                    habilitarBotonLov();
                }
            }

            if (tipoLista == 1) {
                codigoUnidad = conceptoSeleccionado.getUnidad().getCodigo();
                nombreUnidad = conceptoSeleccionado.getUnidad().getNombre();
                tercero = conceptoSeleccionado.getTercero().getNombre();
                deshabilitarBotonLov();
                activoDetalle = false;
                //unaVez = false;
                if (cualCelda == 3) {
                    habilitarBotonLov();
                } else if (cualCelda == 4) {
                    habilitarBotonLov();

                } else if (cualCelda == 10) {
                    habilitarBotonLov();
                }
            }

            //deshabilitarBotonLov();
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
            conceptoSeleccionado.setUnidad(unidadSeleccionada);
            if (!listaConceptosEmpresaCrear.contains(conceptoSeleccionado)) {
                if (listaConceptosEmpresaModificar.isEmpty()) {
                    listaConceptosEmpresaModificar.add(conceptoSeleccionado);
                } else if (!listaConceptosEmpresaModificar.contains(conceptoSeleccionado)) {
                    listaConceptosEmpresaModificar.add(conceptoSeleccionado);
                }
            }
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:DETALLES");
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            context.update("form:datosConceptos");
        } else if (tipoActualizacion == 1) {
//            habilitarBotonLov();
            nuevoConcepto.setUnidad(unidadSeleccionada);
            context.update("formularioDialogos:nuevoCodigoUnidad");
            context.update("formularioDialogos:nuevoNombreUnidad");
        } else if (tipoActualizacion == 2) {
//            habilitarBotonLov();
            duplicarConcepto.setUnidad(unidadSeleccionada);
            context.update("formularioDialogos:duplicarCodigoUnidad");
            context.update("formularioDialogos:duplicarNombreUnidad");
        }

        filtradoUnidades = null;
        unidadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        activoDetalle = true;
        context.update("form:DETALLES");

        context.reset("formularioDialogos:lovUnidades:globalFilter");
        context.execute("lovUnidades.clearFilters()");
        context.execute("unidadesDialogo.hide()");
        context.update("formularioDialogos:lovUnidades");
        context.update("formularioDialogos:unidadesDialogo");
        context.update("formularioDialogos:aceptarU");

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
        context.update("form:DETALLES");
        context.reset("formularioDialogos:lovUnidades:globalFilter");
        context.execute("lovUnidades.clearFilters()");
        context.execute("unidadesDialogo.hide()");
        context.update("formularioDialogos:lovUnidades");
        context.update("formularioDialogos:unidadesDialogo");
        context.update("formularioDialogos:aceptarU");
    }

    public void actualizarTercero() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            conceptoSeleccionado.setTercero(terceroSeleccionado);
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
            context.update("form:datosConceptos");
        } else if (tipoActualizacion == 1) {
            nuevoConcepto.setTercero(terceroSeleccionado);
            context.update("formularioDialogos:nuevoTercero");
        } else if (tipoActualizacion == 2) {
            duplicarConcepto.setTercero(terceroSeleccionado);
            context.update("formularioDialogos:duplicarTercero");
        }
        filtradoTerceros = null;
        terceroSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        activoDetalle = true;

        context.update("form:DETALLES");
        context.reset("formularioDialogos:lovTerceros:globalFilter");
        context.execute("lovTerceros.clearFilters()");
        context.execute("TercerosDialogo.hide()");
        context.update("formularioDialogos:lovTerceros");
        context.update("formularioDialogos:TercerosDialogo");
        context.update("formularioDialogos:aceptarT");
    }

    public void cancelarTercero() {
        filtradoTerceros = null;
        terceroSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        activoDetalle = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:DETALLES");
        context.reset("formularioDialogos:lovTerceros:globalFilter");
        context.execute("lovTerceros.clearFilters()");
        context.execute("TercerosDialogo.hide()");
        context.update("formularioDialogos:lovTerceros");
        context.update("formularioDialogos:TercerosDialogo");
        context.update("formularioDialogos:aceptarT");
    }

    public void lovEmpresas() {
        conceptoSeleccionado = null;
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
        cualCelda = -1;
        habilitarBotonLov();
        modificarInforegistroEmpresa(lovEmpresas.size());
        RequestContext.getCurrentInstance().execute("EmpresasDialogo.show()");
    }

    public void cambiarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (guardado) {
            backUpEmpresaActual = empresaActual;
            listaConceptosEmpresa = null;
            getListaConceptosEmpresa();
            context.update("form:datosConceptos");
            context.update("form:nombreEmpresa");
            context.update("form:nitEmpresa");
            filtradoListaEmpresas = null;
            aceptar = true;

            context.reset("formularioDialogos:lovEmpresas:globalFilter");
            context.execute("lovEmpresas.clearFilters()");
            context.execute("EmpresasDialogo.hide()");
            context.update("formularioDialogos:lovEmpresas");
            context.update("formularioDialogos:EmpresasDialogo");
            context.update("formularioDialogos:aceptarE");
            verCambioEmpresa = false;
        } else {
            verCambioEmpresa = true;
            context.execute("confirmarGuardarSinSalida.show()");
        }
    }

    public void cancelarCambioEmpresa() {
        filtradoListaEmpresas = null;
        verCambioEmpresa = true;
        empresaActual = backUpEmpresaActual;
        conceptoSeleccionado = null;
        activoDetalle = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:DETALLES");
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        context.execute("lovEmpresas.clearFilters()");
        context.execute("EmpresasDialogo.hide()");
        context.update("formularioDialogos:lovEmpresas");
        context.update("formularioDialogos:EmpresasDialogo");
        context.update("formularioDialogos:aceptarE");
    }

    public void lovConcepto(int quien) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (quien == 0) {
            if (guardado) {
                modificarInforegistroConcepto(lovConceptosEmpresa.size());
                habilitarBotonLov();
                context.update("formularioDialogos:ConceptosDialogo");
                context.execute("ConceptosDialogo.show()");
                verSeleccionConcepto = false;
                cambioConcepto = 0;
            } else {
                verSeleccionConcepto = true;
                context.execute("confirmarGuardar.show()");
            }
        } else {
            modificarInforegistroConcepto(lovConceptosEmpresa.size());
            habilitarBotonLov();
            context.update("formularioDialogos:ConceptosDialogo");
            context.execute("ConceptosDialogo.show()");
            cambioConcepto = 1;
        }
        conceptoSeleccionado = null;
        cualCelda = -1;
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
    }

    public void mostrarTodosConceptos() {
        RequestContext context = RequestContext.getCurrentInstance();
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
            context.update("form:datosConceptos");
            context.update("form:mostrarTodos");
        } else {
            verMostrarTodos = true;
            context.execute("confirmarGuardar.show()");
        }
        cualCelda = -1;
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
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
            context.update("form:datosConceptos");
            context.update("form:mostrarTodos");
        } else {
            conceptoOriginal = conceptoSeleccionadoLOV;
            context.update("form:descripcionClon");
        }
        filtradoConceptosEmpresaLOV = null;
        conceptoSeleccionadoLOV = null;
        aceptar = true;

        context.reset("formularioDialogos:lovConceptos:globalFilter");
        context.execute("lovConceptos.clearFilters()");
        context.execute("ConceptosDialogo.hide()");
        context.update("formularioDialogos:lovConceptos");
        context.update("formularioDialogos:ConceptosDialogo");
        context.update("formularioDialogos:aceptarC");
    }

    public void cancelarSeleccionConcepto() {
        filtradoConceptosEmpresaLOV = null;
        conceptoSeleccionadoLOV = null;
        aceptar = true;
        conceptoOriginal.setInformacionConcepto(null);
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovConceptos:globalFilter");
        context.execute("lovConceptos.clearFilters()");
        context.execute("ConceptosDialogo.hide()");
        context.update("formularioDialogos:lovConceptos");
        context.update("formularioDialogos:ConceptosDialogo");
        context.update("formularioDialogos:aceptarC");
    }

    public void borrarConcepto() {
        unaVez = true;
        RequestContext context = RequestContext.getCurrentInstance();
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

            context.update("form:informacionRegistro");
            context.update("form:datosConceptos");
            conceptoSeleccionado = null;
            activoDetalle = true;
            RequestContext.getCurrentInstance().update("form:DETALLES");
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            context.execute("seleccionarRegistro.show()");
        }
    }

    public void guardarSalir() {
        guardarCambios();
        refrescar();
    }

    public void cancelarSalir() {
        refrescar();
    }

    //GUARDAR
    public void guardarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (!listaConceptosBorrar.isEmpty()) {
                    administrarConceptos.borrarConceptos(listaConceptosBorrar);
                    listaConceptosBorrar.clear();
                }
                if (!listaConceptosEmpresaCrear.isEmpty()) {
                    administrarConceptos.crearConceptos(listaConceptosEmpresaCrear);
                    listaConceptosEmpresaCrear.clear();
                }
                if (!listaConceptosEmpresaModificar.isEmpty()) {
                    administrarConceptos.modificarConceptos(listaConceptosEmpresaModificar);
                    listaConceptosEmpresaModificar.clear();
                }
                listaConceptosEmpresa = null;
                getListaConceptosEmpresa();
                contarRegistros();
                deshabilitarBotonLov();
                context.update("form:informacionRegistro");
                context.update("form:datosConceptos");
                guardado = true;
                permitirIndex = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                paraNuevoConcepto = 0;
                conceptoSeleccionado = null;
                activoDetalle = true;
                unaVez = true;
                RequestContext.getCurrentInstance().update("form:DETALLES");
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
                FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                context.update("form:growl");
            }
        } catch (Exception e) {
            System.out.println("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Se presento un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
        }
    }

    public void cancelarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (verCambioEmpresa) {
            empresaActual = backUpEmpresaActual;
            context.update("formularioDialogos:lovEmpresas");
            verCambioEmpresa = false;
        }
        if (verCambioEstado) {
            estadoConceptoEmpresa = backUpEstadoConceptoEmpresa;
            context.update("form:opciones");
            verCambioEstado = false;
        }
    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    public void activarCtrlF11() {
        unaVez = true;
        conceptoSeleccionado = null;
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "183";
            columnaIndependienteConcepto = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaIndependienteConcepto");
            columnaIndependienteConcepto.setFilterStyle("width: 90%;");
            columnaCodigo = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaCodigo");
            columnaCodigo.setFilterStyle("width: 90%;");
            columnaDescripción = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaDescripción");
            columnaDescripción.setFilterStyle("width: 90%;");
            columnaNaturaleza = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaNaturaleza");
            columnaNaturaleza.setFilterStyle("width: 90%;");
//            columnaCodigoUnidad = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaCodigoUnidad");
//            columnaCodigoUnidad.setFilterStyle("width: 90%;");
            columnaNombreUnidad = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaNombreUnidad");
            columnaNombreUnidad.setFilterStyle("width: 90%;");
            columnaCodigoDesprendible = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaCodigoDesprendible");
            columnaCodigoDesprendible.setFilterStyle("width: 90%;");
            columnaDescripcionDesplendible = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaDescripcionDesplendible");
            columnaDescripcionDesplendible.setFilterStyle("width:90%;");
            columnaConjunto = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaConjunto");
            columnaConjunto.setFilterStyle("width: 90%;");
            columnaFechaAcumulado = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaFechaAcumulado");
            columnaFechaAcumulado.setFilterStyle("width: 90%;");
            columnaNombreTercero = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaNombreTercero");
            columnaNombreTercero.setFilterStyle("width: 90%;");
            columnaEstado = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaEstado");
            columnaEstado.setFilterStyle("width: 90%;");
            columnaEnvio = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaEnvio");
            columnaEnvio.setFilterStyle("width: 90%;");
            columnaCodigoAlternativo = (Column) c.getViewRoot().findComponent("form:datosConceptos:columnaCodigoAlternativo");
            columnaCodigoAlternativo.setFilterStyle("width: 90%;");
            RequestContext.getCurrentInstance().update("form:datosConceptos");
            bandera = 1;

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

        RequestContext context = RequestContext.getCurrentInstance();
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
        if (nuevoConcepto.getUnidad().getSecuencia() == null) {
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
                    nuevoConcepto.setEmpresa(empresaActual);
                    if (nuevoConcepto.getTercero().getSecuencia() == null) {
                        nuevoConcepto.setTercero(null);
                    }

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
                    modificarInfoRegistro(listaConceptosEmpresa.size());
                    context.update("form:informacionRegistro");
                    nuevoConcepto = new Conceptos();
                    nuevoConcepto.setUnidad(new Unidades());
                    nuevoConcepto.setTercero(new Terceros());
                    nuevoConcepto.setConjunto(Short.parseShort("45"));

                    if (guardado) {
                        guardado = false;
                        context.update("form:ACEPTAR");
                    }
                    context.execute("NuevoConceptoDialogo.hide()");
                    context.update("formularioDialogos:NuevoConceptoDialogo");
                    conceptoSeleccionado = null;
                    activoDetalle = true;
                    continuarNuevoNat = false;
                    tipoActualizacion = -1;
                    context.update("form:DETALLES");
                    context.update("form:datosConceptos");
                } else {
                    context.update("formularioDialogos:validacioNaturaleza");
                    context.execute("validacioNaturaleza.show()");
                }
            } else {
                context.update("formularioDialogos:validacioNuevoCodigo");
                context.execute("validacioNuevoCodigo.show()");
            }
        } else {
            context.update("formularioDialogos:validacioNuevoConcepto");
            context.execute("validacioNuevoConcepto.show()");
        }
    }
    //LIMPIAR NUEVO REGISTRO

    public void limpiarNuevoConcepto() {
        nuevoConcepto = new Conceptos();
        nuevoConcepto.setUnidad(new Unidades());
        nuevoConcepto.setTercero(new Terceros());
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
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
            context.update("formularioDialogos:duplicarConcepto");
            context.execute("DuplicarConceptoDialogo.show()");
            activoDetalle = true;
            context.update("form:DETALLES");
        } else {
            context.execute("seleccionarRegistro.show()");
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
        if (duplicarConcepto.getUnidad().getSecuencia() == null) {
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
                    modificarInfoRegistro(listaConceptosEmpresa.size());
                    conceptoSeleccionado = null;
                    activoDetalle = true;
                    if (guardado) {
                        guardado = false;
                        context.update("form:ACEPTAR");
                    }

                    if (bandera == 1) {
                        cargarTablaDefault();
                    }
                    tipoActualizacion = -1;
                    continuarNuevoNat = false;
                    duplicarConcepto = new Conceptos();
                    context.update("form:datosConceptos");
                    context.update("form:informacionRegistro");
                    context.update("form:datosConceptos");
                    context.update("form:DETALLES");
                    context.execute("DuplicarConceptoDialogo.hide()");
                } else {
                    context.update("formularioDialogos:validacioNaturaleza");
                    context.execute("validacioNaturaleza.show()");
                }
            } else {
                context.update("formularioDialogos:validacioNuevoCodigo");
                context.execute("validacioNuevoCodigo.show()");
            }
        } else {
            context.update("formularioDialogos:validacioNuevoConcepto");
            context.execute("validacioNuevoConcepto.show()");
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

    public void refrescar() {
        unaVez = true;
        RequestContext context = RequestContext.getCurrentInstance();
        if (bandera == 1) {
            cargarTablaDefault();
        }
        activoDetalle = true;
        altoTabla = "205";
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
        nuevoConcepto.setUnidad(new Unidades());
        nuevoConcepto.setTercero(new Terceros());
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
        if (verMostrarTodos) {
            mostrarTodosConceptos();
        }
        listaConceptosEmpresa.clear();
        for (int i = 0; i < lovConceptosEmpresa.size(); i++) {
            listaConceptosEmpresa.add(lovConceptosEmpresa.get(i));
        }
        contarRegistros();
        deshabilitarBotonLov();
        conceptoSeleccionado = null;
        context.update("form:informacionRegistro");
        context.update("form:mostrarTodos");
        context.update("form:datosConceptos");
        context.update("form:codigoConceptoClon");
        context.update("form:descripcioConceptoClon");
        context.update("form:descripcionClon");
        context.update("form:opciones");
    }

    public void cargarTablaDefault() {
        FacesContext c = FacesContext.getCurrentInstance();
        altoTabla = "205";
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

        //context.execute("focusField(InputId);"); 
        if (conceptoSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(conceptoSeleccionado.getSecuencia(), "CONCEPTOS");
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
        } else if (administrarRastros.verificarHistoricosTabla("CONCEPTOS")) {
            context.execute("confirmarRastroHistorico.show()");
        } else {
            context.execute("errorRastroHistorico.show()");
        }
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tablaE = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConceptosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tablaE, "ConceptosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
    }

    public void exportXLS() throws IOException {
        DataTable tablaE = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConceptosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tablaE, "ConceptosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
    }

    public void activarAceptar() {
        aceptar = false;
    }

    //CLONAR
    public void clonarConcepto() {
        RequestContext context = RequestContext.getCurrentInstance();
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
                guardado = false;
                context.update("form:ACEPTAR");
                FacesMessage msg = new FacesMessage("Información", "Concepto clonado con exito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                context.update("form:growl");
                context.update("form:codigoConceptoClon");
                context.update("form:descripcioConceptoClon");
                context.update("form:descripcionClon");
                context.update("form:datosConceptos");
            } else {
                context.update("formularioDialogos:validacioNuevoCodigo");
                context.execute("validacioNuevoCodigo.show()");
            }
        } else {
            context.update("formularioDialogos:validacioNuevoClon");
            context.execute("validacioNuevoClon.show()");
        }
        tipoActualizacion = -1;
    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        modificarInfoRegistro(filtradoConceptosEmpresa.size());
        System.out.println("EventoFiltrar infoRegistro: " + infoRegistro);
        context.update("form:informacionRegistro");
    }

    public void contarRegistros() {
        if (listaConceptosEmpresa != null) {
            modificarInfoRegistro(listaConceptosEmpresa.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
    }

    public void modificarInforegistroEmpresa(int valor) {
        infoRegistroEmpresa = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpresa");
    }

    public void modificarInforegistroTercero(int valor) {
        infoRegistroTercero = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTercero");
    }

    public void modificarInforegistroConcepto(int valor) {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroConcepto");
        infoRegistroConcepto = String.valueOf(valor);
    }

    public void modificarInfoRegistroUnidades(int valor) {
        infoRegistroUnidad = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroUnidad");
    }

    public void eventoFiltrarEmpresa() {
        modificarInforegistroEmpresa(filtradoListaEmpresas.size());
    }

    public void eventoFiltrarTercero() {
        modificarInforegistroTercero(filtradoTerceros.size());
    }

    public void eventoFiltrarConcepto() {
        modificarInforegistroConcepto(filtradoConceptosEmpresaLOV.size());
    }

    public void eventoFiltrarUnidades() {
        modificarInfoRegistroUnidades(filtradoUnidades.size());
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void recordarSeleccion() {

        if (conceptoSeleccionado != null) {
            if (listaConceptosEmpresa.contains(conceptoSeleccionado)) {
                if (!listaConceptosEmpresa.get(0).equals(conceptoSeleccionado)) {

                    List<Conceptos> listaAux = new ArrayList<Conceptos>();
                    for (int i = 0; i < listaConceptosEmpresa.size(); i++) {
                        if (!listaConceptosEmpresa.get(i).equals(conceptoSeleccionado)) {
                            listaAux.add(listaConceptosEmpresa.get(i));
                        }
                    }
                    listaConceptosEmpresa.set(0, conceptoSeleccionado);
                    for (int n = 0; n < listaAux.size(); n++) {
                        listaConceptosEmpresa.set((n + 1), listaAux.get(n));
                    }
                }
            }
            FacesContext c = FacesContext.getCurrentInstance();
            tabla = (DataTable) c.getViewRoot().findComponent("form:datosConceptos");
            tabla.setSelection(conceptoSeleccionado);
            conceptoSeleccionado = listaConceptosEmpresa.get(0);
        } else {
            unaVez = true;
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        System.out.println("conceptoSeleccionado: " + conceptoSeleccionado);
    }

    public void verDetalle(Conceptos conceptoS) {
        conceptoSeleccionado = conceptoS;
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "detalleConcepto");
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

            if (!estadoConceptoEmpresa.equals("TODOS")) {//Si la consulta no es para todos los conceptos (activos o inactivos))
                listaConceptosEmpresa = administrarConceptos.consultarConceptosEmpresaActivos_Inactivos(empresaActual.getSecuencia(), estadoConceptoEmpresa);
                //LLenamos la lista de valores para todos los conceptos de la empresa solo una vez
                if (lovConceptosEmpresa == null) {
                    lovConceptosEmpresa = administrarConceptos.consultarConceptosEmpresa(empresaActual.getSecuencia());
                }
            } else {//Si la consulta es para TODOS:
                listaConceptosEmpresa = administrarConceptos.consultarConceptosEmpresa(empresaActual.getSecuencia());
                //LLenamos la lista de valores para todos los conceptos de la empresa solo una vez
                if (lovConceptosEmpresa == null) {
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

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroUnidad() {
        return infoRegistroUnidad;
    }

    public void setInfoRegistroUnidad(String infoRegistroUnidad) {
        this.infoRegistroUnidad = infoRegistroUnidad;
    }

    public String getInfoRegistroTercero() {
        return infoRegistroTercero;
    }

    public void setInfoRegistroTercero(String infoRegistroTercero) {
        this.infoRegistroTercero = infoRegistroTercero;
    }

    public String getInfoRegistroEmpresa() {
        return infoRegistroEmpresa;
    }

    public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
        this.infoRegistroEmpresa = infoRegistroEmpresa;
    }

    public String getInfoRegistroConcepto() {

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

}
