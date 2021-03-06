/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.*;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarFormulaConceptoInterface;
import InterfaceAdministrar.AdministrarNovedadesConceptosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import ControlNavegacion.ListasRecurrentes;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
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
public class ControlNovedadesConceptos implements Serializable {

    private static Logger log = Logger.getLogger(ControlNovedadesConceptos.class);

    @EJB
    AdministrarNovedadesConceptosInterface administrarNovedadesConceptos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarFormulaConceptoInterface administrarFormulaConcepto;
    //LISTA NOVEDADES
    private List<Novedades> listaNovedades;
    private List<Novedades> filtradosListaNovedades;
    private Novedades novedadSeleccionada;
    private Novedades novedadBackup;
    //LISTA QUE NO ES LISTA - 1 SOLO ELEMENTO
    private List<Conceptos> listaConceptosNovedad;
    private List<Conceptos> filtradosListaConceptos;
    private Conceptos conceptoSeleccionado; //Seleccion Mostrar
    //editar celda
    private Novedades editarNovedades;
    private int cualCelda, tipoLista, tipoListaNov;
    //OTROS
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //RASTROS
    private boolean guardado;
    //Crear Novedades
    private List<Novedades> listaNovedadesCrear;
    public Novedades nuevaNovedad;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    //Modificar Novedades
    private List<Novedades> listaNovedadesModificar;
    //Borrar Novedades
    private List<Novedades> listaNovedadesBorrar;
    //Autocompletar
    private String codigoEmpleado, nitTercero, formula, descripcionConcepto, descripcionPeriodicidad, nombreTercero;
    private Date FechaFinal;
    // private Short CodigoPeriodicidad;
    private String CodigoPeriodicidad;
    private BigDecimal Saldo;
    private Integer HoraDia, MinutoHora;
    //L.O.V Conceptos
    private List<Conceptos> lovConceptos;
    private List<Conceptos> filtrarlovConceptos;
    private Conceptos conceptoLovSeleccionado;
    //L.O.V Empleados
    private List<Empleados> lovEmpleados;
    private List<Empleados> filtrarLovEmpleados;
    private Empleados empleadoLovSeleccionado;
    //L.O.V PERIODICIDADES
    private List<Periodicidades> lovPeriodicidades;
    private List<Periodicidades> filtrarLovPeriodicidades;
    private Periodicidades periodicidadlovSeleccionada;
    //L.O.V TERCEROS
    private List<Terceros> lovTerceros;
    private List<Terceros> filtrarLovTerceros;
    private Terceros terceroLovSeleccionado;
    //L.O.V FORMULAS
    private List<Formulas> lovFormulas;
    private List<Formulas> filtrarLovFormulas;
    private Formulas formulaLovSeleccionada;
    //Columnas Tabla NOVEDADES
    private Column nCConceptosDescripcion, nCConceptosCodigos;
    private Column nCEmpleadoCodigo, nCEmpleadoNombre, nCFechasInicial, nCFechasFinal,
            nCValor, nCSaldo, nCPeriodicidadCodigo, nCDescripcionPeriodicidad, nCTercerosNit,
            nCTercerosNombre, nCFormulas, nCHorasDias, nCMinutosHoras, nCTipo;
    //Duplicar
    private Novedades duplicarNovedad;
    //USUARIO
    private String alias;
    private Usuarios usuarioBD;
    //VALIDAR SI EL QUE SE VA A BORRAR ESTÃ� EN SOLUCIONES FORMULAS
    private int resultado;
    private boolean todas;
    private boolean actuales;
    private String altoTablaReg, altoTablaRegConc, altoTabla, altoTablaConceptos;
    private String infoRegistroConceptos;
    private String infoRegistroNovedades;
    private String infoRegistroLovConceptos;
    private String infoRegistroLovFormulas;
    private String infoRegistroLovEmpleados;
    private String infoRegistroLovPeriodicidades;
    private String infoRegistroLovTerceros;
    private Date fechaContratacionE;
    private boolean activarLOV;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private String msgError;

    private ListasRecurrentes listasRecurrentes;

    public ControlNovedadesConceptos() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        listasRecurrentes = controlListaNavegacion.getListasRecurrentes();
        permitirIndex = true;
        lovFormulas = null;
        lovEmpleados = null;
        lovPeriodicidades = null;
        lovConceptos = null;
        listaConceptosNovedad = null;
        todas = false;
        actuales = true;
        novedadSeleccionada = null;
        aceptar = true;
        guardado = true;
        tipoLista = 0;
        tipoListaNov = 0;
        listaNovedadesBorrar = new ArrayList<Novedades>();
        listaNovedadesCrear = new ArrayList<Novedades>();
        listaNovedadesModificar = new ArrayList<Novedades>();
        //Crear VC
        nuevaNovedad = new Novedades();
        nuevaNovedad.setEmpleado(new Empleados());
        nuevaNovedad.setFormula(new Formulas());
        nuevaNovedad.setPeriodicidad(new Periodicidades());
        nuevaNovedad.getPeriodicidad().setNombre(" ");
        nuevaNovedad.setTercero(new Terceros());
        nuevaNovedad.getTercero().setNombre(" ");
        nuevaNovedad.setConcepto(new Conceptos());
        nuevaNovedad.getConcepto().setDescripcion(" ");
        nuevaNovedad.setTipo("FIJA");
        nuevaNovedad.setUsuarioreporta(new Usuarios());
        nuevaNovedad.setTerminal(" ");
        nuevaNovedad.setFechareporte(new Date());
        nuevaNovedad.setValortotal(new BigDecimal(0));
        altoTablaReg = "5";
        infoRegistroConceptos = "0";
        infoRegistroNovedades = "0";
        infoRegistroLovConceptos = "0";
        infoRegistroLovFormulas = "0";
        infoRegistroLovEmpleados = "0";
        infoRegistroLovPeriodicidades = "0";
        infoRegistroLovTerceros = "0";
        activarLOV = true;
        paginaAnterior = "nominaf";
        mapParametros.put("paginaAnterior", paginaAnterior);
        fechaContratacionE = new Date();
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
            administrarNovedadesConceptos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            administrarFormulaConcepto.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        getListaConceptosNovedad();
        if (listaConceptosNovedad != null) {
            if (!listaConceptosNovedad.isEmpty()) {
                conceptoSeleccionado = listaConceptosNovedad.get(0);
            }
        }
        if (conceptoSeleccionado != null) {
            listaNovedades = administrarNovedadesConceptos.novedadesConcepto(conceptoSeleccionado.getSecuencia());
        }
        novedadSeleccionada = null;
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        getListaConceptosNovedad();
        if (listaConceptosNovedad != null) {
            conceptoSeleccionado = listaConceptosNovedad.get(0);
        }
        if (conceptoSeleccionado != null) {
            listaNovedades = administrarNovedadesConceptos.novedadesConcepto(conceptoSeleccionado.getSecuencia());
        }
        novedadSeleccionada = null;
    }

    //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "novedadconcepto";
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
        lovConceptos = null;
        lovEmpleados = null;
//      lovFormulas = null;
//      lovPeriodicidades = null;
        lovTerceros = null;
    }

    public String retornarPagina() {
        anularBotonLOV();
        return paginaAnterior;
    }

    //Ubicacion Celda Arriba 
    public void cambiarConcepto() {
        //Si ninguna de las 3 listas (crear,modificar,borrar) tiene algo, hace esto
        if (listaNovedadesCrear.isEmpty() && listaNovedadesBorrar.isEmpty() && listaNovedadesModificar.isEmpty()) {
            if (listaNovedades != null) {
                listaNovedades.clear();
            }
            llenarTablaNovedades();
            if (tipoListaNov == 1) {
                RequestContext.getCurrentInstance().execute("PF('datosNovedadesConcepto').clearFilters()");
            }
            RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
            contarRegistrosNovedades();
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:cambiar");
            RequestContext.getCurrentInstance().execute("PF('cambiar').show()");
        }
    }

    public void limpiarListas() {
        listaNovedadesCrear.clear();
        listaNovedadesBorrar.clear();
        listaNovedadesModificar.clear();
        guardado = true;
        listaNovedades.clear();
        anularBotonLOV();
        RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarCambioEmpleados() {
        filtrarLovEmpleados = null;
        empleadoLovSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovEmpleados:LOVEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().update("formLovEmpleados:LOVEmpleados");
        RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
        RequestContext.getCurrentInstance().update("formLovEmpleados:LOVEmpleados");
        RequestContext.getCurrentInstance().update("formLovEmpleados:aceptarE");
        RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
    }

    public void cancelarCambioPeriodicidades() {
        filtrarLovPeriodicidades = null;
        periodicidadlovSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovPeriodicidad:LOVPeriodicidades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVPeriodicidades').clearFilters()");
        RequestContext.getCurrentInstance().update("formLovPeriodicidad:LOVPeriodicidades");
        RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
        RequestContext.getCurrentInstance().update("formLovPeriodicidad:aceptarP");
        RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').hide()");
    }

    public void cancelarCambioFormulas() {
        filtrarLovFormulas = null;
        formulaLovSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovFormulas:LOVFormulas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVFormulas').clearFilters()");
        RequestContext.getCurrentInstance().update("formLovFormulas:LOVFormulas");
        RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
        RequestContext.getCurrentInstance().update("formLovFormulas:aceptarF");
        RequestContext.getCurrentInstance().execute("PF('formulasDialogo').hide()");
    }

    public void cancelarCambioConceptos() {
        filtrarlovConceptos = null;
        conceptoLovSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovConceptos:LOVConceptos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVConceptos').clearFilters()");
        RequestContext.getCurrentInstance().update("formLovConceptos:LOVConceptos");
        RequestContext.getCurrentInstance().update("formLovConceptos:conceptosDialogo");
        RequestContext.getCurrentInstance().update("formLovConceptos:aceptarC");
        RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
    }

    public void cancelarCambioTerceros() {
        filtrarLovTerceros = null;
        terceroLovSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovTerceros:LOVTerceros:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTerceros').clearFilters()");
        RequestContext.getCurrentInstance().update("formLovTerceros:LOVTerceros");
        RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
        RequestContext.getCurrentInstance().update("formLovTerceros:aceptarT");
        RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').hide()");
    }

    public void seleccionarTipoNuevaNovedad(String tipo, int tipoNuevo) {
        anularBotonLOV();
        if (tipoNuevo == 1) {
            if (tipo.equals("FIJA")) {
                nuevaNovedad.setTipo("FIJA");
            } else if (tipo.equals("OCASIONAL")) {
                nuevaNovedad.setTipo("OCASIONAL");
            } else if (tipo.equals("PAGO POR FUERA")) {
                nuevaNovedad.setTipo("PAGO POR FUERA");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipo");
        } else {
            if (tipo.equals("FIJA")) {
                duplicarNovedad.setTipo("FIJA");
            } else if (tipo.equals("OCASIONAL")) {
                duplicarNovedad.setTipo("OCASIONAL");
            } else if (tipo.equals("PAGO POR FUERA")) {
                duplicarNovedad.setTipo("PAGO POR FUERA");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipo");
        }
    }

    public void seleccionarTipo(Novedades novedad,String estadoTipo, int indice) {
        RequestContext context = RequestContext.getCurrentInstance();
        novedadSeleccionada = novedad;
        anularBotonLOV();
        if (estadoTipo.equals("FIJA")) {
            novedadSeleccionada.setTipo("FIJA");
        } else if (estadoTipo.equals("OCASIONAL")) {
            novedadSeleccionada.setTipo("OCASIONAL");
        } else if (estadoTipo.equals("PAGO POR FUERA")) {
            novedadSeleccionada.setTipo("PAGO POR FUERA");
        }

        if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
            if (listaNovedadesModificar.isEmpty()) {
                listaNovedadesModificar.add(novedadSeleccionada);
            } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                listaNovedadesModificar.add(novedadSeleccionada);
            }
        }
        if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
    }

    public void asignarIndex(Novedades novedad, int dlg, int LND) {

        novedadSeleccionada = novedad;
        tipoActualizacion = LND;
        if (dlg == 0) {
            activarBotonLOV();
            RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
            RequestContext.getCurrentInstance().update("formLovEmpleados:LOVEmpleados");
            contarRegistrosEmpleados();
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
        } else if (dlg == 2) {
            activarBotonLOV();
            RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
            contarRegistrosLovFormulas();
            RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
        } else if (dlg == 3) {
            cargarLOVPeriodicidades();
            activarBotonLOV();
            RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
            contarRegistrosPeriodicidades();
            RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
        } else if (dlg == 4) {
            activarBotonLOV();
            RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
            contarRegistrosTerceros();
            RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
        } else {
            anularBotonLOV();
        }
    }

    public void asignarIndex(int dlg, int LND) {
        anularBotonLOV();
        tipoActualizacion = LND;
        if (dlg == 0) {
            activarBotonLOV();
            RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
            RequestContext.getCurrentInstance().update("formLovEmpleados:LOVEmpleados");
            contarRegistrosEmpleados();
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
        } else if (dlg == 1) {
            activarBotonLOV();
            RequestContext.getCurrentInstance().update("formLovConceptos:conceptosDialogo");
            contarRegistrosLovConceptos();
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
        } else if (dlg == 2) {
            activarBotonLOV();
            RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
            contarRegistrosLovFormulas();
            RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
        } else if (dlg == 3) {
            cargarLOVPeriodicidades();
            activarBotonLOV();
            RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
            contarRegistrosPeriodicidades();
            RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
        } else if (dlg == 4) {
            activarBotonLOV();
            RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
            contarRegistrosTerceros();
            RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
        }
    }

    public void mostrarTodos() {
        if (!listaConceptosNovedad.isEmpty()) {
            listaConceptosNovedad.clear();
        }
        if (lovConceptos != null) {
            listaConceptosNovedad.addAll(lovConceptos);
            conceptoSeleccionado = listaConceptosNovedad.get(0);
        }
        llenarTablaNovedades();
        contarRegistrosConceptos();
        contarRegistrosNovedades();
        anularBotonLOV();
        RequestContext.getCurrentInstance().update("form:datosConceptos");
        filtradosListaConceptos = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
    }

    public void actualizarConceptosNovedad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaConceptosNovedad.isEmpty()) {
            listaConceptosNovedad.clear();
            listaConceptosNovedad.add(conceptoLovSeleccionado);
            conceptoSeleccionado = listaConceptosNovedad.get(0);
        } else {
            listaConceptosNovedad.add(conceptoLovSeleccionado);
            conceptoSeleccionado = listaConceptosNovedad.get(0);
        }
        empleadoLovSeleccionado = null;
        llenarTablaNovedades();
        filtradosListaConceptos = null;
        aceptar = true;
        novedadSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLovConceptos:LOVConceptos:globalFilter");
        context.execute("PF('LOVConceptos').clearFilters()");
        context.update("formLovConceptos:LOVConceptos");
        context.update("formLovConceptos:conceptosDialogo");
        context.update("formLovConceptos:aceptarC");
        context.update("form:datosConceptos");
        context.execute("PF('conceptosDialogo').hide()");
        contarRegistrosConceptos();
    }

    //AUTOCOMPLETAR
    public void modificarNovedades(Novedades novedad, String confirmarCambio, String valorConfirmar) {

        novedadSeleccionada = novedad;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;

        if (novedadSeleccionada.getFechafinal() != null && novedadSeleccionada.getFechainicial() != null) {
            if (novedadSeleccionada.getFechafinal().compareTo(novedadSeleccionada.getFechainicial()) < 0) {
                log.info("La fecha Final es Menor que la Inicial");
                RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
                RequestContext.getCurrentInstance().execute("PF('fechas').show()");
                novedadSeleccionada.setFechainicial(novedadBackup.getFechainicial());
                novedadSeleccionada.setFechafinal(novedadBackup.getFechafinal());
                RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
            }
        }

        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {

                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
                if (guardado) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }

            RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
        } else if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
            novedadSeleccionada.getFormula().setNombresFormula(formula);
            for (int i = 0; i < lovFormulas.size(); i++) {
                if (lovFormulas.get(i).getNombresFormula().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                novedadSeleccionada.setFormula(lovFormulas.get(indiceUnicoElemento));
            } else {
                permitirIndex = false;
                contarRegistrosLovFormulas();
                RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
                RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("NIT")) {
            novedadSeleccionada.getTercero().setNitalternativo(nitTercero);

            for (int i = 0; i < lovTerceros.size(); i++) {
                if (lovTerceros.get(i).getNitalternativo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                novedadSeleccionada.setTercero(lovTerceros.get(indiceUnicoElemento));
            } else {
                permitirIndex = false;
                contarRegistrosTerceros();
                RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
                RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("EMPLEADO")) {

            novedadSeleccionada.getEmpleado().setCodigoempleadoSTR(codigoEmpleado);

            for (int i = 0; i < lovEmpleados.size(); i++) {
                if (lovEmpleados.get(i).getCodigoempleadoSTR().startsWith(valorConfirmar.toString().toUpperCase())) {

                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                novedadSeleccionada.setEmpleado(lovEmpleados.get(indiceUnicoElemento));
            } else {
                permitirIndex = false;
                contarRegistrosEmpleados();
                RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
                RequestContext.getCurrentInstance().update("formLovEmpleados:LOVEmpleados");
                RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("CODIGOPERIODICIDAD")) {
            cargarLOVPeriodicidades();
            novedadSeleccionada.getPeriodicidad().setCodigoStr(CodigoPeriodicidad);

            for (int i = 0; i < lovPeriodicidades.size(); i++) {
                if ((lovPeriodicidades.get(i).getCodigoStr()).startsWith(valorConfirmar.toString().toUpperCase())) {

                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                novedadSeleccionada.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
                contarRegistrosPeriodicidades();
                RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
                if (guardado) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        }
        RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
    }

    public void actualizarEmpleados() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            novedadSeleccionada.setEmpleado(empleadoLovSeleccionado);
            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
            }
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
        } else if (tipoActualizacion == 1) {
            nuevaNovedad.setEmpleado(empleadoLovSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarNovedad.setEmpleado(empleadoLovSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
        }
        filtrarLovEmpleados = null;
        empleadoLovSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLovEmpleados:LOVEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().update("formLovEmpleados:LOVEmpleados");
        RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
        RequestContext.getCurrentInstance().update("formLovEmpleados:LOVEmpleados");
        RequestContext.getCurrentInstance().update("formLovEmpleados:aceptarE");
        RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
    }

    public void actualizarFormulas() {
        // verificarFormulaConcepto(seleccionConceptos.getSecuencia());
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            novedadSeleccionada.setFormula(formulaLovSeleccionada);
            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
            }
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
        } else if (tipoActualizacion == 1) {
            nuevaNovedad.setFormula(formulaLovSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarNovedad.setFormula(formulaLovSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
        }
        filtrarLovFormulas = null;
        formulaLovSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLovFormulas:LOVFormulas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVFormulas').clearFilters()");
        RequestContext.getCurrentInstance().update("formLovFormulas:LOVFormulas");
        RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
        RequestContext.getCurrentInstance().update("formLovFormulas:aceptarF");
        RequestContext.getCurrentInstance().execute("PF('formulasDialogo').hide()");
    }

    public void actualizarPeriodicidades() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            novedadSeleccionada.setPeriodicidad(periodicidadlovSeleccionada);
            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
            }
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
        } else if (tipoActualizacion == 1) {
            nuevaNovedad.setPeriodicidad(periodicidadlovSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarNovedad.setPeriodicidad(periodicidadlovSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
        }
        filtrarLovPeriodicidades = null;
        periodicidadlovSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLovPeriodicidad:LOVPeriodicidades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVPeriodicidades').clearFilters()");
        RequestContext.getCurrentInstance().update("formLovPeriodicidad:LOVPeriodicidades");
        RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
        RequestContext.getCurrentInstance().update("formLovPeriodicidad:aceptarP");
        RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').hide()");
    }

    public void actualizarTerceros() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            novedadSeleccionada.setTercero(terceroLovSeleccionado);
            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
            }
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
        } else if (tipoActualizacion == 1) {
            nuevaNovedad.setTercero(terceroLovSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarNovedad.setTercero(terceroLovSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
        }
        filtrarLovTerceros = null;
        terceroLovSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLovTerceros:LOVTerceros:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTerceros').clearFilters()");
        RequestContext.getCurrentInstance().update("formLovTerceros:LOVTerceros");
        RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
        RequestContext.getCurrentInstance().update("formLovTerceros:aceptarT");
        RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').hide()");
    }

    //Ubicacion Celda Indice Abajo. //Van los que no son NOT NULL.
    public void cambiarIndice(Novedades novedad, int celda) {
        novedadSeleccionada = novedad;
//        if (permitirIndex == true) {
        cualCelda = celda;
        novedadBackup = novedadSeleccionada;
        if (cualCelda == 0) {
            activarBotonLOV();
            codigoEmpleado = novedadSeleccionada.getEmpleado().getCodigoempleadoSTR();
        } else if (cualCelda == 1) {
            activarBotonLOV();
            descripcionConcepto = novedadSeleccionada.getConcepto().getDescripcion();
        } else if (cualCelda == 3) {
            anularBotonLOV();
            FechaFinal = novedadSeleccionada.getFechafinal();
        } else if (cualCelda == 5) {
            anularBotonLOV();
            Saldo = novedadSeleccionada.getSaldo();
        } else if (cualCelda == 6) {
            activarBotonLOV();
            CodigoPeriodicidad = novedadSeleccionada.getPeriodicidad().getCodigoStr();
        } else if (cualCelda == 7) {
            activarBotonLOV();
            descripcionPeriodicidad = novedadSeleccionada.getPeriodicidad().getNombre();
        } else if (cualCelda == 8) {
            activarBotonLOV();
            nitTercero = novedadSeleccionada.getTercero().getNitalternativo();
        } else if (cualCelda == 9) {
            activarBotonLOV();
            nombreTercero = novedadSeleccionada.getTercero().getNombre();
        } else if (cualCelda == 10) {
            activarBotonLOV();
            formula = novedadSeleccionada.getFormula().getNombrelargo();
        } else if (cualCelda == 11) {
            anularBotonLOV();
            HoraDia = novedadSeleccionada.getUnidadesparteentera();
            MinutoHora = novedadSeleccionada.getUnidadespartefraccion();
        } else {
            anularBotonLOV();
        }
//        }
    }

    public void guardarYSalir() {
        guardarCambiosNovedades();
        salir();
    }

    //GUARDAR
    public void guardarCambiosNovedades() {
        int pasas = 0;
        if (guardado == false) {
            msgError = "";
            getResultado();
            if (resultado > 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:solucionesFormulas");
                RequestContext.getCurrentInstance().execute("PF('solucionesFormulas').show()");
                listaNovedadesBorrar.clear();
            }
            if (!listaNovedadesBorrar.isEmpty() && pasas == 0) {
                for (int i = 0; i < listaNovedadesBorrar.size(); i++) {
                    if (listaNovedadesBorrar.get(i).getPeriodicidad().getSecuencia() == null) {
                        listaNovedadesBorrar.get(i).setPeriodicidad(null);
                    }
                    if (listaNovedadesBorrar.get(i).getTercero().getSecuencia() == null) {
                        listaNovedadesBorrar.get(i).setTercero(null);
                    }
                    if (listaNovedadesBorrar.get(i).getSaldo() == null) {
                        listaNovedadesBorrar.get(i).setSaldo(null);
                    }
                    if (listaNovedadesBorrar.get(i).getUnidadesparteentera() == null) {
                        listaNovedadesBorrar.get(i).setUnidadesparteentera(null);
                    }
                    if (listaNovedadesBorrar.get(i).getUnidadespartefraccion() == null) {
                        listaNovedadesBorrar.get(i).setUnidadespartefraccion(null);
                    }
                    msgError = administrarNovedadesConceptos.borrarNovedades(listaNovedadesBorrar.get(i));
                }
                listaNovedadesBorrar.clear();
            }
            if (!listaNovedadesCrear.isEmpty()) {
                for (int i = 0; i < listaNovedadesCrear.size(); i++) {
                    if (listaNovedadesCrear.get(i).getPeriodicidad().getSecuencia() == null) {
                        listaNovedadesCrear.get(i).setPeriodicidad(null);
                    }
                    if (listaNovedadesCrear.get(i).getTercero().getSecuencia() == null) {
                        listaNovedadesCrear.get(i).setTercero(null);
                    }
                    if (listaNovedadesCrear.get(i).getPeriodicidad().getSecuencia() == null) {
                        listaNovedadesCrear.get(i).setPeriodicidad(null);
                    }
                    if (listaNovedadesCrear.get(i).getSaldo() == null) {
                        listaNovedadesCrear.get(i).setSaldo(null);
                    }
                    if (listaNovedadesCrear.get(i).getUnidadesparteentera() == null) {
                        listaNovedadesCrear.get(i).setUnidadesparteentera(null);
                    }
                    if (listaNovedadesCrear.get(i).getUnidadespartefraccion() == null) {
                        listaNovedadesCrear.get(i).setUnidadespartefraccion(null);
                    }
                    msgError = administrarNovedadesConceptos.crearNovedades(listaNovedadesCrear.get(i));
                }
                listaNovedadesCrear.clear();
            }
            if (!listaNovedadesModificar.isEmpty()) {
                for (int i = 0; i < listaNovedadesModificar.size(); i++) {
                    if (listaNovedadesModificar.get(i).getTercero().getSecuencia() == null) {
                        listaNovedadesModificar.get(i).setTercero(null);
                    }
                    if (listaNovedadesModificar.get(i).getPeriodicidad().getSecuencia() == null) {
                        listaNovedadesModificar.get(i).setPeriodicidad(null);
                    }
                    if (listaNovedadesModificar.get(i).getSaldo() == null) {
                        listaNovedadesModificar.get(i).setSaldo(null);
                    }
                    if (listaNovedadesModificar.get(i).getUnidadesparteentera() == null) {
                        listaNovedadesModificar.get(i).setUnidadesparteentera(null);
                    }
                    if (listaNovedadesModificar.get(i).getUnidadespartefraccion() == null) {
                        listaNovedadesModificar.get(i).setUnidadespartefraccion(null);
                    }

                    msgError = administrarNovedadesConceptos.modificarNovedades(listaNovedadesModificar.get(i));
                }
                listaNovedadesModificar.clear();
            }

            if (msgError.equals("EXITO")) {
                listaNovedades = null;
                getListaNovedades();
                novedadSeleccionada = null;
                RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
                guardado = true;
                permitirIndex = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:errorGuardadoBD");
                RequestContext.getCurrentInstance().execute("PF('errorGuardadoBD').show()");
            }
        }
    }

    //CREAR NOVEDADES
    public void agregarNuevaNovedadConcepto() {
        int pasa = 0;
        int pasa2 = 0;
        mensajeValidacion = new String();
        if (nuevaNovedad.getFechainicial() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
            pasa++;
        }
        if (nuevaNovedad.getEmpleado().getCodigoempleado().equals(BigInteger.valueOf(0))) {
            mensajeValidacion = mensajeValidacion + " * Empleado\n";
            pasa++;
        }
        if (nuevaNovedad.getFormula().getNombrelargo().equals("")) {
            mensajeValidacion = mensajeValidacion + " * Formula\n";
            pasa++;
        }
        if (nuevaNovedad.getTipo() == null) {
            mensajeValidacion = mensajeValidacion + " * Tipo\n";
            pasa++;
        }
        if (nuevaNovedad.getPeriodicidad() == null || nuevaNovedad.getPeriodicidad().getCodigo() == 0) {
            mensajeValidacion = mensajeValidacion + " * Periodicidad\n";
            pasa++;
        }
        if (nuevaNovedad.getEmpleado() != null && pasa == 0) {
            fechaContratacionE = administrarNovedadesConceptos.obtenerFechaContratacionEmpleado(nuevaNovedad.getEmpleado().getSecuencia());
            if (fechaContratacionE == null) {
                fechaContratacionE = new Date();
            }
            if (nuevaNovedad.getFechainicial().before(fechaContratacionE)) {
                RequestContext.getCurrentInstance().update("formularioDialogos:inconsistencia");
                RequestContext.getCurrentInstance().execute("PF('inconsistencia').show()");
                log.info("Inconsistencia Empleado");
                pasa2++;
            }
        }
        if (nuevaNovedad.getFechafinal() != null && nuevaNovedad.getFechainicial() != null) {
            if (nuevaNovedad.getFechainicial().compareTo(nuevaNovedad.getFechafinal()) > 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
                RequestContext.getCurrentInstance().execute("PF('fechas').show()");
                log.info("Dialogo de Fechas culas");
                pasa2++;
            }
        }
        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedadConcepto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedadConcepto').show()");
        }
        if (pasa == 0 && pasa2 == 0) {
            log.info("Todo esta Bien");
            if (bandera == 1) {
                restaurarTabla();
            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevaNovedad.setSecuencia(l);
            // OBTENER EL TERMINAL 
            HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
            String equipo = null;
            java.net.InetAddress localMachine = null;
            if (request.getRemoteAddr().startsWith("127.0.0.1")) {
                try {
                    localMachine = java.net.InetAddress.getLocalHost();

                } catch (UnknownHostException ex) {
                    java.util.logging.Logger.getLogger(ControlNovedadesConceptos.class.getName()).log(Level.SEVERE, null, ex);
                }
                equipo = localMachine.getHostAddress();
            } else {
                equipo = request.getRemoteAddr();
            }
            try {
                localMachine = java.net.InetAddress.getByName(equipo);

            } catch (UnknownHostException ex) {
                java.util.logging.Logger.getLogger(ControlNovedadesConceptos.class.getName()).log(Level.SEVERE, null, ex);
            }

            getAlias();
            getUsuarioBD();
            nuevaNovedad.setConcepto(conceptoSeleccionado);
            nuevaNovedad.setTerminal(localMachine.getHostName());
            nuevaNovedad.setUsuarioreporta(usuarioBD);
            if (nuevaNovedad.getValortotal() == null) {
                nuevaNovedad.setValortotal(new BigDecimal(0));
            }
            listaNovedadesCrear.add(nuevaNovedad);
            listaNovedades.add(0, nuevaNovedad);
            novedadSeleccionada = listaNovedades.get(listaNovedades.indexOf(nuevaNovedad));
            contarRegistrosNovedades();

            nuevaNovedad = new Novedades();
            nuevaNovedad.setEmpleado(new Empleados());
            nuevaNovedad.setFormula(new Formulas());
            nuevaNovedad.setPeriodicidad(new Periodicidades());
            nuevaNovedad.getPeriodicidad().setNombre(" ");
            nuevaNovedad.setTercero(new Terceros());
            nuevaNovedad.getTercero().setNombre(" ");
            nuevaNovedad.setConcepto(new Conceptos());
            nuevaNovedad.getConcepto().setDescripcion(" ");
            nuevaNovedad.setTipo("FIJA");
            nuevaNovedad.setUsuarioreporta(new Usuarios());
            nuevaNovedad.setTerminal(" ");
            nuevaNovedad.setFechareporte(new Date());

            RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('NuevaNovedadConcepto').hide()");
        } else {
        }
    }

    public void confirmarDuplicar() throws UnknownHostException {
        int pasa2 = 0;
        int pasa = 0;
        if (duplicarNovedad.getFechainicial() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
            pasa++;
        }
        if (duplicarNovedad.getEmpleado().getNombreCompleto().equals(" ")) {
            mensajeValidacion = mensajeValidacion + " * Empleado\n";
            pasa++;
        }
        if (duplicarNovedad.getFormula().getNombrelargo() == null) {
            mensajeValidacion = mensajeValidacion + " * Formula\n";
            pasa++;
        }
        if (duplicarNovedad.getTipo() == null) {
            mensajeValidacion = mensajeValidacion + " * Tipo\n";
            pasa++;
        }
        if (duplicarNovedad.getPeriodicidad() == null || duplicarNovedad.getPeriodicidad().getCodigo() == 0) {
            mensajeValidacion = mensajeValidacion + " * Periodicidad\n";
            pasa++;
        }
        log.info("duplicarNovedad.getFechainicial() : " + duplicarNovedad.getFechainicial());
        fechaContratacionE = administrarNovedadesConceptos.obtenerFechaContratacionEmpleado(duplicarNovedad.getEmpleado().getSecuencia());
        log.info("duplicarNovedad fechaContratacionE : " + fechaContratacionE);
        if (fechaContratacionE == null) {
            fechaContratacionE = new Date();
        }
        if (duplicarNovedad.getFechainicial().before(fechaContratacionE)) {
            RequestContext.getCurrentInstance().update("formularioDialogos:inconsistencia");
            RequestContext.getCurrentInstance().execute("PF('inconsistencia').show()");
            pasa2++;
        }
        if (duplicarNovedad.getFechainicial().compareTo(duplicarNovedad.getFechafinal()) > 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
            RequestContext.getCurrentInstance().execute("PF('fechas').show()");
            pasa2++;
        }
        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedadConcepto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedadConcepto').show()");
        }
        if (pasa2 == 0) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarNovedad.setSecuencia(l);

            listaNovedades.add(0, duplicarNovedad);
            listaNovedadesCrear.add(duplicarNovedad);
            novedadSeleccionada = listaNovedades.get(listaNovedades.indexOf(duplicarNovedad));
            contarRegistrosNovedades();

            RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                restaurarTabla();
            }
            // OBTENER EL TERMINAL 
            HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
            String equipo = null;
            java.net.InetAddress localMachine = null;
            if (request.getRemoteAddr().startsWith("127.0.0.1")) {
                localMachine = java.net.InetAddress.getLocalHost();
                equipo = localMachine.getHostAddress();
            } else {
                equipo = request.getRemoteAddr();
            }
            localMachine = java.net.InetAddress.getByName(equipo);
            getAlias();
            getUsuarioBD();
            duplicarNovedad.setTerminal(localMachine.getHostName());
            duplicarNovedad.setConcepto(conceptoSeleccionado);
            duplicarNovedad = new Novedades();
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroNovedad");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroNovedad').hide()");
        }
    }

//MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (novedadSeleccionada != null) {
            editarNovedades = novedadSeleccionada;

            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleadosCodigos");
                RequestContext.getCurrentInstance().execute("PF('editarEmpleadosCodigos').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleadosNombres");
                RequestContext.getCurrentInstance().execute("PF('editarEmpleadosNombres').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editFechaInicial");
                RequestContext.getCurrentInstance().execute("PF('editFechaInicial').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editFechasFinales");
                RequestContext.getCurrentInstance().execute("PF('editFechasIniciales').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValores");
                RequestContext.getCurrentInstance().execute("PF('editarValores').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarSaldos");
                RequestContext.getCurrentInstance().execute("PF('editarSaldos').show()");
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarPeriodicidadesCodigos");
                RequestContext.getCurrentInstance().execute("PF('editarPeriodicidadesCodigos').show()");
            } else if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarPeriodicidadesDescripciones");
                RequestContext.getCurrentInstance().execute("PF('editarPeriodicidadesDescripciones').show()");
                cualCelda = -1;
            } else if (cualCelda == 8) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTercerosNit");
                RequestContext.getCurrentInstance().execute("PF('editarTercerosNit').show()");
                cualCelda = -1;
            } else if (cualCelda == 9) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTercerosNombres");
                RequestContext.getCurrentInstance().execute("PF('editarTercerosNombres').show()");
                cualCelda = -1;
            } else if (cualCelda == 10) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulas");
                RequestContext.getCurrentInstance().execute("PF('editarFormulas').show()");
                cualCelda = -1;
            } else if (cualCelda == 11) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarHorasDias");
                RequestContext.getCurrentInstance().execute("PF('editarHorasDias').show()");
                cualCelda = -1;
            } else if (cualCelda == 12) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarMinutosHoras");
                RequestContext.getCurrentInstance().execute("PF('editarMinutosHoras').show()");
                cualCelda = -1;
            } else if (cualCelda == 13) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipos");
                RequestContext.getCurrentInstance().execute("PF('editarTipos').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (novedadSeleccionada != null) {
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
                RequestContext.getCurrentInstance().update("formLovEmpleados:LOVEmpleados");
                contarRegistrosEmpleados();
                RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 6) {
                cargarLOVPeriodicidades();
                RequestContext.getCurrentInstance().update("formLovPeriodicidad:periodicidadesDialogo");
                contarRegistrosPeriodicidades();
                RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 8) {
                RequestContext.getCurrentInstance().update("formLovTerceros:tercerosDialogo");
                contarRegistrosTerceros();
                RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
                tipoActualizacion = 0;
            } else if (cualCelda == 10) {
                RequestContext.getCurrentInstance().update("formLovFormulas:formulasDialogo");
                contarRegistrosLovFormulas();
                RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            nCEmpleadoCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCEmpleadoCodigo");
            nCEmpleadoCodigo.setFilterStyle("width: 85% !important;");
            nCEmpleadoNombre = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCEmpleadoNombre");
            nCEmpleadoNombre.setFilterStyle("width: 85% !important");
            nCFechasInicial = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCFechasInicial");
            nCFechasInicial.setFilterStyle("width: 85% !important;");
            nCFechasFinal = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCFechasFinal");
            nCFechasFinal.setFilterStyle("width: 85% !important;");
            nCValor = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCValor");
            nCValor.setFilterStyle("width: 85% !important;");
            nCSaldo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCSaldo");
            nCSaldo.setFilterStyle("width: 85% !important;");
            nCPeriodicidadCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCPeriodicidadCodigo");
            nCPeriodicidadCodigo.setFilterStyle("width: 85% !important;");
            nCDescripcionPeriodicidad = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCDescripcionPeriodicidad");
            nCDescripcionPeriodicidad.setFilterStyle("width: 85% !important;");
            nCTercerosNit = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCTercerosNit");
            nCTercerosNit.setFilterStyle("width: 85% !important;");
            nCTercerosNombre = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCTercerosNombre");
            nCTercerosNombre.setFilterStyle("width: 85% !important;");
            nCFormulas = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCFormulas");
            nCFormulas.setFilterStyle("width: 85% !important;");
            nCHorasDias = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCHorasDias");
            nCHorasDias.setFilterStyle("width: 85% !important;");
            nCMinutosHoras = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCMinutosHoras");
            nCMinutosHoras.setFilterStyle("width: 85% !important;");
            nCTipo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCTipo");
            nCTipo.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
            altoTablaReg = "4";
            nCConceptosCodigos = (Column) c.getViewRoot().findComponent("form:datosConceptos:nCConceptosCodigos");
            nCConceptosCodigos.setFilterStyle("width: 80% !important;");
            nCConceptosDescripcion = (Column) c.getViewRoot().findComponent("form:datosConceptos:nCConceptosDescripcion");
            nCConceptosDescripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosConceptos");
            bandera = 1;
        } else if (bandera == 1) {
            restaurarTabla();
        }
        contarRegistrosConceptos();
        contarRegistrosNovedades();
    }

    public void restaurarTabla() {
        FacesContext c = FacesContext.getCurrentInstance();
        nCEmpleadoCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCEmpleadoCodigo");
        nCEmpleadoCodigo.setFilterStyle("display: none; visibility: hidden;");
        nCEmpleadoNombre = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCEmpleadoNombre");
        nCEmpleadoNombre.setFilterStyle("display: none; visibility: hidden;");
        nCFechasInicial = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCFechasInicial");
        nCFechasInicial.setFilterStyle("display: none; visibility: hidden;");
        nCFechasFinal = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCFechasFinal");
        nCFechasFinal.setFilterStyle("display: none; visibility: hidden;");
        nCValor = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCValor");
        nCValor.setFilterStyle("display: none; visibility: hidden;");
        nCSaldo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCSaldo");
        nCSaldo.setFilterStyle("display: none; visibility: hidden;");
        nCPeriodicidadCodigo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCPeriodicidadCodigo");
        nCPeriodicidadCodigo.setFilterStyle("display: none; visibility: hidden;");
        nCDescripcionPeriodicidad = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCDescripcionPeriodicidad");
        nCDescripcionPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
        nCTercerosNit = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCTercerosNit");
        nCTercerosNit.setFilterStyle("display: none; visibility: hidden;");
        nCTercerosNombre = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCTercerosNombre");
        nCTercerosNombre.setFilterStyle("display: none; visibility: hidden;");
        nCFormulas = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCFormulas");
        nCFormulas.setFilterStyle("display: none; visibility: hidden;");
        nCHorasDias = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCHorasDias");
        nCHorasDias.setFilterStyle("display: none; visibility: hidden;");
        nCMinutosHoras = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCMinutosHoras");
        nCMinutosHoras.setFilterStyle("display: none; visibility: hidden;");
        nCTipo = (Column) c.getViewRoot().findComponent("form:datosNovedadesConcepto:nCTipo");
        nCTipo.setFilterStyle("display: none; visibility: hidden;");
        altoTablaReg = "5";

        bandera = 0;
        nCConceptosCodigos = (Column) c.getViewRoot().findComponent("form:datosConceptos:nCConceptosCodigos");
        nCConceptosCodigos.setFilterStyle("display: none; visibility: hidden;");
        nCConceptosDescripcion = (Column) c.getViewRoot().findComponent("form:datosConceptos:nCConceptosDescripcion");
        nCConceptosDescripcion.setFilterStyle("display: none; visibility: hidden;");
        filtradosListaNovedades = null;
        filtradosListaConceptos = null;
        RequestContext.getCurrentInstance().execute("PF('datosConceptos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('datosNovedadesConcepto').clearFilters()");
        RequestContext.getCurrentInstance().update("form:datosConceptos");
        RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
        tipoLista = 0;
        tipoListaNov = 0;
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesConceptosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "NovedadesConceptosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesConceptosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "NovedadesConceptosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    //LIMPIAR NUEVO REGISTRO NOVEDAD
    public void limpiarNuevaNovedad() {
        nuevaNovedad = new Novedades();
        nuevaNovedad.setEmpleado(new Empleados());
        nuevaNovedad.getEmpleado().setCodigoempleadoSTR("0");
        nuevaNovedad.setFormula(new Formulas());
        nuevaNovedad.setPeriodicidad(new Periodicidades());
        nuevaNovedad.getPeriodicidad().setNombre(" ");
        nuevaNovedad.setTercero(new Terceros());
        nuevaNovedad.getTercero().setNombre(" ");
        nuevaNovedad.setConcepto(new Conceptos());
        nuevaNovedad.getConcepto().setDescripcion(" ");
        nuevaNovedad.setTipo("FIJA");
        nuevaNovedad.setUsuarioreporta(new Usuarios());
        nuevaNovedad.setTerminal(" ");
        nuevaNovedad.setFechareporte(new Date());
        nuevaNovedad.setValortotal(new BigDecimal(0));
        resultado = 0;
    }

    //LIMPIAR DUPLICAR
    /**
     * Metodo que limpia los datos de un duplicar Encargaturas
     */
    public void limpiarduplicarNovedades() {
        duplicarNovedad = new Novedades();
        duplicarNovedad.setEmpleado(new Empleados());
        duplicarNovedad.getEmpleado().setCodigoempleadoSTR("0");
        duplicarNovedad.setFormula(new Formulas());
        duplicarNovedad.setPeriodicidad(new Periodicidades());
        duplicarNovedad.getPeriodicidad().setNombre(" ");
        duplicarNovedad.setTercero(new Terceros());
        duplicarNovedad.getTercero().setNombre(" ");
        duplicarNovedad.setConcepto(new Conceptos());
        duplicarNovedad.getConcepto().setDescripcion(" ");
        duplicarNovedad.setTipo("FIJA");
        duplicarNovedad.setUsuarioreporta(new Usuarios());
        duplicarNovedad.setTerminal(" ");
        duplicarNovedad.setFechareporte(new Date());
        duplicarNovedad.setValortotal(new BigDecimal(0));
        resultado = 0;
    }

    //BORRAR NOVEDADES
    public void borrarNovedades() {
        if (novedadSeleccionada != null) {
            if (!listaNovedadesModificar.isEmpty() && listaNovedadesModificar.contains(novedadSeleccionada)) {
                listaNovedadesModificar.remove(novedadSeleccionada);
                listaNovedadesBorrar.add(novedadSeleccionada);
            } else if (!listaNovedadesCrear.isEmpty() && listaNovedadesCrear.contains(novedadSeleccionada)) {
                listaNovedadesCrear.remove(novedadSeleccionada);
            } else {
                listaNovedadesBorrar.add(novedadSeleccionada);
            }
            listaNovedades.remove(novedadSeleccionada);
            if (tipoListaNov == 1) {
                filtradosListaNovedades.remove(novedadSeleccionada);
            }
            RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
            novedadSeleccionada = null;
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            contarRegistrosNovedades();
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    //DUPLICAR ENCARGATURA
    public void duplicarCN() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada != null) {
            duplicarNovedad = new Novedades();

            duplicarNovedad.setEmpleado(novedadSeleccionada.getEmpleado());
            duplicarNovedad.setConcepto(novedadSeleccionada.getConcepto());
            duplicarNovedad.setFechainicial(novedadSeleccionada.getFechainicial());
            duplicarNovedad.setFechafinal(novedadSeleccionada.getFechafinal());
            duplicarNovedad.setFechareporte(novedadSeleccionada.getFechareporte());
            duplicarNovedad.setValortotal(novedadSeleccionada.getValortotal());
            duplicarNovedad.setSaldo(novedadSeleccionada.getSaldo());
            duplicarNovedad.setPeriodicidad(novedadSeleccionada.getPeriodicidad());
            duplicarNovedad.setTercero(novedadSeleccionada.getTercero());
            duplicarNovedad.setFormula(novedadSeleccionada.getFormula());
            duplicarNovedad.setUnidadesparteentera(novedadSeleccionada.getUnidadesparteentera());
            duplicarNovedad.setUnidadespartefraccion(novedadSeleccionada.getUnidadespartefraccion());
            duplicarNovedad.setTipo(novedadSeleccionada.getTipo());
            duplicarNovedad.setTerminal(novedadSeleccionada.getTerminal());
            duplicarNovedad.setUsuarioreporta(novedadSeleccionada.getUsuarioreporta());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroNovedad').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("EMPLEADO")) {
            if (tipoNuevo == 1) {
                codigoEmpleado = nuevaNovedad.getEmpleado().getCodigoempleadoSTR();
            } else if (tipoNuevo == 2) {
                codigoEmpleado = duplicarNovedad.getEmpleado().getCodigoempleadoSTR();
            }
        } else if (Campo.equals("CODIGO")) {
            if (tipoNuevo == 1) {
                CodigoPeriodicidad = nuevaNovedad.getPeriodicidad().getCodigoStr();
            } else if (tipoNuevo == 2) {
                CodigoPeriodicidad = duplicarNovedad.getPeriodicidad().getCodigoStr();
            }
        } else if (Campo.equals("NIT")) {
            if (tipoNuevo == 1) {
                nitTercero = nuevaNovedad.getTercero().getNitalternativo();
            } else if (tipoNuevo == 2) {
                nitTercero = duplicarNovedad.getTercero().getNitalternativo();
            }
        } else if (Campo.equals("FORMULAS")) {
            if (tipoNuevo == 1) {
                formula = nuevaNovedad.getFormula().getNombrelargo();
            } else if (tipoNuevo == 2) {
                formula = duplicarNovedad.getFormula().getNombrelargo();
            }
        }

    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
            if (tipoNuevo == 1) {
                nuevaNovedad.getFormula().setNombrelargo(formula);
            } else if (tipoNuevo == 2) {
                duplicarNovedad.getFormula().setNombrelargo(formula);
            }
            for (int i = 0; i < lovFormulas.size(); i++) {
                if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaNovedad.setFormula(lovFormulas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormula");
                } else if (tipoNuevo == 2) {
                    duplicarNovedad.setFormula(lovFormulas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormula");
                }
            } else {
                contarRegistrosLovFormulas();
                RequestContext.getCurrentInstance().update("form:formulasDialogo");
                RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormula");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormula");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("NIT")) {
            if (tipoNuevo == 1) {
                nuevaNovedad.getTercero().setNitalternativo(nitTercero);
            } else if (tipoNuevo == 2) {
                duplicarNovedad.getTercero().setNitalternativo(nitTercero);
            }

            for (int i = 0; i < lovTerceros.size(); i++) {
                if (lovTerceros.get(i).getNitalternativo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaNovedad.setTercero(lovTerceros.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTerceroNit");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTerceroNombre");
                } else if (tipoNuevo == 2) {
                    duplicarNovedad.setTercero(lovTerceros.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroNit");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroNombre");
                }

            } else {
                contarRegistrosTerceros();
                RequestContext.getCurrentInstance().update("form:tercerosDialogo");
                RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTerceroNit");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTerceroNombre");

                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroNit");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroNombre");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("CODIGO")) {
            cargarLOVPeriodicidades();
            if (tipoNuevo == 1) {
                nuevaNovedad.getPeriodicidad().setCodigoStr(CodigoPeriodicidad);
            } else if (tipoNuevo == 2) {
                duplicarNovedad.getPeriodicidad().setCodigoStr(CodigoPeriodicidad);
            }
            for (int i = 0; i < lovPeriodicidades.size(); i++) {
                if (lovPeriodicidades.get(i).getCodigoStr().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaNovedad.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriodicidadCodigo");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriodicidadDescripcion");
                } else if (tipoNuevo == 2) {
                    duplicarNovedad.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidadCodigo");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidadDescripcion");
                }
            } else {
                contarRegistrosPeriodicidades();
                RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriodicidadCodigo");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriodicidadDescripcion");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidadCodigo");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidadDescripcion");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("EMPLEADO")) {
            getLovEmpleados();
            RequestContext.getCurrentInstance().update("form:empleadosDialogo");
            if (tipoNuevo == 1) {
                nuevaNovedad.getEmpleado().setCodigoempleadoSTR(codigoEmpleado);
            } else if (tipoNuevo == 2) {
                duplicarNovedad.getEmpleado().setCodigoempleadoSTR(codigoEmpleado);
            }

            for (int i = 0; i < lovEmpleados.size(); i++) {
                if (lovEmpleados.get(i).getCodigoempleadoSTR().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaNovedad.setEmpleado(lovEmpleados.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmpleadoCodigo");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmpleadoNombre");
                } else if (tipoNuevo == 2) {
                    duplicarNovedad.setEmpleado(lovEmpleados.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpleadoCodigo");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpleadoNombre");
                }
            } else {
                RequestContext.getCurrentInstance().update("form:empleadosDialogo");
                RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
                contarRegistrosEmpleados();
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmpleadoCodigo");
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmpleadoNombre");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpleadoNombre");
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpleadoCodigo");
                }
            }
        }
    }

    //RASTROS 
    public void verificarRastro() {
        if (novedadSeleccionada != null) {
            int result = administrarRastros.obtenerTabla(novedadSeleccionada.getSecuencia(), "NOVEDADES");
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
        } else if (administrarRastros.verificarHistoricosTabla("NOVEDADES")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    //CANCELAR MODIFICACIONES
    public void cancelarModificacion() {
        try {
            restaurarTabla();
            log.warn("CancelarModificacion() 1");
            listaNovedadesBorrar.clear();
            log.warn("CancelarModificacion() 2");
            listaNovedadesCrear.clear();
            log.warn("CancelarModificacion() 3");
            listaNovedadesModificar.clear();
            log.warn("CancelarModificacion() 4");
            mostrarTodos();
            log.warn("CancelarModificacion() 5");
            novedadSeleccionada = null;
            log.warn("CancelarModificacion() 6");
            guardado = true;
            log.warn("CancelarModificacion() 7");
            permitirIndex = true;
            log.warn("CancelarModificacion() 8");
            resultado = 0;
            log.warn("CancelarModificacion() 9");
            contarRegistrosNovedades();
            log.warn("CancelarModificacion() 10");
            RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
            log.warn("CancelarModificacion() 11");
            RequestContext.getCurrentInstance().update("form:datosConceptos");
            log.warn("CancelarModificacion() 12");
        } catch (Exception e) {
            log.warn(this.getClass().getSimpleName() + ".cancelarModificacion().ERROR:  ", e);
        }
    }

    public void salir() {
//      listaNovedadesBorrar.clear();
//      listaNovedadesCrear.clear();
//      listaNovedadesModificar.clear();
//      listaNovedades.clear();
//      listaConceptosNovedad = null;
//      lovConceptos = null;
//      lovEmpleados = null;
//      lovFormulas = null;
//      lovPeriodicidades = null;
//      lovTerceros = null;
//      tipoLista = 0;
//      tipoListaNov = 0;
//      novedadSeleccionada = null;
//      resultado = 0;
//      guardado = true;
//      permitirIndex = true;
        navegar("atras");
    }

    public void todasNovedades() {
        listaNovedades.clear();
        listaNovedades = administrarNovedadesConceptos.todasNovedadesConcepto(conceptoSeleccionado.getSecuencia());
        todas = true;
        actuales = false;
        RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
        RequestContext.getCurrentInstance().update("form:TODAS");
        RequestContext.getCurrentInstance().update("form:ACTUALES");
        contarRegistrosNovedades();
    }

    public void actualesNovedades() {
        listaNovedades.clear();
        listaNovedades = administrarNovedadesConceptos.novedadesConcepto(conceptoSeleccionado.getSecuencia());
        todas = false;
        actuales = true;
        RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
        RequestContext.getCurrentInstance().update("form:TODAS");
        RequestContext.getCurrentInstance().update("form:ACTUALES");
        contarRegistrosNovedades();
    }

    public void abrirDialogo() {
        nuevaNovedad.setFormula(verificarFormulaConcepto(conceptoSeleccionado.getSecuencia()));
        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
        RequestContext.getCurrentInstance().execute("PF('NuevaNovedadConcepto').show()");

    }

    //CARGAR FORMULA AUTOMATICAMENTE
    public Formulas verificarFormulaConcepto(BigInteger secCon) {
        FormulasConceptos formulasConceptoSel = administrarFormulaConcepto.cargarFormulasConcepto(secCon);
        BigInteger autoFormula;
        if (formulasConceptoSel != null) {
//         if (!formulasConceptoSel.isEmpty()) {
            autoFormula = formulasConceptoSel.getFormula();
        } else {
            autoFormula = new BigInteger("4621544");
        }
//      Formulas formulaR = new Formulas();
        log.warn("autoFormula:_" + autoFormula + "_");
        if (getLovFormulas() != null) {
            for (int i = 0; i < lovFormulas.size(); i++) {
                if (autoFormula.equals(lovFormulas.get(i).getSecuencia())) {
                    return lovFormulas.get(i);
                }
            }
        }
        return new Formulas();
    }

//EVENTO FILTRAR
    public void eventoFiltrar() {
        if (tipoListaNov == 0) {
            tipoListaNov = 1;
        }
        anularBotonLOV();
        novedadSeleccionada = null;
        contarRegistrosNovedades();
    }

    public void eventoFiltrarConcepto() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        novedadSeleccionada = null;
        conceptoSeleccionado = null;
        listaNovedades.clear();
        filtradosListaNovedades = null;
        if (tipoListaNov == 1) {
            RequestContext.getCurrentInstance().execute("PF('datosNovedadesConcepto').clearFilters()");
        }
        RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
        anularBotonLOV();
        contarRegistrosConceptos();
        contarRegistrosNovedades();
    }

    // Conteo de registros : 
    public void contarRegistrosConceptos() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    public void contarRegistrosNovedades() {
        RequestContext.getCurrentInstance().update("form:infoRegistroNovedades");
    }

    public void contarRegistrosLovConceptos() {
        RequestContext.getCurrentInstance().update("formLovConceptos:infoRegistroConceptos");
    }

    public void contarRegistrosLovFormulas() {
        RequestContext.getCurrentInstance().update("formLovFormulas:infoRegistroFormulas");
    }

    public void contarRegistrosEmpleados() {
        RequestContext.getCurrentInstance().update("formLovEmpleados:infoRegistroEmpleados");
    }

    public void contarRegistrosPeriodicidades() {
        RequestContext.getCurrentInstance().update("formLovPeriodicidad:infoRegistroPeriodicidades");
    }

    public void contarRegistrosTerceros() {
        RequestContext.getCurrentInstance().update("formLovTerceros:infoRegistroTerceros");
    }

    public void anularBotonLOV() {
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void activarBotonLOV() {
        activarLOV = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void llenarTablaNovedades() {
        listaNovedades.clear();
        if (conceptoSeleccionado != null) {
            listaNovedades = administrarNovedadesConceptos.novedadesConcepto(conceptoSeleccionado.getSecuencia());
        }
        novedadSeleccionada = null;
        anularBotonLOV();
        RequestContext.getCurrentInstance().update("form:datosNovedadesConcepto");
        contarRegistrosNovedades();
    }

    //GETTER & SETTER
    public List<Conceptos> getListaConceptosNovedad() {
        if (listaConceptosNovedad == null || listaConceptosNovedad.isEmpty()) {
            listaConceptosNovedad = administrarNovedadesConceptos.Conceptos();
        }
        return listaConceptosNovedad;
    }

    public void setListaConceptosNovedad(List<Conceptos> listaConceptosNovedad) {
        this.listaConceptosNovedad = listaConceptosNovedad;
    }

    public List<Conceptos> getFiltradosListaConceptos() {
        return filtradosListaConceptos;
    }

    public void setFiltradosListaConceptos(List<Conceptos> filtradosListaConceptos) {
        this.filtradosListaConceptos = filtradosListaConceptos;
    }

    public Conceptos getConceptoSeleccionado() {
        return conceptoSeleccionado;
    }

    public void setConceptoSeleccionado(Conceptos seleccionMostrar) {
        this.conceptoSeleccionado = seleccionMostrar;
    }

    public List<Novedades> getListaNovedades() {
        if (listaNovedades == null) {
            if (conceptoSeleccionado != null) {
                listaNovedades = administrarNovedadesConceptos.novedadesConcepto(conceptoSeleccionado.getSecuencia());
            }
        }
        return listaNovedades;
    }

    public void setListaNovedades(List<Novedades> listaNovedades) {
        this.listaNovedades = listaNovedades;
    }

    public List<Novedades> getFiltradosListaNovedades() {
        return filtradosListaNovedades;
    }

    public void setFiltradosListaNovedades(List<Novedades> filtradosListaNovedades) {
        this.filtradosListaNovedades = filtradosListaNovedades;
    }

    public List<Novedades> getListaNovedadesCrear() {
        return listaNovedadesCrear;
    }

    public void setListaNovedadesCrear(List<Novedades> listaNovedadesCrear) {
        this.listaNovedadesCrear = listaNovedadesCrear;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public List<Novedades> getListaNovedadesModificar() {
        return listaNovedadesModificar;
    }

    public void setListaNovedadesModificar(List<Novedades> listaNovedadesModificar) {
        this.listaNovedadesModificar = listaNovedadesModificar;
    }

    public List<Novedades> getListaNovedadesBorrar() {
        return listaNovedadesBorrar;
    }

    public void setListaNovedadesBorrar(List<Novedades> listaNovedadesBorrar) {
        this.listaNovedadesBorrar = listaNovedadesBorrar;
    }

    public List<Empleados> getLovEmpleados() {
        if (lovEmpleados == null) {
            if (listasRecurrentes.getLovEmpleadosNovedad().isEmpty()) {
                lovEmpleados = administrarNovedadesConceptos.lovEmpleados();
                if (lovEmpleados != null) {
                    log.warn("GUARDANDO lovEmpleadosNovedad en Listas recurrentes");
                    listasRecurrentes.setLovEmpleadosNovedad(lovEmpleados);
                }
            } else {
                lovEmpleados = new ArrayList<Empleados>(listasRecurrentes.getLovEmpleadosNovedad());
                log.warn("CONSULTANDO lovEmpleadosNovedad de Listas recurrentes");
            }
        }
        return lovEmpleados;
    }

    public void setLovEmpleados(List<Empleados> listaEmpleados) {
        this.lovEmpleados = listaEmpleados;
    }

    public List<Empleados> getFiltrarLovEmpleados() {
        return filtrarLovEmpleados;
    }

    public void setFiltrarLovEmpleados(List<Empleados> filtrarLovEmpleados) {
        this.filtrarLovEmpleados = filtrarLovEmpleados;
    }

    public Empleados getEmpleadoLovSeleccionado() {
        return empleadoLovSeleccionado;
    }

    public void setEmpleadoLovSeleccionado(Empleados seleccionEmpleados) {
        this.empleadoLovSeleccionado = seleccionEmpleados;
    }

    public void cargarLOVPeriodicidades() {
        if (lovPeriodicidades == null || lovPeriodicidades.isEmpty()) {
            lovPeriodicidades = administrarNovedadesConceptos.lovPeriodicidades();
            if (lovPeriodicidades == null) {
                lovPeriodicidades = new ArrayList<Periodicidades>();
            }
        }
    }

    public List<Periodicidades> getLovPeriodicidades() {
//      if (lovPeriodicidades == null) {
//         lovPeriodicidades = administrarNovedadesConceptos.lovPeriodicidades();
//      }
        return lovPeriodicidades;
    }

    public void setLovPeriodicidades(List<Periodicidades> listaPeriodicidades) {
        this.lovPeriodicidades = listaPeriodicidades;
    }

    public List<Periodicidades> getFiltrarLovPeriodicidades() {
        return filtrarLovPeriodicidades;
    }

    public void setFiltrarLovPeriodicidades(List<Periodicidades> filtrarLovPeriodicidades) {
        this.filtrarLovPeriodicidades = filtrarLovPeriodicidades;
    }

    public Periodicidades getPeriodicidadlovSeleccionada() {
        return periodicidadlovSeleccionada;
    }

    public void setPeriodicidadlovSeleccionada(Periodicidades seleccionPeriodicidades) {
        this.periodicidadlovSeleccionada = seleccionPeriodicidades;
    }

    public List<Terceros> getLovTerceros() {
        if (lovTerceros == null) {
            lovTerceros = administrarNovedadesConceptos.lovTerceros();
        }
        return lovTerceros;
    }

    public void setLovTerceros(List<Terceros> listaTerceros) {
        this.lovTerceros = listaTerceros;
    }

    public List<Terceros> getFiltrarLovTerceros() {
        return filtrarLovTerceros;
    }

    public void setFiltrarLovTerceros(List<Terceros> filtrarLovTerceros) {
        this.filtrarLovTerceros = filtrarLovTerceros;
    }

    public Terceros getTerceroLovSeleccionado() {
        return terceroLovSeleccionado;
    }

    public void setTerceroLovSeleccionado(Terceros seleccionTerceros) {
        this.terceroLovSeleccionado = seleccionTerceros;
    }

    public List<Formulas> getLovFormulas() {
        if (lovFormulas == null) {
            if (listasRecurrentes.getLovFormulas().isEmpty()) {
                lovFormulas = administrarNovedadesConceptos.lovFormulas();
                if (lovFormulas != null) {
                    listasRecurrentes.setLovFormulas(lovFormulas);
                }
                log.info(this.getClass().getSimpleName() + " Agregando lovFormulas a listasRecurrentes");
            } else {
                lovFormulas = new ArrayList<Formulas>(listasRecurrentes.getLovFormulas());
                log.info(this.getClass().getSimpleName() + " Consultando lovFormulas de listasRecurrentes");
            }
        }
        return lovFormulas;
    }

    public void setLovFormulas(List<Formulas> listaFormulas) {
        this.lovFormulas = listaFormulas;
    }

    public List<Formulas> getFiltrarLovFormulas() {
        return filtrarLovFormulas;
    }

    public void setFiltrarLovFormulas(List<Formulas> filtrarLovFormulas) {
        this.filtrarLovFormulas = filtrarLovFormulas;
    }

    public Formulas getFormulaLovSeleccionada() {
        return formulaLovSeleccionada;
    }

    public void setFormulaLovSeleccionada(Formulas seleccionFormulas) {
        this.formulaLovSeleccionada = seleccionFormulas;
    }

    public List<Conceptos> getLovConceptos() {
        if (lovConceptos == null || lovConceptos.isEmpty()) {
            if (listasRecurrentes.getLovConceptos().isEmpty()) {
                lovConceptos = administrarNovedadesConceptos.Conceptos();
                if (lovConceptos != null) {
                    listasRecurrentes.setLovConceptos(lovConceptos);
                    log.warn("GUARDANDO lovConceptos en Listas recurrentes");
                }
            } else {
                lovConceptos = new ArrayList<Conceptos>(listasRecurrentes.getLovConceptos());
                log.warn("CONSULTANDO lovConceptos de Listas recurrentes");
            }
        }
        return lovConceptos;
    }

    public void setLovConceptos(List<Conceptos> listaConceptos) {
        this.lovConceptos = listaConceptos;
    }

    public List<Conceptos> getFiltrarlovConceptos() {
        return filtrarlovConceptos;
    }

    public void setFiltrarlovConceptos(List<Conceptos> filtrarlovConceptos) {
        this.filtrarlovConceptos = filtrarlovConceptos;
    }

    public Conceptos getConceptoLovSeleccionado() {
        return conceptoLovSeleccionado;
    }

    public void setConceptoLovSeleccionado(Conceptos seleccionConceptos) {
        this.conceptoLovSeleccionado = seleccionConceptos;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public int getResultado() {
        if (!listaNovedadesBorrar.isEmpty()) {
            for (int i = 0; i < listaNovedadesBorrar.size(); i++) {
                resultado = administrarNovedadesConceptos.solucionesFormulas(listaNovedadesBorrar.get(i).getSecuencia());
            }
        }
        return resultado;
    }

    public void setResultado(int resultado) {
        this.resultado = resultado;
    }
//Terminal y Usuario

    public String getAlias() {
        alias = administrarNovedadesConceptos.alias();
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Usuarios getUsuarioBD() {
        getAlias();
        usuarioBD = administrarNovedadesConceptos.usuarioBD(alias);
        return usuarioBD;
    }

    public void setUsuarioBD(Usuarios usuarioBD) {
        this.usuarioBD = usuarioBD;
    }

    public Novedades getNuevaNovedad() {
        return nuevaNovedad;
    }

    public void setNuevaNovedad(Novedades nuevaNovedad) {
        this.nuevaNovedad = nuevaNovedad;
    }

    public boolean isTodas() {
        return todas;
    }

    public boolean isActuales() {
        return actuales;
    }

    public Novedades getEditarNovedades() {
        return editarNovedades;
    }

    public void setEditarNovedades(Novedades editarNovedades) {
        this.editarNovedades = editarNovedades;
    }

    public Novedades getDuplicarNovedad() {
        return duplicarNovedad;
    }

    public void setDuplicarNovedad(Novedades duplicarNovedad) {
        this.duplicarNovedad = duplicarNovedad;
    }

    public Novedades getNovedadSeleccionada() {
        return novedadSeleccionada;
    }

    public void setNovedadSeleccionada(Novedades novedadSeleccionada) {
        this.novedadSeleccionada = novedadSeleccionada;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getAltoTablaReg() {
        return altoTablaReg;
    }

    public void setAltoTablaReg(String altoTablaReg) {
        this.altoTablaReg = altoTablaReg;
    }

    public String getAltoTabla() {
        if (altoTablaReg.equals("5")) {
            altoTabla = "125";
        } else {
            altoTabla = "105";
        }
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getAltoTablaConceptos() {
        if (altoTablaReg.equals("4")) {
            altoTablaConceptos = "76";
        } else {
            altoTablaConceptos = "94";
        }
        return altoTablaConceptos;
    }

    public void setAltoTablaConceptos(String altoTablaConceptos) {
        this.altoTablaConceptos = altoTablaConceptos;
    }

    public String getAltoTablaRegConc() {
        if (altoTablaReg.equals("4")) {
            altoTablaRegConc = "4";
        } else {
            altoTablaRegConc = "5";
        }
        return altoTablaRegConc;
    }

    public void setAltoTablaRegConc(String altoTablaRegConc) {
        this.altoTablaRegConc = altoTablaRegConc;
    }

    public String getInfoRegistroConceptos() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosConceptos");
        infoRegistroConceptos = String.valueOf(tabla.getRowCount());
        return infoRegistroConceptos;
    }

    public String getInfoRegistroNovedades() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosNovedadesConcepto");
        infoRegistroNovedades = String.valueOf(tabla.getRowCount());
        return infoRegistroNovedades;
    }

    public String getInfoRegistroLovConceptos() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovConceptos:LOVConceptos");
        if (filtrarlovConceptos != null) {
            if (filtrarlovConceptos.size() == 1) {
                conceptoLovSeleccionado = filtrarlovConceptos.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVConceptos').unselectAllRows();PF('LOVConceptos').selectRow(0);");
            } else {
                conceptoLovSeleccionado = null;
                RequestContext.getCurrentInstance().execute("PF('LOVConceptos').unselectAllRows();");
            }
        } else {
            conceptoLovSeleccionado = null;
            aceptar = true;
        }
        infoRegistroLovConceptos = String.valueOf(tabla.getRowCount());
        return infoRegistroLovConceptos;
    }

    public String getInfoRegistroLovFormulas() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovFormulas:LOVFormulas");
        if (filtrarLovFormulas != null) {
            if (filtrarLovFormulas.size() == 1) {
                formulaLovSeleccionada = filtrarLovFormulas.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVFormulas').unselectAllRows();PF('LOVFormulas').selectRow(0);");
            } else {
                formulaLovSeleccionada = null;
                RequestContext.getCurrentInstance().execute("PF('LOVFormulas').unselectAllRows();");
            }
        } else {
            formulaLovSeleccionada = null;
            aceptar = true;
        }
        infoRegistroLovFormulas = String.valueOf(tabla.getRowCount());
        return infoRegistroLovFormulas;
    }

    public String getInfoRegistroLovEmpleados() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovEmpleados:LOVEmpleados");
        if (filtrarLovEmpleados != null) {
            if (filtrarLovEmpleados.size() == 1) {
                empleadoLovSeleccionado = filtrarLovEmpleados.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').unselectAllRows();PF('LOVEmpleados').selectRow(0);");
            } else {
                empleadoLovSeleccionado = null;
                RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').unselectAllRows();");
            }
        } else {
            empleadoLovSeleccionado = null;
            aceptar = true;
        }
        infoRegistroLovEmpleados = String.valueOf(tabla.getRowCount());
        return infoRegistroLovEmpleados;
    }

    public String getInfoRegistroLovPeriodicidades() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovPeriodicidad:LOVPeriodicidades");
        if (filtrarLovPeriodicidades != null) {
            if (filtrarLovPeriodicidades.size() == 1) {
                periodicidadlovSeleccionada = filtrarLovPeriodicidades.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVPeriodicidades').unselectAllRows();PF('LOVPeriodicidades').selectRow(0);");
            } else {
                periodicidadlovSeleccionada = null;
                RequestContext.getCurrentInstance().execute("PF('LOVPeriodicidades').unselectAllRows();");
            }
        } else {
            periodicidadlovSeleccionada = null;
            aceptar = true;
        }
        infoRegistroLovPeriodicidades = String.valueOf(tabla.getRowCount());
        return infoRegistroLovPeriodicidades;
    }

    public String getInfoRegistroLovTerceros() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovTerceros:LOVTerceros");
        if (filtrarLovTerceros != null) {
            if (filtrarLovTerceros.size() == 1) {
                terceroLovSeleccionado = filtrarLovTerceros.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVTerceros').unselectAllRows();PF('LOVTerceros').selectRow(0);");
            } else {
                terceroLovSeleccionado = null;
                RequestContext.getCurrentInstance().execute("PF('LOVTerceros').unselectAllRows();");
            }
        } else {
            terceroLovSeleccionado = null;
            aceptar = true;
        }
        infoRegistroLovTerceros = String.valueOf(tabla.getRowCount());
        return infoRegistroLovTerceros;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }
}
