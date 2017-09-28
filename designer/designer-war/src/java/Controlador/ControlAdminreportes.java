/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Inforeportes;
import Entidades.Modulos;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarInforeportesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
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
public class ControlAdminreportes implements Serializable {

    private static Logger log = Logger.getLogger(ControlAdminreportes.class);

    @EJB
    AdministrarInforeportesInterface administrarInforeportes;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //LISTA INFOREPORTES
    private List<Inforeportes> listaInforeportes;
    private List<Inforeportes> filtradosListaInforeportes;
    private Inforeportes inforeporteSeleccionado;
    //L.O.V INFOREPORTES
    private List<Inforeportes> lovlistaInforeportes;
    private List<Inforeportes> filtrarlovInforeportes;
    private Inforeportes inforeportesSeleccionado;
    //editar celda
    private Inforeportes editarInforeportes;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista;
    //OTROS
    private boolean aceptar;
    private int tipoActualizacion;
    private int bandera;
    //RASTROS
    private boolean guardado, guardarOk, activarLov, activarBuscar, activarMostrar;
    //Crear Novedades
    private List<Inforeportes> listaInforeportesCrear;
    public Inforeportes nuevoInforeporte;
    public Inforeportes duplicarInforeporte;
    private int k;
    private BigInteger l;
    //Modificar Novedades
    private List<Inforeportes> listaInforeportesModificar;
    //Borrar Novedades
    private List<Inforeportes> listaInforeportesBorrar;
    //L.O.V MODULOS
    private List<Modulos> lovModulos;
    private List<Modulos> filtrarLovModulos;
    private Modulos moduloSeleccionado;
    //AUTOCOMPLETAR
    private String Modulo;
    //Columnas Tabla Ciudades
    private Column inforeportesCodigos, inforeportesNombres, inforeportesContadores, inforeportesNombresReportes, inforeportesTipos, inforeportesModulos;
    //ALTO SCROLL TABLA
    private String altoTabla, altoTablaAux;
    private String infoRegistroModulo;
    private String infoRegistroInfoReporte;
    public String infoRegistro;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlAdminreportes() {
        nuevoInforeporte = new Inforeportes();
        nuevoInforeporte.setModulo(new Modulos());
        lovModulos = null;
        listaInforeportes = null;
        aceptar = true;
        inforeportesSeleccionado = null;
        guardado = true;
        tipoLista = 0;
        listaInforeportesBorrar = new ArrayList<Inforeportes>();
        listaInforeportesCrear = new ArrayList<Inforeportes>();
        listaInforeportesModificar = new ArrayList<Inforeportes>();
        altoTabla = "275";
        altoTablaAux = "11";
        duplicarInforeporte = new Inforeportes();
        mapParametros.put("paginaAnterior", paginaAnterior);
        activarBuscar = false;
        activarLov = true;
        activarMostrar = true;
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listaInforeportes = null;
        getListaInforeportes();
        if (listaInforeportes != null) {
            if (!listaInforeportes.isEmpty()) {
                inforeporteSeleccionado = listaInforeportes.get(0);
            }
        }
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
        String pagActual = "adminreportes";
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
        } else {
            controlListaNavegacion.guardarNavegacion(pagActual, pag);
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
            //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
            //mapParaEnviar.put("paginaAnterior", pagActual);
            //mas Parametros
            //         if (pag.equals("rastrotabla")) {
            //           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
            //      } else if (pag.equals("rastrotablaH")) {
            //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //     controlRastro.historicosTabla("Conceptos", pagActual);
            //   pag = "rastrotabla";
            //}
        }
        limpiarListasValor();
    }

    public void limpiarListasValor() {
        lovModulos = null;
        lovlistaInforeportes = null;
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
            administrarInforeportes.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    //UBICACION CELDA
    public void cambiarIndice(Inforeportes ir, int celda) {
        inforeporteSeleccionado = ir;
        cualCelda = celda;
        inforeporteSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            inforeporteSeleccionado.getCodigo();
            deshabilitarLov();
        } else if (cualCelda == 1) {
            deshabilitarLov();
            inforeporteSeleccionado.getNombre();
        } else if (cualCelda == 2) {
            deshabilitarLov();
            inforeporteSeleccionado.getContador();
        } else if (cualCelda == 3) {
            deshabilitarLov();
            inforeporteSeleccionado.getNombrereporte();
        } else if (cualCelda == 4) {
            deshabilitarLov();
            inforeporteSeleccionado.getTipo();
        } else if (cualCelda == 5) {
            activarLov();
            inforeporteSeleccionado.getModulo().getNombre();
        }
    }

    //AUTOCOMPLETAR
    public void modificarInforeportes(Inforeportes ir, String confirmarCambio, String valorConfirmar) {
        inforeporteSeleccionado = ir;

        if (!listaInforeportesCrear.contains(inforeporteSeleccionado)) {
            if (listaInforeportesModificar.isEmpty()) {
                listaInforeportesModificar.add(inforeporteSeleccionado);
            } else if (!listaInforeportesModificar.contains(inforeporteSeleccionado)) {
                listaInforeportesModificar.add(inforeporteSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosInforeportes");
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();

    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosLovIR() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroInfoReporte");
    }

    public void contarRegistrosModulos() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroModulos");
    }

    public void seleccionarTipo(String estadoTipo, Inforeportes ir, int celda) {
        inforeporteSeleccionado = ir;
        RequestContext context = RequestContext.getCurrentInstance();

        if (estadoTipo.equals("PDF")) {
            inforeporteSeleccionado.setTipo("PDF");
        } else if (estadoTipo.equals("XHTML")) {
            inforeporteSeleccionado.setTipo("HTML");
        } else if (estadoTipo.equals("XLS")) {
            inforeporteSeleccionado.setTipo("XLS");
        } else if (estadoTipo.equals("EXCEL")) {
            inforeporteSeleccionado.setTipo("XLSX");
        } else if (estadoTipo.equals("TXT")) {
            inforeporteSeleccionado.setTipo("TXT");
        } else if (estadoTipo.equals("CSV")) {
            inforeporteSeleccionado.setTipo("CSV");
        }

        if (!listaInforeportesCrear.contains(inforeporteSeleccionado)) {
            if (listaInforeportesModificar.isEmpty()) {
                listaInforeportesModificar.add(inforeporteSeleccionado);
            } else if (!listaInforeportesModificar.contains(inforeporteSeleccionado)) {
                listaInforeportesModificar.add(inforeporteSeleccionado);
            }
        }
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosInforeportes");
    }

    public void seleccionarTipoNuevoInforeporte(String estadoTipo, int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (estadoTipo.equals("PDF")) {
                nuevoInforeporte.setTipo("PDF");
            } else if (estadoTipo.equals("XHTML")) {
                nuevoInforeporte.setTipo("HTML");
            } else if (estadoTipo.equals("XLS")) {
                nuevoInforeporte.setTipo("XLS");
            } else if (estadoTipo.equals("EXCEL")) {
                nuevoInforeporte.setTipo("XLSX");
            } else if (estadoTipo.equals("TXT")) {
                nuevoInforeporte.setTipo("TXT");
            } else if (estadoTipo.equals("CSV")) {
                nuevoInforeporte.setTipo("CSV");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoInforeporte");
        } else if (tipoNuevo == 2) {
            if (estadoTipo.equals("PDF")) {
                duplicarInforeporte.setTipo("PDF");
            } else if (estadoTipo.equals("XHTML")) {
                duplicarInforeporte.setTipo("HTML");
            } else if (estadoTipo.equals("XLS")) {
                duplicarInforeporte.setTipo("XLS");
            } else if (estadoTipo.equals("EXCEL")) {
                duplicarInforeporte.setTipo("XLSX");
            } else if (estadoTipo.equals("TXT")) {
                duplicarInforeporte.setTipo("TXT");
            } else if (estadoTipo.equals("CSV")) {
                duplicarInforeporte.setTipo("CSV");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoInforeporte");
        }

    }

    public void asignarIndex(int dlg, int LND) {
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        if (dlg == 0) {
            lovlistaInforeportes = null;
            getLovlistaInforeportes();
            contarRegistrosLovIR();
            RequestContext.getCurrentInstance().update("formularioDialogos:inforeportesDialogo");
            RequestContext.getCurrentInstance().execute("PF('inforeportesDialogo').show()");
        } else if (dlg == 1) {
            lovModulos = null;
            getLovModulos();
            contarRegistrosModulos();
            RequestContext.getCurrentInstance().update("formularioDialogos:modulosDialogo");
            RequestContext.getCurrentInstance().execute("PF('modulosDialogo').show()");
        }
    }

    public void mostrarTodos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaInforeportes.isEmpty()) {
            listaInforeportes.clear();
            listaInforeportes = administrarInforeportes.inforeportes();
        } else {
            listaInforeportes = administrarInforeportes.inforeportes();
        }

        RequestContext.getCurrentInstance().update("form:datosInforeportes");
        filtradosListaInforeportes = null;
        aceptar = true;
        inforeportesSeleccionado = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        activarBuscar = false;
        activarMostrar = true;
        RequestContext.getCurrentInstance().update("form:btnBuscarReportes");
        RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
    }

    public void limpiarduplicarInforeportes() {
        duplicarInforeporte = new Inforeportes();
        duplicarInforeporte.setModulo(new Modulos());
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        if (!duplicarInforeporte.getCodigo().equals(null) && duplicarInforeporte.getModulo().getNombre() != null) {
            for (int i = 0; i < listaInforeportes.size(); i++) {
                if (duplicarInforeporte.getCodigo().equals(listaInforeportes.get(i).getCodigo())) {
                    if (duplicarInforeporte.getModulo().getNombre().equals(listaInforeportes.get(i).getModulo().getNombre())) {
                        pasa++;

                    }
                }
            }
        }
        if (pasa == 0) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarInforeporte.setSecuencia(l);
            listaInforeportes.add(duplicarInforeporte);
            listaInforeportesCrear.add(duplicarInforeporte);
            inforeporteSeleccionado = duplicarInforeporte;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                altoTabla = "275";
                altoTablaAux = "11";
                FacesContext c = FacesContext.getCurrentInstance();
                inforeportesCodigos = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesCodigos");
                inforeportesCodigos.setFilterStyle("display: none; visibility: hidden;");
                inforeportesNombres = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesNombres");
                inforeportesNombres.setFilterStyle("display: none; visibility: hidden;");
                inforeportesContadores = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesContadores");
                inforeportesContadores.setFilterStyle("display: none; visibility: hidden;");
                inforeportesNombresReportes = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesNombresReportes");
                inforeportesNombresReportes.setFilterStyle("display: none; visibility: hidden;");
                inforeportesTipos = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesTipos");
                inforeportesTipos.setFilterStyle("display: none; visibility: hidden;");
                inforeportesModulos = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesModulos");
                inforeportesModulos.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosInforeportes");
                bandera = 0;
                filtradosListaInforeportes = null;
                tipoLista = 0;
            }
            RequestContext.getCurrentInstance().update("form:datosInforeportes");
            duplicarInforeporte = new Inforeportes();
            contarRegistros();
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarInforeporte");
            RequestContext.getCurrentInstance().execute("PF('DuplicarInforeporte').hide()");
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:repetido");
            RequestContext.getCurrentInstance().execute("PF('PF('repetido').show()");
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void actualizarInforeportes() {
        RequestContext context = RequestContext.getCurrentInstance();
        Inforeportes i = inforeportesSeleccionado;
        if (!listaInforeportes.isEmpty()) {
            listaInforeportes.clear();
            listaInforeportes.add(i);
        } else {
            listaInforeportes.add(i);
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        context.reset("formularioDialogos:LOVInforeportes:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVInforeportes').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('inforeportesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVInforeportes");
        RequestContext.getCurrentInstance().update("form:datosInforeportes");
        filtradosListaInforeportes = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        activarBuscar = true;
        activarMostrar = false;
        RequestContext.getCurrentInstance().update("form:btnBuscarReportes");
        RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
    }

    public void actualizarModulos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            inforeporteSeleccionado.setModulo(moduloSeleccionado);
            if (!listaInforeportesCrear.contains(inforeporteSeleccionado)) {
                if (listaInforeportesModificar.isEmpty()) {
                    listaInforeportesModificar.add(inforeporteSeleccionado);
                } else if (!listaInforeportesModificar.contains(inforeporteSeleccionado)) {
                    listaInforeportesModificar.add(inforeporteSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosInforeportes");
        } else if (tipoActualizacion == 1) {
            nuevoInforeporte.setModulo(moduloSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoModuloInforeporte");
        } else if (tipoActualizacion == 2) {
            duplicarInforeporte.setModulo(moduloSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarModuloInforeporte");

        }
        filtrarLovModulos = null;
        moduloSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:modulosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVModulos");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarM");
        context.reset("formularioDialogos:LOVModulos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVModulos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('modulosDialogo').hide()");
        //RequestContext.getCurrentInstance().update("formularioDialogos:LOVModulos");

    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("MODULO")) {
            if (tipoNuevo == 1) {
                Modulo = nuevoInforeporte.getModulo().getNombre();
            } else if (tipoNuevo == 2) {
                Modulo = duplicarInforeporte.getModulo().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("MODULO")) {
            if (tipoNuevo == 1) {
                nuevoInforeporte.getModulo().setNombre(Modulo);
            } else if (tipoNuevo == 2) {
                duplicarInforeporte.getModulo().setNombre(Modulo);
            }
            for (int i = 0; i < lovModulos.size(); i++) {
                if (lovModulos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoInforeporte.setModulo(lovModulos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoModuloInforeporte");
                } else if (tipoNuevo == 2) {
                    duplicarInforeporte.setModulo(lovModulos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarModuloInforeporte");
                }
                lovModulos.clear();
                getLovModulos();
            } else {
                RequestContext.getCurrentInstance().update("form:modulosDialogo");
                RequestContext.getCurrentInstance().execute("PF('modulosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoModuloInforeporte");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarModuloInforeporte");
                }
            }
        }
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (inforeporteSeleccionado != null) {
            if (cualCelda == 5) {
                lovModulos = null;
                getLovModulos();
                contarRegistrosModulos();
                RequestContext.getCurrentInstance().update("formularioDialogos:modulosDialogo");
                RequestContext.getCurrentInstance().execute("PF('modulosDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "255";
            altoTablaAux = "10";
            inforeportesCodigos = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesCodigos");
            inforeportesCodigos.setFilterStyle("width: 85% !important;");
            inforeportesNombres = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesNombres");
            inforeportesNombres.setFilterStyle("width: 85% !important;");
            inforeportesContadores = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesContadores");
            inforeportesContadores.setFilterStyle("width: 85% !important;");
            inforeportesNombresReportes = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesNombresReportes");
            inforeportesNombresReportes.setFilterStyle("width: 85% !important;");
            inforeportesTipos = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesTipos");
            inforeportesTipos.setFilterStyle("width: 85% !important;");
            inforeportesModulos = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesModulos");
            inforeportesModulos.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosInforeportes");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            altoTabla = "275";
            altoTablaAux = "11";
            inforeportesCodigos = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesCodigos");
            inforeportesCodigos.setFilterStyle("display: none; visibility: hidden;");
            inforeportesNombres = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesNombres");
            inforeportesNombres.setFilterStyle("display: none; visibility: hidden;");
            inforeportesContadores = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesContadores");
            inforeportesContadores.setFilterStyle("display: none; visibility: hidden;");
            inforeportesNombresReportes = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesNombresReportes");
            inforeportesNombresReportes.setFilterStyle("display: none; visibility: hidden;");
            inforeportesTipos = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesTipos");
            inforeportesTipos.setFilterStyle("display: none; visibility: hidden;");
            inforeportesModulos = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesModulos");
            inforeportesModulos.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosInforeportes");
            bandera = 0;
            filtradosListaInforeportes = null;
            tipoLista = 0;
        }
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();

            altoTabla = "275";
            altoTablaAux ="11";
            inforeportesCodigos = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesCodigos");
            inforeportesCodigos.setFilterStyle("display: none; visibility: hidden;");
            inforeportesNombres = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesNombres");
            inforeportesNombres.setFilterStyle("display: none; visibility: hidden;");
            inforeportesContadores = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesContadores");
            inforeportesContadores.setFilterStyle("display: none; visibility: hidden;");
            inforeportesNombresReportes = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesNombresReportes");
            inforeportesNombresReportes.setFilterStyle("display: none; visibility: hidden;");
            inforeportesTipos = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesTipos");
            inforeportesTipos.setFilterStyle("display: none; visibility: hidden;");
            inforeportesModulos = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesModulos");
            inforeportesModulos.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosInforeportes");
            bandera = 0;
            filtradosListaInforeportes = null;
            tipoLista = 0;
        }

        listaInforeportesBorrar.clear();
        listaInforeportesCrear.clear();
        listaInforeportesModificar.clear();
        inforeportesSeleccionado = null;
        k = 0;
        listaInforeportes = null;
        getListaInforeportes();
        contarRegistros();
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosInforeportes");
        activarBuscar = false;
        activarMostrar = true;
        RequestContext.getCurrentInstance().update("form:btnBuscarReportes");
        RequestContext.getCurrentInstance().update("form:btnMostrarTodos");

    }

    public void cancelarCambioInforeportes() {
        filtrarlovInforeportes = null;
        inforeportesSeleccionado = null;
        aceptar = true;
        inforeportesSeleccionado = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVInforeportes:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVInforeportes').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('inforeportesDialogo').hide()");
    }

    public void cancelarCambioModulos() {
        filtrarLovModulos = null;
        moduloSeleccionado = null;
        aceptar = true;
        inforeportesSeleccionado = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVModulos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVModulos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('modulosDialogo').hide()");
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (inforeporteSeleccionado != null) {
            editarInforeportes = inforeporteSeleccionado;

            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigos");
                RequestContext.getCurrentInstance().execute("PF('editarCodigos').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarReportes");
                RequestContext.getCurrentInstance().execute("PF('editarReportes').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarContadores");
                RequestContext.getCurrentInstance().execute("PF('editarContadores').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNombresReportes");
                RequestContext.getCurrentInstance().execute("PF('editarNombresReportes').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarModulos");
                RequestContext.getCurrentInstance().execute("PF('editarModulos').show()");
                cualCelda = -1;
            }
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosInforeportesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "AdminreportesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosInforeportesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "AdminreportesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }
    //LIMPIAR NUEVO REGISTRO CIUDAD

    public void limpiarNuevoInforeporte() {
        nuevoInforeporte = new Inforeportes();
        nuevoInforeporte.setModulo(new Modulos());
        nuevoInforeporte.getModulo().setNombre("");
    }

    //CREAR NOVEDADES
    public void agregarNuevoInforeporte() {
        int pasa = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoInforeporte.getCodigo() != null) {
            if (!nuevoInforeporte.getCodigo().equals(null) && nuevoInforeporte.getModulo().getNombre() != null) {
                for (int i = 0; i < listaInforeportes.size(); i++) {
                    if (nuevoInforeporte.getCodigo().equals(listaInforeportes.get(i).getCodigo())) {
                        if (nuevoInforeporte.getModulo().getNombre().equals(listaInforeportes.get(i).getModulo().getNombre())) {
                            pasa++;
                        }
                    }
                }
            }
        }
        if (pasa == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();

                altoTabla = "275";
                inforeportesCodigos = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesCodigos");
                inforeportesCodigos.setFilterStyle("display: none; visibility: hidden;");
                inforeportesNombres = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesNombres");
                inforeportesNombres.setFilterStyle("display: none; visibility: hidden;");
                inforeportesContadores = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesContadores");
                inforeportesContadores.setFilterStyle("display: none; visibility: hidden;");
                inforeportesNombresReportes = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesNombresReportes");
                inforeportesNombresReportes.setFilterStyle("display: none; visibility: hidden;");
                inforeportesTipos = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesTipos");
                inforeportesTipos.setFilterStyle("display: none; visibility: hidden;");
                inforeportesModulos = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesModulos");
                inforeportesModulos.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosInforeportes");
                bandera = 0;
                filtradosListaInforeportes = null;
                tipoLista = 0;

            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoInforeporte.setSecuencia(l);
            if (nuevoInforeporte.isEstadoFechaDesde() == true) {
                nuevoInforeporte.setFecdesde("SI");

            } else if (nuevoInforeporte.isEstadoFechaDesde() == false) {
                nuevoInforeporte.setFecdesde("NO");
            }

            if (nuevoInforeporte.isEstadoFechaHasta() == true) {
                nuevoInforeporte.setFechasta("SI");
            } else if (nuevoInforeporte.isEstadoFechaHasta() == false) {
                nuevoInforeporte.setFechasta("NO");
            }

            if (nuevoInforeporte.isEstadoEmpleadoDesde() == true) {
                nuevoInforeporte.setEmdesde("SI");
            } else if (nuevoInforeporte.isEstadoEmpleadoDesde() == false) {
                nuevoInforeporte.setEmdesde("NO");
            }

            if (nuevoInforeporte.isEstadoEmpleadoHasta() == true) {
                nuevoInforeporte.setEmhasta("SI");
            } else if (nuevoInforeporte.isEstadoEmpleadoHasta() == false) {
                nuevoInforeporte.setEmhasta("NO");
            }

            if (nuevoInforeporte.isEstadoLocalizacion() == true) {
                nuevoInforeporte.setLocalizacion("SI");
            } else if (nuevoInforeporte.isEstadoLocalizacion() == false) {
                nuevoInforeporte.setLocalizacion("NO");
            }

            if (nuevoInforeporte.isEstadoEstado() == true) {

                nuevoInforeporte.setEstado("SI");
            } else if (nuevoInforeporte.isEstadoEstado() == false) {
                nuevoInforeporte.setEstado("NO");
            }

            if (nuevoInforeporte.isEstadoGrupo() == true) {

                nuevoInforeporte.setGrupo("SI");
            } else if (nuevoInforeporte.isEstadoGrupo() == false) {
                nuevoInforeporte.setGrupo("NO");
            }

            if (nuevoInforeporte.isEstadoTercero() == true) {

                nuevoInforeporte.setTercero("SI");
            } else if (nuevoInforeporte.isEstadoTercero() == false) {
                nuevoInforeporte.setTercero("NO");
            }

            if (nuevoInforeporte.isEstadoTrabajador() == true) {

                nuevoInforeporte.setTrabajador("SI");
            } else if (nuevoInforeporte.isEstadoTrabajador() == false) {
                nuevoInforeporte.setTrabajador("NO");
            }

            if (nuevoInforeporte.isEstadoTipoTrabajador() == true) {

                nuevoInforeporte.setTipotrabajador("SI");
            } else if (nuevoInforeporte.isEstadoTipoTrabajador() == false) {
                nuevoInforeporte.setTipotrabajador("NO");
            }

            if (nuevoInforeporte.isEstadoSolicitud() == true) {

                nuevoInforeporte.setSolicitud("SI");
            } else if (nuevoInforeporte.isEstadoSolicitud() == false) {
                nuevoInforeporte.setSolicitud("NO");
            }

            if (nuevoInforeporte.isEstadoCiudad() == true) {

                nuevoInforeporte.setCiudad("SI");
            } else if (nuevoInforeporte.isEstadoCiudad() == false) {
                nuevoInforeporte.setCiudad("NO");
            }

            if (nuevoInforeporte.isEstadoTipoTelefono() == true) {

                nuevoInforeporte.setTipotelefono("SI");
            } else if (nuevoInforeporte.isEstadoTipoTelefono() == false) {
                nuevoInforeporte.setTipotelefono("NO");
            }

            if (nuevoInforeporte.isEstadoEstadoCivil() == true) {

                nuevoInforeporte.setEstadocivil("SI");
            } else if (nuevoInforeporte.isEstadoEstadoCivil() == false) {
                nuevoInforeporte.setEstadocivil("NO");
            }

            if (nuevoInforeporte.isEstadoDeporte() == true) {

                nuevoInforeporte.setDeporte("SI");
            } else if (nuevoInforeporte.isEstadoDeporte() == false) {
                nuevoInforeporte.setDeporte("NO");
            }

            if (nuevoInforeporte.isEstadoIdioma() == true) {

                nuevoInforeporte.setIdioma("SI");
            } else if (nuevoInforeporte.isEstadoIdioma() == false) {
                nuevoInforeporte.setIdioma("NO");
            }

            if (nuevoInforeporte.isEstadoAficion() == true) {

                nuevoInforeporte.setAficion("SI");
            } else if (nuevoInforeporte.isEstadoAficion() == false) {
                nuevoInforeporte.setAficion("NO");
            }

            if (nuevoInforeporte.isEstadoJefeDivision() == true) {

                nuevoInforeporte.setJefedivision("SI");
            } else if (nuevoInforeporte.isEstadoJefeDivision() == false) {
                nuevoInforeporte.setJefedivision("NO");
            }

            if (nuevoInforeporte.isEstadoRodamiento() == true) {

                nuevoInforeporte.setRodamiento("SI");
            } else if (nuevoInforeporte.isEstadoRodamiento() == false) {
                nuevoInforeporte.setRodamiento("NO");
            }

            if (nuevoInforeporte.isEstadoEnvioMasivo() == true) {

                nuevoInforeporte.setEnviomasivo("S");
            } else if (nuevoInforeporte.isEstadoEnvioMasivo() == false) {
                nuevoInforeporte.setEnviomasivo("N");
            }

            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            listaInforeportesCrear.add(nuevoInforeporte);
            listaInforeportes.add(nuevoInforeporte);
            inforeportesSeleccionado = nuevoInforeporte;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosInforeportes");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('NuevoInforeporte').hide()");
            nuevoInforeporte = new Inforeportes();
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:repetido");
            RequestContext.getCurrentInstance().update("PF('repetido').show()");
        }
    }

    //BORRAR CIUDADES
    public void borrarInforeportes() {

        if (inforeporteSeleccionado != null) {
            if (!listaInforeportesModificar.isEmpty() && listaInforeportesModificar.contains(inforeporteSeleccionado)) {
                int modIndex = listaInforeportesModificar.indexOf(inforeporteSeleccionado);
                listaInforeportesModificar.remove(modIndex);
                listaInforeportesBorrar.add(inforeporteSeleccionado);
            } else if (!listaInforeportesCrear.isEmpty() && listaInforeportesCrear.contains(inforeporteSeleccionado)) {
                int crearIndex = listaInforeportesCrear.indexOf(inforeporteSeleccionado);
                listaInforeportesCrear.remove(crearIndex);
            } else {
                listaInforeportesBorrar.add(inforeporteSeleccionado);
            }
            listaInforeportes.remove(inforeporteSeleccionado);

            if (tipoLista == 1) {
                filtradosListaInforeportes.remove(inforeporteSeleccionado);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosInforeportes");
            contarRegistros();

            inforeportesSeleccionado = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    //GUARDAR
    public void guardarCambiosInforeportes() {
        if (guardado == false) {
            if (!listaInforeportesBorrar.isEmpty()) {
                for (int i = 0; i < listaInforeportesBorrar.size(); i++) {
                    log.info("Borrando..." + listaInforeportesBorrar.size());

                    if (listaInforeportesBorrar.get(i).getAficion() == null) {
                        listaInforeportesBorrar.get(i).setAficion(null);
                    }
                    if (listaInforeportesBorrar.get(i).getCiudad() == null) {
                        listaInforeportesBorrar.get(i).setCiudad(null);
                    }
                    if (listaInforeportesBorrar.get(i).getCodigo() == null) {
                        listaInforeportesBorrar.get(i).setCodigo(null);
                    }
                    if (listaInforeportesBorrar.get(i).getContador() == null) {
                        listaInforeportesBorrar.get(i).setContador(null);
                    }
                    if (listaInforeportesBorrar.get(i).getDeporte() == null) {
                        listaInforeportesBorrar.get(i).setDeporte(null);
                    }
                    if (listaInforeportesBorrar.get(i).getEmdesde() == null) {
                        listaInforeportesBorrar.get(i).setEmdesde(null);
                    }
                    if (listaInforeportesBorrar.get(i).getEmhasta() == null) {
                        listaInforeportesBorrar.get(i).setEmhasta(null);
                    }
                    if (listaInforeportesBorrar.get(i).getEnviomasivo() == null) {
                        listaInforeportesBorrar.get(i).setEnviomasivo(null);
                    }
                    if (listaInforeportesBorrar.get(i).getEstado() == null) {
                        listaInforeportesBorrar.get(i).setEstado(null);
                    }
                    if (listaInforeportesBorrar.get(i).getEstadocivil() == null) {
                        listaInforeportesBorrar.get(i).setEstadocivil(null);
                    }
                    if (listaInforeportesBorrar.get(i).getFecdesde() == null) {
                        listaInforeportesBorrar.get(i).setFecdesde(null);
                    }
                    if (listaInforeportesBorrar.get(i).getFechasta() == null) {
                        listaInforeportesBorrar.get(i).setFechasta(null);
                    }
                    if (listaInforeportesBorrar.get(i).getGrupo() == null) {
                        listaInforeportesBorrar.get(i).setGrupo(null);
                    }
                    if (listaInforeportesBorrar.get(i).getIdioma() == null) {
                        listaInforeportesBorrar.get(i).setIdioma(null);
                    }
                    if (listaInforeportesBorrar.get(i).getJefedivision() == null) {
                        listaInforeportesBorrar.get(i).setJefedivision(null);
                    }
                    if (listaInforeportesBorrar.get(i).getLocalizacion() == null) {
                        listaInforeportesBorrar.get(i).setLocalizacion(null);
                    }
                    if (listaInforeportesBorrar.get(i).getModulo().getSecuencia() == null) {
                        listaInforeportesBorrar.get(i).setModulo(null);
                    }
                    if (listaInforeportesBorrar.get(i).getNombre() == null) {
                        listaInforeportesBorrar.get(i).setNombre(null);
                    }
                    if (listaInforeportesBorrar.get(i).getNombrereporte() == null) {
                        listaInforeportesBorrar.get(i).setNombrereporte(null);
                    }
                    if (listaInforeportesBorrar.get(i).getRodamiento() == null) {
                        listaInforeportesBorrar.get(i).setRodamiento(null);
                    }
                    if (listaInforeportesBorrar.get(i).getSolicitud() == null) {
                        listaInforeportesBorrar.get(i).setSolicitud(null);
                    }
                    if (listaInforeportesBorrar.get(i).getTercero() == null) {
                        listaInforeportesBorrar.get(i).setTercero(null);
                    }
                    if (listaInforeportesBorrar.get(i).getTipo() == null) {
                        listaInforeportesBorrar.get(i).setTipo(null);
                    }
                    if (listaInforeportesBorrar.get(i).getTipotelefono() == null) {
                        listaInforeportesBorrar.get(i).setTipotelefono(null);
                    }
                    if (listaInforeportesBorrar.get(i).getTipotrabajador() == null) {
                        listaInforeportesBorrar.get(i).setTipotrabajador(null);
                    }
                    if (listaInforeportesBorrar.get(i).getTrabajador() == null) {
                        listaInforeportesBorrar.get(i).setTrabajador(null);
                    }
                    administrarInforeportes.borrarInforeporte(listaInforeportesBorrar.get(i));
                }
                log.info("Entra");
                listaInforeportesBorrar.clear();
            }

            if (!listaInforeportesCrear.isEmpty()) {
                for (int i = 0; i < listaInforeportesCrear.size(); i++) {
                    log.info("Creando...");

                    if (listaInforeportesCrear.get(i).getAficion() == null) {
                        listaInforeportesCrear.get(i).setAficion(null);
                    }
                    if (listaInforeportesCrear.get(i).getCiudad() == null) {
                        listaInforeportesCrear.get(i).setCiudad(null);
                    }
                    if (listaInforeportesCrear.get(i).getCodigo() == null) {
                        listaInforeportesCrear.get(i).setCodigo(null);
                    }
                    if (listaInforeportesCrear.get(i).getContador() == null) {
                        listaInforeportesCrear.get(i).setContador(null);
                    }
                    if (listaInforeportesCrear.get(i).getDeporte() == null) {
                        listaInforeportesCrear.get(i).setDeporte(null);
                    }
                    if (listaInforeportesCrear.get(i).getEmdesde() == null) {
                        listaInforeportesCrear.get(i).setEmdesde(null);
                    }
                    if (listaInforeportesCrear.get(i).getEmhasta() == null) {
                        listaInforeportesCrear.get(i).setEmhasta(null);
                    }
                    if (listaInforeportesCrear.get(i).getEnviomasivo() == null) {
                        listaInforeportesCrear.get(i).setEnviomasivo(null);
                    }
                    if (listaInforeportesCrear.get(i).getEstado() == null) {
                        listaInforeportesCrear.get(i).setEstado(null);
                    }
                    if (listaInforeportesCrear.get(i).getEstadocivil() == null) {
                        listaInforeportesCrear.get(i).setEstadocivil(null);
                    }
                    if (listaInforeportesCrear.get(i).getFecdesde() == null) {
                        listaInforeportesCrear.get(i).setFecdesde(null);
                    }
                    if (listaInforeportesCrear.get(i).getFechasta() == null) {
                        listaInforeportesCrear.get(i).setFechasta(null);
                    }
                    if (listaInforeportesCrear.get(i).getGrupo() == null) {
                        listaInforeportesCrear.get(i).setGrupo(null);
                    }
                    if (listaInforeportesCrear.get(i).getIdioma() == null) {
                        listaInforeportesCrear.get(i).setIdioma(null);
                    }
                    if (listaInforeportesCrear.get(i).getJefedivision() == null) {
                        listaInforeportesCrear.get(i).setJefedivision(null);
                    }
                    if (listaInforeportesCrear.get(i).getLocalizacion() == null) {
                        listaInforeportesCrear.get(i).setLocalizacion(null);
                    }
                    if (listaInforeportesCrear.get(i).getModulo().getSecuencia() == null) {
                        listaInforeportesCrear.get(i).setModulo(null);
                    }
                    if (listaInforeportesCrear.get(i).getNombre() == null) {
                        listaInforeportesCrear.get(i).setNombre(null);
                    }
                    if (listaInforeportesCrear.get(i).getNombrereporte() == null) {
                        listaInforeportesCrear.get(i).setNombrereporte(null);
                    }
                    if (listaInforeportesCrear.get(i).getRodamiento() == null) {
                        listaInforeportesCrear.get(i).setRodamiento(null);
                    }
                    if (listaInforeportesCrear.get(i).getSolicitud() == null) {
                        listaInforeportesCrear.get(i).setSolicitud(null);
                    }
                    if (listaInforeportesCrear.get(i).getTercero() == null) {
                        listaInforeportesCrear.get(i).setTercero(null);
                    }
                    if (listaInforeportesCrear.get(i).getTipo() == null) {
                        listaInforeportesCrear.get(i).setTipo(null);
                    }
                    if (listaInforeportesCrear.get(i).getTipotelefono() == null) {
                        listaInforeportesCrear.get(i).setTipotelefono(null);
                    }
                    if (listaInforeportesCrear.get(i).getTipotrabajador() == null) {
                        listaInforeportesCrear.get(i).setTipotrabajador(null);
                    }
                    if (listaInforeportesCrear.get(i).getTrabajador() == null) {
                        listaInforeportesCrear.get(i).setTrabajador(null);
                    }

                    administrarInforeportes.crearInforeporte(listaInforeportesCrear.get(i));
                }
                log.info("LimpiaLista");
                listaInforeportesCrear.clear();
            }
            if (!listaInforeportesModificar.isEmpty()) {
                administrarInforeportes.modificarInforeporte(listaInforeportesModificar);
                listaInforeportesModificar.clear();
            }

            log.info("Se guardaron los datos con exito");
            listaInforeportes = null;
            getListaInforeportes();
            if (listaInforeportes != null && !listaInforeportes.isEmpty()) {
                inforeporteSeleccionado = listaInforeportes.get(0);
                infoRegistro = "Cantidad de registros: " + listaInforeportes.size();
            } else {
                infoRegistro = "Cantidad de registros: 0";
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosInforeportes");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            //  k = 0;
        }
        inforeportesSeleccionado = null;
    }

    //DUPLICAR CIUDAD
    public void duplicarI() {
        if (inforeporteSeleccionado != null) {
            duplicarInforeporte = new Inforeportes();
            duplicarInforeporte.setCodigo(inforeporteSeleccionado.getCodigo());
            duplicarInforeporte.setNombre(inforeporteSeleccionado.getNombre());
            duplicarInforeporte.setContador(inforeporteSeleccionado.getContador());
            duplicarInforeporte.setNombrereporte(inforeporteSeleccionado.getNombrereporte());
            duplicarInforeporte.setTipo(inforeporteSeleccionado.getTipo());
            duplicarInforeporte.setModulo(inforeporteSeleccionado.getModulo());
            duplicarInforeporte.setFecdesde(inforeporteSeleccionado.getFecdesde());
            duplicarInforeporte.setFechasta(inforeporteSeleccionado.getFechasta());
            duplicarInforeporte.setEmdesde(inforeporteSeleccionado.getEmdesde());
            duplicarInforeporte.setEmhasta(inforeporteSeleccionado.getEmhasta());
            duplicarInforeporte.setLocalizacion(inforeporteSeleccionado.getLocalizacion());
            duplicarInforeporte.setEstado(inforeporteSeleccionado.getEstado());
            duplicarInforeporte.setGrupo(inforeporteSeleccionado.getGrupo());
            duplicarInforeporte.setTercero(inforeporteSeleccionado.getTercero());
            duplicarInforeporte.setTrabajador(inforeporteSeleccionado.getTrabajador());
            duplicarInforeporte.setTipotrabajador(inforeporteSeleccionado.getTipotrabajador());
            duplicarInforeporte.setSolicitud(inforeporteSeleccionado.getSolicitud());
            duplicarInforeporte.setCiudad(inforeporteSeleccionado.getCiudad());
            duplicarInforeporte.setTipotelefono(inforeporteSeleccionado.getTipotelefono());
            duplicarInforeporte.setEstadocivil(inforeporteSeleccionado.getEstadocivil());
            duplicarInforeporte.setDeporte(inforeporteSeleccionado.getDeporte());
            duplicarInforeporte.setIdioma(inforeporteSeleccionado.getIdioma());
            duplicarInforeporte.setAficion(inforeporteSeleccionado.getAficion());
            duplicarInforeporte.setJefedivision(inforeporteSeleccionado.getJefedivision());
            duplicarInforeporte.setRodamiento(inforeporteSeleccionado.getRodamiento());
            duplicarInforeporte.setEnviomasivo(inforeporteSeleccionado.getEnviomasivo());

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarInforeporte");
            RequestContext.getCurrentInstance().execute("PF('DuplicarInforeporte').show()");
        }
    }

    //RASTROS 
    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (inforeporteSeleccionado != null) {
            log.info("lol 2");
            int result = administrarRastros.obtenerTabla(inforeporteSeleccionado.getSecuencia(), "INFOREPORTES");
            log.info("resultado: " + result);
            if (result == 1) {
                RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
            } else if (result == 2) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
            } else if (result == 3) {
                RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
            } else if (result == 4) {
                RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
            } else if (result == 5) {
                RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
            }
        } else if (administrarRastros.verificarHistoricosTabla("INFOREPORTES")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void activarLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTabla = "275";
            altoTablaAux = "11";
            inforeportesCodigos = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesCodigos");
            inforeportesCodigos.setFilterStyle("display: none; visibility: hidden;");
            inforeportesNombres = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesNombres");
            inforeportesNombres.setFilterStyle("display: none; visibility: hidden;");
            inforeportesContadores = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesContadores");
            inforeportesContadores.setFilterStyle("display: none; visibility: hidden;");
            inforeportesNombresReportes = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesNombresReportes");
            inforeportesNombresReportes.setFilterStyle("display: none; visibility: hidden;");
            inforeportesTipos = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesTipos");
            inforeportesTipos.setFilterStyle("display: none; visibility: hidden;");
            inforeportesModulos = (Column) c.getViewRoot().findComponent("form:datosInforeportes:inforeportesModulos");
            inforeportesModulos.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosInforeportes");
            bandera = 0;
            filtradosListaInforeportes = null;
            tipoLista = 0;
        }

        listaInforeportesBorrar.clear();
        listaInforeportesCrear.clear();
        listaInforeportesModificar.clear();
        inforeportesSeleccionado = null;
        k = 0;
        listaInforeportes = null;
        guardado = true;
        activarBuscar = false;
        activarMostrar = true;
        navegar("atras");
    }

    //Getter & Setter
    public List<Inforeportes> getListaInforeportes() {
        if (listaInforeportes == null) {
            listaInforeportes = administrarInforeportes.listaInfoReportes();
        }
        return listaInforeportes;
    }

    public void setListaInforeportes(List<Inforeportes> listaInforeportes) {
        this.listaInforeportes = listaInforeportes;
    }

    public List<Inforeportes> getFiltradosListaInforeportes() {
        return filtradosListaInforeportes;
    }

    public void setFiltradosListaInforeportes(List<Inforeportes> filtradosListaInforeportes) {
        this.filtradosListaInforeportes = filtradosListaInforeportes;
    }

    public List<Inforeportes> getLovlistaInforeportes() {
        if (lovlistaInforeportes == null) {
            lovlistaInforeportes = administrarInforeportes.inforeportes();
        }
        return lovlistaInforeportes;
    }

    public void setLovlistaInforeportes(List<Inforeportes> lovlistaInforeportes) {
        this.lovlistaInforeportes = lovlistaInforeportes;
    }

    public List<Inforeportes> getFiltrarlovInforeportes() {
        return filtrarlovInforeportes;
    }

    public void setFiltrarlovInforeportes(List<Inforeportes> filtrarlovInforeportes) {
        this.filtrarlovInforeportes = filtrarlovInforeportes;
    }

    public Inforeportes getInforeportesSeleccionado() {
        return inforeportesSeleccionado;
    }

    public void setInforeportesSeleccionado(Inforeportes inforeportesSeleccionado) {
        this.inforeportesSeleccionado = inforeportesSeleccionado;
    }

    public List<Modulos> getLovModulos() {
        if (lovModulos == null) {
            lovModulos = administrarInforeportes.lovmodulos();
        }
        return lovModulos;
    }

    public void setLovModulos(List<Modulos> lovModulos) {
        this.lovModulos = lovModulos;
    }

    public List<Modulos> getFiltrarLovModulos() {
        return filtrarLovModulos;
    }

    public void setFiltrarLovModulos(List<Modulos> filtrarLovModulos) {
        this.filtrarLovModulos = filtrarLovModulos;
    }

    public Modulos getModuloSeleccionado() {
        return moduloSeleccionado;
    }

    public void setModuloSeleccionado(Modulos moduloSeleccionado) {
        this.moduloSeleccionado = moduloSeleccionado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public Inforeportes getEditarInforeportes() {
        return editarInforeportes;
    }

    public void setEditarInforeportes(Inforeportes editarInforeportes) {
        this.editarInforeportes = editarInforeportes;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getAltoTablaAux() {
        return altoTablaAux;
    }

    public void setAltoTablaAux(String altoTablaAux) {
        this.altoTablaAux = altoTablaAux;
    }
    
    public Inforeportes getNuevoInforeporte() {
        return nuevoInforeporte;
    }

    public void setNuevoInforeporte(Inforeportes nuevoInforeporte) {
        this.nuevoInforeporte = nuevoInforeporte;
    }

    public Inforeportes getDuplicarInforeporte() {
        return duplicarInforeporte;
    }

    public void setDuplicarInforeporte(Inforeportes duplicarInforeporte) {
        this.duplicarInforeporte = duplicarInforeporte;
    }

    public Inforeportes getInforeporteSeleccionado() {
        return inforeporteSeleccionado;
    }

    public void setInforeporteSeleccionado(Inforeportes inforeporteSeleccionado) {
        this.inforeporteSeleccionado = inforeporteSeleccionado;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getInfoRegistroModulo() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVModulos");
        infoRegistroModulo = String.valueOf(tabla.getRowCount());
        return infoRegistroModulo;
    }

    public void setInfoRegistroModulo(String infoRegistroModulo) {
        this.infoRegistroModulo = infoRegistroModulo;
    }

    public String getInfoRegistroInfoReporte() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVInforeportes");
        infoRegistroInfoReporte = String.valueOf(tabla.getRowCount());
        return infoRegistroInfoReporte;
    }

    public void setInfoRegistroInfoReporte(String infoRegistroInfoReporte) {
        this.infoRegistroInfoReporte = infoRegistroInfoReporte;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosInforeportes");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public boolean isActivarBuscar() {
        return activarBuscar;
    }

    public void setActivarBuscar(boolean activarBuscar) {
        this.activarBuscar = activarBuscar;
    }

    public boolean isActivarMostrar() {
        return activarMostrar;
    }

    public void setActivarMostrar(boolean activarMostrar) {
        this.activarMostrar = activarMostrar;
    }

}
